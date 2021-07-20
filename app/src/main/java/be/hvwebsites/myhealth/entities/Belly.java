package be.hvwebsites.myhealth.entities;

public class Belly extends Measurement {
/*
    private String date;
    private float bellyRadius;
    private String remark; // initieel niet gebruikt
    private int dateInt;
    public static final String EXTRA_INTENT_KEY_DATE =
            "be.hvwebsites.myhealth.INTENT_KEY_DATE";
    public static final String EXTRA_INTENT_KEY_RADIUS =
            "be.hvwebsites.myhealth.INTENT_KEY_RADIUS";
*/

    public Belly() {
    }

    public Belly(String inputDate, float inputValue) {
        super(inputDate, inputValue);
/*
        setDate(trimDate(inputDate));
        this.bellyRadius = bellyRadius;
*/
    }
/*

    private String trimDate(String inputDate){
        String[] dateStringParts = inputDate.split("/");
        String day = leadingZero(dateStringParts[0]);
        String month = leadingZero(dateStringParts[1]);
        String year = dateStringParts[2];
        return day + month + year;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
        this.dateInt = Integer.parseInt(date.substring(4) + date.substring(2,4) + date.substring(0,2));
    }

    private String leadingZero(String string){
        if (Integer.parseInt(string) < 10 && string.length() < 2){
            return  "0" + string;
        }else {
            return string;
        }
    }

    public String getFormatDate() {
        String day = date.substring(0,2);
        String month = date.substring(2,4);
        String year = date.substring(4);
        return day + "/" + month + "/" + year;
    }

    public String getDate(){
        return this.date;
    }
    public float getBellyRadius() {
        return bellyRadius;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setBellyRadius(float bellyRadius) {
        this.bellyRadius = bellyRadius;
    }

    public int getDateInt() {
        return dateInt;
    }

    public void setDateInt(int dateInt) {
        this.dateInt = dateInt;
    }

    public void setBelly(Belly belly2){
        this.date = belly2.getDate();
        this.bellyRadius = belly2.getBellyRadius();
        this.dateInt = belly2.getDateInt();
        this.remark = belly2.getRemark();
    }

    @Override
    public String toString() {
        return "Belly{" +
                "date='" + date + '\'' +
                ", bellyRadius=" + bellyRadius +
                ", remark='" + remark + '\'' +
                ", dateInt=" + dateInt +
                '}';
    }
*/

}
