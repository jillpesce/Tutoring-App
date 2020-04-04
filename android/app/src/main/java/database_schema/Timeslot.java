package database_schema;

import java.util.Date;
import java.text.*;

public class Timeslot implements Comparable {
    private String tutorEmail;
    private String date; // start date + time, appointments all 30 min increments
    private String[] courses;

    public Timeslot(String a,String d, String[] s) {
        tutorEmail = a;
        date = d;
        courses = s;
    }

    public String getTutor() {
        return tutorEmail;
    }

    public String getDate() {
        return date;
    }

    public String[] getCourses() { return courses; }

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