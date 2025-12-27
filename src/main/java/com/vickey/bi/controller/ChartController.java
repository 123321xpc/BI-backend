package com.vickey.bi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vickey.bi.annotation.AuthCheck;
import com.vickey.bi.common.BaseResponse;
import com.vickey.bi.common.DeleteRequest;
import com.vickey.bi.common.ErrorCode;
import com.vickey.bi.common.ResultUtils;
import com.vickey.bi.constant.FileConstant;
import com.vickey.bi.constant.UserConstant;
import com.vickey.bi.exception.BusinessException;
import com.vickey.bi.exception.ThrowUtils;
import com.vickey.bi.manager.RedissonLimiter;
import com.vickey.bi.manager.deepseek.DeepSeekApiService;
import com.vickey.bi.manager.excel.ExcelUtils;
import com.vickey.bi.model.dto.chart.*;
import com.vickey.bi.model.dto.file.UploadFileRequest;
import com.vickey.bi.model.entity.Chart;
import com.vickey.bi.model.entity.User;
import com.vickey.bi.model.enums.FileUploadBizEnum;
import com.vickey.bi.model.vo.ChartVO;
import com.vickey.bi.service.ChartService;
import com.vickey.bi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * 图表接口
 *
 */
@RestController
@RequestMapping("/chart")
@Slf4j
@Tag(name = "图表相关接口")
public class ChartController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    @Resource
    private DeepSeekApiService deepSeekApiService;

    @Resource
    private RedissonLimiter redissonLimiter;

    public final int MAX_FILE_SIZE = 5 * 1024 * 1024;   // 最大文件大小 5M
    public final String[] ALLOWED_FILE_TYPES = {"xls", "xlsx"};


    @PostMapping("/generate")
    public BaseResponse<ChartVO> getChartByAI(@RequestPart("file") MultipartFile multipartFile,
                                             GenChartRequest genChartRequest, HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        //  用户限流(不通过直接抛异常)
        redissonLimiter.doLimit("user_" + loginUser.getId());

        String goal = genChartRequest.getGoal();
        String chartType = genChartRequest.getChartType();

        ThrowUtils.throwIf(StringUtils.isAnyBlank(goal), ErrorCode.PARAMS_ERROR);

        // 校验文件
        // 校验文件大小
        ThrowUtils.throwIf(multipartFile.getSize() > MAX_FILE_SIZE, ErrorCode.OPERATION_ERROR, "文件大小不能超过5M！");

        //  校验文件格式
        String suffix = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        ThrowUtils.throwIf(!ArrayUtils.contains(ALLOWED_FILE_TYPES, suffix), ErrorCode.OPERATION_ERROR, "文件格式不支持！");


        String csv = ExcelUtils.excelToCsv(multipartFile);

        StringBuilder sb = new StringBuilder();
        sb.append("【【【【").append("\n");
        sb.append("{{").append(goal).append("}}").append("\n");
        sb.append("{{").append(csv).append("}}").append("\n");
        sb.append("{{").append(chartType).append("}}").append("\n");
        sb.append("】】】】");

        String aiAns = deepSeekApiService.chatWithDeepSeek(sb.toString());

        List<String> ansList = deepSeekApiService.extractContent(aiAns);

        ChartVO res = new ChartVO();

        BeanUtils.copyProperties(genChartRequest, res);
        res.setName(ansList.get(0));
        res.setGenResult(ansList.get(1));
        res.setGenChart(ansList.get(2));
        res.setChartData(csv);
        res.setChartType(StringUtils.isBlank(chartType)? ansList.get(3) : chartType);
        res.setUserId(loginUser.getId());

        Chart chart = new Chart();
        BeanUtils.copyProperties(res, chart);

        boolean saveRes = chartService.save(chart);

        ThrowUtils.throwIf(!saveRes, ErrorCode.OPERATION_ERROR, "保存数据库失败");

        return ResultUtils.success(res);

    }

    // region 增删改查

    @PostMapping("/add")
    @Operation(summary = "创建图表")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(chartAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newChartId = chart.getId();
        return ResultUtils.success(newChartId);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除图表")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = chartService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "更新图表（仅管理员可用）")
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);
        // 判断是否存在
        long id = chartUpdateRequest.getId();
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = chartService.updateById(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @GetMapping("/get/vo")
    @Operation(summary = "根据 id 获取图表（封装类）")
    public BaseResponse<ChartVO> getChartVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Chart chart = chartService.getById(id);
        ThrowUtils.throwIf(chart == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(chartService.getChartVO(chart, request));
    }

    @PostMapping("/list/page")
    @Operation(summary = "分页获取图表列表（仅管理员可用）")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 查询数据库
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    @PostMapping("/list/page/vo")
    @Operation(summary = "分页获取图表列表（封装类）")
    public BaseResponse<Page<ChartVO>> listChartVOByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                         HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        // 获取封装类
        return ResultUtils.success(chartService.getChartVOPage(chartPage, request));
    }

    @PostMapping("/my/list/page/vo")
    @Operation(summary = "分页获取当前登录用户创建的图表列表")
    public BaseResponse<Page<ChartVO>> listMyChartVOByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                           HttpServletRequest request) {
        ThrowUtils.throwIf(chartQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        // 获取封装类
        return ResultUtils.success(chartService.getChartVOPage(chartPage, request));
    }

    @PostMapping("/edit")
    @Operation(summary = "编辑图表（给用户使用）")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditRequest, chart);
        // 判断是否存在
        long id = chartEditRequest.getId();
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        User loginUser = userService.getLoginUser(request);
        // 仅本人或管理员可编辑
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = chartService.updateById(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
