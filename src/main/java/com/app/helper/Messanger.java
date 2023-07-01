package com.app.helper;

import org.springframework.stereotype.Component;

@Component
public class Messanger {
	
	private String content;
	private String type;
	public Messanger(String content, String type) {
		this.content = content;
		this.type = type;
	}
	
	public Messanger() {

	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
