package com.fwk.es.impl;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fwk.es.CallESService;

@Service("CallESService")
public class CallESServiceImpl implements CallESService {

	private static final Logger logger = LoggerFactory.getLogger(CallESServiceImpl.class);

	@Value("${es.url}")
	private String host;

	@Value("${es.path}")
	private String path;

	@Value("${es.port}")
	private String port;

	private String hostInf;
	private String pathInf;
	private String portStrInf;
	private int portInf;
	
	@Override
	public String sendToEs(String jsonStr) throws IOException, Exception{
		// TODO Auto-generated method stub
		RestClient restClient = null;		
		
		try{
			this.setESEnv();
			//엘라스틱서치에서 제공하는 response 객체
			Response response = null;
			if(logger.isInfoEnabled()) {
				logger.info("jsonString ["+jsonStr+"] ");
			}
			
			restClient = RestClient.builder(
					new HttpHost(hostInf, portInf, "http")).build();
			Request request = new Request("GET", pathInf );
			request.addParameter("pretty", "true");
			request.setEntity(new NStringEntity(jsonStr, ContentType.APPLICATION_JSON));

			response = restClient.performRequest(request);


			//앨라스틱서치에서 리턴되는 응답코드를 받는다
			int statusCode = response.getStatusLine().getStatusCode();
			if(logger.isDebugEnabled()) {
				logger.debug("statusCode" + statusCode);
			}
			//엘라스틱서치에서 리턴되는 응답메시지를 받는다
			String responseBody = EntityUtils.toString(response.getEntity());

			restClient.close();

			
			return responseBody;
		}catch(IOException ioe) {
			logger.error(ioe.getMessage());
			
			throw ioe;
		}catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}finally {
			restClient.close();
		}
	}
	
	private void setESEnv() {
			logger.info("setESEnv");
		    hostInf = new String(Base64.decodeBase64(host));
	        pathInf = new String(Base64.decodeBase64(path));
	        portStrInf = new String(Base64.decodeBase64(port));
	        if(null == portStrInf) {
	        	portInf = 9200;
	        }else {
	        	portInf = Integer.parseInt(portStrInf);
	        }
	        if(logger.isDebugEnabled()) {
		        logger.debug("host ["+host+"] ");
		        logger.debug("path ["+path+"] ");
		        logger.debug("portInf ["+portInf+"] ");
	        }
	}
}