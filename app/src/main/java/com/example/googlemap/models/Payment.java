package com.example.googlemap.models;

public class Payment {
    // The database function in the CridetCardActivity and the time function in the BookingDetailScreen should be in the payment class as functions .
    //There should be 2 functions : 1- payment: Cridet Card information  2- price calculation : TotalPrice( hours , price ,vat)
    //And it should be connected with the booking id to take the start time
    private int price;
    private String PaymentId;   //Payment id must be unique for each Payment
    private Booking booking;

    public Payment()
    {

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPaymentId() {
        return PaymentId;
    }

    public void setPaymentId(String paymentId) {
        PaymentId = paymentId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
