package biblioteca.repositorio.sql;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

class DateUtil {
  
  static LocalDate convertDateToLocalDate(Date date) {
    if (date == null) {
      return null;
    }
  
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    int year = calendar.get(Calendar.YEAR);
    // É preciso adicionar 1 ao month porque o month
    // do LocalDate começa no 1, e o do Date começa no 0.
    int month = calendar.get(Calendar.MONTH) + 1;
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    
    return LocalDate.of(year, month, dayOfMonth);
  }
  
  static Date convertLocalDateToDate(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }
  
    Calendar calendar = Calendar.getInstance();
    calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());

    return new Date(calendar.getTimeInMillis());
  }

}
