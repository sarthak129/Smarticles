package com.asdc.smarticle.twittertagcount;

import com.asdc.smarticle.articletag.Tag;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**Service interface to retrieve the tag count*/

public interface TwitterTagCountService {
    List<Map<String,Object>> getTwitterTagCount(Set<Tag> tags) throws Exception;
}
