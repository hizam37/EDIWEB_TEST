package org.car_factory_rest.car_factory_rest.controller;

import java.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.car_factory.car_factory_logic.dao.BodyEJB;
import org.car_factory.car_factory_logic.dao.CarEJB;
import org.car_factory.car_factory_logic.dao.EngineEJB;
import org.car_factory.car_factory_logic.dao.TransmissionEJB;
import org.car_factory.car_factory_logic.entity.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/cars")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Stateless
public class CarController {

	@EJB
	private CarEJB carEJB;

	@EJB
	private BodyEJB bodyEJB;

	@EJB
	private TransmissionEJB transmissionEJB;

	@EJB
	private EngineEJB engineEJB;

	// Here we create a car with a unique VIN and serial number for
	// engine and transmission to avoid them being used in different cars
	@POST
	@Path("/create")
	public Response createCar(Car car) {
	    // Validate that transmission and engine serial numbers match
	    if (car.getTransmission() == null || car.getEngine() == null 
	        || !car.getTransmission().getSerialNumber().equals(car.getEngine().getSerialNumber())) {
	        return Response.status(Response.Status.FORBIDDEN)
	                .entity("Serial number for transmission and engine should be the same").build();
	    }

	    Collection<Car> carList = carEJB.findAll();
	    for (Car perCar : carList) {
	        // Check for VIN uniqueness only if VIN is non-null
	        if (perCar.getBody() != null && perCar.getBody().getVin() != null 
	            && perCar.getBody().getVin().equals(car.getBody() != null ? car.getBody().getVin() : null)) {
	            return Response.status(Response.Status.CONFLICT).entity("VIN must be unique").build();
	        }

	        // Check for existing engine and transmission serial numbers
	        if ((perCar.getTransmission() != null 
	                && perCar.getTransmission().getSerialNumber() != null 
	                && perCar.getTransmission().getSerialNumber().equals(car.getTransmission() != null ? car.getTransmission().getSerialNumber() : null))
	            || (perCar.getEngine() != null 
	                && perCar.getEngine().getSerialNumber() != null 
	                && perCar.getEngine().getSerialNumber().equals(car.getEngine() != null ? car.getEngine().getSerialNumber() : null))) {
	            return Response.status(Response.Status.CONFLICT)
	                    .entity("A car with the same engine or transmission serial number already exists").build();
	        }
	    }

	    // If all validations pass, create the car
	    carEJB.create(car);
	    return Response.status(Response.Status.CREATED).build();
	}


	
	//Here we are updating the components(Engine,Transmission,Body) of the car separately 
	@PUT
	@Path("/update")
	public Response update(Car car) {
		Car existingCar = carEJB.findById(car.getId());
		if (existingCar == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Car not found").build();
		}

		if (!existingCar.getEngine().getSerialNumber().equals(car.getEngine().getSerialNumber())) {
			existingCar.getEngine().setType(null);
			existingCar.getEngine().setVolume(0);
			existingCar.getEngine().setPowerKw(0);
			existingCar.getEngine().setSerialNumber(null);
			carEJB.edit(existingCar);
		}

		if (!existingCar.getTransmission().getSerialNumber().equals(car.getTransmission().getSerialNumber())) {
			existingCar.getTransmission().setType(null);
			existingCar.getTransmission().setSerialNumber(null);
			carEJB.edit(existingCar);
		}

		if (!existingCar.getBody().getVin().equals(car.getBody().getVin())) {
			existingCar.getBody().setType(null);
			existingCar.getBody().setDoorCount(0);
			existingCar.getBody().setColor(null);
			existingCar.getBody().setVin(null);
			carEJB.edit(existingCar);
		}

		return Response.ok(existingCar).build(); // Return updated car
	}

	@GET
	@Path("/getCars")
	public Response getAllCars() {
		Collection<Car> cars = carEJB.findAll();

		if (cars == null || cars.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity("No cars found.").build();
		}
		return Response.ok(cars).build();
	}

	@GET
	@Path("/getCarById/{id}")
	public Response getCarById(@PathParam("id") Long id) {
		Car car = carEJB.findById(id);
		if (car == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Please enter id of the car").build();
		}
		return Response.status(Response.Status.ACCEPTED).entity(car).build();
	}

	@DELETE
	@Path("/deleteBodyById/{id}")
	public void deleteBodyById(@PathParam("id") Long id) {
		Body body = bodyEJB.findById(id);
		Car car = carEJB.findById(body.getId());
		if (car != null) {
			car.setBody(null);
			carEJB.edit(car);
		}
		bodyEJB.removeById(id);

	}

	@DELETE
	@Path("/deleteEngineById/{id}")
	public void deleteEngineById(@PathParam("id") Long id) {
		Engine engine = engineEJB.findById(id);
		Car car = carEJB.findById(engine.getId());
		if (car != null) {
			car.setEngine(null);
			carEJB.edit(car);
		}
		engineEJB.removeById(id);
	}

	@DELETE
	@Path("/deleteTransmissionById/{id}")
	public void deleteTransmissionById(@PathParam("id") Long id) {
		Transmission transmission = transmissionEJB.findById(id);
		Car car = carEJB.findById(transmission.getId());
		if (car != null) {
			car.setTransmission(null);
			carEJB.edit(car);
		}
		transmissionEJB.removeById(id);
	}

	@GET
	@Path("/sortBy/{the_component_to_be_sorted}")
	public Response sortBy(@PathParam("the_component_to_be_sorted") String the_component_to_be_sorted) {
		Collection<Car> cars = carEJB.sortBy(the_component_to_be_sorted);
		return Response.ok(cars).build();
	}

}
