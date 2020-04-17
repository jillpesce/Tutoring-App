package database_schema;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String email;
    private String name;
    private String school;
    private String major;
    private String gradYear;
    private String bio;
    private ArrayList<String> courses;
    private boolean isTutor;

    public User(String email, String name, String school, String major, String gradYear, String bio)
    {
        this.email = email;
        this.name = name;
        this.school = school;
        this.major = major;
        this.gradYear = gradYear;
        this.bio = bio;
        this.courses = new ArrayList<String>();
        this.isTutor = false;
    }

    public String getName() { return this.name; }

    public String getEmail() { return this.email; }

    public String getSchool() { return this.school; }

    public String getMajor() { return this.major; }

    public String getGradYear() { return this.gradYear; }

    public String getBio() { return this.bio; }

    public ArrayList<String> getCourses() { return this.courses; }

    public boolean getIsTutor() { return this.isTutor; }

    public void setIsTutor(boolean isTutor) {
        this.isTutor = isTutor;
    }

    public void addCourse(String c) { courses.add(c); }

    public boolean hasCourse(String c) {
        for (int i = 0; i < courses.size(); i++) {
            if (c.equals(courses.get(i))){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "User({\n email:" + this.email + "\nname: " + this.name + "\n})";
    }
}
