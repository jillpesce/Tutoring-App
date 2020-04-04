package database_schema;

import java.util.*;

import database_schema.User;

public class Tutor {

    private User user;
    private String[] courses;

    public Tutor(User u, String[] cs) {
        user = u;
        courses = cs;
    }

    public String getName() {
        return user.getName();
    }

    public String[] getCourses() {
        return courses;
    }

}

