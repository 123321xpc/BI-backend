package com.vickey.bi.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vickey.bi.mapper.ChartMapper;
import com.vickey.bi.model.dto.chart.ChartQueryRequest;
import com.vickey.bi.model.entity.Chart;
import com.vickey.bi.model.vo.ChartVO;
import com.vickey.bi.service.ChartService;
import com.vickey.bi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图表服务实现
 */
@Service
@Slf4j
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements ChartService {

    @Resource
    private UserService userService;

    /**
     * 获取查询条件
     */
    @Override
    public QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (chartQueryRequest == null) {
            return queryWrapper;
        }

        Long id = chartQueryRequest.getId();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        Long userId = chartQueryRequest.getUserId();


        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.eq("isDelete", false);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);

        return queryWrapper;
    }

    /**
     * 获取图表封装
     */
    @Override
    public ChartVO getChartVO(Chart chart, HttpServletRequest request) {
        // 对象转封装类
        ChartVO chartVO = new ChartVO();
        BeanUtils.copyProperties(chart, chartVO);

        return chartVO;
    }

    /**
     * 分页获取图表封装
     */
    @Override
    public Page<ChartVO> getChartVOPage(Page<Chart> chartPage, HttpServletRequest request) {
        List<Chart> chartList = chartPage.getRecords();
        Page<ChartVO> chartVOPage = new Page<>(chartPage.getCurrent(), chartPage.getSize(), chartPage.getTotal());
        if (CollUtil.isEmpty(chartList)) {
            return chartVOPage;
        }
        // 对象列表 => 封装对象列表
        List<ChartVO> chartVOList = chartList.stream().map(chart -> {
            ChartVO chartVO = new ChartVO();
            BeanUtils.copyProperties(chart, chartVO);
            return chartVO;
        }).collect(Collectors.toList());

        chartVOPage.setRecords(chartVOList);
        return chartVOPage;
    }

}
