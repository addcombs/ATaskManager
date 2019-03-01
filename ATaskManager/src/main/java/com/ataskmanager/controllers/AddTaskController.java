package com.ataskmanager.controllers;

import com.ataskmanager.dao.*;
import com.ataskmanager.entities.Location;
import com.ataskmanager.entities.Task;
import com.ataskmanager.entities.User;

import com.ataskmanager.messages.TaskProducer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.*;

/** Controller for the Add Task Form
 * @author Adam Combs
 * @author Seth Wampole
 */
public class AddTaskController {

          private User currentUser;

          private TaskDao taskDao = new TaskDao();
          private UserDao userDao = new UserDao();
          private LocationDao locationDao = new LocationDao();

          @FXML private ComboBox<User> requestedBy;
          @FXML private ComboBox<User> assignedTo;
          @FXML private ComboBox<Location> taskLocation;
          @FXML private ComboBox priorityLevel;
          @FXML private ComboBox dueHour;
          @FXML private ComboBox dueMinute;
          @FXML private DatePicker dueDate;
          @FXML private DatePicker scheduledDate;
          @FXML private TextField subject;
          @FXML private TextArea description;

          /**
           *        Runs when stage is opened.
           *        Reads users from database.
           *        Iterates through users and puts workers into assigned to list and managers into requested by list.
           *        Creates Observable List for each drop down (Assigned To, Requested By, Task Location, Priority, Hour, and Minute)
           *        Links list items to objects with Cell Factories
           */
          public void initialize(){
                    List<User> users = userDao.loadUsers();
                    List<User> requestedByList = new ArrayList<>();
                    List<User> assignedToList = new ArrayList<>();
                    for (User user: users){
                              if (user.getRole().equalsIgnoreCase("w")){
                                        assignedToList.add(user);
                              } else {
                                        requestedByList.add(user);
                              }
                    }
                    ObservableList<User> observableAssignedToList = FXCollections.observableArrayList(assignedToList);
                    ObservableList<User> observableRequestedByList = FXCollections.observableArrayList(requestedByList);
                    ObservableList<Location> observableTaskLocationList = FXCollections.observableArrayList(locationDao.getAllLocations());
                    ObservableList<String> observablePriorityList = FXCollections.observableArrayList("Low","Medium","High");
                    ObservableList<String> observableHourList = FXCollections.observableArrayList(taskDao.getHours());
                    ObservableList<String> observableMinuteList = FXCollections.observableArrayList("00","15","30","45");

                    assignedTo.setButtonCell(userDao.cellFactory.call(null));
                    assignedTo.setCellFactory(userDao.cellFactory);
                    requestedBy.setButtonCell(userDao.cellFactory.call(null));
                    requestedBy.setCellFactory(userDao.cellFactory);
                    taskLocation.setButtonCell(locationDao.cellFactory.call(null));
                    taskLocation.setCellFactory(locationDao.cellFactory);

                    assignedTo.setItems(observableAssignedToList);
                    requestedBy.setItems(observableRequestedByList);
                    taskLocation.setItems(observableTaskLocationList);
                    priorityLevel.setItems(observablePriorityList);
                    dueHour.setItems(observableHourList);
                    dueMinute.setItems(observableMinuteList);
                    requestedBy.getSelectionModel().select(currentUser);
          }

          /**
           *        On Submit of form.
           *        Checks for empty fields.
           *        Sets task status to "OPEN"
           *        Creates timestamp with date and time input.
           *        Sets priority level.
           *        Saves task to database
           *        Sends task message.
           *        Closes stage.
           */
          @FXML
          public void onSubmit(){
                    if (checkBlanks()){
                              return;
                    }
                    Timestamp scheduledDateTimestamp = null;
                    String taskStatus = "OPEN";
                    Timestamp dueDateTimestamp = Timestamp.valueOf(
                            dueDate.getValue()+" "+
                                    dueHour.getSelectionModel().getSelectedItem().toString()+ ":"+
                                    dueMinute.getSelectionModel().getSelectedItem().toString()+":00");
                    if (scheduledDate.getValue()!=null) {
                              scheduledDateTimestamp = Timestamp.valueOf(scheduledDate.getValue() + " 00:00:00");
                              taskStatus = "SCHEDULED";
                    }
                    int priority;
                    if (priorityLevel.getSelectionModel().getSelectedItem().equals("High")){
                              priority = 1;
                    } else if (priorityLevel.getSelectionModel().getSelectedItem().equals("Medium")) {
                              priority = 2;
                    } else {
                              priority = 3;
                    }
                    if(subject.getLength()<20) {
                              Task task = taskDao.saveTask(new Task(requestedBy.getSelectionModel().getSelectedItem().getId(), assignedTo.getSelectionModel().getSelectedItem().getId(),
                                      subject.getText(), description.getText(), taskStatus, taskLocation.getSelectionModel().getSelectedItem().getId(), priority, dueDateTimestamp, scheduledDateTimestamp));
                              TaskProducer.sendMessage(task.getId().toString());
                              Stage addLocationStage = (Stage) assignedTo.getScene().getWindow();
                              addLocationStage.close();
                    }
          }

          /**
           *        Checks for blank fields.
           *
           *        @return             Boolean
           */
          private Boolean checkBlanks(){
                    if(requestedBy.getSelectionModel().isEmpty()){
                              return true;
                    }
                    if(taskLocation.getSelectionModel().isEmpty() || priorityLevel.getSelectionModel().isEmpty()){
                              return true;
                    }
                    if(subject.getText().isEmpty()){
                              return true;
                    }

                    return false;
          }

          /**
           *        On cancel, closes stage.
           */
          @FXML
          public void onCancel(){
                    Stage addLocationStage = (Stage) assignedTo.getScene().getWindow();
                    addLocationStage.close();
          }

          void setCurrentUser(User currentUser) {
                    this.currentUser = currentUser;
          }

}
