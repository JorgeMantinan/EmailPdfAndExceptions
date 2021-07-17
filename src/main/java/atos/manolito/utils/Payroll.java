package atos.manolito.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Esta clase representa una nómina de un usuario.
 * @author FGS
 * @since  27/12/2019
 *
 */
public class Payroll {
	String name;
	String surname1;
	String surname2;
	String idCard;
	String ssNumber;
	String monthName;
	String year;
	// Salario bruto mensual
	double grossMonthlySalary;
	double netMonthlySalary;
	double taxRetentionPercent;
	double taxDiscountPercent;
	
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
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getSsNumber() {
		return ssNumber;
	}
	public void setSsNumber(String ssNumber) {
		this.ssNumber = ssNumber;
	}
	public double getGrossMonthlySalary() {
		return grossMonthlySalary;
	}
	public void setGrossMonthlySalary(double grossMonthlySalary) {
		this.grossMonthlySalary = grossMonthlySalary;
	}
	public double getNetMonthlySalary() {
		return netMonthlySalary;
	}
	public void setNetMonthlySalary(double netMonthlySalary) {
		this.netMonthlySalary = netMonthlySalary;
	}
	public double getTaxRetentionPercent() {
		return taxRetentionPercent;
	}
	public void setTaxRetentionPercent(double taxRetentionPercent) {
		this.taxRetentionPercent = taxRetentionPercent;
	}
	public double getTaxDiscountPercent() {
		return taxDiscountPercent;
	}
	public void setTaxDiscountPercent(double taxDiscountPercent) {
		this.taxDiscountPercent = taxDiscountPercent;
	}
	
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	@Override
	public String toString() {
		return "Payroll [name=" + name + ", surname1=" + surname1 + ", surname2=" + surname2 + ", idCard=" + idCard
				+ ", ssNumber=" + ssNumber + ", grossMonthlySalary=" + grossMonthlySalary + ", netMonthlySalary="
				+ netMonthlySalary + ", taxRetentionPercent=" + taxRetentionPercent + ", taxDiscountPercent="
				+ taxDiscountPercent + "]";
	}	

	/**
	 * Este método convierte un objeto de tipo Date en un objeto de tipo LocalDate (java 8).
	 * 
	 * @author FGS
	 * @since 31/12/2019
	 * @param dateToConvert
	 * @return
	 */
	public static LocalDate convertToLocalDate(Date dateToConvert) {
				 
		Date safeDate = new Date(dateToConvert.getTime());
		
		return safeDate.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();	     
		
	}
}
