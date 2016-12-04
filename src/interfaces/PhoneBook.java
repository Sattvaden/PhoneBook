package interfaces;


import objects.Person;

public interface PhoneBook {

    void add(Person person);

    void edit(Person person);

    void delete(Person person);
}
