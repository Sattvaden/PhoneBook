package controllers;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import objects.Model;
import objects.Person;


import java.io.IOException;

public class Controller {

    Stage stage;
    ModalController modalController;
    FXMLLoader loader = new FXMLLoader();
    Parent modalRoot;

    Person selectedPerson;
    public void initLoader(){

        try {
            loader.setLocation(getClass().getResource("../fxml/modal.fxml"));
            modalRoot = loader.load();
            modalController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Model model = new Model();
    @FXML
    private TextField searchText;
    @FXML
    private Label contacts;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn phoneColumn;
    @FXML
    private TableView mainTable;
    @FXML
    public void initialize(){
        initLoader();
        nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));
        model.fillList();
        contacts.setText("contacts: " + model.getList().size());
        mainTable.setItems(model.getList());
        model.getList().addListener(new ListChangeListener<Person>() {
            @Override
            public void onChanged(Change<? extends Person> c) {
                contacts.setText("contacts: " + model.getList().size());
            }
        });
        mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Window owner = ((Node) event.getSource()).getScene().getWindow();
                if (event.getClickCount() == 2){
                    selectedPerson = (Person)mainTable.getSelectionModel().getSelectedItem();
                    modalController.setPerson(selectedPerson);
                    openModalWindow(owner);
                }
            }
        });

    }

    public void openModalWindow(Window owner){
        if (stage == null){
            stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(modalRoot));
            stage.initOwner(owner);
        }

        stage.show();

    }

    public void allButtonsAction(ActionEvent actionEvent) {
        Window owner = ((Node) actionEvent.getSource()).getScene().getWindow();
        try{
            Node node = (Node) actionEvent.getSource();
            if (!(node instanceof Button)) return;//не нужная проверка, т.к. метод установлен только у кнопок
            selectedPerson = (Person)mainTable.getSelectionModel().getSelectedItem();


            switch (node.getId()){
                case "addButton":
                    modalController.setPerson(new Person());
                    model.getList().add(modalController.getPerson());
                    openModalWindow(owner);
                    mainTable.getSelectionModel().clearSelection();
                    break;
                case "editButton":
                    modalController.setPerson(selectedPerson);
                    openModalWindow(owner);
                    mainTable.getSelectionModel().clearSelection();
                    break;
                case "deleteButton":
                    model.delete(selectedPerson);
                    mainTable.getSelectionModel().clearSelection();
                    break;
            }

        }catch (RuntimeException e){
            System.out.println("RunTimeException have been chaught");
        }
    }

    public void searchAction(ActionEvent actionEvent) {
        mainTable.getSelectionModel().clearSelection();
        String name = searchText.getText();
        for (Person p: model.getList()){
            if(p.getName().toLowerCase().contains(name.toLowerCase())){
                System.out.println(p.getName());
                mainTable.getSelectionModel().select(p);
            }
        }
    }

    public void enterPressed(KeyEvent keyEvent) {
        mainTable.getSelectionModel().clearSelection();
        String name = searchText.getText();
        for (Person p: model.getList()){
            if(p.getName().toLowerCase().contains(name.toLowerCase())){
                mainTable.getSelectionModel().select(p);
            }
        }
    }

}
