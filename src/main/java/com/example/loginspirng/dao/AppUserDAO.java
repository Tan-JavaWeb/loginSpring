package com.example.loginspirng.dao;

import com.example.loginspirng.entity.AppUser;

public interface AppUserDAO {

	public AppUser findUserAccount(String userName);
}
