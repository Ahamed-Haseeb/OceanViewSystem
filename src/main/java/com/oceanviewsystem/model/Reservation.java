package com.oceanviewsystem.model;

import java.sql.Date;

public class Reservation {
    private int reservationNo;
    private String guestName;
    private String address;
    private String contactNumber;
    private int roomId;
    private Date checkInDate;
    private Date checkOutDate;
    private double totalBill;
    private String status;
    private String createdBy; // NEW: To track which staff member made the booking

    public Reservation() {}

    public Reservation(String guestName, String address, String contactNumber, int roomId, Date checkInDate, Date checkOutDate) {
        this.guestName = guestName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public int getReservationNo() { return reservationNo; }
    public void setReservationNo(int reservationNo) { this.reservationNo = reservationNo; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public Date getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Date checkInDate) { this.checkInDate = checkInDate; }

    public Date getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Date checkOutDate) { this.checkOutDate = checkOutDate; }

    public double getTotalBill() { return totalBill; }
    public void setTotalBill(double totalBill) { this.totalBill = totalBill; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}