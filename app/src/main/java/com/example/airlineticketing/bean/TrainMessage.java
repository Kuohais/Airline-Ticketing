package com.example.airlineticketing.bean;

import java.io.Serializable;

/**
 * 飞机信息
 */
public class TrainMessage implements Serializable {
    private String time;
    private String trainId;
    private String trainname;
    private String traintype;
    private String startstation;
    private String arrivestation;
    private int carriagenumber;
    private int seatnumber;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainname() {
        return trainname;
    }

    public void setTrainname(String trainname) {
        this.trainname = trainname;
    }

    public String getTraintype() {
        return traintype;
    }

    public void setTraintype(String traintype) {
        this.traintype = traintype;
    }

    public String getStartstation() {
        return startstation;
    }

    public void setStartstation(String startstation) {
        this.startstation = startstation;
    }

    public String getArrivestation() {
        return arrivestation;
    }

    public void setArrivestation(String arrivestation) {
        this.arrivestation = arrivestation;
    }

    public int getCarriagenumber() {
        return carriagenumber;
    }

    public void setCarriagenumber(int carriagenumber) {
        this.carriagenumber = carriagenumber;
    }

    public int getSeatnumber() {
        return seatnumber;
    }

    public void setSeatnumber(int seatnumber) {
        this.seatnumber = seatnumber;
    }

    public TrainMessage(String time, String trainId, String trainname, String traintype, String startstation, String arrivestation, int carriagenumber, int seatnumber) {
        this.time = time;
        this.trainId = trainId;
        this.trainname = trainname;
        this.traintype = traintype;
        this.startstation = startstation;
        this.arrivestation = arrivestation;
        this.carriagenumber = carriagenumber;
        this.seatnumber = seatnumber;
    }
}
