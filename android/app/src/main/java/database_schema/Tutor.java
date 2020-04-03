package database_schema;

public class Tutor {

    private User user;

    public Tutor(User u) {
        user = u;
    }

    public String getName() {
        return user.getName();
    }

}
