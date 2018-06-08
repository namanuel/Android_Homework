package at.fh.manuel.ex_03;

import android.support.annotation.NonNull;

import java.util.Date;

public class VitalData implements Comparable<VitalData>{
    private Date timestamp;
    private int systolicPressure;
    private int diastolicPressure;
    private int heartRate;

    public VitalData(Date timestamp, int systolicPressure, int diastolicPressure, int heartRate) {
        this.timestamp = timestamp;
        this.systolicPressure = systolicPressure;
        this.diastolicPressure = diastolicPressure;
        this.heartRate = heartRate;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(int systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public int getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    @Override
    public int compareTo(@NonNull VitalData second) {
        return second.timestamp.compareTo(timestamp);
    }
}