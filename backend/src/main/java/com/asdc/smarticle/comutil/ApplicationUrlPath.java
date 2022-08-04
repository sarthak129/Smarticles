package com.asdc.smarticle.comutil;

/**
 * URL constant to map request to controller.
 * 
 * @author Vivekkumar Patel, Sarthak Patel
 * @version 1.0
 * @since 2022-02-19
 */
public class ApplicationUrlPath {

	public static final String USER_REGISTER_REQ_PATH = "/register";

	public static final String USER_ACCOUNT_ACTIVATION_REQ_PATH = "/activateAccount";

	public static final String USER_LOGIN = "/login";

	public static final String USER_LOGOUT = "/logout";

	public static final String USER_FORGOT_PASSWORD = "/forgotPassword";

	public static final String SET_PASSWORD_PATH = "/resetPassword";

	public static final String USER_POST_REQ_PATH = "/postarticle";

	public static final String RETRIEVE_ARTICLE = "/retrieveArticle";

	/*
	 * public static final String RETRIEVE_POST_ARTICLE = "/retrievePostArticle";
	 * 
	 * public static final String RETRIEVE_PUBLIC_AND_PRIVATE_ARICLE =
	 * "/retriveRelatedArticle";
	 */
	public static final String ALL_ARTICLE = "ALL";

	public static final String RETRIEVE_USER_PREF_TAG = "/retriveTags";
	
	public static final String RETRIEVE_ALL_TAG = "/retriveAllTags";

	public static final String GET_ARTICLE_BY_ID = "/getArticleById";

	public static final String SAVE_USER_TAG_PREFERENCE = "/saveUserTagPref";

	public static final String GET_USER_PROFILE = "/getUserProfile";

	public static final String UPDATE_USER_PROFILE = "/updateUserProfile";
	public static final String GET_ARTICLE_BY_USER = "/getArticleByUser";

	public static final String CREATE_ARTICLE_TAG = "/createArticleTag";

	public static final String GET_USER_TAGS = "/getUserTags";

	public static final String GET_TWEET_DATA_BY_ARTICLE_ID= "/getTweetDataOfArticle";

	public static final String GET_USER_DETAILS_POSTED_ARTICLE = "/getUserDetailsPostedArticle";

	public static final String SET_LIKE = "/setLike";
}
