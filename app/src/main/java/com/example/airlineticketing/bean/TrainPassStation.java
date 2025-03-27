package com.example.airlineticketing.bean;

import java.io.Serializable;

/**
 * 经停站
 */
public class TrainPassStation implements Serializable {
    private String time;
    private String trainid;
    private String cityname;
    private String stationname;
    private String stationid;
    private String arrivetime;
    private String staytime;
    private String leavetime;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTrainid() {
        return trainid;
    }

    public void setTrainid(String trainid) {
        this.trainid = trainid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public String getArrivetime() {
        return arrivetime;
    }

    public void setArrivetime(String arrivetime) {
        this.arrivetime = arrivetime;
    }

    public String getStaytime() {
        return staytime;
    }

    public void setStaytime(String staytime) {
        this.staytime = staytime;
    }

    public String getLeavetime() {
        return leavetime;
    }

    public void setLeavetime(String leavetime) {
        this.leavetime = leavetime;
    }

    public TrainPassStation(String time, String trainid, String cityname, String stationname, String stationid, String arrivetime, String staytime, String leavetime) {
        this.time = time;
        this.trainid = trainid;
        this.cityname = cityname;
        this.stationname = stationname;
        this.stationid = stationid;
        this.arrivetime = arrivetime;
        this.staytime = staytime;
        this.leavetime = leavetime;
    }
}
