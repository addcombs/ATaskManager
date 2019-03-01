package com.ataskmanager.controllers;

import com.ataskmanager.ATaskManager;
import com.ataskmanager.dao.LocationDao;
import com.ataskmanager.dao.UserDao;
import com.ataskmanager.entities.Location;
import com.ataskmanager.entities.User;
import com.ataskmanager.messages.NewUserProducer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Controller for the Add User Form
 * @author Adam Combs
 * @author Seth Wampole
 */
public class AddUserController {

          private UserDao userDao = new UserDao();
          private LocationDao locationDao = new LocationDao();

          @FXML private TextField username;
          @FXML private TextField firstName;
          @FXML private TextField lastName;
          @FXML private ComboBox<Location> locationBox;
          @FXML private ComboBox<String> roleBox;

          /**
           *        Loads on stage open.
           *        Create Observable List with all locations and worker or manager for dropdowns
           */
          public void initialize(){
                    ObservableList<Location> observableLocationList = FXCollections.observableArrayList(locationDao.getAllLocations());
                    ObservableList<String> observableRoleList = FXCollections.observableArrayList("Worker","Manager");

                    locationBox.setItems(observableLocationList);
                    locationBox.setButtonCell(locationDao.cellFactory.call(null));
                    locationBox.setCellFactory(locationDao.cellFactory);
                    roleBox.setItems(observableRoleList);
          }

          /**
           *        On submit of form.
           *        Checks for blank fields.
           *        Gets user input.
           *        Saves user to database.
           *        Refreshs data and UI of main stage.
           *        Sends New User retained message
           *        Closes add user stage.
           */
          @FXML
          public void onSubmit(){
                    if (checkBlanks()){
                              return;
                    }
                    Location userLocation = locationBox.getSelectionModel().getSelectedItem();
                    Integer onlineStatus = 0;
                    String role = "";
                    if (roleBox.getSelectionModel().getSelectedItem().equals("Worker")){
                              role = "w";
                    }else {
                              role = "m";
                    }
                    userDao.saveUser(new User(username.getText(),username.getText(),firstName.getText(),lastName.getText(),
                            userLocation.getId(),onlineStatus,role));

                    ATaskManager.getATMController().refreshData();
                    ATaskManager.getATMController().refreshUI();
                    NewUserProducer.publishMQTT(username.getText());
                    Stage addUserStage = (Stage) username.getScene().getWindow();
                    addUserStage.close();
          }

          /**
           *        On cancel, closes add user stage
           */
          @FXML
          public void onCancel(){
                    Stage addUserStage = (Stage) username.getScene().getWindow();
                    addUserStage.close();
          }

          /**
           *        Checks for blank fields.
           *        Returns true if blanks exist.  Otherwise, returns false.
           *
           *        @return             Boolean
           */
          private Boolean checkBlanks(){
                    if (username.getText().trim().isEmpty() || firstName.getText().trim().isEmpty() || lastName.getText().trim().isEmpty()){
                              return true;
                    }
                    if (locationBox.getSelectionModel().isEmpty()||roleBox.getSelectionModel().isEmpty()){
                              return true;
                    }
                    return false;
          }

}
