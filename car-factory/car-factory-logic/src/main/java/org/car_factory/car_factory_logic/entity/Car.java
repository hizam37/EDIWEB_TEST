package org.car_factory.car_factory_logic.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.NoArgsConstructor;



@XmlRootElement 
@Data
@NoArgsConstructor
@Entity
@Table(name="cars")
public class Car implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@OneToOne(cascade = CascadeType.ALL) 
	@JoinColumn(name="engine_id")
	private Engine engine;
	
	@OneToOne(cascade = CascadeType.ALL) 
	@JoinColumn(name="transmission_id")
	private Transmission transmission;
	
	@OneToOne(cascade = CascadeType.ALL) 
	@JoinColumn(name="body_id")
	private Body body;
	
	
	
}