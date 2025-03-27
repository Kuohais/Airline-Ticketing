package com.example.airlineticketing.bean;

import java.io.Serializable;

/**
 * 座次
 */
public class TrainTicketPrice implements Serializable {
    private String trainid;
    private String fromstationname;
    private String tostationname;
    private Integer fromstationid;
    private Integer tostationid;
    private String seattype;
    private Integer price;

    public String getTrainid() {
        return trainid;
    }

    public void setTrainid(String trainid) {
        this.trainid = trainid;
    }

    public String getFromstationname() {
        return fromstationname;
    }

    public void setFromstationname(String fromstationname) {
        this.fromstationname = fromstationname;
    }

    public String getTostationname() {
        return tostationname;
    }

    public void setTostationname(String tostationname) {
        this.tostationname = tostationname;
    }

    public Integer getFromstationid() {
        return fromstationid;
    }

    public void setFromstationid(Integer fromstationid) {
        this.fromstationid = fromstationid;
    }

    public Integer getTostationid() {
        return tostationid;
    }

    public void setTostationid(Integer tostationid) {
        this.tostationid = tostationid;
    }

    public String getSeattype() {
        return seattype;
    }

    public void setSeattype(String seattype) {
        this.seattype = seattype;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public TrainTicketPrice(String trainid, String fromstationname, String tostationname, Integer fromstationid, Integer tostationid, String seattype, Integer price) {
        this.trainid = trainid;
        this.fromstationname = fromstationname;
        this.tostationname = tostationname;
        this.fromstationid = fromstationid;
        this.tostationid = tostationid;
        this.seattype = seattype;
        this.price = price;
    }
}
