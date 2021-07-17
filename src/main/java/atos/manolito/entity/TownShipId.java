package atos.manolito.entity;

import java.io.Serializable;

// t2007-DCS-19/11/2019
// Clase que se utiliza para la definicion de la clave primaria compuesta de la tabla de TownShip
public class TownShipId implements Serializable {

	private static final long serialVersionUID = 1L;

	private int cod;
	
	private int province;
	
	public TownShipId(){}
	
	public TownShipId(int cod, int province) {
		this.cod = cod;
		this.province = province;
	}

	public int getCod() {
		return cod;
	}

	public int getProvince() {
		return province;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cod;
		result = prime * result + province;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TownShipId other = (TownShipId) obj;
		if (cod != other.cod)
			return false;
		if (province != other.province)
			return false;
		return true;
	}

}
