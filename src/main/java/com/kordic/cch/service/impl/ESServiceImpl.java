package com.kordic.cch.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fwk.es.CallESService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kordic.cch.controller.MainCchController;
import com.kordic.cch.service.ESService;
import com.music.completion.vo.AggsVO;
import com.music.completion.vo.BoolVO;
import com.music.completion.vo.DocVO;
import com.music.completion.vo.PrefixVO;
import com.music.completion.vo.QueryVO;
import com.music.completion.vo.RangeVO;
import com.music.completion.vo.ReqDtVO;
import com.music.completion.vo.ResVO;
import com.music.completion.vo.SearchVO;
import com.music.completion.vo.ShouldVO;
import com.music.completion.vo.TermVO;
import com.music.completion.vo.Top_5_word;

@Service("ESService")
public class ESServiceImpl implements ESService {
	private static final Logger logger = LoggerFactory.getLogger(MainCchController.class);
	
	@Resource(name="CallESService")
	private CallESService caller;
	
	
	/**
	 * Call Elasticsearch API
	 */
	@Override
	public String autoComplete(Object obj, String jsonData) {
		try{
            String jsonString;
            Gson gson = null;
            if(null != jsonData) {
            	jsonString = jsonData;
            }else {
            	gson = new Gson();
                jsonString = gson.toJson(obj);
            }
            if(logger.isDebugEnabled()) {
            	logger.debug("jsonString["+jsonString+"]");
            }
            
            String responseBody = caller.sendToEs(jsonString);
            JsonParser parser = new JsonParser();
            JsonElement rootObject = parser.parse(responseBody)
            .getAsJsonObject().get("hits")
            .getAsJsonObject().get("hits");
            
            JsonArray jarr = rootObject.getAsJsonArray();
            JsonObject jHitData = null;
            JsonObject jData = null;
            ResVO rtn = null;
            List<ResVO> list = new ArrayList<ResVO>();
            List<String> dupChkList = new ArrayList<String>();
            
            for(int i=0; i<jarr.size(); i++) {
            	rtn = new ResVO();
            	jHitData = jarr.get(i).getAsJsonObject();
            	jData = jHitData.getAsJsonObject("_source");
            	dupChkList.add(jData.get("voca").getAsString());
            }
            
            		// 2. 중복제거
            List<String> uniqueList = this.removeDup(dupChkList);
            
            for(String str : uniqueList) {
            	rtn = new ResVO();
            	rtn.setVoca(str);
            	list.add(rtn);
            }
        	
            gson = new Gson();
            String rtnStr = gson.toJson(list);
            return rtnStr;

        } catch (Exception e) {
        	e.printStackTrace();
        }
		return null;
	}
	
	
	@Override
	public String searchKorDict(Object obj, String jsonData) {

		try{
            String jsonString;
            Gson gson = null;
            if(null != jsonData) {
            	jsonString = jsonData;
            }else {
            	gson = new Gson();
                jsonString = gson.toJson(obj);
            }
            String responseBody = caller.sendToEs(jsonString);
            if(logger.isDebugEnabled()) {
            	logger.debug("responseBody["+responseBody+"]");
            }
            JsonParser parser = new JsonParser();
            JsonElement rootObject = parser.parse(responseBody)
            .getAsJsonObject().get("hits")
            .getAsJsonObject().get("hits");
            
            JsonArray jarr = rootObject.getAsJsonArray();
            JsonObject jHitData = null;
            JsonObject jData = null;
            ResVO rtn = null;
            List<ResVO> list = new ArrayList<ResVO>();
            List<String> dupChkList = new ArrayList<String>();
            
            for(int i=0; i<jarr.size(); i++) {
            	rtn = new ResVO();
            	jHitData = jarr.get(i).getAsJsonObject();
            	jData = jHitData.getAsJsonObject("_source");
            	dupChkList.add(jData.get("voca").getAsString());
            	
            	rtn.set_id(jHitData.get("_id").getAsString());
            	rtn.set_index(jHitData.get("_index").getAsString());
            	rtn.set_type(jHitData.get("_type").getAsString());
            	rtn.setVoca(jData.get("voca").getAsString());
            	rtn.setIsNative(jData.get("isNative").getAsString());
            	rtn.setCategory(jData.get("category").getAsString());
            	rtn.setMeaning(jData.get("meaning").getAsString());
            	list.add(rtn);
            }
            
            gson = new Gson();
            String rtnStr = gson.toJson(list);
            return rtnStr;

        } catch (Exception e) {
        	e.printStackTrace();
        }
		return null;
	}
	

	/**
	 * Make Json String
	 */
	@Override
	public String makeJsonStr(String schWord) {
		// TODO Auto-generated method stub
		TermVO termNg = new TermVO();
		TermVO termNgE = new TermVO();
		TermVO termNgEb = new TermVO();
		PrefixVO prefix = new PrefixVO();
//		termNg.setTitleNgram(schWord);
//		termNgE.setTitleNgramEdge(schWord);
//		termNgEb.setTitleNgramEdgeBack(schWord);
		
		termNg.setVocaNgram(schWord);
		termNgE.setVocaNgramEdge(schWord);
		termNgEb.setVocaNgramEdgeBack(schWord);
		
//		prefix.setTitle(schWord);
		prefix.setVoca(schWord);
		ShouldVO sdVO = null;
		List<ShouldVO> shList = new ArrayList<ShouldVO>();
		sdVO = new ShouldVO();
		sdVO.setPrefix(prefix);		
		shList.add(sdVO);
		
		sdVO = new ShouldVO();
		sdVO.setTerm(termNg);
		shList.add(sdVO);
		
		sdVO = new ShouldVO();
		sdVO.setTerm(termNgE);
		shList.add(sdVO);
		
		sdVO = new ShouldVO();
		sdVO.setTerm(termNgEb);
		shList.add(sdVO);
		
		BoolVO bVO = new BoolVO();
		bVO.setShould(shList);
		bVO.setMinimum_should_match(1);
		
		DocVO docVO = new DocVO();
		QueryVO qVO = new QueryVO();
		qVO.setBool(bVO);
		docVO.setQuery(qVO);		
		
		Gson gson = new Gson();
		String jsonStr = gson.toJson(docVO);

		
		return jsonStr;
	}


	/**
	 * 조회된 데이터 중복 제거
	 * @param dataList
	 * @return
	 */
	private List<String> removeDup(List<String> dataList) {
		List<String> resultList = null;
		int x = 0;
		while(x < 1000) {
			
			resultList = new ArrayList<String>();
			for(int i=0; i < dataList.size(); i++) {
				if(!resultList.contains(dataList.get(i))) {
					resultList.add(dataList.get(i));
				}
			}
			x++;
		}
		return resultList;
	}
	
	public String getTopSearch() {
		
		SearchVO schVO = new SearchVO();
		ReqDtVO reqdt = new ReqDtVO();
		RangeVO range = new RangeVO();
		QueryVO query = new QueryVO();
		query.setRange(range);
		range.setReqdt(reqdt);
		
		
		// 최근 20초
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddhhmmss");
		Calendar cal = Calendar.getInstance();
		String curTm = fm.format(cal.getTime());
		cal.add(Calendar.MINUTE, -10);
		String bfTm = fm.format(cal.getTime());
		System.out.println("Time cch : " + bfTm + " - " + curTm);
		reqdt.setGte(curTm);
		reqdt.setLte(bfTm);
		schVO.setSize(0);
		schVO.setQuery(query);
		
		AggsVO aggsVO = new AggsVO();
		TermVO termVO = new TermVO();
		termVO.setField("searchWd");
		Top_5_word top_5_word = new Top_5_word();
		top_5_word.setTerms(termVO);
		aggsVO.setTop_5_word(top_5_word);
		schVO.setAggs(aggsVO);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(schVO);
		
		logger.info("cchlogger jsonString: " + jsonString);
		
		try{
            String responseBody = caller.sendToEs(jsonString);
            if(logger.isDebugEnabled()) {
            	logger.debug("responseBody["+responseBody+"]");
            }
            JsonParser parser = new JsonParser();
            JsonElement rootObject = parser.parse(responseBody)
            .getAsJsonObject().get("aggregations")
            .getAsJsonObject().get("top_5_word")
            .getAsJsonObject().get("buckets");
            
            JsonArray jarr = rootObject.getAsJsonArray();
            JsonObject jHitData = null;
            ResVO rtn = null;
            List<ResVO> list = new ArrayList<ResVO>();
            
            for(int i=0; i<jarr.size(); i++) {
            	rtn = new ResVO();
            	jHitData = jarr.get(i).getAsJsonObject();
            	System.out.println("key : " +jHitData);
            	rtn.setTopKey(jHitData.get("key").getAsString());
            	rtn.setTopCnt(jHitData.get("doc_count").getAsInt());
            	
            	list.add(rtn);
            }
            
            gson = new Gson();
            String rtnStr = gson.toJson(list);
            return rtnStr;

        } catch (Exception e) {
        	e.printStackTrace();
        }
		return null;
		
	}
}
