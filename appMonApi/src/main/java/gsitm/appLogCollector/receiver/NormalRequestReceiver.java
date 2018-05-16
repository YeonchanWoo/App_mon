package gsitm.appLogCollector.receiver;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import gsitm.appLogCollector.service.LogWriteService;

@Controller
public class NormalRequestReceiver {
	
	@Autowired
	LogWriteService logWriteService;
	
	@RequestMapping(value = "/normal/collector", method = { RequestMethod.GET, RequestMethod.POST })
	public void receiver(@RequestParam HashMap<String, String> requestData) {
		logWriteService.logWrite(requestData);
	}

}
