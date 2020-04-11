package com.example.loginspirng.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.loginspirng.entity.UserRole;

@Repository("appdao")
public class AppRoleDAOImpl implements AppRoleDAO {
	
	@Autowired
	private EntityManager entityManager;

	public AppRoleDAOImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getRoleNames(Long userId) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ur.appRole.roleName ");
			sql.append("FROM " + UserRole.class.getName() + " ur");
			sql.append(" WHERE ur.appUser.userId = :userId ");
			
			Query query = this.entityManager.createQuery(sql.toString(), String.class);
			query.setParameter("userId", userId);
			
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
}
