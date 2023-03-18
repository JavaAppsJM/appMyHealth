package be.hvwebsites.myhealth.helpers;

public class ReturnInfo {
    private int returnCode = 0;
    private String returnMessage;

    public ReturnInfo(int returnCode, String returnMessage) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

}
