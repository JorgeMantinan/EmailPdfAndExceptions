package atos.manolito.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

// Generated 17-dic-2019 13:14:52 by Hibernate Tools 5.4.7.Final

/**
 * Specialconditions generated by hbm2java
 */
@Entity
public class Specialconditions implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private int id;
	
	private String description;
	
	private int discount;

	public Specialconditions() {
	}

	public Specialconditions(int id, String description, int discount) {
		this.id = id;
		this.description = description;
		this.discount = discount;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDiscount() {
		return this.discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

}