package com.example.googlemap.models;

public class Locker
{
    //In the locker class , the price shouldn’t be there it should be in the payment class
    // and the starting time and otp should be in the booking class
    private int id;
    private boolean isAvailable = true;
    private Booking booking;
    private String size;
    private int price;

    public Locker(int id, int price, String size) {
        this.id = id;
        this.price = price;
        this.size = size;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Locker()
    {

    }


    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
