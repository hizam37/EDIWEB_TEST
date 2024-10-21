package org.car_factory.car_factory_logic.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Car.class)
public abstract class Car_ {

	public static volatile SingularAttribute<Car, Transmission> transmission;
	public static volatile SingularAttribute<Car, Engine> engine;
	public static volatile SingularAttribute<Car, Long> id;
	public static volatile SingularAttribute<Car, Body> body;

}

