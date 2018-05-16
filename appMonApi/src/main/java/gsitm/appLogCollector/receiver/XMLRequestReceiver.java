package gsitm.appLogCollector.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gsitm.appLogCollector.service.LogWriteService;

@Controller
public class XMLRequestReceiver {
	
	@Autowired
	LogWriteService logWriteService;
	
	@RequestMapping(value = "/xml/collector", method = { RequestMethod.GET, RequestMethod.POST })
	public void receiver(@RequestBody Object requestData) {
		logWriteService.logWrite(requestData);
	}

}










