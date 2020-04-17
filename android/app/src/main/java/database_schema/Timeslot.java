package database_schema;
import database_schema.Date;

import java.text.*;

public class Timeslot implements Comparable {
    private String tutorEmail;
    private String tutorName;
    private String date; // start date + time, appointments all 30 min increments
    private String[] courses;

    public Timeslot(String a,String d, String[] s, String n) {
        tutorEmail = a;
        date = d;
        courses = s;
        tutorName = n;
    }

    public String getTutor() {
        return tutorEmail;
    }

    public String getDate() {
        return date;
    }

    public String[] getCourses() { return courses; }

    public String getTutorName() { return tutorName; }

    public String getCoursesString() {
        if (courses.length == 0) {
            return "None";
        } else {
            String c = "";
            for (String course : courses) {
                c += course;
                c += ", ";
            }
            c.substring(0, c.length() - 2);
            return c;
        }
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

    @Override
    public String toString() {
        return "Timeslot: " + tutorName +" @ " + date + " with courses " +courses;
    }

}