package com.vickey.bi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vickey.bi.annotation.AuthCheck;
import com.vickey.bi.common.BaseResponse;
import com.vickey.bi.common.DeleteRequest;
import com.vickey.bi.common.ErrorCode;
import com.vickey.bi.common.ResultUtils;
import com.vickey.bi.constant.UserConstant;
import com.vickey.bi.exception.BusinessException;
import com.vickey.bi.exception.ThrowUtils;
import com.vickey.bi.model.dto.chart.ChartAddRequest;
import com.vickey.bi.model.dto.chart.ChartEditRequest;
import com.vickey.bi.model.dto.chart.ChartQueryRequest;
import com.vickey.bi.model.dto.chart.ChartUpdateRequest;
import com.vickey.bi.model.entity.Chart;
import com.vickey.bi.model.entity.User;
import com.vickey.bi.model.vo.ChartVO;
import com.vickey.bi.service.ChartService;
import com.vickey.bi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
