package org.xas.uned.camip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xas.uned.camip.bean.DeviceCheckData;
import org.xas.uned.camip.checker.DoubleCheckChecker;

@RestController
@RequestMapping("/check")
public class CheckController {

	@Autowired
	private DoubleCheckChecker doubleCheckChecker;

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/use", method = RequestMethod.POST)
	public ResponseEntity checkDeviceAccesed(@RequestHeader(name = "camip-token", required = false) String token,
			@RequestBody DeviceCheckData deviceCheckData) {
		System.out.println(
				"****** XAS ************ XAS ************ XAS ************ XAS ************ XAS ************ XAS ************ XAS ************ XAS ************ XAS ******");
		System.out.println("************End:" + System.currentTimeMillis());
		doubleCheckChecker.onTokenResponse(deviceCheckData);

		return new ResponseEntity(HttpStatus.OK);

	}

}
