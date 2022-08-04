package com.asdc.smarticle.twittertagcount;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.comutil.AppConstant;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Implementing the tag count functionlity.
 * @author Rushi Patel
 * @version 1.0
 * @since 2022-03-22
 */
@Service
public class TwitterTagCountServiceImpl implements TwitterTagCountService{

    /**This metho will return the tweets counts which contains the tags associated with the user preferencee
     * @param tags is the set of tags preferenced by the user
     * @returns metadata alongwith the tweet counts comtining the tag*/
    @Override
    public List<Map<String,Object>> getTwitterTagCount(Set<Tag> tags) throws Exception {

        FileInputStream in = null;
        Properties props=null;
        String url = "";
        String getMethod = "";
        String authorizationKey= " ";
        String authorizationValue ="";
        String cookieKey = " ";
        String cookieValue = "";

        try {
            in = new FileInputStream("src/main/resources/application.properties");
            props = new Properties();
            props.load(in);
            
         
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(tags == null || tags.isEmpty()){
            return new ArrayList<>();
        }
        HashMap<String,Integer> tagCount = new HashMap<>();
        List<String> tagNameList = new ArrayList<>();
        List<Map<String,Object>> finalResponse = new ArrayList<>();
        ieterateTag(tags, tagNameList,
				finalResponse,props); 

        int n = finalResponse.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < (n - i-1); j++) {
                if ((int) finalResponse.get(j).get("tweetCount") < (int) finalResponse.get(j+1).get("tweetCount")) {
                    Map<String, Object> temp = finalResponse.get(j);
                    finalResponse.set(j, finalResponse.get(j+1));
                    finalResponse.set(j+1, temp);
                }
            }
        }
        
        in.close();
        return finalResponse;
    } 

	private void ieterateTag(Set<Tag> tags,List<String> tagNameList,
			List<Map<String, Object>> finalResponse,Properties props) {
		
		
		String   url = props.get("url").toString();
		String   getMethod = props.get("getMethod").toString();
		String authorizationKey = props.get("authorization.key").toString();
		String authorizationValue = props.get("authorization.value").toString();
		String cookieKey = props.get("cookie.key").toString();
		String cookieValue = props.get("cookie.value").toString();
		
		for(Tag tag : tags){
            Map<String,Object> data = new HashMap<>(); 
            String tagName = tag.getTagName();
            if(tagName==null){
                continue;
            }
            tagNameList.add(tagName);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url+tagName)
                    .method(getMethod, null)
                    .addHeader(authorizationKey, authorizationValue)
                    .addHeader(cookieKey, cookieValue)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.peekBody(AppConstant.BYTE_COUNT).string());
                System.out.println(jsonObject.toString());
                org.json.JSONArray jsonArray = jsonObject.getJSONArray("data");

                iterateJsonArray(finalResponse, data, tagName, jsonArray);
            } catch (IOException e) {
                e.printStackTrace(); 
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
	}

	private void iterateJsonArray(List<Map<String, Object>> finalResponse, Map<String, Object> data, String tagName,
			org.json.JSONArray jsonArray) {
		for(int i=0;i<jsonArray.length();i++){
		    if(i==jsonArray.length()-1){
		        org.json.JSONObject jsonObject1 = (org.json.JSONObject) jsonArray.get(i);
		        Integer tweetCount = (Integer)jsonObject1.get("tweet_count");
		        data.put("tagName",tagName);
		        data.put("tweetCount",tweetCount);
		        finalResponse.add(data);
		    }
		}
	}
}
