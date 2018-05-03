package com.admin.web.controller.dashboard;

import com.admin.web.controller.BaseController;
import com.admin.web.dto.HitSource;
import com.admin.web.service.DashBoardService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class DashBoardController extends BaseController {

	@Autowired
	protected DashBoardService dashBoardService;

	private static final String VIEW_PATH = "dashboard/";
	private static final String COMPONENT_PATH = "component/";

	@RequestMapping( value = "/dashboard/realtime", method = RequestMethod.GET )
	public ModelAndView realTimeDashboard(Model model
			, @RequestParam(value="startDate", defaultValue = "", required = false) String startDate
			, @RequestParam(value="endDate", defaultValue = "", required = false) String endDate){

		return new ModelAndView(VIEW_PATH+"dashboard_realtime");
	}


	@RequestMapping( value = "/component/grape/realtime", method = RequestMethod.GET )
	@ResponseBody
	public  Collection<Histogram.Bucket> realTimeGrape(Model model
			, @RequestParam(value="startDate", defaultValue = "", required = false) String startDate
			, @RequestParam(value="endDate", defaultValue = "", required = false) String endDate
	){
		Map<String, String> DateMap = getInitDate(startDate, endDate);
		SearchResponse searchResponse = dashBoardService.getGrapeRealtime(DateMap.get("startDate"), DateMap.get("endDate"));
		if(null != searchResponse){
			Histogram aggregations = searchResponse.getAggregations().get("agg");
			return  (Collection<Histogram.Bucket>) aggregations.getBuckets();
		}
		return null;
	}

	@RequestMapping( value = "/component/table/realtime", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView realTimeTable(Model model
			,@RequestParam(value="startDate", defaultValue = "", required = false) String startDate
			,@RequestParam(value="endDate", defaultValue = "", required = false) String endDate)
	{
		Map<String, String> DateMap = getInitDate(startDate, endDate);
		List<HitSource> results = dashBoardService.getTableRealtime(DateMap.get("startDate"), DateMap.get("endDate"));
		if(null != results){
			return new ModelAndView(VIEW_PATH+COMPONENT_PATH+"table_realtime", "results", results);
		}
		return new ModelAndView(VIEW_PATH+COMPONENT_PATH+"table_realtime", "results", null);
	}

	private Map<String, String> getInitDate(String startDate, String endDate){
		Map<String, String> map = new HashMap<String, String>();
		// 시간을 선택하지 않았을 경우
		if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate) ){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar cal = Calendar.getInstance();
			endDate = sdf.format(cal.getTime());
			cal.add(Calendar.MINUTE, -10);
			startDate = sdf.format(cal.getTime());
		}
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

}
