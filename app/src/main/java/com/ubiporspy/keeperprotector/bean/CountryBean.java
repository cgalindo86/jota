package com.ubiporspy.keeperprotector.bean;

public class CountryBean {

    private String name;
    private String phoneCode;

    public CountryBean() {
    }

    public CountryBean(String name, String phoneCode) {
        this.name = name;
        this.phoneCode = phoneCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

}