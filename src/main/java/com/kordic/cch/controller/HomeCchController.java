package com.kordic.cch.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
public class HomeCchController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeCchController.class);
	
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
//	{"query":
//	{"bool":
//	{"should":[{"prefix":{"title":"노래"}},
//	           {"term":{"titleNgram":"노래"}},
//	           {"term":{"titleNgramEdge":"노래"}},
//	           {"term":{"titleNgramEdgeBack":"노래"}}],
//		"minimum_should_match":1}
//	}
//	}
	
	@RequestMapping(value="/search", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	public @ResponseBody String search(SearchVO inVO){
		System.out.println(inVO.toString());
		String rtnStr = "";
		String jsonStr = makeJsonStr(inVO.getSchWord());
		
//		private void callElasticApi(String method, String url, Object obj, String jsonData) {
		try {
			rtnStr = callElasticApi("GET", "/music_title/_search", null, jsonStr);
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		return rtnStr;
	}
	
	private String makeJsonStr(String schWord) {
		TermVO termNg = new TermVO();
		TermVO termNgE = new TermVO();
		TermVO termNgEb = new TermVO();
		PrefixVO prefix = new PrefixVO();
		termNg.setTitleNgram(schWord);
		termNgE.setTitleNgramEdge(schWord);
		termNgEb.setTitleNgramEdgeBack(schWord);
		prefix.setTitle(schWord);
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

		
		System.out.println(jsonStr);
		return jsonStr;
	}
	
	private String callElasticApi(String method, String url, Object obj, String jsonData) throws Exception {
		String host = "13.125.238.20";
		int port = 9200;
		try{
            //엘라스틱서치에서 제공하는 response 객체
            Response response = null;
            String jsonString;
            Gson gson = null;
            if(null != jsonData) {
            	jsonString = jsonData;
            }else {
            	
            	gson = new Gson();
                jsonString = gson.toJson(obj);
            }
            System.out.println("jsonString : " +jsonString);
            RestClient restClient = RestClient.builder(
            	    new HttpHost(host, port, "http")).build();
            Request request = new Request(method, url);
            request.addParameter("pretty", "true");
            request.setEntity(new NStringEntity(jsonString, ContentType.APPLICATION_JSON));
            
            response = restClient.performRequest(request);
            
            
//            curl -XGET "http://localhost:9200/sch_lyrics/_analyze" -H 'Content-Type: application/json' -d'
//            {
//              "analyzer": "lyric_analyzer",
//              "text" :"진정인"
//            }'
            //앨라스틱서치에서 리턴되는 응답코드를 받는다
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("status Code : " + statusCode);
            //엘라스틱서치에서 리턴되는 응답메시지를 받는다
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("response Body : " + responseBody);
            
            restClient.close();
            JsonParser parser = new JsonParser();
            JsonElement rootObject = parser.parse(responseBody)
            .getAsJsonObject().get("hits")
            .getAsJsonObject().get("hits");
            
            JsonArray jarr = rootObject.getAsJsonArray();
            System.out.println(jarr.size());
            JsonObject jHitData = null;
            JsonObject jData = null;
            ResVO rtn = null;
            List<ResVO> list = new ArrayList<ResVO>();
            List<String> dupChkList = new ArrayList<String>();
            
            for(int i=0; i<jarr.size(); i++) {
            	rtn = new ResVO();
            	jHitData = jarr.get(i).getAsJsonObject();
            	jData = jHitData.getAsJsonObject("_source");
            	System.out.println(jData.get("musicTitle").getAsString());
            	dupChkList.add(jData.get("musicTitle").getAsString());
            }
            
            		// 2. 중복제거
            List<String> uniqueList = this.removeDup(dupChkList);
            
            for(String str : uniqueList) {
            	rtn = new ResVO();
            	rtn.setMusicTitle(str);
            	list.add(rtn);
            }
        	
            gson = new Gson();
            String rtnStr = gson.toJson(list);
            System.out.println("rtnStr " + rtnStr);
            return rtnStr;

        } catch (Exception e) {
        	e.printStackTrace();
        }
		return null;
	}
	
	
	private List<String> removeDup(List<String> dataList) {
		List<String> resultList = null;
		int x = 0;
		while(x < 1000) {
			long starttime = System.nanoTime();
			
			resultList = new ArrayList<String>();
			for(int i=0; i < dataList.size(); i++) {
				if(!resultList.contains(dataList.get(i))) {
					resultList.add(dataList.get(i));
				}
			}
			long endtime = System.nanoTime();
			long estimatedTime = endtime - starttime;
			System.out.println(estimatedTime);
			x++;
		}
		return resultList;
	}
}
