package com.asdc.smarticle.articletag;


import javax.persistence.*;
/**
 * @author Khushboo Patel
 * @version 1.0
 * @since 2022-02-28
 */

@Entity
@Table(name = "tags")
public class Tag{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String tagName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

 
    
    
}
