package controllers;

import db.SQLiteConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import objects.Model;
import objects.Person;
import utils.DialogManager;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ModalController implements Initializable {

    public Person getPerson() {
        return person;
    }

    private Person person;

    private Model model = Model.getInstance();
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;


    public void setPerson(Person person) {
        this.person = person;
        nameTextField.setText(person.getName());
        phoneTextField.setText(person.getPhone());
    }


    public void okButton(ActionEvent actionEvent) {
        if (nameTextField.getText().equals("") || phoneTextField.getText().equals("")) {
            DialogManager.showErrorDialog(resourceBundle.getString("key.emptySpaceTitle"),
                    resourceBundle.getString("key.emptySpaceText"));
            return;
        }
        person.setName(nameTextField.getText());
        person.setPhone(phoneTextField.getText());
        cancelAction(actionEvent);
        if (person.getId() == 0)
            model.add(person);
        else
            model.edit(person);

    }


    public void cancelAction(ActionEvent actionEvent) {

        ((Stage) (((Node) actionEvent.getSource()).getScene().getWindow())).close();
    }

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
    }
}
