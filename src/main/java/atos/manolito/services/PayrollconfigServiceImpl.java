package atos.manolito.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atos.manolito.dao.IPayrollconfigDao;
import atos.manolito.entity.Payrollconfig;

@Service
public class PayrollconfigServiceImpl implements IPayrollconfigService{

	@Autowired
	private IPayrollconfigDao payrollconfig;
	
	@Override
	public List<Payrollconfig> saveConfig(List<Payrollconfig> configurations) {
		return payrollconfig.saveAll(configurations);
	}

	@Override
	public List<Payrollconfig> findAll() {
		return payrollconfig.findAll();
	}

	/**
	 * 
	 * @author FGS
	 * @since  30/12/2019
	 */
	@Override
	public Payrollconfig findByKeyField(String keyfield) {
		return payrollconfig.findByKeyField(keyfield);
	}
}
