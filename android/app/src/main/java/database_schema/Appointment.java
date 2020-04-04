package database_schema;

import database_schema.Date;

public class Appointment implements Comparable {
    private String tutorName;
    private String torEmail;
    private String tuteeName;
    private String teeEmail;
    private String date; // start date + time, appointments all 1 hr increments, YYYYMMDDHH
    private Boolean confirmed;

    public Appointment(String tutorN, String tuteeN, String d, String tutorE, String tuteeE) {
        tutorName = tutorN;
        tuteeName = tuteeN;
        date = d;
        torEmail = tutorE;
        teeEmail = tuteeE;
        confirmed = false;
    }

    public String getTutor() {
        return tutorName;
    }

    public String getTutee() {
        return tuteeName;
    }

    public String getDate() {
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

    @Override
    public int compareTo(Object o) {
        String d1 = this.date;
        String d2 = ((Timeslot) o).getDate();
        if(d1.compareTo(d2) > 0) {
            return 1;
        } else if(d1.compareTo(d2) < 0) {
            return -1;
        } else {
            return 0;
        }
    }

}
