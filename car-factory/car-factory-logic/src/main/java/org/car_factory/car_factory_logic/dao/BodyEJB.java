package org.car_factory.car_factory_logic.dao;


import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.car_factory.car_factory_logic.entity.Body;




@Stateless
public class BodyEJB extends BaseEJB<Body>{
	
	@Inject
	private EntityManager entityManager;
		
	public BodyEJB() {
		super(Body.class);
	}

	@Override
	public void create(Body body) {
		super.create(body);
	}
	


	@Override
	public List<Body> findAll() {
        return super.findAll();
    }
	
	
	public Body findByVin(String vin)
	{
		TypedQuery<Body> query = entityManager.createQuery("SELECT b FROM Body b WHERE b.vin = :vin", Body.class);
	    query.setParameter("vin", vin);
	    List<Body> results = query.getResultList();
	    return results.isEmpty() ? null : results.get(0);
	}
	

}
