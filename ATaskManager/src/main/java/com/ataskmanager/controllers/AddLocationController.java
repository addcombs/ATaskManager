package com.ataskmanager.controllers;

import com.ataskmanager.ATaskManager;
import com.ataskmanager.dao.LocationDao;
import com.ataskmanager.entities.Location;
import com.ataskmanager.entities.States;
import com.ataskmanager.messages.LocationProducer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Controller for the Add Location Form
 * @author Adam Combs
 * @author Seth Wampole
 */
public class AddLocationController {

          private LocationDao locationDao = new LocationDao();

          @FXML private TextField locationName;
          @FXML private TextField locationCode;
          @FXML private TextField address;
          @FXML private TextField city;
          @FXML private ComboBox state;
          @FXML private TextField zip;

          /**
           *        Runs when stage is opened
           *        Sets states in drop down box
           */
          public void initialize(){
                    ObservableList<String> observableStatesList = FXCollections.observableArrayList(States.getStateList());
                    state.setItems(observableStatesList);
          }

          /**
           *        When form is submitted.
           *        Validates for blank fields.
           *        Location saved in database.
           *        Reloads database information and main stage UI
           *        Closes location form stage.
           */
          @FXML
          public void onSubmit(){
                    if (checkBlanks()){
                              return;
                    }
                    locationDao.addNewLocation(new Location(locationCode.getText(),locationName.getText(),0.0f,0.0f,address.getText(),city.getText(),state.getSelectionModel().getSelectedItem().toString(),Integer.parseInt(zip.getText())));

                    ATaskManager.getATMController().refreshData();
                    ATaskManager.getATMController().refreshUI();
                    LocationProducer.sendMessage("New location: " + locationName.getText());
                    Stage addLocationStage = (Stage) zip.getScene().getWindow();
                    addLocationStage.close();
          }

          /**
           *        When form is cancelled.
           *        Closes location form stage.
           */
          @FXML
          public void onCancel(){
                    Stage addLocationStage = (Stage) zip.getScene().getWindow();
                    addLocationStage.close();
          }

          /**
           *        Called when form is submitted.
           *        If blanks exist, true is returned. Otherwise, false is returned.
           *
           *        @return          Boolean  value
           */
          private Boolean checkBlanks(){
                    if(locationCode.getText().isEmpty() || address.getText().isEmpty()){
                              return true;
                    }
                    if(city.getText().isEmpty() || state.getSelectionModel().isEmpty() || zip.getText().isEmpty()){
                              return true;
                    }
                    return false;
          }
}
