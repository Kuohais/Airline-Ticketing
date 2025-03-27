package com.example.airlineticketing.bean;

import java.io.Serializable;

/**
 * 订单
 */
public class Order implements Serializable {
    private Integer id;//
    private String time;
    private Integer userId;
    private String trainId;
    private String start_station_name;
    private String leaveTime;
    private String arrive_station_name;
    private String arriveTime;
    private Integer carriageId;
    private Integer rowId;
    private String position;
    private String seatType;
    private Integer price;
    private String orderTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getArrive_station_name() {
        return arrive_station_name;
    }

    public void setArrive_station_name(String arrive_station_name) {
        this.arrive_station_name = arrive_station_name;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Integer getCarriageId() {
        return carriageId;
    }

    public void setCarriageId(Integer carriageId) {
        this.carriageId = carriageId;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Order(Integer id, String time, Integer userId, String trainId, String start_station_name, String leaveTime, String arrive_station_name, String arriveTime, Integer carriageId, Integer rowId, String position, String seatType, Integer price, String orderTime) {
        this.id = id;
        this.time = time;
        this.userId = userId;
        this.trainId = trainId;
        this.start_station_name = start_station_name;
        this.leaveTime = leaveTime;
        this.arrive_station_name = arrive_station_name;
        this.arriveTime = arriveTime;
        this.carriageId = carriageId;
        this.rowId = rowId;
        this.position = position;
        this.seatType = seatType;
        this.price = price;
        this.orderTime = orderTime;
    }
}
