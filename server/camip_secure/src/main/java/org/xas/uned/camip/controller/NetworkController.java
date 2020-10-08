package org.xas.uned.camip.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/net")
public class NetworkController {

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/flow", method = RequestMethod.GET)
	public ResponseEntity getNetFlow() {

		return new ResponseEntity(HttpStatus.FORBIDDEN);

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/rules", method = RequestMethod.GET)
	public ResponseEntity getNetRules() {

		return new ResponseEntity(HttpStatus.FORBIDDEN);

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/rules/{id}", method = RequestMethod.DELETE)
	public ResponseEntity getNetRules(@PathVariable long id) {

		return new ResponseEntity(HttpStatus.FORBIDDEN);

	}
}
