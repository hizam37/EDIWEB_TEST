package org.car_factory.car_factory_logic.dao;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.car_factory.car_factory_logic.entity.Car;

@Stateless
public class CarEJB extends BaseEJB<Car> {

	@Inject
	private EntityManager entityManager;

	public CarEJB() {
		super(Car.class);
	}

	@Override
	public void create(Car car) {
		super.create(car);
	}

	@Override
	public void edit(Car car) {
		super.edit(car);
	}

	@Override
	public List<Car> findAll() {
		return super.findAll();
	}

	@Override
	public Car findById(Long id) {
		return super.findById(id);
	}

	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	
	public List<Car> sortBy(String field) {
		if (!isValidField(field)) {
			throw new IllegalArgumentException("Invalid field for sorting: " + field);
		}

		String jpql = "SELECT c FROM Car c JOIN c.engine e JOIN c.transmission t ORDER BY e." + field + ", t." + field;
		TypedQuery<Car> query = entityManager.createQuery(jpql, Car.class);
		return query.getResultList();
	}

	private boolean isValidField(String field) {
		List<String> allowedFields = Arrays.asList("type", "serialNumber");
		return allowedFields.contains(field);
	}

}
