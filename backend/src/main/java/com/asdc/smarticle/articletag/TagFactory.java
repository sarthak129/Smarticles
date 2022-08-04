package com.asdc.smarticle.articletag;

import org.springframework.stereotype.Component;

@Component
public class TagFactory {

	public Tag getTagInstance() {
		
		return new Tag();
	}
	
} 
