package com.admin.web.controller.dashboard;

import com.admin.web.controller.BaseController;
import com.admin.web.controller.settings.SettingsAppsController;
import com.admin.web.dto.HitSource;
import com.admin.web.service.DashBoardService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class DashBoardController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(SettingsAppsController.class);

    @Autowired
	protected DashBoardService dashBoardService;

	private static final String VIEW_PATH = "dashboard/";
	private static final String COMPONENT_PATH = "component/";

	@RequestMapping( value = "/dashboard/realtime", method = RequestMethod.GET )
	public ModelAndView realTimeDashboard(Model model){
		return new ModelAndView(VIEW_PATH+"dashboard_realtime");
	}

	@RequestMapping( value = "/component/grape/realtime", method = RequestMethod.GET )
	@ResponseBody
	public  Collection<Histogram.Bucket> realTimeGrape(Model model
			, @RequestParam(value="appDivision", defaultValue = "", required = false) String appDivision
			, @RequestParam(value="startDate", defaultValue = "", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate
			, @RequestParam(value="endDate", defaultValue = "", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate	){
	    SearchResponse searchResponse = dashBoardService.getGrapeRealtime(appDivision, startDate, endDate);
		if(null != searchResponse && null != searchResponse.getAggregations()){
			Histogram aggregations = searchResponse.getAggregations().get("agg");
			return  (Collection<Histogram.Bucket>) aggregations.getBuckets();
		}
		return null;
	}

	@RequestMapping( value = "/component/table/realtime", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView realTimeTable(Model model
			, @RequestParam(value="appDivision", defaultValue = "", required = false) String appDivision
			, @RequestParam(value="startDate", defaultValue = "", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate
			, @RequestParam(value="endDate", defaultValue = "", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<HitSource> results = dashBoardService.getTableRealtime(appDivision, startDate, endDate);
		if(null != results){
			return new ModelAndView(VIEW_PATH+COMPONENT_PATH+"table_realtime", "results", results);
		}
		return new ModelAndView(VIEW_PATH+COMPONENT_PATH+"table_realtime", "results", null);
	}

}
