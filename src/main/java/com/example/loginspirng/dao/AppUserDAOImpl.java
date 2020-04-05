package com.example.loginspirng.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.loginspirng.entity.AppUser;

@Repository("appuser")
public class AppUserDAOImpl implements AppUserDAO {
	
	@Autowired
	private EntityManager entityManager;
	
	public AppUserDAOImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public AppUser findUserAccount(String userName) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT u ");
			sql.append("FROM " + AppUser.class.getName() + "u");
			sql.append(" WHERE u.userName = :userName");
			
			Query query = entityManager.createQuery(sql.toString(), AppUser.class);
            query.setParameter("userName", userName);
            
			return (AppUser) query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Exception");
			return null;
		}
	}
}
