package be.hvwebsites.myhealth.entities;

import androidx.annotation.NonNull;

public class Measurement {
    private String measurementDate;
    private float measurementValue;
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
        setMeasurementDate(trimDate(inputDate));
        measurementValue = inputValue;
    }

    public void setMeasurement(Measurement measurement2){
        // Zet de values van measurement2 in this measurement
        setMeasurementDate(measurement2.getMeasurementDate());
        this.measurementValue = measurement2.getMeasurementValue();
        setDateInt(measurement2.getDateInt());
        setRemark(measurement2.getRemark());
    }

    public String getMeasurementDate(){
        return this.measurementDate;
    }

    public float getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(float measurementValue) {
        this.measurementValue = measurementValue;
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
        this.measurementDate = date;
        this.dateInt = Integer.parseInt(date.substring(4) + date.substring(2,4) + date.substring(0,2));
    }

    public String getDateAndValue(){
        return this.getFormatDate() + ": " + measurementValue;
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

    // Datum methodes
    private String trimDate(String inputDate){
        // Haal de / uit de datum
        String[] dateStringParts = inputDate.split("/");
        String day = leadingZero(dateStringParts[0]);
        String month = leadingZero(dateStringParts[1]);
        String year = dateStringParts[2];
        return day + month + year;
    }

    private String leadingZero(String string){
        // Zet een leading zero indien die ontbreekt
        if (Integer.parseInt(string) < 10 && string.length() < 2){
            return  "0" + string;
        }else {
            return string;
        }
    }

    public String getFormatDate() {
        // Zet / in de datum
        String day = measurementDate.substring(0,2);
        String month = measurementDate.substring(2,4);
        String year = measurementDate.substring(4);
        return day + "/" + month + "/" + year;
    }
}
