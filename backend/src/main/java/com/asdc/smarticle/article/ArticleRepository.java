package com.asdc.smarticle.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.user.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	Optional<Article> findById(Long id);

	List<Article> findAllByOrderByCreationDateAsc();
	
	Page<Article> findAllByVisibility(boolean visibility, Pageable pagination);

	Page<Article> findByUserId(User user, Pageable pagination);

	Page<Article> findAll(Pageable pagination);
	
	// Get the data by visibility,user and tags associated with articles
	Page<Article> findAllByUserIdInAndTagIdInAndVisibility(List<User> users, Set<Tag> tags, boolean visibility,
			Pageable pagination);

	Page<Article> findAllByUserIdInAndVisibility(List<User> users, boolean visibility, Pageable pagination);

	Page<Article> findAllByTagIdInAndVisibility(Set<Tag> tags, boolean visibility, Pageable pagination);

	// Get the data by user and tags associated with articles
	Page<Article> findAllByUserIdInAndTagIdIn(List<User> users, Set<Tag> tags, Pageable pagination);

	Page<Article> findAllByUserIdIn(List<User> users, Pageable pagination);

	Page<Article> findAllByTagIdIn(Set<Tag> tags, Pageable pagination);
		
}
