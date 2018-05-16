package gsitm.appLogCollector;

import org.springframework.beans.factory.annotation.Value;

public class CommonConstants {

	//  로그 파일 위치
	@Value("${log.path}")
	public final static String LOG_PATH = "";
}
