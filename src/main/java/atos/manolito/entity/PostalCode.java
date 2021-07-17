package atos.manolito.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// t2007-DCS-19/11/2019
@Entity
@Table(name="POSTALCODE")
public class PostalCode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_POSTAL")
	private int cod;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="COD_TOWNSHIP")
	@JoinColumn(name="COD_PROVINCE")
	@JsonIgnoreProperties(value={"cod","province"})
	private TownShip codTownship;
	
	public PostalCode() {}
	
	public PostalCode(int cod, TownShip codTownship) {
		this.cod = cod;
		this.codTownship = codTownship;
	}

	public int getCod() {
		return cod;
	}

	public TownShip getCodTownship() {
		return codTownship;
	}

	public void setCod(int cp) {
		this.cod = cp;
	}

	public void setCodTownship(TownShip codTownship) {
		this.codTownship = codTownship;
	}

	@Override
	public String toString() {
		return "PostalCode [cod=" + cod + ", codTownship=" + codTownship + "]";
	}
	
	
}
