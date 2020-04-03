package database_schema;

import java.util.Date;
import java.text.*;

public class Timeslot implements Comparable {
    private Tutor tor;
    private Date date; // start date + time, appointments all 30 min increments
    private String[] courses;

    public Timeslot(Tutor a,Date d, String[] s) {
        tor = a;
        date = d;
        courses = s;
    }

    public Tutor getTutor() {
        return tor;
    }

    public Date getDate() {
        return date;
    }

    public String[] getCourses() { return courses; }

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