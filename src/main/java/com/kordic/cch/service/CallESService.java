package com.kordic.cch.service;

public interface CallESService {

	/**
	 * Call Elasticsearch API
	 * inquiry Json Data
	 * @param obj
	 * @param jsonData
	 * @return
	 */
	public String searchKorDict(Object obj, String jsonData);
	
	/**
	 * Make Json String
	 * @param schWord
	 * @return
	 */
	public String makeJsonStr(String schWord);
}
