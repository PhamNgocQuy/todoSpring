package com.haku.model;

import java.sql.*;

/**
 * Created by QuyPN on 6/22/2017.
 */
public class AccountService {

    public AccountService() {
    }

    public static Boolean checkUser(String user, String password) {
        System.out.println("Checking");
        try {
            Statement statement = DataBaseService.getConnection().createStatement();
            String query = "SELECT * FROM account";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                //System.out.println(resultSet.getString(1));
                if (resultSet.getString(2).equals(user) && resultSet.getString(3).equals(password)) {
                    // System.out.printf("correct");
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerAccount(String user, String password) {

        try {
            Statement statement = DataBaseService.getConnection().createStatement();
            String query = "SELECT * FROM account";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getString(2).equals(user)) {
                        return false;
                }
            }
            String query_ = "INSERT INTO account (username,password)" + "value(?,?)";
            PreparedStatement preparedStatement = DataBaseService.getConnection().prepareStatement(query_);
            preparedStatement.setString(1,user);
            preparedStatement.setString(2,password);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.printf("Error Insern Account");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.printf("Error Insern Account");
            e.printStackTrace();
        }

        return true;
    }

    public static int getIdUser(String user, String password) {
        int idUser = 0;
        try {
            Statement statement = DataBaseService.getConnection().createStatement();
            String query = "SELECT * FROM account";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getString(2).equals(user) && resultSet.getString(3).equals(password)) {
                    idUser = resultSet.getInt(1);
                }
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return idUser;
    }


}
