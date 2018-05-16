package gsitm.study.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gsitm.study.utils.DateUtil;

@RestController
public class LogCollecterControllor {

//	private String logPath = "c:/";
	 private String logPath = "/applications/tomcat/logs/appLogs/";

	@RequestMapping(value = "/helloworld", method = RequestMethod.GET)
	public String HelloWorld() {
		ObjectMapper mapper = new ObjectMapper();
		String json2 = "";
		try {
			System.out.println("-------------------MAP을 JSON String으로 변환-----------------------");
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("name", "mkgil");
			map2.put("age", 25);
			json2 = mapper.writeValueAsString(map2);
			json2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map2);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json2;
	}

	@RequestMapping(value = "/collectorOld", method = { RequestMethod.GET, RequestMethod.POST })
	public void logCollecterOld(@RequestParam HashMap<String, String> map) {
		try {
			Calendar cal = Calendar.getInstance();

			String fileName = DateUtil.getcurrentDate();
			int cnt = 0;
			PrintWriter pw = new PrintWriter(new FileWriter(logPath + fileName + ".txt", true));
			for (Map.Entry<String, String> entry : map.entrySet()) {
				pw.write(entry.getKey() + ":" + entry.getValue());
				if (map.size() - 1 != cnt) {
					pw.write("|");
				}
				cnt++;
			}
			pw.write("\n");
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/collector", method = { RequestMethod.GET, RequestMethod.POST })
	public void logCollecter(@RequestParam HashMap<String, String> requstMap) {
		try {
			Calendar cal = Calendar.getInstance();
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonResult = "";
			String fileName = DateUtil.getcurrentDate();
			jsonResult = mapper.writeValueAsString(requstMap);
//			jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requstMap);	// 들여쓰기 하려면
			
			PrintWriter pw = new PrintWriter(new FileWriter(logPath + fileName + ".txt", true));
			pw.write(jsonResult);
			pw.write("\n");
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/collectorTest", method = { RequestMethod.GET, RequestMethod.POST })
	public void logCollecterTest(@RequestParam HashMap<String, String> requstMap) {
		try {
			
			System.out.println("defaultCharset->"+Charset.defaultCharset());
			Calendar cal = Calendar.getInstance();
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonResult = "";
			String fileName = DateUtil.getcurrentDate();
			jsonResult = mapper.writeValueAsString(requstMap);
//			jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requstMap);	// 들여쓰기 하려면
			
			PrintWriter pw = new PrintWriter(new FileWriter(logPath + fileName + "_test.txt", true));
			pw.write(new String(jsonResult.getBytes("UTF-8")));
			pw.write("\n");
			pw.write(new String(jsonResult.getBytes("EUC-KR")));
			pw.write("\n");
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
