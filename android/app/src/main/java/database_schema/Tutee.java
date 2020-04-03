
package database_schema;

public class Tutee {
    private User user;

    public Tutee(User u) {
        user = u;
    }

    public String getName() {
        return user.getName();
    }



}