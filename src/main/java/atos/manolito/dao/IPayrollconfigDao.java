package atos.manolito.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import atos.manolito.entity.Payrollconfig;

public interface IPayrollconfigDao extends JpaRepository<Payrollconfig, Integer> {
	
	@Query("SELECT prc FROM Payrollconfig prc WHERE prc.keyField = :keyfield") // 30/12/19 FGS 
	public Payrollconfig findByKeyField(@Param("keyfield") String keyfield);
}
