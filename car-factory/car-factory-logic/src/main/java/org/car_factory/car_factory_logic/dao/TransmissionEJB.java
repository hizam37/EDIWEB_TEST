package org.car_factory.car_factory_logic.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.car_factory.car_factory_logic.entity.Transmission;

@Stateless
public class TransmissionEJB extends BaseEJB<Transmission> {

	@Inject
	private EntityManager entityManager;

	public TransmissionEJB() {
		super(Transmission.class);
	}

	public Transmission findBySerialNumber(String serialNumber) {
		TypedQuery<Transmission> query = entityManager
				.createQuery("SELECT b FROM Body b WHERE b.serialNumber = :serialNumber", Transmission.class);
		query.setParameter("serialNumber", serialNumber);
		List<Transmission> results = query.getResultList();
		return results.isEmpty() ? null : results.get(0);
	}
}
