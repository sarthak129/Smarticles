package com.asdc.smarticle.articletag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Vivekkumar Patel
 * @version 1.0
 * @since 2022-03-27
 */
@Service
public class TagServiceImpl implements TagService {

	@Autowired
	TagRepository articletagRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TagRepository tagRepository;
	
	@Autowired
	private TagFactory tagFactory;
 
	@Override
	public List<Tag> getTags(String userName) {
		User user = null;
		if (!userName.isEmpty()) {
			user = userRepository.findByUserName(userName);
		}
		
		
		if (userName.isEmpty() || isUserTagsExist(user)) {
			return articletagRepository.findAll(); 
		} else {
			Set<Tag> setOfTags = user.getTags();
			return new ArrayList<>(setOfTags);
		}
	} 
	/**
	 * @author Vivekkumar Patel 
	 * This method checks that does user has set tags for thier preference or not.
	 * @param user instnace of the use class .
	 * @return true if tags are set as a user's preference else false.
	 */
	public boolean isUserTagsExist(User user) {
		return user != null && user.getTags().isEmpty();
	} 

	@Override
	public List<Tag> createArticleTag(String tagName) { 

		//Tag tag = new Tag();
		
		Tag tag=tagFactory.getTagInstance();

		tag.setTagName(tagName);
		tagRepository.save(tag);

		List<Tag> listOfTags = tagRepository.findAll();

		return listOfTags;
	} 

	/**
	 * @author Vivekkumar Patel
	 * Service to retrieve all 
	 * @return List<Tag> containing all tags details.
	 */
	@Override
	public List<Tag> retrieveAllTags() {

		return tagRepository.findAll();
	}
}
