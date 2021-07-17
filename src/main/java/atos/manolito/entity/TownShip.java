package atos.manolito.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//t2007-DCS-19/11/2019
@Entity
@IdClass(TownShipId.class)
@Table(name="TOWNSHIP")
public class TownShip implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_TOWNSHIP")
	private int cod;
	
	@Column(name="NAME_TOWNSHIP")
	private String name;
	
	@Id
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="COD_PROVINCE")
	@JsonIgnoreProperties(value = {"townShip"})
	private Province province;
	
	@JsonIgnore
	@OneToMany(
			mappedBy = "codTownship",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@JsonIgnoreProperties(value = {"codTownship"})
	private Set<PostalCode> postalCode;

	public int getCod() {
		return cod;
	}

	public String getName() {
		return name;
	}

	public Province getProvince() {
		return province;
	}

	public Set<PostalCode> getPostalCode() {
		return postalCode;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public void setPostalCode(Set<PostalCode> postalCode) {
		this.postalCode = postalCode;
	}

	@Override
	public String toString() {
		return "TownShip [cod=" + cod + ", name=" + name + ", province=" + province + ", postalCode=" + postalCode
				+ "]";
	}

	
}
