package com.asdc.smarticle.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.asdc.smarticle.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Sarthak Patel
 * @version 1.0
 * @since 2022-02-22
 */

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	private Long id;

	private String username;

	private String email;

	@JsonIgnore
	private String password;

	//private Collection<? extends GrantedAuthority> authorities;
	private final Set<GrantedAuthority> authorities = new HashSet<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}
	
	public static UserDetailsImpl build(User user) {
		 List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		 authorities.add(new SimpleGrantedAuthority("USER"));
	    return new UserDetailsImpl(
	        user.getId(), 
	        user.getUserName(), 
	        user.getEmailID(),
	        user.getPswd(), 
	        authorities);
	  }

	public UserDetailsImpl() {
        authorities.add(new SimpleGrantedAuthority("USER"));
        // ... add further roles if required
    }
	
	public UserDetailsImpl(Long id2, String userName2, String emailID, String pswd,
			List<GrantedAuthority> authorities2) {
		// TODO Auto-generated constructor stub
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
}
