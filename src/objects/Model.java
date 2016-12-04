package objects;

import interfaces.PhoneBook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model implements PhoneBook{
    private ObservableList<Person> list = FXCollections.observableArrayList();

    public ObservableList<Person> getList() {
        return list;
    }

    public void fillList(){
        for (int i = 1; i < 21; i++){
            String name = "person" + i;
            String phone = "777 - 777 - 0" + i;
            list.add(new Person(name, phone));
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

        list.remove(person);
    }
}
