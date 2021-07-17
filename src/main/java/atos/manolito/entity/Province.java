package atos.manolito.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//t2007-DCS-19/11/2019
@Entity
@Table(name="PROVINCE")
public class Province implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="COD_PROVINCE")
	private int cod;
	
	@Column(name="NAME_PROVINCE")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="COD_COMMUNITY")
	@JsonIgnoreProperties(value = {"province"})
	private Community community;

	@JsonIgnore
	@OneToMany(
			mappedBy = "province",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@JsonIgnoreProperties(value = {"province"})
	private Set<TownShip> townShip;

	public int getCod() {
		return cod;
	}

	public String getName() {
		return name;
	}

	public Community getCommunity() {
		return community;
	}

	public Set<TownShip> getTownShip() {
		return townShip;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public void setTownShip(Set<TownShip> townShip) {
		this.townShip = townShip;
	}

//	@Override
//	public String toString() {
//		return "Province [cod=" + cod + ", name=" + name + ", community=" + community + ", townShip=" + townShip + "]";
//	}
//	
	
	
}
