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
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ROLE")
public class Role implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotEmpty
	@Size(min=2,max=40, message="el tamaño estar entre 2 y 40")
	@Column(unique=true)
	private String name;

	
	@ManyToMany(mappedBy="roles")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE , CascadeType.REFRESH})
	@JsonIgnoreProperties(value = {"roles"})
	private Set<UserData> usersData;
	
	@ManyToMany(fetch = FetchType.LAZY)  
	@Cascade({ CascadeType.REFRESH }) 
	@JoinTable(name = "PERMISSION_ROLE", 
     joinColumns = {
           @JoinColumn(name = "ROLE_FK", referencedColumnName = "ID")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "PERMISSION_FK", referencedColumnName = "ID")
        }
	)
	@JsonIgnoreProperties(value = {"roles"})
	private Set<Permission> permissions;
	
	//FGS. 28/10/19. Elimina la relación con usuarios para poder borrar el rol
	@PreRemove
	private void removeUsersDataRole() {
	    for (UserData u : usersData) {
	        u.getRoles().remove(this);
	    }
	}
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Set<UserData> getUsersData() {
		return usersData;
	}

	public void setUsersData(Set<UserData> usersData) {
		this.usersData = usersData;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", nombre=" + name + ", usuarios=" + usersData + ", permisos=" + permissions + "]";
	}


}
