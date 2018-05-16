package com.admin.web.service;

import com.admin.web.dto.HitSource;
import org.elasticsearch.action.search.SearchResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DashBoardService {
    SearchResponse getGrapeRealtime(String appDivision, LocalDateTime startDate, LocalDateTime endDate);
    List<HitSource> getTableRealtime(String appDivision,LocalDateTime startDate, LocalDateTime endDate);
    SearchResponse getGaugeTotalCount(String appDivision,LocalDateTime startDate, LocalDateTime endDate);
    SearchResponse getGrapeIosAndroid(String appDivision,LocalDateTime startDate, LocalDateTime endDate);
    SearchResponse getPieChartOs(String appDivision,LocalDateTime startDate, LocalDateTime endDate);
    SearchResponse getPieChartVersion(String appDivision,LocalDateTime startDate, LocalDateTime endDate);
    SearchResponse getPieChartDevice(String appDivision,LocalDateTime startDate, LocalDateTime endDate);
    Map<String, LocalDateTime> settingDate(LocalDateTime startDate, LocalDateTime endDate);
}
