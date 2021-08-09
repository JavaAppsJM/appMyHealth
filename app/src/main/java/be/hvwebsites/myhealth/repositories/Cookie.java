package be.hvwebsites.myhealth.repositories;

public class Cookie {
    private String cookieLabel;
    private String cookieValue;

    public Cookie() {
    }

    public Cookie(String cookieLabel, String cookieValue) {
        this.cookieLabel = cookieLabel;
        this.cookieValue = cookieValue;
    }

    public String getCookieLabel() {
        return cookieLabel;
    }

    public void setCookieLabel(String cookieLabel) {
        this.cookieLabel = cookieLabel;
    }

    public String getCookieValue() {
        return cookieValue;
    }

    public void setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
    }
}
