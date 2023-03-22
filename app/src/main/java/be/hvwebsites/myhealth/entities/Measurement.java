package be.hvwebsites.myhealth.entities;

import androidx.annotation.NonNull;

import be.hvwebsites.myhealth.helpers.DateString;

public class Measurement {
    private DateString measurementDate;
    private float measurementValue;
    private DateString latestEmailDate;

    private String remark; // initieel niet gebruikt
    private int dateInt;
    // Intent doorgeef data definities via EXTRAS
    public static final String EXTRA_INTENT_KEY_ACTION =
            "be.hvwebsites.myhealth.EXTRA_INTENT_KEY_ACTION";
    public static final String EXTRA_INTENT_KEY_INDEX =
            "be.hvwebsites.myhealth.EXTRA_INTENT_KEY_INDEX";
    public static final String EXTRA_INTENT_KEY_TYPE =
            "be.hvwebsites.myhealth.INTENT_KEY_TYPE";
    public static final String EXTRA_INTENT_KEY_DATE =
            "be.hvwebsites.myhealth.INTENT_KEY_DATE";
    public static final String EXTRA_INTENT_KEY_VALUE =
            "be.hvwebsites.myhealth.INTENT_KEY_VALUE";
    public static final String EXTRA_INTENT_KEY_REMARK =
            "be.hvwebsites.myhealth.INTENT_KEY_REMARK";

    public Measurement() {
    }

    public Measurement(String inputDate, float inputValue) {
        setMeasurementDate(inputDate);
        measurementValue = inputValue;
        latestEmailDate = new DateString();
        setLatestEmailDate(DateString.EMPTY_DATESTRING);
    }

    public Measurement(String inFileLine){
        // fileLine splitsen in argumenten
        String[] fileLineContent = inFileLine.split("<");
        for (int i = 0; i < fileLineContent.length; i++) {
            if (fileLineContent[i].matches("date.*")){
                setMeasurementDate(fileLineContent[i+1].replace(">",""));
            }
            if (fileLineContent[i].matches("measurement.*")){
                setMeasurementValue(Float.valueOf(fileLineContent[i+1].replace(">","")));
            }
            if (fileLineContent[i].matches("emaildate.*")){
                setLatestEmailDate(fileLineContent[i+1].replace(">",""));
            }
        }
    }

    public String convertToFileLine(){
        return  "<date><" + getMeasurementDate()
                + "><measurement><" + String.valueOf(getMeasurementValue())
                + "><emaildate><" + getLatestEmailDate()
                + ">";

    }

    public void setMeasurement(Measurement measurement2){
        // Zet de values van measurement2 in this measurement
        setMeasurementDate(measurement2.getMeasurementDate());
        this.measurementValue = measurement2.getMeasurementValue();
        setDateInt(measurement2.getDateInt());
        setLatestEmailDate(measurement2.getLatestEmailDate());
        setRemark(measurement2.getRemark());
    }

    public String getMeasurementDate(){
        return this.measurementDate.getDateString();
    }

    public String getMeasurementDateFormatted(){
        return this.measurementDate.getFormatDate();
    }

    public float getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(float measurementValue) {
        this.measurementValue = measurementValue;
    }

    public String getLatestEmailDate() {
        return latestEmailDate.getDateString();
    }

    public void setLatestEmailDate(String latestEmailDate) {
        this.latestEmailDate = new DateString(latestEmailDate);
    }

    public String getRemark() {
        return remark;
    }

    public int getDateInt() {
        return dateInt;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDateInt(int dateInt) {
        this.dateInt = dateInt;
    }

    public void setMeasurementDate(@NonNull String date) {
        this.measurementDate = new DateString(date);
        //this.dateInt = Integer.parseInt(date.substring(4) + date.substring(2,4) + date.substring(0,2));
        this.dateInt = measurementDate.getIntDate();
        //this.dateInt = convertDateStringToDateInt(measurementDate.getDateString());
    }

    public String getDateAndValue(){
        return this.getMeasurementDateFormatted() + ": " + measurementValue;
    }

    public String getValueAsString(){
        return String.valueOf(measurementValue);
    }

    @Override
    public String toString() {
        return "Meting{" +
                "Datum = '" + measurementDate + '\'' +
                ", Value ='" + measurementValue + '\'' +
                '}';
    }

/*
    // Datum methodes
    private static String leadingZero(String string){
        // Zet een leading zero indien die ontbreekt
        if (Integer.parseInt(string) < 10 && string.length() < 2){
            return  "0" + string;
        }else {
            return string;
        }
    }

    private String trimDate(String inputDate){
        // Haal de / uit de datum
        String[] dateStringParts = inputDate.split("/");
        String day = leadingZero(dateStringParts[0]);
        String month = leadingZero(dateStringParts[1]);
        String year = dateStringParts[2];
        return day + month + year;
    }

    public static int convertDateStringToDateInt(String inDate){
        return Integer.parseInt(leadingZero(inDate.substring(4)) + leadingZero(inDate.substring(2,4)) + inDate.substring(0,2));
    }

    public String getFormatMsrmtDate() {
        // Zet / in de datum
        return measurementDate.getFormatDate();
        String day = measurementDate.substring(0,2);
        String month = measurementDate.substring(2,4);
        String year = measurementDate.substring(4);
        return day + "/" + month + "/" + year;
*/

}
