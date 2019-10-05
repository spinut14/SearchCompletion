package com.kordic.kdy.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kordic.kdy.vo.SearchWord;

//@RunWith(SpringJunit4ClassRunner.class)
public class SearchControllerTest {

	
	@Autowired
	private HomeKDYController kdycontroller;
	
	@Test
	public void test() throws Exception {
		System.out.println("test");
		SearchWord sw = new SearchWord("의자");
		kdycontroller.searchDictionary(sw);
		
	}
}
