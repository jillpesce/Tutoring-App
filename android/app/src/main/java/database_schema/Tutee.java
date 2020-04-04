package database_schema;

import java.util.*;
import java.text.*;

public class Tutee {
    private User user;

    public Tutee(User u) {
        user = u;
    }

    public String getName() {
        return user.getName();
    }



}

