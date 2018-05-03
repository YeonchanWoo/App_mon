package com.admin.web.service;

import com.admin.web.dto.HitSource;
import org.elasticsearch.action.search.SearchResponse;

import java.util.List;

public interface DashBoardService {
    SearchResponse getGrapeRealtime(String startDate, String endDate);
    List<HitSource> getTableRealtime(String startDate, String endDate);

    SearchResponse getGaugeTotalCount(String startDate, String endDate);
    SearchResponse getGrapeIosAndroid(String startDate, String endDate);
    SearchResponse getPieChartOs(String startDate, String endDate);
    SearchResponse getPieChartVersion(String startDate, String endDate);
    SearchResponse getPieChartDevice(String startDate, String endDate);
}
