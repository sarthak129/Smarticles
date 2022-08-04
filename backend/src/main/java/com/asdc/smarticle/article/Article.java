package com.asdc.smarticle.article;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.user.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/** This class represents the model of the article entity
* the data are stored in articles table
 * */
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String heading;

    @Column(columnDefinition = "text")
    private String content;

    @Column
    private boolean visibility;

    @Column
    @CreationTimestamp
    private Date creationDate;
 
    @Column
    @CreationTimestamp
    private Date updationDate;


    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private User userId;
    
	@ManyToMany
	@JoinTable(name = "article_tags", joinColumns = @JoinColumn(name = "articles_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
	private Set<Tag> tagId = new HashSet<Tag>();
	
	@ManyToMany
	@JoinTable(name = "article_like", joinColumns = @JoinColumn(name = "articles_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private Set<User> like = new HashSet<User>();

	@Column(columnDefinition = "integer default 0")
  	private int likeCount;

	//getter and setter
	public Long getId() {
		return id;
	}

	public Set<User> getLike() {
		return like;
	}

	public void setLike(Set<User> like) {
		this.like = like;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdationDate() {
		return updationDate;
	}

	public void setUpdationDate(Date updationDate) {
		this.updationDate = updationDate;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Set<Tag> getTagId() {
		return tagId;
	}

	public void setTagId(Set<Tag> tagId) {
		this.tagId = tagId;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	
	
}
