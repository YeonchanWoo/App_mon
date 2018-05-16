package gsitm.appLogCollector.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import gsitm.appLogCollector.CommonConstants;
import gsitm.appLogCollector.util.DateUtil;

@Service
public class LogWriteServiceImpl implements LogWriteService {
	
	public void logWrite(Object data) {

		try {

			ObjectMapper mapper = new ObjectMapper();
			String msg = mapper.writeValueAsString(data);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
			String date = sdf.format(new Date());
			JSONObject obj = new JSONObject(msg);
			String appGubun = obj.get("app_gubun").toString();
			String filePath = CommonConstants.LOG_PATH + appGubun + "/";

			File f = new File(filePath);
			if (!f.isDirectory()) {
				f.mkdirs();
			}

			PrintWriter pw = new PrintWriter(new FileWriter(filePath + appGubun + "_" + date + ".txt", true));
			pw.write(msg.toString());
			pw.write("\n");
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}




