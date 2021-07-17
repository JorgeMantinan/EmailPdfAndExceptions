package atos.manolito.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import atos.manolito.security.constraint.ValidPassword;
import atos.manolito.entity.UserData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="login")
public class Login implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	
//	@NotEmpty(message="no puede estar vacío") 
//	@Size(min=7, max=7, message="el tamaño tiene ser de 7")
//	// FGS 30/10/19 Cambiado patrón para permitir cualquier letra en el DAS ID
//	@Pattern(regexp="^[a-zA-Z]{1}[0-9]{6}$", message="no tiene el formato apropiado") // 1 Letra y 6 dígitos numéricos
//	private String dasId;
	
	
	
	@NotEmpty
	@ValidPassword	// 19/10/02 fgs. Introducida anotación Usa lib. passay -> incorporada al pom.xml.
	private String password;
	
	@Column(columnDefinition = "integer(2) default 3 COMMENT 'El número de intentos es 3 inicialmente y se decrementa hasta 0'", name="attempts_num" )
	private int attemptsNum;
	
	@OneToOne
	@JoinColumn(name = "id")
	@MapsId
	@JsonIgnoreProperties(value = {"login"})
	private UserData userData;
	
	public Login() {		
	}

	public Login(int attemptsNum, UserData userData) {
		super();
		this.attemptsNum = attemptsNum;
		this.userData = userData;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


//	public String getDasId() {
//		return dasId;
//	}
//
//
//	public void setDasId(String dasId) {
//		this.dasId = dasId;
//	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public int getAttemptsNum() {
		return attemptsNum;
	}


	public void setAttemptsNum(int attemptsNum) {
		this.attemptsNum = attemptsNum;
	}


	public UserData getUserData() {
		return userData;
	}


	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	
}
