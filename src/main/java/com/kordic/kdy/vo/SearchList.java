package com.kordic.kdy.vo;

import java.util.ArrayList;
import java.util.List;

public class SearchList {

	private List searchList;

	public SearchList() {
		searchList = new ArrayList<Word>();
	}

	public void addSearchWord(Word word) {
		searchList.add(word);
	}

	public List getSearchResults() {
		return this.searchList;
	}
}
