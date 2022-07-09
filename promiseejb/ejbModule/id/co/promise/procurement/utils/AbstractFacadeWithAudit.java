/*
 * File: AbstractFacade.java
 * This class is created to handle all processing involved
 * in a PT. MMI Research.
 *
 * Copyright (c) 2015 Mitra Mandiri Informatika
 * Jl. Tebet Raya no. 11 B Jakarta Selatan
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary
 * information of Mitra Mandiri Informatika ("Confidential
 * Information").
 *
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the
 * license agreement you entered into with MMI.
 *
 * Date Author Version Changes
 * Apr 21, 2015	Agus Rochmad TR<mamat@mmi-pt.com> 		1.0 	Created
 */

/**
 * 
 */
package id.co.promise.procurement.utils;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;

public abstract class AbstractFacadeWithAudit<T> {
	private Class<T> entityClass;

	TokenSession tokenSession;

	public AbstractFacadeWithAudit(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected abstract EntityManager getEntityManager();

	protected abstract EntityManager getEntityManagerAudit();

	public void create(T entity, int actionType, Token token) {
		AuditHelper.logActionUpdate(getEntityManager(), getEntityManagerAudit(), actionType, entity, token);
		getEntityManager().persist(entity);
	}

	public void edit(T entity, int actionType, Token token) {
		AuditHelper.logActionUpdate(getEntityManager(), getEntityManagerAudit(), actionType, entity, token);
		getEntityManager().merge(entity);
	}

	public void edit(T entity) {
		getEntityManager().merge(entity);
	}

	public void remove(T entity, int actionType, Token token) {
		AuditHelper.logActionUpdate(getEntityManager(), getEntityManagerAudit(), actionType, entity, token);
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	@TransactionAttribute
	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@TransactionAttribute
	public List<T> findAll() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
				.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> findRange(int[] range) {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
				.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		return q.getResultList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int count() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
				.getCriteriaBuilder().createQuery();
		javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(getEntityManager().getCriteriaBuilder().count(rt));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

}