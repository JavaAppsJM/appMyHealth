package be.hvwebsites.myhealth.helpers;

import be.hvwebsites.myhealth.entities.Measurement;

public class BloodPressureM {
    private String measurementDate;
    private Measurement upperPressure;
    private Measurement lowerPressure;
    private Measurement heartBeat;
    public static final String EXTRA_INTENT_KEY_UPPER =
            "be.hvwebsites.myhealth.INTENT_KEY_UPPER";
    public static final String EXTRA_INTENT_KEY_LOWER =
            "be.hvwebsites.myhealth.INTENT_KEY_LOWER";
    public static final String EXTRA_INTENT_KEY_HEARTB =
            "be.hvwebsites.myhealth.INTENT_KEY_HEARTB";

    public BloodPressureM(Measurement upperPressure,
                          Measurement lowerPressure,
                          Measurement heartBeat) {
        if (checkInput(
                upperPressure.getDateInt(),
                lowerPressure.getDateInt(),
                heartBeat.getDateInt())){
            this.measurementDate = upperPressure.getMeasurementDateFormatted();
            this.upperPressure = upperPressure;
            this.lowerPressure = lowerPressure;
            this.heartBeat = heartBeat;
        }
    }

    private static boolean checkInput(
            int bloodUpperDate,
            int bloodLowerDate,
            int heartbeatDate) {
        if ((bloodUpperDate == bloodLowerDate) &&
        (bloodUpperDate == heartbeatDate)) {
            return true;
        } else return false;
    }

    public String getEmailString(){
        return this.measurementDate
                + " bovendruk: "
                + String.valueOf(this.upperPressure.getMeasurementValue())
                + " onderdruk: "
                + this.lowerPressure.getMeasurementValue()
                + " hartslag: "
                + this.heartBeat.getMeasurementValue();
    }

    public String getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

    public Measurement getUpperPressure() {
        return upperPressure;
    }

    public void setUpperPressure(Measurement upperPressure) {
        this.upperPressure = upperPressure;
    }

    public Measurement getLowerPressure() {
        return lowerPressure;
    }

    public void setLowerPressure(Measurement lowerPressure) {
        this.lowerPressure = lowerPressure;
    }

    public Measurement getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(Measurement heartBeat) {
        this.heartBeat = heartBeat;
    }
}
