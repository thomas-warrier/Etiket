package exportkit;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/*###################################### changer le UTC +2 #########################################################*/
public class DateFormat {
    public static String dateFormat(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM à hh:mm",Locale.FRANCE);
        return dateFormat.format(date);
    }

    public static String dateDeduction(Date date){

        if (DateUtils.isToday(date.getTime())){ //if today
            SimpleDateFormat dateFormat = new SimpleDateFormat("aujourd'hui à hh:mm", Locale.FRANCE);
            return dateFormat.format(date);

        }

        Calendar c1=Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR, -2);
        if (c1.toInstant().isBefore(date.toInstant())){ //if yesterday
            SimpleDateFormat dateFormat = new SimpleDateFormat("hier à hh:mm",Locale.FRANCE);
            return dateFormat.format(date);
        }
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.YEAR,-1);
                if (date.toInstant().isAfter(c2.toInstant())) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM à hh:mm", Locale.FRANCE);
                    return dateFormat.format(date);
                }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY à hh:mm",Locale.FRANCE); //if more than a year
        return dateFormat.format(date);

    }
}
