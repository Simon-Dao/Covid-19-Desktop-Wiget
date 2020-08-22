package config;

public class ConfigModel {

    private int refreshRateInSeconds;
    private String countryName;
    private String countryCode;

    public ConfigModel() {
        refreshRateInSeconds = 30;
        countryName = "usa";
        countryCode = "usa";
    }

    public int getRefreshRateInSeconds() {
        return refreshRateInSeconds;
    }

    public void setRefreshRateInSeconds(int refreshRateInSeconds) {
        this.refreshRateInSeconds = refreshRateInSeconds;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
