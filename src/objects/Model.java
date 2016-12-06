package objects;

import db.SQLiteConnection;
import interfaces.PhoneBook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Model implements PhoneBook {

    private static Model ourInstance = new Model();

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {

    }

    private ObservableList<Person> list;

    public ObservableList<Person> getList() {
        return list;
    }

    public void fillList() {
        if (list == null) {
            list = FXCollections.observableArrayList();

            try (Connection connection = SQLiteConnection.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("select * from Person");) {
                while (resultSet.next()) {
                    list.add(new Person(resultSet.getString(2), resultSet.getString(3)));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void add(Person person) {
        list.add(person);
    }

    @Override
    public void edit(Person person) {

    }

    @Override
    public void delete(Person person) {
        try(Connection connection = SQLiteConnection.getConnection();
            Statement statement = connection.createStatement();){
            String query = "delete from Person where id=" + (list.indexOf(person)+ 1) + ";";
            statement.execute(query);
            System.out.println(getList().indexOf(person)+ 1);
            list.remove(person);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
//    DELETE FROM Customers

