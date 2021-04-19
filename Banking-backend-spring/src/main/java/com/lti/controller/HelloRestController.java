package com.lti.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {

	//@RequestMapping(path ="/hello", method = RequestMethod.GET)
	//or
	@GetMapping("/hello")
	public  String hello() {
		return "Welcome Banking Backend";
	}
}
