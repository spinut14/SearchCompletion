package com.kordic.kdy.controller;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kordic.kdy.service.SearchService;
import com.kordic.kdy.vo.SearchWord;
import com.kordic.kdy.vo.Word;
import com.music.completion.vo.BoolVO;
import com.music.completion.vo.DocVO;
import com.music.completion.vo.PrefixVO;
import com.music.completion.vo.QueryVO;
import com.music.completion.vo.ResVO;
import com.music.completion.vo.SearchVO;
import com.music.completion.vo.ShouldVO;
import com.music.completion.vo.TermVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeKDYController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeKDYController.class);
	
// constructor 주입 방식 알아보기 
	

	@Autowired
	SearchService searchService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/kdy", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "dyhome";
	}
//	{"query":
//	{"bool":
//	{"should":[{"prefix":{"title":"노래"}},
//	           {"term":{"titleNgram":"노래"}},
//	           {"term":{"titleNgramEdge":"노래"}},
//	           {"term":{"titleNgramEdgeBack":"노래"}}],
//		"minimum_should_match":1}
//	}
//	}
	/*
	 *TODO : parameter vo 객체로 변환하기 
	 */
	@RequestMapping(value="/kdy/searchWord", produces="application/json;charset=UTF-8",method = RequestMethod.POST)
	public @ResponseBody Map<String, List> searchDictionary( SearchWord word) {		
		//@ResponseBody를 붙여주면 
		String searchWord = word.getWord();

		List searchList = searchService.getSearchContext(searchWord);
		
//		Word wd = new Word();
//		wd.setCategory("category");
//		wd.setVoca("voca");
//		wd.setMeaning("meaning");
		Map data = new HashMap<String,List>();
		data.put("data", searchList);
		
		return data;
	}
	

}
