package org.car_factory.car_factory_logic.dao;


import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.car_factory.car_factory_logic.entity.Engine;


@Stateless
public class EngineEJB extends BaseEJB<Engine>{

	
	@Inject
	private EntityManager entityManager;

	public EngineEJB() {
		super(Engine.class);
	}
	
	
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
	
	public Engine findBySerialNumber(String serialNumber)
	{
		TypedQuery<Engine> query = entityManager.createQuery("SELECT b FROM Body b WHERE b.serialNumber = :serialNumber", Engine.class);
	    query.setParameter("serialNumber", serialNumber);
	    List<Engine> results = query.getResultList();
	    return results.isEmpty() ? null : results.get(0);
	}
}

