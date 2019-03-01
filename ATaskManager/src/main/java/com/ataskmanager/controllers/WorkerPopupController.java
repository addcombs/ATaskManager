package com.ataskmanager.controllers;

import com.ataskmanager.ATaskManager;
import com.ataskmanager.dao.LocationDao;
import com.ataskmanager.dao.TaskDao;
import com.ataskmanager.dao.UserDao;
import com.ataskmanager.entities.ChatUser;
import com.ataskmanager.entities.Location;
import com.ataskmanager.entities.Task;
import com.ataskmanager.entities.User;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

/** Controller for the Worker Details Popup
 * @author Adam Combs
 * @author Seth Wampole
 */
public class WorkerPopupController {

    @FXML private ScrollPane scrollHistory;
    @FXML private Button buttonPin;
    @FXML private Button messageButton;
    @FXML private GridPane archivedGrid;
    @FXML private Label workerName;
    @FXML private Label workerUsername;
    @FXML private Label workerLocation;

    private User worker;

    private Boolean pinned = Boolean.FALSE;

    Boolean deleted;

    UserDao userDao = new UserDao();
    TaskDao taskDao = new TaskDao();
    LocationDao locationDao = new LocationDao();

    public void initialize(){
        buttonPin.setOnAction(event -> pinUser());
    }

    public void fillStuff(){
        workerName.setText(worker.getFirstName() + " " + worker.getLastName());
        workerUsername.setText(worker.getUsername());
        Location location = locationDao.getLocationById(worker.getLocationId());
        workerLocation.setText(location.getName());
        fillHistory(worker);
    }

    public void fillHistory(User user){
        List<Task> archivedTasks = taskDao.getArchivedTasks(user.getId());
        archivedGrid.setGridLinesVisible(Boolean.FALSE);
        FlowPane archivedFlow = new FlowPane();
        archivedFlow.setOrientation(Orientation.VERTICAL);

        int i = 1;

        for (Task task:archivedTasks){
            Label idLabel = new Label(i+")  ");
            Label titleLabel = new Label(task.getShortSubject());
            Label dueLabel = new Label(task.getDueDateTime().toString());
            archivedGrid.addRow(i,idLabel);
            archivedGrid.add(titleLabel,1,i);
            archivedGrid.add(dueLabel,2,i);
            i++;
        }

        archivedFlow.getChildren().add(archivedGrid);
        scrollHistory.setContent(archivedFlow);
    }

    private void pinUser(){
        this.pinned = Boolean.TRUE;
        close();
    }

    @FXML
    public void  addChat(){
        ATaskManager.getATMController().updateMessageTable(new ChatUser(worker.getId().toString(),worker.getFirstName(),worker.getLastName()));
        close();
    }

    @FXML
    public void deleteWorker(){
        userDao.deleteUser(worker);
        ATaskManager.getATMController().refreshData();
        ATaskManager.getATMController().refreshUI();
        this.deleted=Boolean.TRUE;
        close();
    }

    public void close(){
        Stage stage = (Stage) messageButton.getScene().getWindow();
        stage.close();
    }

    public Boolean getDeleted(){ return this.deleted;}

    public Boolean getPin(){
        return this.pinned;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

}
