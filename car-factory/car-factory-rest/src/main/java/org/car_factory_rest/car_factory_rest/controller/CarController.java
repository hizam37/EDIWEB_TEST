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

		if (!car.getTransmission().getSerialNumber().equals(car.getEngine().getSerialNumber())) {
			return Response.status(Response.Status.FORBIDDEN)
					.entity("Serial number for transmission and engine should be the same").build();
		}

		Collection<Car> carList = carEJB.findAll();
		for (Car perCar : carList) {

			if (perCar.getBody().getVin().equals(car.getBody().getVin())) {
				return Response.status(Response.Status.CONFLICT).entity("VIN must be unique").build();
			}

			if ((perCar.getTransmission().getSerialNumber().equals(car.getTransmission().getSerialNumber())
					&& !perCar.getBody().getVin().equals(car.getBody().getVin()))
					|| (perCar.getEngine().getSerialNumber().equals(car.getEngine().getSerialNumber())
							&& !perCar.getBody().getVin().equals(car.getBody().getVin()))) {
				return Response.status(Response.Status.CONFLICT)
						.entity("A car with the same engine or transmission serial number already exists").build();
			}
		}

	
		carEJB.create(car);
		return Response.status(Response.Status.CREATED).build();
	}

	@PUT
	@Path("/update")
	public Response update(Car car) {
		carEJB.edit(car);
		return Response.status(Response.Status.CREATED).build();

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
		if(car==null)
		{
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
