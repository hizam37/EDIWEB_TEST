package org.car_factory.car_factory_logic.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;


public abstract class BaseEJB<T> {

	private Class<T> entityClass;

	public BaseEJB(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Inject
	private EntityManager entityManager;

	@Inject
	private Logger logger;

	public void create(List<T> entity) {
		logger.info("adding new " + entity.getClass().getName() + " ");
		entityManager.persist(entity);
	}

	public void create(T entity) {
		logger.info("adding new " + entity.getClass().getName() + " ");
		entityManager.persist(entity);
	}

	public void edit(T entity) {
		logger.info("updating " + entity.getClass().getName());
		entityManager.merge(entity);
	}

	public void remove(T entity) {
		logger.info("deleting " + entity.getClass().getName());
		entityManager.remove(entityManager.merge(entity));
	}

	public T findById(Long id) {
		logger.info("get " + entityClass.getName() + " by id = " + id.toString());
		return entityManager.find(entityClass, id);
	}

	public List<T> findAll() {
		logger.info(" getting all " + entityClass.getName());
		CriteriaQuery<T> cq = entityManager.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return entityManager.createQuery(cq).getResultList();
	}

	public void removeById(Long id) {
		logger.info("delete " + entityClass.getName());
		T entity = entityManager.find(entityClass, id);
        entityManager.remove(entity);
	}

}