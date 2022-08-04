package com.asdc.smarticle.article;

import java.util.List;
import java.util.Set;

import com.asdc.smarticle.articletag.Tag;

/**This FilterPojo class contains pagination and metadata that is to be applied for filter.
 * */
public class FilterPojo {

	private int page;
	
	private int totalPage;
	
	private String sortBy; 
	
	private Set<Tag> tagList;
	
	private List<Long> userIdList;

	public Set<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(Set<Tag> tagList) {
		this.tagList = tagList;
	}

	public List<Long> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
	
	
	
	
}
