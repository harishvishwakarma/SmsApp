package com.tychestudios.android.buthatkesms.model;

import java.security.Timestamp;

/**
 * Created by Harish Vishwakarma on 4/26/2016.
 */
public class Message {
    private String smsBody;
    private Timestamp time;
    private String smsFrom;

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getSmsFrom() {
        return smsFrom;
    }

    public void setSmsFrom(String smsFrom) {
        this.smsFrom = smsFrom;
    }

    @Override
    public String toString() {
        return "Message{" +
                "smsBody='" + smsBody + '\'' +
                ", time=" + time +
                ", smsFrom='" + smsFrom + '\'' +
                '}';
    }
}
