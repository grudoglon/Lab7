package database;

public class SqlQuery {
    public static class Get {
        //City
        public static final String PEN = "SELECT pen.id, pen.name, pen.creation_date, pen.width_of_kernel, pen.amount, pen.length_of_kernel, pen.weight, pen.exists\n" +
                "FROM pen\n";

        public static final String PEN_BY_ID = "SELECT id FROM pen where id = ?";

        //Users
        public static final String USERS = "SELECT * FROM users";
        public static final String PASS_USING_USERNAME = "SELECT password, id FROM users WHERE username = ?";
        public static final String ID_USING_USERNAME = "SELECT id FROM users WHERE username = ?";

        public static final String USER_HAS_PERMISSIONS = "" +
                "SELECT exists(SELECT 1 from users_pen where user_id = ? AND pen_id = ?)";
    }
    public static class Add {
        //City
        public static final String PEN = "" +
                "INSERT INTO pen(name, creation_date, width_of_kernel, amount, length_of_kernel, weight, exists)\n"+
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";





        //Users
        public static final String USER = "" +
                "INSERT INTO users(username, password) VALUES(?, ?)";

        public static final String PEN_USER_RELATIONSHIP = "" +
                "INSERT INTO users_pen VALUES (?, ?)";
    }
    public static class Update {
        public static final String PEN = "" +
        "UPDATE pen SET name=?, creation_date=?, width_of_kernel=?, amount=?, length_of_kernel=?, weight=?, exists=?\n"+
                "WHERE pen.id = ?;";


    }
    public static class Delete {
        public static final String ALL_PEN = "DELETE FROM pen";
        public static final String PEN_BY_ID = "DELETE FROM pen where id = ?";

        public static final String USER = "DELETE FROM users where username = ?";
    }
}
