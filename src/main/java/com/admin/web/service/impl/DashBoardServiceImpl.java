package com.admin.web.service.impl;

import com.admin.web.controller.LoginController;
import com.admin.web.dto.HitSource;
import com.admin.web.service.DashBoardService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class DashBoardServiceImpl implements DashBoardService {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Value("${elasticsearch.host}") private String host;
    @Value("${elasticsearch.port}") private String port;
    @Value("${elasticsearch.search.size}") private String searchSize;

    @Override
    public SearchResponse getGrapeRealtime(String startDate, String endDate) {
        try {
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(host, Integer.parseInt(port)))
            );

            String sDate = String.valueOf(timestampToString(startDate));
            String eDate = String.valueOf(timestampToString(endDate));

            SearchRequest searchRequest = new SearchRequest("appmon*");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.rangeQuery("@timestamp").to(eDate).from(sDate));
            ExtendedBounds extendedBounds = new ExtendedBounds(sDate, eDate);
            AggregationBuilder aggregation =
                    AggregationBuilders
                            .dateHistogram("agg")
                            .field("@timestamp")
                            .minDocCount(0)
                            .extendedBounds(extendedBounds)
                            .dateHistogramInterval(DateHistogramInterval.MINUTE);
            searchSourceBuilder.aggregation(aggregation);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            client.close();
            return searchResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HitSource> getTableRealtime(String startDate, String endDate) {
        try {
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(host, Integer.parseInt(port)))
            );

            String sDate = String.valueOf(timestampToString(startDate));
            String eDate = String.valueOf(timestampToString(endDate));

            SearchRequest searchRequest = new SearchRequest("appmon*");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.rangeQuery("@timestamp").to(eDate).from(sDate));
            searchSourceBuilder.size(Integer.parseInt(searchSize));
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);
            List<SearchHit[]> totalHits = new ArrayList<SearchHit[]>();
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            totalHits.add(searchHits);
            List<HitSource> results = new ArrayList<HitSource>();
            if(!CollectionUtils.isEmpty(totalHits)){
                for(SearchHit[] hits : totalHits){
                    for(SearchHit hit : hits){
                        HitSource source = new HitSource();
                        Map<String, Object> map = hit.getSourceAsMap();
                        source.setDevice_model( (String)map.get("device_model") );
                        source.setApp_gubun( (String)map.get("app_gubun") );
                        source.setOs_version( (String)map.get("os_version") );
                        source.setIp( (String)map.get("ip") );
                        source.setUuid( (String)map.get("uuid") );
                        source.setErr_message( (String)map.get("err_message") );
                        source.setErr_time( (String)map.get("err_time") );
                        source.setRefer( (String)map.get("refer") );
                        source.setErr_name( (String)map.get("err_name") );
                        source.setApp_ver( (String)map.get("app_ver") );
                        source.setDevice_gubun( (String)map.get("device_gubun") );
                        source.setNetwork_type( (String)map.get("network_type") );
                        source.setCustomer_id( (String)map.get("customer_id") );
                        results.add(source);
                    }
                }
            }
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SearchResponse getGaugeTotalCount(String startDate, String endDate) {
        try {
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(host, Integer.parseInt(port)))
            );

            String sDate = String.valueOf(timestampToString(startDate));
            String eDate = String.valueOf(timestampToString(endDate));

            SearchRequest searchRequest = new SearchRequest("appmon*");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.rangeQuery("@timestamp").to(eDate).from(sDate));
            AggregationBuilder aggregation =
                    AggregationBuilders
                            .filters("agg",
                                    new FiltersAggregator.KeyedFilter("Android", QueryBuilders.termQuery("device_gubun.keyword", "Android")),
                                    new FiltersAggregator.KeyedFilter("iOS", QueryBuilders.termQuery("device_gubun.keyword", "iOS")));
            searchSourceBuilder.aggregation(aggregation);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            client.close();
            return searchResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SearchResponse getGrapeIosAndroid(String startDate, String endDate) {
        try {
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(host, Integer.parseInt(port)))
                    );

            String sDate = String.valueOf(timestampToString(startDate));
            String eDate = String.valueOf(timestampToString(endDate));

            SearchRequest searchRequest = new SearchRequest("appmon*");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.rangeQuery("@timestamp").to(eDate).from(sDate));
            ExtendedBounds extendedBounds = new ExtendedBounds(sDate, eDate);

            AggregationBuilder subAgg =
                    AggregationBuilders
                            .filters("agg",
                                    new FiltersAggregator.KeyedFilter("Android", QueryBuilders.termQuery("device_gubun.keyword", "Android")),
                                    new FiltersAggregator.KeyedFilter("iOS", QueryBuilders.termQuery("device_gubun.keyword", "iOS")));

            AggregationBuilder aggregation =
                    AggregationBuilders
                            .dateHistogram("agg")
                            .field("@timestamp")
                            .minDocCount(0)
                            .extendedBounds(extendedBounds)
                            .dateHistogramInterval(DateHistogramInterval.MINUTE)
                            .subAggregation(subAgg);
            searchSourceBuilder.aggregation(aggregation);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            client.close();
            return searchResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SearchResponse getPieChartOs(String startDate, String endDate) {
        try {
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(host, Integer.parseInt(port)))
                    );

            String sDate = String.valueOf(timestampToString(startDate));
            String eDate = String.valueOf(timestampToString(endDate));

            SearchRequest searchRequest = new SearchRequest("appmon*");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.rangeQuery("@timestamp").to(eDate).from(sDate));
            AggregationBuilder aggregation =
                    AggregationBuilders
                            .filters("agg",
                                    new FiltersAggregator.KeyedFilter("Android", QueryBuilders.termQuery("device_gubun.keyword", "Android")),
                                    new FiltersAggregator.KeyedFilter("iOS", QueryBuilders.termQuery("device_gubun.keyword", "iOS")));
            searchSourceBuilder.aggregation(aggregation);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            client.close();
            return searchResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SearchResponse getPieChartVersion(String startDate, String endDate) {
        try {
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(host, Integer.parseInt(port)))
            );

            String sDate = String.valueOf(timestampToString(startDate));
            String eDate = String.valueOf(timestampToString(endDate));

            SearchRequest searchRequest = new SearchRequest("appmon*");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.rangeQuery("@timestamp").to(eDate).from(sDate));
            AggregationBuilder aggregation =
                    AggregationBuilders
                            .terms("agg")
                            .field("app_ver.keyword");
            searchSourceBuilder.aggregation(aggregation);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            client.close();
            return searchResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SearchResponse getPieChartDevice(String startDate, String endDate) {
        try {
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(host, Integer.parseInt(port)))
            );

            String sDate = String.valueOf(timestampToString(startDate));
            String eDate = String.valueOf(timestampToString(endDate));

            SearchRequest searchRequest = new SearchRequest("appmon*");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.rangeQuery("@timestamp").to(eDate).from(sDate));
            AggregationBuilder aggregation =
                    AggregationBuilders
                            .terms("agg")
                            .field("device_model.keyword");
            searchSourceBuilder.aggregation(aggregation);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            client.close();
            return searchResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private LocalDateTime timestampToString(String date){
        Calendar cal;
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        try{
            sd.parse(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        cal = sd.getCalendar();
        cal.add(Calendar.HOUR, -9);
        //System.out.println("timestamp : " + new Timestamp(cal.getTime().getTime()).toLocalDateTime());
        return new Timestamp(cal.getTime().getTime()).toLocalDateTime();
    }
}