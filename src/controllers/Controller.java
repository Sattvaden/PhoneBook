package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import objects.Lang;
import objects.Model;
import objects.Person;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import utils.DialogManager;
import utils.LocaleManager;


import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class Controller extends Observable implements Initializable {

    Stage stage;
    ModalController modalController;
    FXMLLoader loader = new FXMLLoader();
    Parent modalRoot;

    Person selectedPerson;

    ResourceBundle resourceBundle;

    ObservableList<Person> copy;

    private static final String RU_CODE = "ru";
    private static final String EN_CODE = "en";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupClearButtonField(searchText);
        resourceBundle = resources;
        initLoader();
        nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));
        model.fillList();

        copy = FXCollections.observableArrayList();
        contacts.setText(resourceBundle.getString("key.contacts") + ": " + model.getList().size());
        mainTable.setItems(model.getList());
        initListeners();
        fillLangComboBox();
    }

    private void fillLangComboBox() {
        Lang langRU = new Lang(1, RU_CODE, resourceBundle.getString("ru"), LocaleManager.RU_LOCALE);
        Lang langEN = new Lang(0, EN_CODE, resourceBundle.getString("en"), LocaleManager.EN_LOCALE);

        comboLocales.getItems().add(langEN);
        comboLocales.getItems().add(langRU);

        if (LocaleManager.getCurrentLang() == null) {
            comboLocales.getSelectionModel().select(0);
        } else {
            comboLocales.getSelectionModel().select(LocaleManager.getCurrentLang().getIndex());
        }
    }

    public void initLoader() {

        try {
            loader.setLocation(getClass().getResource("../fxml/modal.fxml"));
            loader.setResources(ResourceBundle.getBundle("bundles.Locale", new Locale("ru")));
            modalRoot = loader.load();
            modalController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Model model = Model.getInstance();
    @FXML
    private CustomTextField searchText;
    @FXML
    private Label contacts;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn phoneColumn;
    @FXML
    private TableView mainTable;
    @FXML
    private ComboBox comboLocales;


    private void initListeners() {

        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (searchText.getText().equals(""))
                mainTable.setItems(model.getList());
            //System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });

        model.getList().addListener(new ListChangeListener<Person>() {
            @Override
            public void onChanged(Change<? extends Person> c) {
                contacts.setText(resourceBundle.getString("key.contacts") + ": " + model.getList().size());
            }
        });
        mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Window owner = ((Node) event.getSource()).getScene().getWindow();
                if (event.getClickCount() == 2) {
                    selectedPerson = (Person) mainTable.getSelectionModel().getSelectedItem();
                    modalController.setPerson(selectedPerson);
                    openModalWindow(owner);
                    mainTable.getSelectionModel().clearSelection();
                }
            }
        });
        comboLocales.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Lang selectedLang = (Lang) comboLocales.getSelectionModel().getSelectedItem();
                LocaleManager.setCurrentLang(selectedLang);
                setChanged();
                notifyObservers(selectedLang);
            }
        });
    }

    public void openModalWindow(Window owner) {
        if (stage == null) {
            stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(modalRoot));
            stage.initOwner(owner);
        }

        stage.show();

    }

    public void allButtonsAction(ActionEvent actionEvent) {
        Window owner = ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            Node node = (Node) actionEvent.getSource();
            if (!(node instanceof Button)) return;//не нужная проверка, т.к. метод установлен только у кнопок
            selectedPerson = (Person) mainTable.getSelectionModel().getSelectedItem();


            switch (node.getId()) {
                case "addButton":
                    modalController.setPerson(new Person());
                    model.getList().add(modalController.getPerson());
                    openModalWindow(owner);
                    break;
                case "editButton":
                    if (selectedPerson == null) {
                        DialogManager.showInfoDialog(resourceBundle.getString("key.alertTitle"),
                                resourceBundle.getString("key.alertMessage"));
                        return;
                    }
                    modalController.setPerson(selectedPerson);
                    openModalWindow(owner);
                    break;
                case "deleteButton":
                    if (selectedPerson == null) {
                        DialogManager.showInfoDialog(resourceBundle.getString("key.alertTitle"),
                                resourceBundle.getString("key.alertMessage"));
                        return;
                    }
                    Alert alert = DialogManager.showConfirmDialog(resourceBundle.getString("key.confirmTitle"),
                            resourceBundle.getString("key.confirmMessage"));

                    if (alert.getResult() == ButtonType.CANCEL) {
                        mainTable.getSelectionModel().clearSelection();
                        return;
                    }
                    model.delete(selectedPerson);
                    break;
            }
            mainTable.getSelectionModel().clearSelection();
        } catch (RuntimeException e) {
            System.out.println("RunTimeException have been chaught");
        }
    }

    public void searchAction(ActionEvent actionEvent) {
        ObservableList<Person> temp = FXCollections.observableArrayList();
        if (searchText.getText().equals("")) {
            mainTable.setItems(model.getList());
            return;
        }
        for (Person p : model.getList()) {
            if (p.getName().toLowerCase().contains(searchText.getText().toLowerCase()) ||
                    p.getPhone().toLowerCase().contains(searchText.getText().toLowerCase())) {
                temp.add(p);
            }
        }
        mainTable.setItems(temp);
    }

    public void enterPressed(KeyEvent keyEvent) {
        mainTable.getSelectionModel().clearSelection();
        String name = searchText.getText();
        for (Person p : model.getList()) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                mainTable.getSelectionModel().select(p);
            }
        }
    }

    private void setupClearButtonField(CustomTextField customTextField) {
        try {
            Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
            m.setAccessible(true);
            m.invoke(null, customTextField, customTextField.rightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
