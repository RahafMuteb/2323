package com.example.googlemap.models;

import com.example.googlemap.utils.Globals;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Booking {
//  The booking class should have the booking data and function itself ( which stores all the booking information in the database )
//  and it should be connected with the locker id from the locker class and address from the model class
    private String BookingId; //booking id must be unique for each booking
    private String otp;
    private long startTime;
    private Payment payment;
    private double timeConsumed;
    private String email;

    public Booking()
    {

    }


    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public double getTimeConsumed() {
        return timeConsumed;
    }

    public void setTimeConsumed(double timeConsumed) {
        this.timeConsumed = timeConsumed;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getBookingId() {
        return BookingId;
    }

    public void setBookingId(String bookingId) {
        BookingId = bookingId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public void startTimer()
    {
        startTime = Calendar.getInstance().getTimeInMillis();
    }


    // Function to calculate total hours from the given start time in milliseconds
    public void calculateTotalHours() {
        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Calculate the time difference in milliseconds
        long timeDifferenceMillis = currentTimeMillis - startTime;

        // Convert the time difference to hours using TimeUnit
        double totalHours = TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis);

        // Format the result for human-readable output
        if (totalHours < 1) {

            timeConsumed =  1;
        } else timeConsumed =  totalHours;
    }

    public void calculateTotalPayment()
    {
        calculateTotalHours();
        payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        double totalPrice = timeConsumed * Globals.currentPost.getPrice();
        payment.setPrice((int) totalPrice);
    }

}
