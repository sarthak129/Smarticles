package com.asdc.smarticle.twittertagcount;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.comutil.ApplicationUrlPath;
import com.asdc.smarticle.httpresponse.BaseController;
import com.asdc.smarticle.httpresponse.ResponseVO;
import com.asdc.smarticle.security.JwtUtils;
import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserService;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



import java.util.*;

/**
 * @author Rushi Patel
 * @version 1.0
 * @since 2022-03-18
 */
@CrossOrigin
@RestController
@RequestMapping("/smarticleapi/twittertagcount")
public class TwitterTagCountController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    TwitterTagCountService twitterTagCountService;

    /**
     * This method will retrieve the tag followed by the user
     * @param http header containing jwt token to validate the user.
     * @return the user tag count by retrieving the tweet metadata
     * */
    @GetMapping(ApplicationUrlPath.GET_USER_TAGS)
    public List<Map<String,Object>> fetchUserTags(@RequestHeader HttpHeaders http) {
        if(http ==null){
            return new ArrayList<>();
        }
        List<Map<String,Object>> responseData = new ArrayList<>();
        try {
            String jwtToken = http.getFirst("jwt-token");
            if (!jwtToken.isEmpty()) {
                String userName = jwtUtils.getUserNameFromJwt(jwtToken);

                User user = userService.getUserByUserName(userName);
                Set<Tag> userTags = user.getTags();
                responseData = twitterTagCountService.getTwitterTagCount(userTags);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseData;
    }
}