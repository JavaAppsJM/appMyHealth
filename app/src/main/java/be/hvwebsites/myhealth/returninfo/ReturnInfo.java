package be.hvwebsites.myhealth.returninfo;

public class ReturnInfo {
    private int returnCode = 0;
    private String returnMessage;

    public ReturnInfo(int returnCode) {
        this.returnCode = returnCode;
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
