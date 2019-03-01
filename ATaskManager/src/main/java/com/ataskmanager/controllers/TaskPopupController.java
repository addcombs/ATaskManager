package com.ataskmanager.controllers;

import com.ataskmanager.ATaskManager;
import com.ataskmanager.messages.TaskProducer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import com.ataskmanager.dao.LocationDao;
import com.ataskmanager.dao.TaskDao;
import com.ataskmanager.dao.UserDao;
import com.ataskmanager.entities.Location;
import com.ataskmanager.entities.Task;
import com.ataskmanager.entities.User;

/** Controller for the Task Details Popup
 * @author Adam Combs
 * @author Seth Wampole
 */
public class TaskPopupController {
          @FXML     private             Label               taskAssignee;
          @FXML     private             Label               taskRequestee;
          @FXML     private             Label               taskStatus;
          @FXML     private             Label               taskLocation;
          @FXML     private             Label               taskDueDate;
          @FXML     private             Label               taskPriority;
          @FXML     private             Label               taskDescription;
          @FXML     private             Label               taskSubject;
          @FXML     private             Button                 buttonComplete;
          @FXML     private             Button                 buttonCache;
          @FXML     private             Button                 buttonReassign;
          @FXML     private             Task                      tempTask;

          TaskDao taskDao = new TaskDao();

          private Task task;

          public Task getTask() {
                    return task;
          }

          public void setTask(Task task) {
                    this.task = task;
          }

          public void initialize(){
                    buttonComplete.setOnAction(event -> completeTask(this.tempTask));
                    buttonReassign.setOnAction(event -> reassignTask(this.tempTask));
          }

          public void fillText(){
                    this.tempTask = task;
                    UserDao userDAO = new UserDao();
                    if (!(task.getAssignedTo()==(null))){
                              User worker = userDAO.getUserById(task.getAssignedTo());
                              taskAssignee.setText(worker.getFirstName()+ " "+worker.getLastName());
                    }
                    User requester = userDAO.getUserById(task.getRequesterId());
                    taskRequestee.setText(requester.getFirstName()+ " "+requester.getLastName());
                    taskStatus.setText(task.getTaskStatus());
                    LocationDao locationDao = new LocationDao();
                    Location location = locationDao.getLocationById(task.getLocationId());
                    taskLocation.setText(location.getName());
                    taskDueDate.setText(task.getDueDateTime().toString());
                    switch(task.getPriority()){
                              case 1:
                                        taskPriority.setText("HIGH");
                                        break;
                              case 2:
                                        taskPriority.setText("MEDIUM");
                                        break;
                              case 3:
                                        taskPriority.setText("LOW");
                                        break;
                    }
                    taskSubject.setText(task.getShortSubject());
                    taskDescription.setText(task.getLongDescription());
          }

          public void setMode(){
                    String status = task.getTaskStatus();
                    if (status.equals("OPEN")){
                              buttonCache.setDisable(true);
                              buttonReassign.setDisable(true);
                              return;
                    }
                    if (status.equals("COMPLETED")){
                              buttonComplete.setDisable(true);
                              return;
                    }
                    //Will need another case for Cached; All but Cache and Complete buttons available---------------------------------------
                    else{
                              buttonComplete.setDisable(true);
                              buttonCache.setDisable(true);
                              buttonReassign.setDisable(true);
                              return;
                    }
          }

          @FXML
          public void deleteTask(){
                    taskDao.deleteTask(tempTask);
                    ATaskManager.getATMController().refreshData();
                    ATaskManager.getATMController().refreshUI();;
          }

          public void completeTask(Task task){
                    task.setTaskStatus("COMPLETED");
                    taskDao.saveTask(task);
                    TaskProducer.sendMessage(task.getId().toString());
                    Stage stage = (Stage) buttonComplete.getScene().getWindow();
                    stage.close();
          }

          @FXML
          public void archiveTask(){
                    task.setTaskStatus("ARCHIVED");
                    taskDao.saveTask(task);
                    TaskProducer.sendMessage(task.getId().toString());
                    Stage stage = (Stage) buttonComplete.getScene().getWindow();
                    stage.close();
          }

          public void reassignTask(Task task){
                    task.setTaskStatus("OPEN");
                    taskDao.saveTask(task);
                    TaskProducer.sendMessage(task.getId().toString());
                    Stage stage = (Stage) buttonComplete.getScene().getWindow();
                    stage.close();
          }

}
