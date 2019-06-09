package com.music.completion.vo;

import java.util.List;

public class BoolVO {
	private List<ShouldVO> should;
	private int minimum_should_match;

	public List<ShouldVO> getShould() {
		return should;
	}

	public void setShould(List<ShouldVO> should) {
		this.should = should;
	}

	public int getMinimum_should_match() {
		return minimum_should_match;
	}

	public void setMinimum_should_match(int minimum_should_match) {
		this.minimum_should_match = minimum_should_match;
	}
}
