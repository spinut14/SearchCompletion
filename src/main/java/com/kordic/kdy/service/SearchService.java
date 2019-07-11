package com.kordic.kdy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kordic.kdy.dao.ElasticsearchDAO;

@Service
public class SearchService {

	@Autowired
	ElasticsearchDAO edao;

	public List getSearchContext(String str) {
		List list= edao.searchContext(str);
		return list;
	}

}
