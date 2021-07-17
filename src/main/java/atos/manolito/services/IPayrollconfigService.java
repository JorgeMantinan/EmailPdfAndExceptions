package atos.manolito.services;

import java.util.List;

import atos.manolito.entity.Payrollconfig;


public interface IPayrollconfigService {

	List<Payrollconfig> saveConfig(List<Payrollconfig> configurarions);
	
	List<Payrollconfig> findAll();

	Payrollconfig findByKeyField(String keyfield);

}
