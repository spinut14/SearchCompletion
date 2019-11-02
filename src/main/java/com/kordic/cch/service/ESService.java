package com.kordic.cch.service;

public interface ESService {

	/**
	 * Call Elasticsearch API
	 * Word AutoComplete
	 * @param obj
	 * @param jsonData
	 * @return
	 */
	public String autoComplete(Object obj, String jsonData);
	
	/**
	 * Call Elasticsearch API
	 * Search Korean Word
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
	
	
	/**
	 * getTopSearch Data
	 * @param obj
	 * @param jsonData
	 * @return
	 */
	public String getTopSearch();
}
