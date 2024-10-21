package org.car_factory.car_factory_logic.utils;

import java.util.logging.Logger;


import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.enterprise.inject.spi.InjectionPoint;



public class EntityManagerProducer {

    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint){
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}
