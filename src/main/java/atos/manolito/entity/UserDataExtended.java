package atos.manolito.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import atos.manolito.entity.UserData;
import atos.manolito.security.constraint.ValidIban;
import atos.manolito.security.constraint.ValidNif;
import atos.manolito.security.constraint.ValidNie;
import atos.manolito.security.constraint.ValidSSNumber;

/**
 * Entidad que guarda la información adicional sobre el usuario. 
 * Su id es el mismo del usuario
 * 
 * @author FGS
 * @since  08/11/2019
 * 
 *  
 */
@Entity
@Table(name="USERDATA")
public class UserDataExtended implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="userdataextended_id")
	private int id;

	// FGS 10/12/19 Cambios para almacenar de forma separada NIF, NIE y pasaporte
	@Column(name="NIF",unique = true, columnDefinition = "VARCHAR(9) COMMENT 'Formato NIF:  8 dígitos y 1 letra.'") 
	@ValidNif(optional=true)
	private String nif;
	
	@Column(name="NIE",unique = true, columnDefinition = "VARCHAR(9) COMMENT 'Formato NIE: 1 letra (X,Y o Z), 7 dígitos y 1 letra.'") 
	@ValidNie(optional=true)
	private String nie;
	
	@Column(name="PASSPORT",unique = true, columnDefinition = "VARCHAR(9) COMMENT 'Formato pasaporte: 3 letras y 6 dígitos.'") 
	@Pattern(regexp="^[A-Z]{3}[0-9]{6}$", message="no tiene el formato apropiado (3 letras y 6 dígitos)")
	private String passport;
	
	// Número de la Seguridad Social del usuario.
	@Column(name="SS_NUMBER",unique = true,columnDefinition = "VARCHAR(12) COMMENT 'Formato: 12 dígitos.'") 
	@ValidSSNumber(optional=true)
	private String ssNumber;
	
	@Column(name="IBAN_NUMBER",unique = true,columnDefinition = "VARCHAR(24) COMMENT 'Formato: ES seguido de 22 dígitos.'")
	@ValidIban(optional=true)
	private String ibanNumber;
	   
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade({CascadeType.ALL})
	@JoinColumn(name="userdataextended_user_data_id")
	@JsonIgnoreProperties(value = {"userdataextended"})
	private Set<Address> addresses;
		
	//FGS 08/11/19. Introducida la relación con los datos básicos del usuario.
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JsonIgnoreProperties(value = {"userDataExtended"})
	private UserData userData;
	
	private int salary;
	
	private int payments;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="special_condition")
	private Specialconditions specialCondition;
	
	@Temporal(TemporalType.DATE)
	private Date firedDate;
	
	@Temporal(TemporalType.DATE)
	private Date hiredDate;

	
	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNie() {
		return nie;
	}

	public void setNie(String nie) {
		this.nie = nie;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getSsNumber() {
		return ssNumber;
	}

	public void setSsNumber(String ssNumber) {
		this.ssNumber = ssNumber;
	}

	public String getIbanNumber() {
		return ibanNumber;
	}

	public void setIbanNumber(String ibanNumber) {
		this.ibanNumber = ibanNumber;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public int getPayments() {
		return payments;
	}

	public Specialconditions getSpecialCondition() {
		return specialCondition;
	}

	public Date getFiredDate() {
		return firedDate;
	}

	public Date getHiredDate() {
		return hiredDate;
	}

	public void setPayments(int payments) {
		this.payments = payments;
	}

	public void setSpecialCondition(Specialconditions specialCondition) {
		this.specialCondition = specialCondition;
	}

	public void setFiredDate(Date firedDate) {
		this.firedDate = firedDate;
	}

	public void setHiredDate(Date hiredDate) {
		this.hiredDate = hiredDate;
	}

	
	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "UserDataExtended [id=" + id + ", nif=" + nif + ", nie=" + nie + ", passport=" + passport + ", ssNumber="
				+ ssNumber + ", ibanNumber=" + ibanNumber + ", salary=" + salary + ", payments=" + payments
				+ ", specialCondition=" + specialCondition + ", firedDate=" + firedDate + ", hiredDate=" + hiredDate
				+ "]";
	}
	
}
