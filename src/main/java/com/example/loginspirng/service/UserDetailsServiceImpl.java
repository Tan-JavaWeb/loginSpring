package com.example.loginspirng.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.loginspirng.dao.AppRoleDAO;
import com.example.loginspirng.dao.AppUserDAO;
import com.example.loginspirng.entity.AppUser;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private AppRoleDAO appRoleDAO;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		System.out.println("d");
		AppUser appUser = this.appUserDAO.findUserAccount(userName);

		if (appUser == null) {
			System.out.println("User not found! " + userName);
			throw new UsernameNotFoundException("User " + userName + " was not found in the database");
		}

		System.out.println("Found User: " + appUser);

		// [ROLE_USER, ROLE_ADMIN,..]
		List<String> roleName = this.appRoleDAO.getRoleNames(appUser.getUserId());
		List<GrantedAuthority> listauthorities = new ArrayList<GrantedAuthority>();

		if (roleName != null) {
			for (String role : roleName) {
				// ROLE_USER, ROLE_ADMIN,..
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				listauthorities.add(authority);
			}
		}

		return new User(appUser.getUserName(), appUser.getEncrytedPassword(), listauthorities);
	}

}
