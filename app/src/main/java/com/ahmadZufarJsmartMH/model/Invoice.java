package com.ahmadZufarJsmartMH.model;

import java.util.Date;

/**
 * Merupakan Class sebagai model untuk invoice
 * @author Ahmad Zufar A
 * @version 17 Desember 2021
 */

public abstract class Invoice extends Serializable{
    public enum Status
    {
        WAITING_CONFIRMATION,
        CANCELLED,
        DELIVERED,
        ON_PROGRESS,
        ON_DELIVERY,
        COMPLAINT,
        FINISHED,
        FAILED
    }

    enum Rating
    {
        NONE,
        BAD,
        NEUTRAL,
        GOOD
    }

    public Date date;
    public int buyerId;
    public int productId;
    public int complaintId;
    public Rating rating;
}

