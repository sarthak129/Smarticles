package com.asdc.smarticle.user.userVo;

import org.springframework.stereotype.Component;

@Component
public class UserProfileRespVoFactory {

	public UserProfileRespVo getUserProfileRespVoInstance() {
		return new UserProfileRespVo();
	}
}
