package it.macros.security.repositories.impl;

import javax.persistence.*;

public abstract class BaseRepositoryImpl
{
	@PersistenceContext
	protected EntityManager entityManager = null;

	/**
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}