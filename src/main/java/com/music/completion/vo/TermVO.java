package com.music.completion.vo;

public class TermVO {
	private String titleNgram;
	private String titleNgramEdge;
	private String titleNgramEdgeBack;
	
	private String vocaNgram;
	private String vocaNgramEdge;
	private String vocaNgramEdgeBack;
	
	private String field;

	public String getTitleNgram() {
		return titleNgram;
	}
	public void setTitleNgram(String titleNgram) {
		this.titleNgram = titleNgram;
	}
	public String getTitleNgramEdge() {
		return titleNgramEdge;
	}
	public void setTitleNgramEdge(String titleNgramEdge) {
		this.titleNgramEdge = titleNgramEdge;
	}
	public String getTitleNgramEdgeBack() {
		return titleNgramEdgeBack;
	}
	public void setTitleNgramEdgeBack(String titleNgramEdgeBack) {
		this.titleNgramEdgeBack = titleNgramEdgeBack;
	}
	public String getVocaNgram() {
		return vocaNgram;
	}
	public void setVocaNgram(String vocaNgram) {
		this.vocaNgram = vocaNgram;
	}
	public String getVocaNgramEdge() {
		return vocaNgramEdge;
	}
	public void setVocaNgramEdge(String vocaNgramEdge) {
		this.vocaNgramEdge = vocaNgramEdge;
	}
	public String getVocaNgramEdgeBack() {
		return vocaNgramEdgeBack;
	}
	public void setVocaNgramEdgeBack(String vocaNgramEdgeBack) {
		this.vocaNgramEdgeBack = vocaNgramEdgeBack;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
}
