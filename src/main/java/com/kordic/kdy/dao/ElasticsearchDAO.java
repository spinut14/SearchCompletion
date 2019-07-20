package com.kordic.kdy.dao;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kordic.kdy.vo.SearchList;
import com.kordic.kdy.vo.Word;


@Repository
public class ElasticsearchDAO {
	
	private HttpHost httphost;
	private RestClient restClient;
		
	public ElasticsearchDAO() {}

	public List searchContext(String searchWord) {
		String url = "52.78.51.15";
		String searchIndex = "dic_kor";
		httphost = new HttpHost(url, 9200, "http");
		restClient = RestClient.builder(httphost).build();
		Request request = new Request("GET","/"+searchIndex+"/_search");
		request.addParameter("pretty", "true");
		Response response ;
		request.setJsonEntity(makeSearchJsonTemplate(searchWord,5));
		try {
			response = restClient.performRequest(request);
			int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("status Code : " + statusCode);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("response Body : " + responseBody);
            
            return makeResultList(responseBody);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Arrays.asList();
	}
	//json형식으로 분리 
	private List makeResultList(String jsonResult) {
		SearchList sl = new SearchList(); 
		
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonResult);
		
		JsonElement hits = element.getAsJsonObject().get("hits").getAsJsonObject();
		JsonElement result= hits.getAsJsonObject().get("hits").getAsJsonArray();
		
		System.out.println("result : "+result);
		
		for(int i=0;i<result.getAsJsonArray().size();i++) {
			Gson gson = new Gson();
			Word wd = gson.fromJson(result.getAsJsonArray().get(i).getAsJsonObject().get("_source").toString(),Word.class); 
			
			System.out.println("category : "+wd.getCategory());
			sl.addSearchWord(wd);
		}
//		System.out.println(result.getAsJsonArray().get(0).getAsJsonObject().get("_source"));
		System.out.println(sl.getSearchResults().size());
		return sl.getSearchResults();
	}
	
	
	private String makeSearchJsonTemplate(String str,int number) {
		String search = str;

//		String query = "{\n" + 
//				"  \"query\": {\n" + 
//				"    \"bool\": {\n" + 
//				"      \"should\": [\n" + 
//				"        {\n" + 
//				"          \"term\": {\n" + 
//				"            \"meaning\": \""+str+"\"\n" + 
//				"          }\n" + 
//				"        }\n" + 
//				"      ]\n" + 
//				"    }\n" + 
//				"  },\"size\":"+number+"\n" + 
//				"}";
		String serverQuery = 
				"{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"should\": [\n" + 
				"        {\n" + 
				"          \"term\": {\n" + 
				"            \"vocaNgram\": \""+search+"\"\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\"size\":"+number+"\n" + 
				"}\n" + 
				"";
				
		return serverQuery;
	}
	
}
