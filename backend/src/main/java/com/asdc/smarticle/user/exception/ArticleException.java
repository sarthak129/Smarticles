package com.asdc.smarticle.user.exception;
/**
 * Custom exception to notify is user that fields while posting the article cannot be null or blank
 *
 * @author Khushboo Patel
 * @version 1.0
 * @since 2022-02-26
 */
public class ArticleException extends Exception{

    private String message;

    public ArticleException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
