package com.kordic.cch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kordic.cch.service.CallESService;
import com.music.completion.vo.BoolVO;
import com.music.completion.vo.DocVO;
import com.music.completion.vo.PrefixVO;
import com.music.completion.vo.QueryVO;
import com.music.completion.vo.ResVO;
import com.music.completion.vo.ShouldVO;
import com.music.completion.vo.TermVO;

@Service("CallESService")
public class CallESServiceImpl implements CallESService {

	/**
	 * Call Elasticsearch API
	 */
	@Override
	public String searchKorDict(Object obj, String jsonData) {
		String host = "52.78.51.15";
		String url = "dic_kor/_search";
//		String url = "music_title/_search";
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
            Request request = new Request("GET", url );
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
//            System.out.println("status Code : " + statusCode);
            //엘라스틱서치에서 리턴되는 응답메시지를 받는다
            String responseBody = EntityUtils.toString(response.getEntity());
//            System.out.println("response Body : " + responseBody);
            
            restClient.close();
            JsonParser parser = new JsonParser();
            JsonElement rootObject = parser.parse(responseBody)
            .getAsJsonObject().get("hits")
            .getAsJsonObject().get("hits");
            
            JsonArray jarr = rootObject.getAsJsonArray();
//            System.out.println(jarr.size());
            JsonObject jHitData = null;
            JsonObject jData = null;
            ResVO rtn = null;
            List<ResVO> list = new ArrayList<ResVO>();
            List<String> dupChkList = new ArrayList<String>();
            
            for(int i=0; i<jarr.size(); i++) {
            	rtn = new ResVO();
            	jHitData = jarr.get(i).getAsJsonObject();
            	jData = jHitData.getAsJsonObject("_source");
//            	System.out.println(jData.get("musicTitle").getAsString());
            	dupChkList.add(jData.get("voca").getAsString());
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
//            System.out.println("rtnStr " + rtnStr);
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
}
