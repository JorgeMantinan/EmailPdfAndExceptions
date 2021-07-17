package atos.manolito.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import atos.manolito.entity.UserDataExtended;

/**
 * 
 * @author FGS
 * @since  11/11/2019
 *
 */
public interface IUserDataExtendedDao extends JpaRepository<UserDataExtended, Integer> {
	// FGS 10/12/19 Consultas separadas para nif,nie y pasaporte.
	@Query("select ude from UserDataExtended ude where ude.nif = :NIF")
	public UserDataExtended findByNif(@Param("NIF") String nif);
	@Query("select ude from UserDataExtended ude where ude.nie = :NIE")
	public UserDataExtended findByNie(@Param("NIE") String nie);
	@Query("select ude from UserDataExtended ude where ude.passport = :PASSPORT")
	public UserDataExtended findByPassport(@Param("PASSPORT") String passport);	
	
	// FGS 12/11/19
	@Query("select ude from UserDataExtended ude where ude.ssNumber = :SS_NUMBER")
	public UserDataExtended findBySSNumber(@Param("SS_NUMBER") String ssNumber);
	
	// FGS 11/12/19
	@Query("select ude from UserDataExtended ude where ude.ibanNumber = :IBAN_NUMBER")
	public UserDataExtended findByIbanNumber(@Param("IBAN_NUMBER") String ibanNumber);
	
	// DCS 03/01/2020
	@Query("select ude from UserDataExtended ude join ude.userData u where u.dasId LIKE CONCAT('%',:DAS,'%') and u.name LIKE CONCAT('%',:NAME,'%') and u.surname1 LIKE CONCAT('%',:SURNAME1,'%') and ude.ssNumber LIKE CONCAT('%',:SSNUMBER,'%')")
	public Page<UserDataExtended> search(Pageable pageable,@Param("DAS") String dasId,@Param("NAME")String name,@Param("SURNAME1")String surname1,@Param("SSNUMBER")String ssNumber);
}
