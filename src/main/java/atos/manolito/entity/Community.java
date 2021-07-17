package atos.manolito.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//t2007-DCS-19/11/2019
@Entity
@Table(name="COMMUNITY")
public class Community implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="COD_COMMUNITY")
	private int cod;
	
	@Column(name="NAME_COMMUNITY")
	private String name;
	
	@JsonIgnore
	@OneToMany(
			mappedBy = "community",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@JsonIgnoreProperties(value = {"community"})
	private Set<Province> province;

	public int getCod() {
		return cod;
	}

	public String getName() {
		return name;
	}

	public Set<Province> getProvince() {
		return province;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProvince(Set<Province> province) {
		this.province = province;
	}

//	@Override
//	public String toString() {
//		return "Community [cod=" + cod + ", name=" + name + ", province=" + province + "]";
//	}

}
