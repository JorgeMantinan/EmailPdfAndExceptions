package atos.manolito.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//t2007-DCS-19/11/2019
@Entity
@Table(name="ADDRESS")
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	public Address(int id, UserDataExtended userdataextended, String addressName, int streetType,int portal, String floor) {
		this.id = id;
		this.userdataextended = userdataextended;
		this.addressName = addressName;
		this.streetType = streetType;
		this.portal = portal;
		this.floor = floor;
	}
	
	public Address() {}

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties(value = {"addresses"})
	private UserDataExtended userdataextended;
	
	@Size(min=10,max=50, message="el tamaño estar entre 10 y 50")
	@Column(name="NAME_ADDRESS", nullable = false)
	private String addressName;
	
	@Column(name="TYPE_STREET")
	private int streetType;
	
	@Column(name="PORTAL")
	private int portal;
	
	@Column(name="FLOOR")
	private String floor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="COD_POSTAL")
	private PostalCode postalCode;
	
	@Column(name="door")
	private String door;
	
	public String getDoor() {
		return door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	public int getId() {
		return id;
	}

	public String getAddressName() {
		return addressName;
	}

	public int getStreetType() {
		return streetType;
	}

	public int getPortal() {
		return portal;
	}

	public String getFloor() {
		return floor;
	}

	public PostalCode getPostalCode() {
		return postalCode;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public void setStreetType(int streetType) {
		this.streetType = streetType;
	}

	public void setPortal(int portal) {
		this.portal = portal;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public void setPostalCode(PostalCode postalCode) {
		this.postalCode = postalCode;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", addressName=" + addressName + ", streetType=" + streetType + ", portal="
				+ portal + ", floor=" + floor + ", postalCode=" + postalCode + "]";
	}

	public UserDataExtended getUserdataextended() {
		return userdataextended;
	}

	public void setUserdataextended(UserDataExtended userdataextended) {
		this.userdataextended = userdataextended;
	}
	
	
	
}
