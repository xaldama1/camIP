package org.xas.uned.camip.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing")
public class TestController {

//	@Autowired
//	private TrafficCheckerServiceImpl traficFacade;

//	@SuppressWarnings("rawtypes")
//	@RequestMapping(value = "/addMalicious")
//	public ResponseEntity addMalicious(@RequestParam final String ip) {
//
//		traficFacade.addMaliciousIp(Arrays.asList(ip));
//		return new ResponseEntity<Void>(HttpStatus.OK);
//	}
//
//	@SuppressWarnings("rawtypes")
//	@RequestMapping(value = "/cleanMalicious")
//	public ResponseEntity cleanMalicious() {
//
//		traficFacade.cleanMaliciousIp();
//		return new ResponseEntity<Void>(HttpStatus.OK);
//	}

//	@SuppressWarnings("rawtypes")
//	@RequestMapping(value = "/cleanDDoS")
//	public ResponseEntity cleanDDoS() {
//
//		traficFacade.cleanDDoS();
//		return new ResponseEntity<Void>(HttpStatus.OK);
//	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/t")
	public ResponseEntity test() {

		return new ResponseEntity<String>("Test done", HttpStatus.OK);
	}

}
