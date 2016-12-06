package objects;

import db.SQLiteConnection;
import interfaces.Dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Model implements Dao {

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

    private void execQuery(String query) {
        try (Connection connection = SQLiteConnection.getConnection();
             Statement statement = connection.createStatement();) {
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getIndex(){
        int index = 0;
        for (int i = list.size(); i > 0; i--){
            if ((index = list.get(i - 1).getId()) != 0){
                break;
            }
        }
        return index;
    }
    public void fillList() {
        if (list == null) {
            list = FXCollections.observableArrayList();

            try (Connection connection = SQLiteConnection.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("select * from Person");) {
                while (resultSet.next()) {
                    list.add(new Person(resultSet.getString(2), resultSet.getString(3), resultSet.getInt(1)));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void add(Person person) {
        list.add(person);
        int index = getIndex();
        String insertQuery = null;
            person.setId(index + 1);
            insertQuery = "insert into Person (id, name, phone) " +
                    "values (" + person.getId() + ", '" + person.getName() + "', '" + person.getPhone() + "');";
            execQuery(insertQuery);
    }

    @Override
    public void edit(Person person) {
        String updateQuery = "update Person set name='" + person.getName() +
                "', phone='" + person.getPhone() + "' where id=" + person.getId() + ";";
        execQuery(updateQuery);
    }

    @Override
    public void delete(Person person) {
        try(Connection connection = SQLiteConnection.getConnection();
            Statement statement = connection.createStatement();){
            String query = "delete from Person where name=" + "'" + person.getName() + "';";
            statement.execute(query);
            System.out.println(getList().indexOf(person)+ 1);
            list.remove(person);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
//    DELETE FROM Customers

