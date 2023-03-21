package be.hvwebsites.myhealth.helpers;

import static java.lang.Character.isDigit;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class DateString {
    // format: ddmmyyyy
    private String dateString;
    public static final String EMPTY_DATESTRING = "00000000";
    private static final long MILLIS_IN_DAY = (1000*60*60*24);

    public DateString(String dateString) {
        // Voldoet de datestring aan dd/mm/jjjj
        if (isDatePattern(dateString)){
            this.dateString = trimDate(dateString);
        }else {
            // Datestring mag alleen cijfers bevatten !
            boolean containsOnlyDigits = true;
            char[] dateStringInChar = dateString.toCharArray();
            for (int i = 0; i < dateStringInChar.length; i++) {
                if (!isDigit(dateStringInChar[i])){
                    containsOnlyDigits = false;
                }
            }
            if (!containsOnlyDigits){
                this.dateString = EMPTY_DATESTRING;
            }else {
                // Datestring bevat alleen cijfers
                // Afdwingen dat er een correcte date of empty date string in zit
                final int lowestIntDate = 19000101; // 1 januari 1970
                final int highestIntDate = 30001231; // 31 december 3000
                this.dateString = dateString;
                if ((getIntDate() < lowestIntDate) || (getIntDate() > highestIntDate)){
                    setDateString(EMPTY_DATESTRING);
                }
            }
        }
    }

    public DateString(long dateInMillis){
        this.dateString = getDateFromMillis(dateInMillis);
    }

    public DateString() {

    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getDateString() {
        return dateString;
    }

    // Datum methodes
    private static String trimDate(String inputDate){
        // Haal de / uit de datum
        String[] dateStringParts = inputDate.split("/");
        String day = leadingZero(dateStringParts[0]);
        String month = leadingZero(dateStringParts[1]);
        String year = dateStringParts[2];
        return day + month + year;
    }

    private static String leadingZero(String string){
        // Zet een leading zero indien die ontbreekt
        if (Integer.parseInt(string) < 10 && string.length() < 2){
            return  "0" + string;
        }else {
            return string;
        }
    }

    public String getFormatDate() {
        // Zet / in de datum
        String day = dateString.substring(0,2);
        String month = dateString.substring(2,4);
        String year = dateString.substring(4);
        return day + "/" + month + "/" + year;
    }

    public int getIntDate(){
        String day = dateString.substring(0,2);
        String month = dateString.substring(2,4);
        String year = dateString.substring(4);
        String revDateString = year + month + day;

        return Integer.parseInt(revDateString);
    }

    public Calendar getCalendarDate(){
        Calendar calendarDate = Calendar.getInstance();
        int day = Integer.parseInt(dateString.substring(0,2));
        int month = Integer.parseInt(dateString.substring(2,4))-1;
        int year = Integer.parseInt(dateString.substring(4));
        calendarDate.set(year, month, day);
        return calendarDate;
    }

    private static String getDateFromMillis(long inMillis){
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTimeInMillis(inMillis);

        String day = leadingZero(String.valueOf(calendarDate.get(Calendar.DAY_OF_MONTH)));
        String month = leadingZero(String.valueOf(calendarDate.get(Calendar.MONTH)+1));
        String year = String.valueOf(calendarDate.get(Calendar.YEAR));
        return day + month + year;
    }

    public long getDateInMillis(){
        return getCalendarDate().getTimeInMillis();
    }

    public void setDateToday(){
        Calendar calendarDate = Calendar.getInstance();

        String day = String.valueOf(calendarDate.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendarDate.get(Calendar.MONTH)+1);
        String year = String.valueOf(calendarDate.get(Calendar.YEAR));

        dateString = leadingZero(day) + leadingZero(month) + year;
    }

    public int calculateDateDifference(DateString inDate){
        Calendar currentdate;
        Calendar previousdate;

        currentdate = this.getCalendarDate();
        previousdate = inDate.getCalendarDate();

        return  (int)((currentdate.getTimeInMillis() - previousdate.getTimeInMillis())
                /MILLIS_IN_DAY);
    }

    public void addNbrOfYears(int freqNumber){
        String stringDay = dateString.substring(0,2);
        String stringMonth = dateString.substring(2,4);
        int year = Integer.parseInt(dateString.substring(4));
        String stringYear = String.valueOf(year + freqNumber);

        this.dateString = stringDay + stringMonth + stringYear;
    }

    public void addNbrOfMonths(int freqNumber){
        int day = Integer.parseInt(dateString.substring(0,2));
        int month = Integer.parseInt(dateString.substring(2,4));
        int year = Integer.parseInt(dateString.substring(4));
        String stringDay = dateString.substring(0,2);
        String stringMonth = dateString.substring(2,4);
        String stringYear = dateString.substring(4);

        // Bepaal nieuwe maand en jaar
        double nbrOfYears = Math.floor(freqNumber / 12);
        int restOfMonths = (int) (freqNumber - (nbrOfYears * 12));
        int nMonth = restOfMonths + month;
        int nYear = (int) (year + nbrOfYears);

        if (nMonth > 12){
            // we zitten nog 1 jaar verder
            nYear++;
            nMonth = nMonth - 12;
        }
        stringMonth = leadingZero(String.valueOf(nMonth));
        stringYear = String.valueOf(nYear);
        this.dateString = stringDay + stringMonth + stringYear;

        // Corrigeer dag en maand via Calendar bvb 31/2 moet 3/3 zijn indien geen schrikkeljaar
        this.dateString = getDateFromMillis(getDateInMillis());
    }

    public void addNbrOfWeeks(int freqNumber){
        this.dateString = getDateFromMillis(getDateInMillis() + ((long) freqNumber * 7 * MILLIS_IN_DAY));
    }

    public void addNbrOfDays(int freqNumber){
        this.dateString = getDateFromMillis(getDateInMillis() + ((long) freqNumber * MILLIS_IN_DAY));
    }

    public boolean isEmpty() {
        return false;
    }

    private static boolean isDatePattern(@NonNull String inString){
        boolean result = false;

        if (inString.matches("\\d{2}/\\d{2}/\\d{4}")){
            // Date pattern no leading zero's required
            result = true;
        }else if (inString.matches("\\d{1}/\\d{1}/\\d{4}")){
            result = true;
        }else if (inString.matches("\\d{1}/\\d{2}/\\d{4}")){
            result = true;
        }else if (inString.matches("\\d{2}/\\d{1}/\\d{4}")){
            result = true;
        }
        return result;
    }
}
