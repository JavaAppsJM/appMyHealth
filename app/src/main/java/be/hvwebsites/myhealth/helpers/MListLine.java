package be.hvwebsites.myhealth.helpers;

import java.util.List;
import java.util.Optional;

import be.hvwebsites.myhealth.constants.GlobalConstant;
import be.hvwebsites.myhealth.entities.Measurement;

public class MListLine {
    private String mDate;
    private float mValue1;
    private float mValue2;
    private float mValue3;
    private float mValue4;
    private String mType;

    public MListLine() {
    }

    public void fillListLine(Measurement belly,
                             Measurement upperP,
                             Measurement lowerP,
                             Measurement heartbeatP){
        if (belly != null){
            mDate = belly.getMeasurementDate();
            mValue1 = belly.getMeasurementValue();
            mType = GlobalConstant.CASE_BELLY;
        } else if (upperP != null && lowerP != null && heartbeatP != null ){
            mDate = upperP.getMeasurementDate();
            mValue2 = upperP.getMeasurementValue();
            mValue3 = lowerP.getMeasurementValue();
            mValue4 = heartbeatP.getMeasurementValue();
            mType = GlobalConstant.CASE_BLOOD;
        }else {
            // geen belly noch blood
        }
    }

    public void fillListLineWBelly(Measurement belly){
        mDate = belly.getMeasurementDate();
        mValue1 = belly.getMeasurementValue();
        mType = GlobalConstant.CASE_BELLY;
    }

    public void fillListLineWBlood(Measurement upperP,
                                   Measurement lowerP,
                                   Measurement heartbeatP){
        mDate = upperP.getMeasurementDate();
        mValue2 = upperP.getMeasurementValue();
        mValue3 = lowerP.getMeasurementValue();
        mValue4 = heartbeatP.getMeasurementValue();
        mType = GlobalConstant.CASE_BLOOD;
    }

    public Measurement findCorM(List<Measurement> measurementList){
        Measurement fMeasurement = new Measurement();
        for (int i = 0; i < measurementList.size(); i++) {
            if (measurementList.get(i).getMeasurementDate().equals(mDate)){
                fMeasurement = measurementList.get(i);
            }
        }
        return fMeasurement;
    }

    public String getFormatDate() {
        String day = mDate.substring(0,2);
        String month = mDate.substring(2,4);
        String year = mDate.substring(4);
        return day + "/" + month + "/" + year;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public float getmValue1() {
        return mValue1;
    }

    public void setmValue1(float mValue1) {
        this.mValue1 = mValue1;
    }

    public float getmValue2() {
        return mValue2;
    }

    public void setmValue2(float mValue2) {
        this.mValue2 = mValue2;
    }

    public float getmValue3() {
        return mValue3;
    }

    public void setmValue3(float mValue3) {
        this.mValue3 = mValue3;
    }

    public float getmValue4() {
        return mValue4;
    }

    public void setmValue4(float mValue4) {
        this.mValue4 = mValue4;
    }
}
