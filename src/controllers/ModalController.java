package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import objects.Person;

public class ModalController {

    public Person getPerson() {
        return person;
    }

    private Person person;

    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;


    public void setPerson(Person person){
        this.person = person;
        nameTextField.setText(person.getName());
        phoneTextField.setText(person.getPhone());
    }


    public void saveAction(ActionEvent actionEvent) {
        person.setName(nameTextField.getText());
        person.setPhone(phoneTextField.getText());
        ((Stage)(((Node)actionEvent.getSource()).getScene().getWindow())).close();
    }


    public void okButton(ActionEvent actionEvent) {
        System.out.println(nameTextField.getText());
        person.setName(nameTextField.getText());
        person.setPhone(phoneTextField.getText());
        ((Stage)(((Node)actionEvent.getSource()).getScene().getWindow())).close();
    }

    public void cancelAction(ActionEvent actionEvent) {
        ((Stage)(((Node)actionEvent.getSource()).getScene().getWindow())).close();
    }


    public TextField getNameTextField() {
        return nameTextField;
    }

    public TextField getPhoneTextField() {
        return phoneTextField;
    }

    public void enterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            person.setName(nameTextField.getText());
            person.setPhone(phoneTextField.getText());
            ((Stage)(((Node)keyEvent.getSource()).getScene().getWindow())).close();
        }
    }
}
