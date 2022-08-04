package com.asdc.smarticle.article;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.articletag.TagRepository;
import com.asdc.smarticle.comutil.ApiError;
import com.asdc.smarticle.comutil.AppConstant;
import com.asdc.smarticle.comutil.ApplicationUrlPath;
import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserRepository;
import com.asdc.smarticle.user.UserService;
import com.asdc.smarticle.user.exception.ArticleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TagRepository tagRepository;
	
	@Autowired
	UserService userService;
	

	@Override
	public Article saveArticle(Article article, String userName) throws ArticleException {

		User user = userRepository.findByUserName(userName);
		article.setUserId(user);

		if (isContentEmpty(article) || isHeadingEmpty(article)) {
			throw new ArticleException(ApiError.ARTICLE_FIELD_NOT_NULL);
		}
 
		return articleRepository.save(article);
	} 
	 
	/** 
	 * @author Vivekkumar Patel 
	 * This method checks that header of the article is empty or not.
	 * @param article instance containng details such as content,heading,visibility etc.
	 * @return true if header of the article is empty else false.
	 */
	public boolean isHeadingEmpty(Article article) {
		return article.getContent() == null ||article.getContent().isEmpty() ;
	} 

	/**
	 * @author Vivekkumar Patel 
	 * This method checks that content of the article is empty or not.
	 * @param article instance containng details such as content,heading,visibility etc.
	 * @return true if content of the article is empty else false.
	 */

	public boolean isContentEmpty(Article article) {
		return article.getHeading() == null || article.getHeading().isEmpty();
	}

	/**
	 * @author Vivekkumar Patel 
	 * This method retrieve list of articles filtered by visibility,user,tags and sort by date or number of likes 
	 * @param visibility Is article visible before login or not.If visibility=1 article is visible before login else not.If visibility=ALL
	 * then data is retrieved isrespective of visibility.
	 * @param filterPojo Instance of FilterPojo containing pagination and metadata to apply filter.
	 * @return List of article after applying sorting and filter with pagination.
	 */
	@Override
	public Page<Article> getArticle(String visibility,FilterPojo filterPojo) throws ArticleException {
		Page<Article> articleList = null;
		Pageable pagination = PageRequest.of(filterPojo.getPage(), filterPojo.getTotalPage(),Sort.by(filterPojo.getSortBy()).descending());
		
		
		 
		try {

			if (visibility.equalsIgnoreCase(ApplicationUrlPath.ALL_ARTICLE)) {
				articleList=getDataWithOutVisibility(filterPojo,pagination);
				
				
			} else {
				articleList=getDataWithVisibility(filterPojo,visibility,pagination);
			}

		} catch (Exception e) {
			throw new ArticleException(ApiError.ARTICLE_NOT_PRESENT);
		}
		
		
		
		return articleList;
	}

	/** 
	 * @author Vivekkumar Patel 
	 * This method retrieve list of articles filtered by visibility,user,tags and sort by date or number of likes 
	 * @param visibility Is article visible before login or not.If visibility=1 article is visible before login else not.
	 * @param filterPojo Instance of FilterPojo containing pagination and metadata to apply filter.
	 * @param pagination Instance of Page containing pagination details details 
	 * @return List of article after applying sorting and filter with pagination.
	 */
	private Page<Article> getDataWithVisibility(FilterPojo filterPojo, String visibility, Pageable pagination) {
		// TODO Auto-generated method stub
		Page<Article> articleList = null;

		boolean isVisibility = visibility.equals("1") ? true : false;

		// articleList = articleRepository.findByVisibility(visibility.equals("1") ?
		// true : false);

		if (isUserListEmpty(filterPojo) && isTagListEmpty(filterPojo)) {
			// Get the data if userlist and tag list is empty
			articleList = articleRepository.findAllByVisibility(isVisibility, pagination);
		} else if (isTagListEmpty(filterPojo) && !isUserListEmpty(filterPojo)) {

			// Get the data if user list is not empty but tag list is empty
			List<User> userList = userService.getUserList(filterPojo.getUserIdList());
			articleList = articleRepository.findAllByUserIdInAndVisibility(userList, isVisibility, pagination);

		} else if (!isTagListEmpty(filterPojo) && isUserListEmpty(filterPojo)) {

			// Get the data if tag list is not empty but user list is empty
			articleList = articleRepository.findAllByTagIdInAndVisibility(filterPojo.getTagList(), isVisibility,
					pagination);

		} else {

			// get the data if tag list and user list is not empty
			List<User> userList = userService.getUserList(filterPojo.getUserIdList());
			articleList = articleRepository.findAllByUserIdInAndTagIdInAndVisibility(userList, filterPojo.getTagList(),
					isVisibility, pagination);
		}

		return articleList;
	}
	
	
	/**
	 * @author Vivekkumar Patel 
	 * This method checks that list of taglist is empty or not in instance of  FilterPojo.
	 * @param filterPojo Instance of FilterPojo containing pagination and metadata to apply filter.
	 * @return true if list of taglist  is empty else false.
	 */
	public boolean isTagListEmpty(FilterPojo filterPojo) {
		return filterPojo.getTagList()!=null  && filterPojo.getTagList().size()==0;
	}

	/**
	 * @author Vivekkumar Patel 
	 * This method checks that list of userid is empty or not in instance of  FilterPojo.
	 * @param filterPojo Instance of FilterPojo containing pagination and metadata to apply filter.
	 * @return true if list of userid  is empty else false.
	 */
	public  boolean isUserListEmpty(FilterPojo filterPojo) {
		return filterPojo.getUserIdList() !=null  && filterPojo.getUserIdList().size()==0;
	}
 
	  
	/**
	 * @author Vivekkumar Patel 
	 * This method retrieve list of articles filtered by user,tags and sort by date or number of likes 
	 * @param filterPojo Instance of FilterPojo containing pagination and metadata to apply filter.
	 * @param pagination Instance of Page containing pagination details details 
	 * @return List of article after applying sorting and filter with pagination.
	 */
	private Page<Article> getDataWithOutVisibility(FilterPojo filterPojo, Pageable pagination) {
		Page<Article> articleList = null;
		if (isUserListEmpty(filterPojo) && isTagListEmpty(filterPojo)) {
			// Get the data if userlist and tag list is empty
			articleList = articleRepository.findAll(pagination);
		} else if (isTagListEmpty(filterPojo) && !isUserListEmpty(filterPojo)) {

			// Get the data if user list is not empty but tag list is empty
			List<User> userList = userService.getUserList(filterPojo.getUserIdList());
			articleList = articleRepository.findAllByUserIdIn(userList, pagination);

		} else if (!isTagListEmpty(filterPojo) && isUserListEmpty(filterPojo)) {

			// Get the data if tag list is not empty but user list is empty
			articleList = articleRepository.findAllByTagIdIn(filterPojo.getTagList(), pagination);

		} else {

			// get the data if tag list and user list is not empty
			List<User> userList = userService.getUserList(filterPojo.getUserIdList());
			articleList = articleRepository.findAllByUserIdInAndTagIdIn(userList, filterPojo.getTagList(), pagination);
		}

		return articleList;
	}

	/**
	 * This method retrieve list of articles based on the id.
	 * @param id is the id of the article
	 * @return List of article based on the id
	 */
	@Override
	public Article getArticleById(Long id) {
		Optional<Article> article = articleRepository.findById(id);
		if (article.isPresent()) {
			return article.get();
		}
		return null;
	}

	/**
	 * This method retrieve list of articles based on the username for retrieveing article based on authors.
	 * @param page is the current page
	 * @return total page is the total number of page that will be shown after applying the pagination
	 */
	@Override
	public Page<Article> getArticleByUser(String userName, int page, int totalPage) {
		Pageable pagination = PageRequest.of(page, totalPage,Sort.by("creationDate"));
		Page<Article> listArticle = null;
		User user = userRepository.findByUserName(userName);
		listArticle = articleRepository.findByUserId(user,pagination);
		return listArticle;
	}

	/**
	 * This method is used to retrieve the count of the tweets containing the tag attached with the particular article.
	 * @param id is the id of the tag whose total tweet count is needed to be found
	 * @return the tweets metadata that were retrieved from the twitter
	 */
	@Override
	public List<Map<String,Object>> getTwitterCountOfArticleTags(Long id){
		if(id==null){
			return new ArrayList<Map<String,Object>>();
		}
		Article article = getArticleById(id);
		if(article == null){
			return new ArrayList<Map<String,Object>>();
		}
		Set<Tag> tags = article.getTagId();
		if(tags.size()==0){
			return new ArrayList<Map<String,Object>>();
		}
		List<String> tagNames = new ArrayList<>();
		Map<String,String> responseTweetTextAndURL = new HashMap<>();
		List<Map<String,Object>> responseTweetData = new ArrayList<>();
		String query = "lang:en (";
		for(Tag tag : tags){
			query += tag.getTagName() +" OR ";
			tagNames.add(tag.getTagName());
		}

		String searchQuery = query.substring(0, query.length()-AppConstant.TOTAL_RECORDS);
		searchQuery += ")";
		Twitter twitter = authentication();

		Query search = new Query(searchQuery);
		search.setCount(AppConstant.TEST_EXP_COUNT_SIZE);
		//search.count(5); 
		int count=0;
		QueryResult tweetData;
		try {
			tweetData = twitter.search(search);

			count = iterateTweet(responseTweetData, count, tweetData);
			setTwitterData(responseTweetData, twitter, search, count);

		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return responseTweetData;
	}

	private int iterateTweet(List<Map<String, Object>> responseTweetData, int count, QueryResult tweetData) {
		for (Status tweet : tweetData.getTweets()) {
			Map<String,Object> tweetDataMap = new HashMap();
			String tweetLink = "https://twitter.com/" + tweet.getUser().getScreenName() + "/status/" + tweet.getId();
			String removeURL = tweet.getText().replaceAll("((https?|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", "");
			String transformedTweetText = removeURL.replaceAll("[^\\w\\s]", "");
			String authorName = tweet.getUser().getName();
			Date creationDate = tweet.getUser().getCreatedAt();
			int retweetCount = tweet.getRetweetCount();
			String userImage = tweet.getUser().getProfileImageURL();
			tweetDataMap.put("userImageURL",userImage);
			tweetDataMap.put("authorName",authorName);
			tweetDataMap.put("tweetLink",tweetLink);
			tweetDataMap.put("tweetText",transformedTweetText);
			tweetDataMap.put("creationDate",creationDate);
			tweetDataMap.put("retweetCount",retweetCount);
			responseTweetData.add(tweetDataMap);
			count++;
		}
		return count;
	}

	private void setTwitterData(List<Map<String, Object>> responseTweetData, Twitter twitter, Query search, int count)
			throws TwitterException {
		if(count!=AppConstant.MAX_TWEET){
			QueryResult extraTweetData = twitter.search(search);
			for(Status tweet : extraTweetData.getTweets()){
				if(count==AppConstant.MAX_TWEET){
					break;
				}
				Map<String,Object> tweetDataMap1 = new HashMap();
				String tweetLink = "https://twitter.com/" + tweet.getUser().getScreenName() + "/status/" + tweet.getId();
				String removeURL = tweet.getText().replaceAll("((https?|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", "");
				String transformedTweetText = removeURL.replaceAll("[^\\w\\s]", "");
				String authorName = tweet.getUser().getName();
				Date creationDate = tweet.getUser().getCreatedAt();
				int retweetCount = tweet.getRetweetCount();
				String userImage = tweet.getUser().getProfileImageURL();
				tweetDataMap1.put("userImageURL",userImage);
				tweetDataMap1.put("authorName",authorName);
				tweetDataMap1.put("tweetLink",tweetLink);
				tweetDataMap1.put("tweetText",transformedTweetText);
				tweetDataMap1.put("creationDate",creationDate);
				tweetDataMap1.put("retweetCount",retweetCount);
				responseTweetData.add(tweetDataMap1);
				count++;
			}
		}
	} 

	/**
	 * This method is used to aunthenticate the twitter and establish the connection with twitter.
	 */
	//Reference: https://www.tabnine.com/code/java/methods/twitter4j.conf.ConfigurationBuilder/setOAuthConsumerKey
	public static Twitter authentication() {
		ConfigurationBuilder confBuild = new ConfigurationBuilder();
		FileInputStream in = null;
		String OAuthConsumerKey="";
		String OAuthConsumerSecret = "";
		String OAuthAccessToken = "";
		String OAuthAccessTokenSecret = "";
		try {
			in = new FileInputStream("src/main/resources/application.properties");
			Properties props = new Properties();
			props.load(in);
			in.close();
			OAuthConsumerKey = props.get("OAuthConsumerKey").toString();
			OAuthConsumerSecret = props.get("OAuthConsumerSecret").toString();
			OAuthAccessToken = props.get("OAuthAccessToken").toString();
			OAuthAccessTokenSecret = props.get("OAuthAccessTokenSecret").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		confBuild.setDebugEnabled(true);
		confBuild.setJSONStoreEnabled(true);
		confBuild.setOAuthConsumerKey(OAuthConsumerKey);
		confBuild.setOAuthConsumerSecret(OAuthConsumerSecret);
		confBuild.setOAuthAccessToken(OAuthAccessToken);
		confBuild.setOAuthAccessTokenSecret(OAuthAccessTokenSecret);
		return new TwitterFactory(confBuild.build()).getInstance();
	}

	/**
	 * This method is used to set the like count and remove the like count if user double taps it.
	 * @param article is the instance of article contains the article details
	 * @param userName contains the unique username of the user who likes/dislike the article
	 */
	@Override
	public void setLike(Article article, String userName) {
		User user = userRepository.findByUserName(userName);
		Article article1 = articleRepository.getById(article.getId());
		if(article1.getLike().contains(user)) {
			article1.getLike().remove(user);
		}else {
			article1.getLike().add(user);
		}
		article1.setLikeCount(article1.getLike().size());
		article1.setLike(article1.getLike());
		articleRepository.save(article1);
	}  
} 
