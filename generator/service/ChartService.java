package com.vickey.bi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vickey.bi.model.dto.chart.ChartQueryRequest;
import com.vickey.bi.model.entity.Chart;
import com.vickey.bi.model.vo.ChartVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 图表服务
 *

 */
public interface ChartService extends IService<Chart> {

    /**
     * 获取查询条件
     */
    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);
    
    /**
     * 获取图表封装
     */
    ChartVO getChartVO(Chart chart, HttpServletRequest request);

    /**
     * 分页获取图表封装
     */
    Page<ChartVO> getChartVOPage(Page<Chart> chartPage, HttpServletRequest request);
}
