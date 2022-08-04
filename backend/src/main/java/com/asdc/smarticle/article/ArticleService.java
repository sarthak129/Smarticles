package com.asdc.smarticle.article;

import com.asdc.smarticle.user.exception.ArticleException;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

public interface ArticleService {

    Article saveArticle(Article postArticle,String useName)  throws ArticleException;

    Page<Article> getArticle(String visibility,FilterPojo filterPojo) throws ArticleException;

	Article getArticleById(Long id);

	Page<Article> getArticleByUser(String userName, int page, int totalPage);

	List<Map<String,Object>> getTwitterCountOfArticleTags(Long id);

	void setLike(Article article, String userName);
	
	boolean isUserListEmpty(FilterPojo filterPojo) ;
	
	boolean isTagListEmpty(FilterPojo filterPojo);
	}
