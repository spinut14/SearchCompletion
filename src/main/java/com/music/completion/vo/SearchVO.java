package com.music.completion.vo;

public class SearchVO {

	private String schWord;
	private int size;
	private AggsVO aggs;
	private QueryVO query;

	public String getSchWord() {
		return schWord;
	}

	public void setSchWord(String schWord) {
		this.schWord = schWord;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public AggsVO getAggs() {
		return aggs;
	}

	public void setAggs(AggsVO aggs) {
		this.aggs = aggs;
	}

	
	public QueryVO getQuery() {
		return query;
	}

	public void setQuery(QueryVO query) {
		this.query = query;
	}

	@Override
	public String toString() {
		return "SearchVO [schWord=" + schWord + "]";
	}
	
	
}
