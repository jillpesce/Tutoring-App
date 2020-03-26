package database_schema;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String name;
    private String school;
    private String major;
    private String gradYear;
    private String bio;

    public User(String email, String name, String school, String major, String gradYear, String bio)
    {
        this.email = email;
        this.name = name;
        this.school = school;
        this.major = major;
        this.gradYear = gradYear;
        this.bio = bio;
    }

    public String getName() { return this.name; }

    public String getEmail() { return this.email; }

    public String getSchool() { return this.school; }

    public String getMajor() { return this.major; }

    public String getGradYear() { return this.gradYear; }

    public String getBio() { return this.bio; }

    @Override
    public String toString() {
        String result = "User({\n email:" + this.email + "\nname: " + this.name + "\n})";
        return result;
    }
}
