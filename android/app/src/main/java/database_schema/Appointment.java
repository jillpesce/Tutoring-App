package database_schema;

import java.util.Date;

public class Appointment {
    private Tutor tor;
    private Tutee tee;
    private Date date; // start date + time, appointments all 30 min increments
    private String cl;
    private Boolean confirmed;

    public Appointment(Tutor a, Tutee b, Date d, String c) {
        tor = a;
        tee = b;
        date = d;
        cl = c;
        confirmed = false;
    }

    public Tutor getTutor() {
        return tor;
    }

    public Tutee getTutee() {
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