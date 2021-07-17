package atos.manolito.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import atos.manolito.entity.Salaryrange;

public interface ISalaryrangeDao extends JpaRepository<Salaryrange,Integer>{
	
	//@Query("select sr from SalaryRange sr where sr.range >= :salary") // 30/12/19 Para buscar rangos salariales superiores a un determinado sueldo
	//public List<Salaryrange> findUpperRange(@Param("salary") int salary);
	
	@Query(value = "SELECT * FROM SALARYRANGES WHERE salaryranges.range >= :salary  LIMIT 1", 
			nativeQuery = true) // 30/12/19 Para buscar rangos salariales superiores a un determinado sueldo	
	public Salaryrange findSalaryRange(@Param("salary") int salary);

}
