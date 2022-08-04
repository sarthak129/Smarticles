package com.asdc.smarticle.articletag;

import com.asdc.smarticle.comutil.ApplicationUrlPath;
import com.asdc.smarticle.httpresponse.BaseController;
import com.asdc.smarticle.httpresponse.ResponseVO;
import com.asdc.smarticle.security.JwtUtils;
import com.asdc.smarticle.user.User;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Vivekkumar Patel
 * @version 1.0
 * @since 2022-03-27
 */
@CrossOrigin
@RestController
@RequestMapping("/smarticleapi/tag")
public class TagController extends BaseController {
 
	@Autowired
	TagService tagService;
	
	@Autowired
	JwtUtils jwtUtils;

	/**
	 * Fetch the tags associated with user .
	 *
	 * @param http request header
	 * @return list of tags.
	 */
	@GetMapping(ApplicationUrlPath.RETRIEVE_USER_PREF_TAG)
	public List<Tag> getUserTag(@RequestHeader HttpHeaders http) {
		List<Tag> tags = null;
		String jwtToken = http.getFirst("jwt-token");
		String userName = "";
		if (jwtUtils.isTokenEmpty(jwtToken) && jwtUtils.isTokenUndefined(jwtToken)) {
			userName = jwtUtils.getUserNameFromJwt(jwtToken);			
		}
		tags = tagService.getTags(userName);
		return tags;
	}

	
	/**
	 * @author Vivekkumar Patel
	 * Create tag with the given tag name
	 * @param http header containing jet token to validate the user.
	 * @return List<Tag> containing all tags details.
	 */
	@PostMapping(ApplicationUrlPath.CREATE_ARTICLE_TAG)
	public ResponseVO<List<Tag>> updateUserProfile(@RequestHeader HttpHeaders http,String tagName) {
		List<Tag> tagList= null;
		try {
			String jwtToken = http.getFirst("jwt-token");
			if (!jwtToken.isEmpty()) {
				tagList = tagService.createArticleTag(tagName);
				if (tagList == null) {
					return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
							false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareSuccessResponse(tagList);
	}
	
	
	/**
	 * @author Vivekkumar Patel
	 * Retrieve all tags exist in the system
	 * @return List<Tag> containing all tags details.
	 */
	@GetMapping(ApplicationUrlPath.RETRIEVE_ALL_TAG)
	public List<Tag> getTag(@RequestHeader HttpHeaders http) {
		List<Tag> tags = null;
		tags = tagService.retrieveAllTags();
		return tags;
	}
}
