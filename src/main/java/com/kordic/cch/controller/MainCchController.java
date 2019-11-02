package com.kordic.cch.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kordic.cch.service.ESService;
import com.music.completion.vo.SearchVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MainCchController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainCchController.class);
	private static Marker SEARCH_WD = MarkerFactory.getMarker("SEARCH_WD");
	
	@Resource(name="ESService")
	private ESService eSService;
	
	@RequestMapping(value="/autoComplete", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody String autoComplete(@RequestBody SearchVO inVO){
		String rtnStr = "";
		 String jsonStr = eSService.makeJsonStr(inVO.getSchWord());
		logger.info("autoComplete"+inVO.getSchWord());
		
		try {
			rtnStr = eSService.autoComplete(null, jsonStr);
			if(logger.isDebugEnabled()) {
				logger.debug("/autocomplete Test rtnStr : " + rtnStr);
			}
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
	public @ResponseBody String search(@RequestBody SearchVO inVO){
		String rtnStr = "";
		String jsonStr = eSService.makeJsonStr(inVO.getSchWord());
		Date time = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
		
		logger.info(MainCchController.SEARCH_WD, "search["+inVO.getSchWord() + "] timestamp["+fmt.format(time) +"]");
		try {
			rtnStr = eSService.searchKorDict(null, jsonStr);
			if(logger.isDebugEnabled()) {
				logger.debug("/search Test rtnStr : " + rtnStr);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		return rtnStr;
	}
	
	@RequestMapping(value="/getTopSearch", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody String getTopSearch(@RequestBody SearchVO inVO){
		String rtnStr = "";
		String jsonStr = eSService.getTopSearch();
		
		try {
//			rtnStr = eSService.searchKorDict(null, jsonStr);
			if(logger.isDebugEnabled()) {
				logger.debug("/search Test rtnStr : " + rtnStr);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		return jsonStr;
	}
	
}
