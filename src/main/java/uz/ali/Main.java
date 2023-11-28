package uz.ali;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        // database uxladi hamma data lar ochib ketti
//        Profile profile = authorizationWrittenWithStatement("ali';delete from profile;--", "alishman");
//        Profile profile = authorizationWrittenWithStatement("alish", "alishman");
//        System.out.println(profile);

        // Boyagi xakkerskiy query bi yozsak ham ta'sir qila olmaydi
//        Profile profile = authorizationWrittenWithPreparedtatement("ali';delete from profile;--", "alishman");
        Profile profile = authorizationWrittenWithPreparedtatement("alish", "alishman");
        System.out.println(profile);

    }

    // statement dan String larni qabul qilganda parametrlar bilan ishlaganda ishlatmaslik kerak
    // xafi kotta, SQL injection ga olib keladi
    public static Profile authorizationWrittenWithStatement(String login, String password) {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            String selectQuery = "select * from profile where login = '%s' and password = '%s'";
            selectQuery = String.format(selectQuery, login, password);
//            System.out.println(selectQuery);

            Profile profile = null;
            ResultSet resultSet = statement.executeQuery(selectQuery);

            if (resultSet.next()) {
                profile = new Profile(resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("login"),
                        resultSet.getString("password"));
            }
            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // PreparedStatement Bilan yozilgan query va method ancha havsiz va bunda sql injection ni oldina olinadi.
    public static Profile authorizationWrittenWithPreparedtatement(String login, String password) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String selectQuery = "select * from profile where login = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            Profile profile = null;
            System.out.println(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                profile = new Profile(resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("login"),
                        resultSet.getString("password"));
            }
            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}