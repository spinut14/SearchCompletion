package com.music.completion.vo;

public class ResVO {
	private String musicTitle;
	private String voca;
	private String meaning;
	private String isNative;
	private String category;
	private String _id;
	private String _index;
	private String _type;
	
	
	// 실시간 검색어
	private String topKey;
	private int topCnt;
	

	public String getMusicTitle() {
		return musicTitle;
	}

	public void setMusicTitle(String musicTitle) {
		this.musicTitle = musicTitle;
	}

	public String getVoca() {
		return voca;
	}

	public void setVoca(String voca) {
		this.voca = voca;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getIsNative() {
		return isNative;
	}

	public void setIsNative(String isNative) {
		this.isNative = isNative;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_index() {
		return _index;
	}

	public void set_index(String _index) {
		this._index = _index;
	}

	public String get_type() {
		return _type;
	}

	public void set_type(String _type) {
		this._type = _type;
	}

	public String getTopKey() {
		return topKey;
	}

	public void setTopKey(String topKey) {
		this.topKey = topKey;
	}

	public int getTopCnt() {
		return topCnt;
	}

	public void setTopCnt(int topCnt) {
		this.topCnt = topCnt;
	}
	
}
