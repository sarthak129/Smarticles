package com.asdc.smarticle;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.comutil.AppConstant;
import com.asdc.smarticle.twittertagcount.TwitterTagCountService;
import com.asdc.smarticle.twittertagcount.TwitterTagCountServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TwitterTagServiceUnitTests {

	@Autowired
    private TwitterTagCountService twitterTagCountService;

    @Test
    public void testNullGetTwitterTagCount() throws Exception{
        Set<Tag> tags = null;
        twitterTagCountService = new TwitterTagCountServiceImpl();
        //Denoting the response is empty list.
        int expectedSize = 0;
        int actualSize = twitterTagCountService.getTwitterTagCount(tags).size();
        Assert.assertEquals(expectedSize,actualSize);

    }

    @Test
    public void testPositiveGetTwitterTagCount() throws Exception{
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag();
        tag1.setTagName("Web");
        tag1.setId(Long.valueOf(1));
        tags.add(tag1);
        twitterTagCountService = new TwitterTagCountServiceImpl();

        //Denoting the response is not empty
        Assert.assertFalse(twitterTagCountService.getTwitterTagCount(tags).isEmpty());
    }

    @Test
    public void testGetTwitterTagCount() throws Exception{
        Set<Tag> tags = new HashSet<>();


        Tag tag1 = new Tag();
        tag1.setTagName("Web");
        tag1.setId(Long.valueOf(1));
        Tag tag2 = new Tag(); 
        tag2.setTagName("Cloud");
        Tag tag3 = new Tag();
        tag3.setTagName("Sports");
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);

        twitterTagCountService = new TwitterTagCountServiceImpl();

        Assert.assertEquals(AppConstant.TEST_TAG_COUNT_SIZE,twitterTagCountService.getTwitterTagCount(tags).size());
    }

    @Test
    public void testNegativeGetTwitterTagCount() throws Exception{
        Set<Tag> tags = new HashSet<>();
        Tag tag = new Tag();
        //tagName is not set so there is essentially no tag name here to fetch the tweet count of this tag
        tags.add(tag);
        twitterTagCountService = new TwitterTagCountServiceImpl();
        Integer expectedTweetCount = 0;
        //Since the size is empty it is clear that the actual tweet count is 0
        Integer actualTweetCount = (Integer) twitterTagCountService.getTwitterTagCount(tags).size();
        Assert.assertEquals(expectedTweetCount,actualTweetCount);
    }
}