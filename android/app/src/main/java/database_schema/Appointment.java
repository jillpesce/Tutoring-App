package database_schema;

import java.util.Date;

public class Appointment implements Comparable {
    private String tor;
    private String tee;
    private Date date; // start date + time, appointments all 1 hr increments
    private String cl;
    private Boolean confirmed;

    public Appointment(String a, String b, Date d, String c) {
        tor = a;
        tee = b;
        date = d;
        cl = c;
        confirmed = false;
    }

    public String getTutor() {
        return tor;
    }

    public String getTutee() {
        return tee;
    }

    public String getCourse() {
        return cl;
    }

    public Date getDate() {
        return date;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void confirmAppointment() {
        confirmed = true;
    }

    public void cancelAppointment() {
        confirmed = false;
    }

}
    @Override
    public int compareTo(Object o) {
        Date d1 = this.date;
        Date d2 = ((Timeslot) o).getDate();
        if(d1.compareTo(d2) > 0) {
            return 1;
        } else if(d1.compareTo(d2) < 0) {
            return -1;
        } else {
            return 0;
        }
    }

}
