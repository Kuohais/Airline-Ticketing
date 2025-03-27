package com.example.airlineticketing.bean;

import java.io.Serializable;

/**
 * 飞机信息
 */
public class TrainVo implements Serializable {
    private String time;
    private String trainId;
    private String start_station_name;
    private int start_station_id;
    private String arrive_station_name;
    private int arrive_station_id;
    private String leaveTime;
    private String arriveTime;
    private int firstSeatNumber;
    private int secondSeatNumber;
    private double firstSeatPrice;
    private double secondSeatPrice;

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

    public String getStart_station_name() {
        return start_station_name;
    }

    public void setStart_station_name(String start_station_name) {
        this.start_station_name = start_station_name;
    }

    public int getStart_station_id() {
        return start_station_id;
    }

    public void setStart_station_id(int start_station_id) {
        this.start_station_id = start_station_id;
    }

    public String getArrive_station_name() {
        return arrive_station_name;
    }

    public void setArrive_station_name(String arrive_station_name) {
        this.arrive_station_name = arrive_station_name;
    }

    public int getArrive_station_id() {
        return arrive_station_id;
    }

    public void setArrive_station_id(int arrive_station_id) {
        this.arrive_station_id = arrive_station_id;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public int getFirstSeatNumber() {
        return firstSeatNumber;
    }

    public void setFirstSeatNumber(int firstSeatNumber) {
        this.firstSeatNumber = firstSeatNumber;
    }

    public int getSecondSeatNumber() {
        return secondSeatNumber;
    }

    public void setSecondSeatNumber(int secondSeatNumber) {
        this.secondSeatNumber = secondSeatNumber;
    }

    public double getFirstSeatPrice() {
        return firstSeatPrice;
    }

    public void setFirstSeatPrice(double firstSeatPrice) {
        this.firstSeatPrice = firstSeatPrice;
    }

    public double getSecondSeatPrice() {
        return secondSeatPrice;
    }

    public void setSecondSeatPrice(double secondSeatPrice) {
        this.secondSeatPrice = secondSeatPrice;
    }

    public TrainVo(String time, String trainId, String start_station_name, int start_station_id, String arrive_station_name, int arrive_station_id, String leaveTime, String arriveTime, int firstSeatNumber, int secondSeatNumber, double firstSeatPrice, double secondSeatPrice) {
        this.time = time;
        this.trainId = trainId;
        this.start_station_name = start_station_name;
        this.start_station_id = start_station_id;
        this.arrive_station_name = arrive_station_name;
        this.arrive_station_id = arrive_station_id;
        this.leaveTime = leaveTime;
        this.arriveTime = arriveTime;
        this.firstSeatNumber = firstSeatNumber;
        this.secondSeatNumber = secondSeatNumber;
        this.firstSeatPrice = firstSeatPrice;
        this.secondSeatPrice = secondSeatPrice;
    }
}
