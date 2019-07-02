package com.kordic.cch.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kordic.cch.service.CallESService;
import com.music.completion.vo.SearchVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeCchController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeCchController.class);
	@Resource(name="CallESService")
	private CallESService callESService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/cch", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "chhome";
	}
	
	@RequestMapping(value="/search", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody String search(SearchVO inVO){
		String rtnStr = "";
		String jsonStr = callESService.makeJsonStr(inVO.getSchWord());
		logger.debug("Text cch/index");
		logger.info("Text cch/index");
		logger.warn("Text cch/index");
		logger.error("Text cch/index");
		try {
			rtnStr = callESService.searchKorDict(null, jsonStr);
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		return rtnStr;
	}
	
	@RequestMapping(value="/cch/main", produces="application/json;charset=UTF-8", method = RequestMethod.GET)
	public String main(SearchVO inVO){
		
		logger.debug("Text cch/index");
		logger.info("Text cch/index");
		logger.warn("Text cch/index");
		logger.error("Text cch/index");
		return "cch/index";
	}
	
}
