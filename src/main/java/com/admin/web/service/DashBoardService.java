package com.admin.web.service;

import com.admin.web.dto.HitSource;
import org.elasticsearch.action.search.SearchResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DashBoardService {
    SearchResponse getGrapeRealtime(LocalDateTime startDate, LocalDateTime endDate);
    List<HitSource> getTableRealtime(LocalDateTime startDate, LocalDateTime endDate);
    SearchResponse getGaugeTotalCount(LocalDateTime startDate, LocalDateTime endDate);
    SearchResponse getGrapeIosAndroid(LocalDateTime startDate, LocalDateTime endDate);
    SearchResponse getPieChartOs(LocalDateTime startDate, LocalDateTime endDate);
    SearchResponse getPieChartVersion(LocalDateTime startDate, LocalDateTime endDate);
    SearchResponse getPieChartDevice(LocalDateTime startDate, LocalDateTime endDate);
    Map<String, LocalDateTime> settingDate(LocalDateTime startDate, LocalDateTime endDate);
}
