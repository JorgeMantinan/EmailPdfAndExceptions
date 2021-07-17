package atos.manolito.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import atos.manolito.entity.UserData;

public interface IUserDataDao extends JpaRepository<UserData, Integer>{
	
	@Query("select u from UserData u where u.dasId = :DAS_ID") // 19/10/02 introducido para buscar por dasId
	public UserData findByDasId(@Param("DAS_ID") String dasId); 
	
	@Query("select u from UserData u where u.dasId like CONCAT('%',:DAS_ID,'%') and u.name like CONCAT('%',:NAME,'%') and u.surname1 like CONCAT('%',:SURNAME,'%')")
	public List<UserData> filter(@Param("DAS_ID") String dasId, @Param("NAME") String name, @Param("SURNAME") String surname1);
	
	// FGS 15/11/19. Recupero los usuarios que no tienen datos extendidos.
	@Query("select u from UserData u where u.userDataExtended is empty")
	public List<UserData> findAllWithoutDataExtended();

	@Query("select u from UserData u where u.dasId like CONCAT('%',:DAS_ID,'%') and u.name like CONCAT('%',:NAME,'%') and u.surname1 like CONCAT('%',:SURNAME1,'%') and u.surname2 like CONCAT('%',:SURNAME2,'%') and u.email like CONCAT('%',:MAIL,'%') and u.state = :STATE")
	public Page<UserData> search(Pageable pageable,@Param("DAS_ID") String dasId,@Param("NAME") String name, @Param("SURNAME1") String surname1,@Param("SURNAME2") String surname2, @Param("MAIL") String mail, @Param("STATE") int param);
	
	@Query("select u from UserData u where u.dasId like CONCAT('%',:DAS_ID,'%') and u.name like CONCAT('%',:NAME,'%') and u.surname1 like CONCAT('%',:SURNAME1,'%') and u.surname2 like CONCAT('%',:SURNAME2,'%') and u.email like CONCAT('%',:MAIL,'%')")
	public Page<UserData> search(Pageable pageable,@Param("DAS_ID") String dasId,@Param("NAME") String name, @Param("SURNAME1") String surname1,@Param("SURNAME2") String surname2, @Param("MAIL") String mail);

	
}
