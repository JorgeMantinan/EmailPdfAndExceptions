package atos.manolito.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class GenerationDateUtils {

	/**
	 * Este método crea un objeto de tipo LocalDate a 
	 * @author FGS
	 * @since  02/01/2020
	 * @param yearMonth
	 * @return
	 */
	public static LocalDate generateLocalDate(String strYearMonth) {
		LocalDate localDateCalculated = null;
		int intMonthNumber = 0;
		
		intMonthNumber = getMonthNumber(strYearMonth.substring(4));
		if (intMonthNumber>0) {
			YearMonth yearMonth = YearMonth.of(Integer.parseInt(strYearMonth.substring(0, 4)), intMonthNumber);
			localDateCalculated = LocalDate.of(yearMonth.getYear(),yearMonth.getMonthValue(), yearMonth.lengthOfMonth());			
		}

		return localDateCalculated;
	}
	
	/**
	 * 
	 * @author FGS
	 * @since 07/01/2020
	 * @param strMonthName
	 * @return
	 */
	public static int getMonthNumber(String strMonthName) {
		int intMonthNumber = 0;
		Date date = new Date();
		try {
			date = new SimpleDateFormat("MMMM").parse(strMonthName);
			intMonthNumber = date.getMonth() + 1;
		} catch (ParseException e) {
			intMonthNumber = -1;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return intMonthNumber;
	}
}
