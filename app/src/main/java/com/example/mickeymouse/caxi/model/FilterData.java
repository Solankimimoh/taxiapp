package com.example.mickeymouse.caxi.model;

/**
 * Created by MickeyMouse on 05-Apr-17.
 */

public class FilterData
{

    private String bookid;
    private String userName;
    private String carType;
    private String time;

    public FilterData() {
    }

    public FilterData(String bookid, String userName, String carType, String time) {
        this.bookid = bookid;
        this.userName = userName;
        this.carType = carType;
        this.time = time;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
