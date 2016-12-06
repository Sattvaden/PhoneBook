package interfaces;


import objects.Person;

public interface Dao {

    void add(Person person);

    void edit(Person person);

    void delete(Person person);
}
