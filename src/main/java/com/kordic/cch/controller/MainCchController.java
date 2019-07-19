package com.kordic.cch.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kordic.cch.service.CallESService;
import com.music.completion.vo.SearchVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MainCchController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainCchController.class);
	@Resource(name="CallESService")
	private CallESService callESService;
	
	@RequestMapping(value="/autoComplete", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody String autoComplete(SearchVO inVO){
		String rtnStr = "";
		String jsonStr = callESService.makeJsonStr(inVO.getSchWord());
		logger.info("/autoComplete");
		try {
			rtnStr = callESService.autoComplete(null, jsonStr);
			logger.info("/autocomplete Test rtnStr : " + rtnStr);
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		return rtnStr;
	}
	
	@RequestMapping(value="/cch/main", produces="application/json;charset=UTF-8", method = RequestMethod.GET)
	public String main(SearchVO inVO){
		
		return "cch/index2";
	}
	
	@RequestMapping(value="/search", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody String search(SearchVO inVO){
		String rtnStr = "";
		String jsonStr = callESService.makeJsonStr(inVO.getSchWord());
		logger.info("/search");
		try {
			rtnStr = callESService.searchKorDict(null, jsonStr);
			logger.info("/search Test rtnStr : " + rtnStr);
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		return rtnStr;
	}
	
}
