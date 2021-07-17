package atos.manolito.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import atos.manolito.entity.UserDataExtended;


@Entity
@Table(name="USER",
		uniqueConstraints = {@UniqueConstraint(columnNames = { "DAS_ID" })})
public class UserData implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty(message="no puede estar vacío")
	@Size(min=7, max=7, message="el tamaño tiene ser de 7")
	@Column(name="DAS_ID",unique = true, nullable=false,
	  columnDefinition = "VARCHAR(7) COMMENT 'El DAS ID contiene 7 carácteres: 1 letra y 6 números.'")
	// FGS 30/10/19 Cambiado patrón para permitir cualquier letra en el DAS ID
	@Pattern(regexp="^[a-zA-Z]{1}[0-9]{6}$", message="no tiene el formato apropiado") // 1 Letra y 6 dígitos numéricos
	private String dasId;

	
	@Size(min=2,max=40, message="el tamaño estar entre 2 y 40")
	// FGS 30/10/19 Cambiado patrón para permitir acentos y la ñ
	@Pattern(regexp="^([a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]{2,40})$", message="no tiene el formato apropiado")
	@Column(nullable=false)
	private String name;
	
	@Size(min=2,max=40, message="el tamaño estar entre 2 y 40")
	// FGS 30/10/19 Cambiado patrón para permitir acentos y la ñ y Ñ
	@Pattern(regexp="^([a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]{2,40})$", message="no tiene el formato apropiado")
	@Column(nullable=false)
	private String surname1;
	
	@Size(min=2,max=40, message="el tamaño estar entre 2 y 40")
	// FGS 30/10/19 Cambiado patrón para permitir acentos y la ñ
	@Pattern(regexp="^([a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]{2,40})$", message="no tiene el formato apropiado")
	@Column(nullable=false)
	private String surname2;
	
	@NotEmpty(message="no puede estar vacío") 
	@Size(min=5,max=50, message="el tamaño estar entre 5 y 50")
	@Email(message="no es un dirección de correo bien formada")
	@Column(nullable=false)
	private String email;

	@Column(columnDefinition = "integer(2) default 1 COMMENT " 
	    + "'Hay 4 posibles estados: 1 Pte. Activación; 2 Activo; 3 Bloqueado y 4 Inactivo.'") 
	private int state;
	

	@ManyToMany(fetch = FetchType.LAZY) 
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.REFRESH })
	@JoinTable(name = "USER_ROLE", 
		uniqueConstraints = @UniqueConstraint(columnNames={"ROLE_FK", "USER_FK"}),
		joinColumns = 
			{@JoinColumn(name = "USER_FK", referencedColumnName = "ID")},
		inverseJoinColumns = 
			{@JoinColumn(name = "ROLE_FK", referencedColumnName = "ID")}
		)
	@JsonIgnoreProperties(value = {"usersData"})
	private Set<Role> roles;

	
	@OneToOne(mappedBy="userData")
	@Cascade ({CascadeType.MERGE, CascadeType.REMOVE})
	@JsonIgnoreProperties(value = {"userData"})
	private Login login;

	// FGS 08/11/19.		
	@OneToOne(mappedBy="userData", fetch = FetchType.LAZY)
	@Cascade ({CascadeType.ALL})
	@JsonIgnoreProperties(value = {"userData"}, allowSetters = true)
	private UserDataExtended userDataExtended; 
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDasId() {
		return dasId;
	}

	public void setDasId(String dasId) {
		this.dasId = dasId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname1() {
		return surname1;
	}

	public void setSurname1(String surname1) {
		this.surname1 = surname1;
	}

	public String getSurname2() {
		return surname2;
	}

	public void setSurname2(String surname2) {
		this.surname2 = surname2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Método que añade un rol a la lista de roles del usuario.
	 * @author FGS
	 * @since 04/11/2019
	 * @param rol Objeto de tipo rol que se añadirá a la lista.
	 */
	public void addRole(Role role) {
		this.roles.add(role);
	}
	/**
	 * Método que elimina un role a la lista de roles del usuario.
	 * @author FGS
	 * @since 04/11/2019
	 * @param role Objeto de tipo role que se eliminará la lista.
	 */
	public void removeRole(Role role) {
		this.roles.remove(role);
	}
	
	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public UserDataExtended getUserDataExtended() {
		return userDataExtended;
	}

	public void setUserDataExtended(UserDataExtended userDataExtended) {
		this.userDataExtended = userDataExtended;
	}

	@Override
	public String toString() {
		return "UserData [id=" + id + ", dasId=" + dasId + ", name=" + name + ", surname1=" + surname1 + ", surname2=" + surname2
				+ ", email=" + email + ", state=" + state
				+ ", login=" + login + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dasId == null) ? 0 : dasId.hashCode());
		result = prime * result + id;
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
		UserData other = (UserData) obj;
		if (dasId == null) {
			if (other.dasId != null)
				return false;
		} else if (!dasId.equals(other.dasId))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

}