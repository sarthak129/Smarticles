package com.asdc.smarticle;


import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.articletag.TagFactory;
import com.asdc.smarticle.articletag.TagRepository;
import com.asdc.smarticle.articletag.TagService;
import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TagServiceUnitTest {

	 


	@MockBean
	private TagRepository tagRepo;// Dependency is mocked.

	@MockBean
	private UserRepository userRepository;
	
	@Autowired
	TagService tagService;
	
	@MockBean
	private User user;
	
	@MockBean
	private Tag tag;
	
	@MockBean
	private TagFactory tagFactory;
	
	
	@Test
	void userTagExist() {
		Set<Tag> tags = new HashSet<>();

		Mockito.when(user.getTags()).thenReturn(tags);
		Assert.assertTrue(tagService.isUserTagsExist(user));

		Assert.assertFalse(tagService.isUserTagsExist(null));

		Mockito.when(user.getTags()).thenReturn(tags);
		Assert.assertTrue(tagService.isUserTagsExist(user));
		
 		tag.setTagName("BLOCK");
		tags.add(tag);
		Mockito.when(user.getTags()).thenReturn(tags);
		Assert.assertFalse(tagService.isUserTagsExist(user));
	}
	
	@Test
	void getTags() {
		
		List<Tag> tags = new ArrayList<>();
		
		Set<Tag> tagSet=new HashSet<>();
		
		Mockito.when(userRepository.findByUserName("vivek")).thenReturn(user);
		Mockito.when(user.getTags()).thenReturn(tagSet);
		Assert.assertEquals(tags, tagService.getTags("vivek"));
		
		
		
		Mockito.when(tagRepo.findAll()).thenReturn(tags);
		//Mockito.when(tagService).isUserTagsExist(null).thenReturn(false);
		Mockito.doReturn(false).when(Mockito.spy(tagService)).isUserTagsExist(null);
		Assert.assertEquals(tags, tagService.getTags(""));
		Mockito.doReturn(true).when(Mockito.spy(tagService)).isUserTagsExist(user);
		Assert.assertEquals(tags, tagService.getTags(""));
//		Mockito.when(tagService.isUserTagsExist(user)).thenReturn(true);
//		Assert.assertEquals(tags, tagService.getTags("vivek"));
		
		Mockito.doReturn(false).when(Mockito.spy(tagService)).isUserTagsExist(null);
		Assert.assertEquals(tags, tagService.getTags("vivek"));
		Mockito.doReturn(true).when(Mockito.spy(tagService)).isUserTagsExist(user);
		Assert.assertEquals(tags, tagService.getTags("vivek"));
	}

	
	@Test
	void testRetrieveAllTags() {
		
		List<Tag> tags = new ArrayList<>();
		Mockito.when(tagRepo.findAll()).thenReturn(tags);
		Assert.assertEquals(tags, tagService.retrieveAllTags());
  }
	
	@Test
	void createArticleTag() {

		List<Tag> tags = new ArrayList<>();
		Mockito.when(tagFactory.getTagInstance()).thenReturn(tag);
		tag.setTagName("BLOCKCHAIN");
		Mockito.when(tagRepo.save(tag)).thenReturn(tag);
		Mockito.when(tagRepo.findAll()).thenReturn(tags);
		Assert.assertEquals(tags, tagService.createArticleTag("BLOCKCHAIN"));
	}
}  
 