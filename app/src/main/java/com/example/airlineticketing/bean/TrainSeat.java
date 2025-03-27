package com.example.airlineticketing.bean;

import java.io.Serializable;

/**
 * 座次
 */
public class TrainSeat implements Serializable {
    private String time;
    private String trainid;
    private String carriageid;
    private Integer rowid;
    private Integer position;
    private String seattype;
    private Integer fromstation;
    private Integer tostation;

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

    public String getCarriageid() {
        return carriageid;
    }

    public void setCarriageid(String carriageid) {
        this.carriageid = carriageid;
    }

    public Integer getRowid() {
        return rowid;
    }

    public void setRowid(Integer rowid) {
        this.rowid = rowid;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getSeattype() {
        return seattype;
    }

    public void setSeattype(String seattype) {
        this.seattype = seattype;
    }

    public Integer getFromstation() {
        return fromstation;
    }

    public void setFromstation(Integer fromstation) {
        this.fromstation = fromstation;
    }

    public Integer getTostation() {
        return tostation;
    }

    public void setTostation(Integer tostation) {
        this.tostation = tostation;
    }

    public TrainSeat(String time, String trainid, String carriageid, Integer rowid, Integer position, String seattype, Integer fromstation, Integer tostation) {
        this.time = time;
        this.trainid = trainid;
        this.carriageid = carriageid;
        this.rowid = rowid;
        this.position = position;
        this.seattype = seattype;
        this.fromstation = fromstation;
        this.tostation = tostation;
    }
}
