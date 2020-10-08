package org.xas.uned.camip.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xas.uned.camip.bean.SyncData;

@RestController
@RequestMapping("/device")
public class DeviceController {

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sync")
	public ResponseEntity<SyncData> sync(@RequestHeader(name = "camip-token", required = false) String token) {

		return new ResponseEntity(HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{id}/status", method = RequestMethod.GET)
	public ResponseEntity checkerStatus(@PathVariable long id) {

		return new ResponseEntity(HttpStatus.FORBIDDEN);

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{id}/status/{validator}", method = RequestMethod.PUT)
	public ResponseEntity updateCheckerStatus(@PathVariable long id, @PathVariable String validator,
			final HttpServletRequest request) {

		String status = request.getParameter("status");

		return new ResponseEntity(HttpStatus.FORBIDDEN);

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity getDevicesData() {

		return new ResponseEntity(HttpStatus.FORBIDDEN);

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity getDevicesData(final HttpServletRequest request) {

		String deviceName = request.getParameter("name");

		return new ResponseEntity(HttpStatus.FORBIDDEN);

	}

}
