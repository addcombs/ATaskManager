package com.ataskmanager.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.ataskmanager.messages.*;
import com.ataskmanager.dao.*;
import com.ataskmanager.entities.*;
import com.ataskmanager.utils.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** Controller for the Main Stage
 * @author Adam Combs
 * @author Seth Wampole
 */
public class MainController {
          // Factories
          private StageFactory stageFactory = new StageFactory();
          private TaskCardFactory taskCardFactory = new TaskCardFactory();

          // DAOs
          private LocationDao locationDao = new LocationDao();
          private UserDao userDao = new UserDao();
          private TaskDao taskDao = new TaskDao();

          // Collections
          private List<Location> locations;
          private List<User> users;
          private List<Task> tasks;
          private HashMap<Integer, ArrayList<AnchorPane>> chats = new HashMap<>();
          private ObservableList<ChatUser> chatUsers = FXCollections.observableArrayList(new ArrayList<>());

          // FXML
          @FXML private AnchorPane mainPane;
          @FXML private TabPane bottomTabPane;
          @FXML private HBox locationsHBoxPane;
          @FXML private FlowPane unassignedFlowPane;
          @FXML private FlowPane scheduledFlowPane;
          @FXML private ScrollPane unassignedScrollPane;
          @FXML private ScrollPane scheduledScrollPane;
          @FXML private ScrollPane locationScroll;
          @FXML private Label messagesLabel;
          @FXML private Label userLabel;
          @FXML private ImageView messageToggleLeft;
          @FXML private ImageView messageToggleRight;
          @FXML private Pane userLabelPane;
          @FXML private Pane messagesPane;
          @FXML private TableView chatTable;
          @FXML private TableColumn chatTableFirstName;
          @FXML private TableColumn chatTableLastName;
          @FXML private ListView chatList;
          @FXML private TextArea textMessage;
          @FXML private TitledPane chatTitledPane;
          @FXML private ScrollPane pinPane;
          @FXML private TitledPane pinTitle;
          @FXML private FlowPane pinFlowPane;
          @FXML private ImageView notificationImage;
          @FXML private Label notificationLabel;
          private TitledPane tempTitledPane;
          private GridPane tempGridPane;

          //  Custom Objects
          private User currentUser;
          private ChatUser currentChat;

          // Primitive and Wrapper Objects
          private int pinnedWorker;
          private int messageToggleFlag = 0;
          private Integer notificationCount = 0;
          private Double scrollHeight = 385.0;

          Date date1;
          Date date2;
          Date date3;

          /**
           *        Runs on load of main stage.
           *        Loads data from database, loads all data in UI.
           *        Sets handlers for unassigned tasks and chat.
           *        Sets chat table columns.
           */
          public void initialize() {
                    loadData();
                    loadLocations();
                    setTasksFlowPaneHandlers(unassignedFlowPane);
                    setChatTextAreaHandler(textMessage);
                    chatTableColumns();
          }

          /**
           *        Gets all locations, workers/managers, and tasks from database.
           *        Sets users and and tasks in TaskDao and TaskCardFactory
           */
          private void loadData(){
                    locations = locationDao.getAllLocations();
                    users = userDao.loadUsers();
                    tasks = taskDao.loadTasks();

                    taskCardFactory.setUsers(users);
                    taskDao.setTasks(tasks);
                    taskDao.setTaskLists();
          }

          /**
           *        Clears location HBox, unassigned FlowPane, scheduled FlowPane.
           *        Loads unassigned tasks, scheduled tasks, and loops through all locations.
           *        For each location loadWorkers is called to populate each worker's FlowPane with tasks.
           */
          private void loadLocations(){
                    locationsHBoxPane.getChildren().clear();
                    unassignedFlowPane.getChildren().clear();
                    scheduledFlowPane.getChildren().clear();

                    List<Task> unassignedTasks =  taskDao.getUnassignedTasks();
                    for (Task task:unassignedTasks){
                              GridPane grid = taskCardFactory.createTaskCard(task);
                              setTaskGridPaneHandlers(grid);
                              unassignedFlowPane.getChildren().add(grid);
                    }

                    List<Task> scheduledTasks = taskDao.getScheduledTasks();
                    for (Task task:scheduledTasks){
                              GridPane grid = taskCardFactory.createTaskCard(task);
                              setTaskGridPaneHandlers(grid);
                              scheduledFlowPane.getChildren().add(grid);
                    }

                    for (Location location : locations){
                              FlowPane workerFlowPane = loadWorkers(location);
                              workerFlowPane.setId("workerFlowPane");
                              TitledPane locationTitledPane = new TitledPane();
                              locationTitledPane.setId("locationTitledPane");
                              locationTitledPane.setText(location.getId()+") "+location.getLocationCode()+ " - "+location.getName());
                              locationTitledPane.setContent(workerFlowPane);
                              locationTitledPane.setMaxHeight(Double.MAX_VALUE);
                              setLocationTitledPaneHandlers(locationTitledPane);
                              locationsHBoxPane.getChildren().add(locationTitledPane);
                    }
          }

          /**
           *        Creates FlowPane for location.  Loops through workers, if worker's location equals current
           *        location then worker's TitledPane is added with FlowPane of tasks as content.
           *
           *        @param             location                                  Location object of location being added
           *        @return             locationFlowPane              FlowPane for location
           */
          private FlowPane loadWorkers(Location location){
                    FlowPane locationFlowPane = new FlowPane();
                    locationFlowPane.setId("locationFlowPane");
                    for (User worker: users){
                              if(worker.getLocationId()!=null && worker.getLocationId().equals(location.getId()) && worker.getRole().equalsIgnoreCase("w")){
                                        List<Task> workerCompletedTasks = taskDao.getCompletedTasksByWorker(worker);
                                        List<Task> workerAssignedTasks = taskDao.getAssignedTasksByWorker(worker);

                                        FlowPane workerFlowPane = new FlowPane();
                                        TitledPane workerCountPane = new TitledPane("Completed " + workerCompletedTasks.size() + " / Assigned " + workerAssignedTasks.size(), workerFlowPane);
                                        TitledPane workerTitledPane = new TitledPane(worker.getId() + " - " + worker.getFirstName() + " " + worker.getLastName(),workerCountPane);

                                        Label completedLabel = new Label("Completed");
                                        Label assignedLabel = new Label("Assigned");
                                        workerFlowPane.setId("workerFlowPane");
                                        workerCountPane.setId("workerCountPane");
                                        workerTitledPane.setId("workerTitledPane");
                                        completedLabel.setId("completedStatusLabel");
                                        assignedLabel.setId("assignedStatusLabel");

                                        workerFlowPane.setPrefWrapLength(scrollHeight);

                                        loadTasksToWorkerFlowPane(workerCompletedTasks, completedLabel, workerFlowPane);
                                        loadTasksToWorkerFlowPane(workerAssignedTasks, assignedLabel, workerFlowPane);

                                        if (worker.getId()==pinnedWorker){
                                                  pinFlowPane.getChildren().clear();
                                                  pinFlowPane.getChildren().add(workerTitledPane);
                                                  pinFlowPane.setPrefWrapLength(scrollHeight);
                                        } else {
                                                  locationFlowPane.getChildren().add(workerTitledPane);
                                        }

                                        setWorkerTitledPaneHandlers(workerTitledPane);
                                        setTasksFlowPaneHandlers(workerFlowPane);
                              }
                    }
                    return locationFlowPane;
          }

          /**
           *        Adds worker's tasks to FlowPane by calling createTaskCard method from TaskCardFactory
           *
           *        @param           workersTasks                  List of current workers tasks
           *        @param           label                                     Label for Assigned or Completed
           *        @param           workerFlowPane           FlowPane to add tasks
           */
          private void loadTasksToWorkerFlowPane(List<Task> workersTasks, Label label, FlowPane workerFlowPane) {
                    workerFlowPane.getChildren().add(label);
                    for (Task task:workersTasks){
                              GridPane taskCard = taskCardFactory.createTaskCard(task);
                              setTaskGridPaneHandlers(taskCard);
                              workerFlowPane.getChildren().add(taskCard);
                    }
          }

          /**
           *        Saves horizontal scroll bar position and global ScrollPane height,
           *        refreshes UI by calling loadLocations, and sets scroll bar position to
           *        saved position.
           */
          public void refreshUI(){
                    Platform.runLater(() ->{
                              Double locationScrollPos = locationScroll.getHvalue();
                              scrollHeight = locationScroll.getHeight()-140.0;
                              loadLocations();
                              locationScroll.layout();
                              locationScroll.setHvalue( locationScrollPos);
                              this.date3 = new Date();
                    });
          }

          /**
           *        Refreshes data by calling loadData
           */
          public void refreshData(){
                    Platform.runLater(() -> {
                              loadData();
                    });
          }

          public void refreshTasks(){
                    Platform.runLater(()->{
                              tasks = taskDao.loadTasks();

                              taskDao.setTasks(tasks);
                              taskDao.setTaskLists();
                    });
          }

          public void refreshWorkers(){
                    Platform.runLater(()->{
                              users = userDao.loadUsers();
                    });
          }

          /**
           *        On successful login, blur is removed from UI, current user is set, user subscribes to
           *        task, chat, and location ActiveMQ messages, starts timer for 1 min refresh.
           *
           *        @param              user                User object that is logging in
           */
          public void loggedIn(User user){
                    Platform.runLater(()->{
                              mainPane.setEffect(null);
                              currentUser = user;
                              userLabel.setText(currentUser.getFirstName()+" "+currentUser.getLastName());
                              TaskSubscription.subscribe(currentUser.getUsername()+"_task");
                              ChatSubscription.subscribe(currentUser.getUsername()+"_text");
                              LocationSubscription.subscribe(currentUser.getUsername()+"_location");
                              startTimer();
                    });
          }

          /**
           *        Begins timer for refresh every 1 min
           */
          private void startTimer(){
                    Timer timer = new Timer(true);
                    TimerTask timerTask = new RefreshTimer();
                    timer.scheduleAtFixedRate(timerTask, 0, 60000);
          }

          // PIN WORKER -------------------------------------------------------------------------------------------------------------------

          /**
           *        Unpins worker and adds worker to be pinned to pin.
           *
           *        @param              titledPane                    TitledPane of worker to be pinned
           */
          private void pinWorker(TitledPane titledPane){
                    unpinWorker();
                    pinnedWorker=Integer.parseInt(titledPane.getText().substring(0,titledPane.getText().indexOf(' ')));
                    mainPane.setLeftAnchor(locationScroll,180.0);
                    pinFlowPane.getChildren().add(titledPane);
                    pinTitle.setContent(pinFlowPane);
                    pinTitle.setMaxHeight(Double.MAX_VALUE);
          }

          /**
           *         If global pinPane has content, content is set to null, refreshes data, refreshes UI, and
           *         removes worker from pin.
           */
          private void unpinWorker(){
                    if(pinPane.getContent()==null){
                              return;
                    }
                    refreshData();
                    refreshUI();
                    pinTitle.setContent(null);
                    pinnedWorker=0;
                    mainPane.setLeftAnchor(locationScroll,0.0);
          }

          // EVENT HANDLERS -----------------------------------------------------------------------------------------------------------

          /**
           *        Sets Event handlers for task's GridPane.
           *        Includes drag detected and double clicked events
           *
           *        @param              gridPane                      task's GridPane to assign event handlers
           */
          private void setTaskGridPaneHandlers(GridPane gridPane){
                    gridPane.setOnDragDetected((MouseEvent event) -> {
                              setDragboard(gridPane);
                              tempGridPane = gridPane;
                              event.consume();
                    });
                    gridPane.setOnMouseClicked((MouseEvent event) -> {
                              if (event.getButton().equals(MouseButton.PRIMARY)&&event.getClickCount()==2){
                                        try {
                                                  for (Task task : tasks){
                                                            Label idLabel = (Label) gridPane.getChildren().get(0);
                                                            if (idLabel.getText().equals(task.getId().toString())) {
                                                                      taskDetailsPopup(task);
                                                            }
                                                  }
                                        }
                                        catch(IOException e){
                                                  e.printStackTrace();
                                        }
                              }
                    });
          }

          /**
           *        Sets Event handlers for task's FlowPane.
           *        Includes drag over and drag dropped events.
           *
           *        @param              flowPane                      task FlowPane to assign event handlers
           */
          private void setTasksFlowPaneHandlers(FlowPane flowPane){
                    setDragOverEvent(flowPane);
                    flowPane.addEventHandler(DragEvent.DRAG_DROPPED, (DragEvent event) -> {
                              this.date1 = new Date();
                              Dragboard db = event.getDragboard();
                              boolean success = false;
                              GridPane gridPane = tempGridPane;
                              if (db.hasString()){
                                        flowPane.getChildren().add(gridPane);
                                        success=true;
                              }
                              event.setDropCompleted(success);
                              tempGridPane=null;

                              event.consume();
                              getNewAssigned(gridPane);
                    });
          }

          /**
           *        Sets Event handlers for worker's TitledPane.  Includes drag detected and double clicked events
           *
           *        @param              titledPane                    worker TitledPane to assign event handlers
           */
          private void setWorkerTitledPaneHandlers(TitledPane titledPane){
                    titledPane.setOnDragDetected(event -> {
                              setDragboard(titledPane);
                              tempTitledPane = titledPane;
                              event.consume();
                    });
                    titledPane.setOnMouseClicked(event -> {
                              try {
                                        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() > 1) {
                                                  FXMLLoader fxmlLoader = new FXMLLoader(MainController.this.getClass().getResource("/fxml/WorkerPopupView.fxml"));
                                                  String workerNum = titledPane.getText().substring(0, titledPane.getText().indexOf(" "));
                                                  User user = userDao.getUserById(Integer.parseInt(workerNum));

                                                  Stage addWorkerStage = stageFactory.createNewStage(fxmlLoader, "Worker Details", mainPane);
                                                  addWorkerStage.initModality(Modality.WINDOW_MODAL);
                                                  addWorkerStage.initOwner(titledPane.getScene().getWindow());

                                                  WorkerPopupController workerPopUpController = fxmlLoader.getController();
                                                  workerPopUpController.setWorker(user);

                                                  workerPopUpController.fillStuff();

                                                  addWorkerStage.showAndWait();

                                                  if (workerPopUpController.getPin() == Boolean.TRUE) {
                                                            pinWorker(titledPane);
                                                  }

                                                  if (workerPopUpController.getDeleted() == Boolean.TRUE){
                                                            unassignTasks(titledPane);
                                                  }

                                        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                                                  if (titledPane.getParent().getId().equals("workerFlowPane")) {
                                                            pinWorker(titledPane);
                                                  } else {
                                                            unpinWorker();
                                                  }
                                        }
                              } catch (IOException e) {
                                        e.printStackTrace();
                              }
                    });
          }

          /**
           *        Sets Event handlers for location's TitledPane.  Only includes drag over event.
           *
           *        @param              titledPane                    location TitledPane to assign event handler
           */
          private void setLocationTitledPaneHandlers(TitledPane titledPane){
                    setDragOverEvent(titledPane);
                    titledPane.addEventHandler(DragEvent.DRAG_DROPPED, (DragEvent event) -> {
                              if(tempTitledPane.getParent().getClass().toString().contains("Title")){
                                        pinTitle.setContent(null);
                                        pinnedWorker=0;
                                        mainPane.setLeftAnchor(locationScroll,0.0);
                              }
                              Dragboard db = event.getDragboard();
                              boolean success = false;
                              TitledPane workerPane=tempTitledPane;
                              if (db.hasString()){
                                        FlowPane locationPane = (FlowPane)titledPane.getContent();
                                        locationPane.getChildren().add(workerPane);
                                        success=true;
                              }
                              event.setDropCompleted(success);
                              tempTitledPane=null;
                              event.consume();
                              getNewLocation(workerPane);
                    });
          }

          /**
           *        Event handlers for dragging and dropping task into chat message.
           *        Gets task id and appends to message inside brackets.
           *
           *        @param              messageBox          TextArea containing chat message.
           */
          private void setChatTextAreaHandler(TextArea messageBox){
                    messageBox.addEventHandler(DragEvent.DRAG_OVER,(DragEvent event)->{
                              event.acceptTransferModes(TransferMode.ANY);
                              event.consume();
                    });
                    messageBox.addEventHandler(DragEvent.DRAG_DROPPED, (DragEvent event) ->{
                              if (event.getDragboard().hasString()){
                                        Label idLabel = (Label) tempGridPane.getChildren().get(0);
                                        messageBox.setText(messageBox.getText() + "[" + idLabel.getText() +"]");
                              }
                              tempGridPane=null;
                              event.consume();
                    });
          }

          /**
            *       Creates dragboard with snapshot image of node being dragged. Node could be TitledPane(Worker
            *       move) or GridPane(Task move).
            *
            *      @param               node                Node dragged
            */
          private void setDragboard(Node node) {
                    Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
                    SnapshotParameters snapParams = new SnapshotParameters();
                    snapParams.setFill(Color.TRANSPARENT);
                    Image dragImage = node.snapshot(snapParams, null);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(dragImage);
                    content.putString(node.getId());
                    db.setContent(content);
          }

          /**
           *        Triggered by node being dragged over.  Node could be TitledPane(Worker move) or
           *        GridPane(Task move).
           *
           *        @param              node                Node dragged
           */
          private void setDragOverEvent(Node node){
                    node.addEventHandler(DragEvent.DRAG_OVER, (DragEvent event) ->{
                              if (event.getGestureSource() != node && event.getDragboard().hasString()){
                                        event.acceptTransferModes(TransferMode.MOVE);
                              }
                              event.consume();
                    });
          }

          /**
           *        GridPane of task to new worker.  If task was "COMPLETED", set to "OPEN"
           *
           *        @param              gridPane            GridPane of task which is being moved.
           */
          private void getNewAssigned(GridPane gridPane){
                    if (!gridPane.getParent().getParent().getClass().toString().contains("TitledPane")){
                              Label idLabel = (Label) gridPane.getChildren().get(0);
                              Task task = taskDao.findById(Integer.parseInt(idLabel.getText()));
                              task.setAssignedTo(null);
                              taskDao.saveTask(task);
                              TaskProducer.sendMessage(task.getId().toString());
                              return;
                    }
                    TitledPane parentPane = (TitledPane) gridPane.getParent().getParent().getParent().getParent().getParent();
                    String newAssigned = parentPane.getText();
                    newAssigned=newAssigned.substring(0,newAssigned.indexOf(' '));

                    Label assignedLabel = (Label) gridPane.getChildren().get(0);
                    Task task = taskDao.findById(Integer.parseInt(assignedLabel.getText()));
                    task.setAssignedTo(Integer.parseInt(newAssigned));

                    if(task.getTaskStatus().equalsIgnoreCase("COMPLETED") || task.getTaskStatus().equalsIgnoreCase("SCHEDULED")){
                              task.setTaskStatus("OPEN");
                    }
                    taskDao.saveTask(task);
                    this.date2 = new Date();
                    TaskProducer.sendMessage(task.getId().toString());
          }

          /**
           *        Worker to new location.
           *
           *        @param           workerPane          TitledPane for worker being moved to new location.
           */
          private void getNewLocation(TitledPane workerPane){
                    TitledPane locationPane = (TitledPane) workerPane.getParent().getParent().getParent();
                    String workerId = workerPane.getText().substring(0,workerPane.getText().indexOf(" "));
                    User worker = userDao.getUserById(Integer.parseInt(workerId));
                    String locationId = locationPane.getText().substring(0,locationPane.getText().indexOf(")"));
                    worker.setLocationId(Integer.parseInt(locationId));
                    userDao.saveUser(worker);
                    if (Integer.parseInt(workerId) == pinnedWorker){
                              unpinWorker();
                    }
                    LocationProducer.sendMessage(locationId);
          }

          /**
           *        Set all worker's tasks to unassigned.
           *
           *        @param              titledPane          TitledPane including tasks to be unassigned
           */
          private void unassignTasks(TitledPane titledPane){
                    TitledPane countPane = (TitledPane) titledPane.getContent();
                    FlowPane workerFlow = (FlowPane) countPane.getContent();
                    for (int i = 0; i<workerFlow.getChildren().size();i++){
                              if (workerFlow.getChildren().get(i).getClass().toString().contains("Grid")){
                                        GridPane gridPane = (GridPane) workerFlow.getChildren().get(i);
                                        Label label = (Label) gridPane.getChildren().get(0);
                                        Task task = taskDao.findById(Integer.parseInt(label.getText()));
                                        task.setAssignedTo(null);
                                        taskDao.saveTask(task);
                                        TaskProducer.sendMessage(task.getId().toString());
                              }
                    }
          }

          // MESSAGING ---------------------------------------------------------------------------------------------------------------------

          /**
           *        Creates popup to display selected task's details.
           *
           *        @param           task      Task to be displayed in new stage.
           */
          private void taskDetailsPopup(Task task) throws IOException {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/TaskPopupView.fxml"));
                    Stage addTaskDetailsStage = stageFactory.createNewStage(fxmlLoader, "Task Details", mainPane);
                    TaskPopupController taskController = fxmlLoader.getController();
                    taskController.setTask(task);
                    taskController.fillText();
                    taskController.setMode();
                    addTaskDetailsStage.show();
          }

          /**
           *        All text messages are injected from ActiveMQ chat subscription.
           *        If chat exists, add message to chat's array list. Otherwise, create new array list with message.
           *        If message sender is current user, message is aligned left.  Otherwise, it will align to right.
           *
           *        @param           message      injected from ActiveMQ message
           */
          public void incomingMessages(String message){
                    Platform.runLater(() -> {
                              String[] values = message.split(";",4);
                              Integer senderId = Integer.parseInt(values[0]);
                              Integer recipientId = Integer.parseInt(values[1]);
//                              String timestamp = values[2];
                              TextFlow thisMessage = formatMessage(values[3]);

                              if (senderId.equals(currentUser.getId())) {
                                        thisMessage.setId("senderLabel");
                                        AnchorPane senderAnchor = new AnchorPane();
                                        senderAnchor.setId("senderAnchor");
                                        senderAnchor.getChildren().add(thisMessage);

                                        ArrayList<AnchorPane> userChat;
                                        if (chats.containsKey(recipientId)){
                                                  userChat = chats.get(recipientId);
                                                  userChat.add(senderAnchor);
                                                  chats.replace(recipientId,userChat);
                                        } else {
                                                  userChat = new ArrayList<>();
                                                  userChat.add(senderAnchor);
                                                  chats.put(recipientId,userChat);
                                        }
                                        ObservableList<AnchorPane> chat = FXCollections.observableArrayList(userChat);
                                        chatList.setItems(chat);
                              }

                              if (recipientId.equals(currentUser.getId())) {
                                        thisMessage.setId("recipientLabel");
                                        AnchorPane recipientAnchor = new AnchorPane();
                                        recipientAnchor.setId("recipientAnchor");
                                        AnchorPane.setRightAnchor(thisMessage,0.0);
                                        recipientAnchor.getChildren().add(thisMessage);

                                        if(chats.containsKey(senderId)){
                                                  ArrayList<AnchorPane> tempChats = chats.get(senderId);
                                                  chats.replace(senderId, tempChats);
                                                  for (ChatUser user : chatUsers){
                                                            if (user.getId().equals(senderId)){
                                                                      currentChat = user;
                                                            }
                                                  }
                                        } else {
                                                  ArrayList<AnchorPane> tempChats = new ArrayList<>();
                                                  chats.put(senderId, tempChats);
                                                  User newChatUser = userDao.getUserById(senderId);
                                                  currentChat = new ChatUser(newChatUser.getId().toString(),newChatUser.getFirstName(),newChatUser.getLastName());
                                                  chatUsers.add(currentChat);
                                                  chatTable.setItems(chatUsers);
                                        }

                                        chatTitledPane.setText(currentChat.getFirstName() + " " + currentChat.getLastName());
                                        ArrayList<AnchorPane> userChat = chats.get(senderId);
                                        userChat.add(recipientAnchor);
                                        chats.replace(senderId,userChat);
                                        ObservableList<AnchorPane> chat = FXCollections.observableArrayList(userChat);
                                        textMessage.setDisable(false);
                                        chatList.setItems(chat);
                                        if (messageToggleFlag==1){
                                                  notificationCount++;
                                                  notificationImage.setVisible(true);
                                                  notificationLabel.setVisible(true);
                                                  notificationLabel.setText(notificationCount.toString());
                                                  messageAlert();
                                        }
                              }
                    });
          }

          /**
           *        Plays notification with new message is received and message panel is minimized.
           */
          public void messageAlert() {
                    try {
                              File messageAlert = new File(getClass().getResource("/audio/data_beep.wav").toExternalForm());
                              AudioInputStream audioIn = AudioSystem.getAudioInputStream(messageAlert);
                              Clip clip = AudioSystem.getClip();
                              clip.open(audioIn);
                              clip.start();
                    } catch (UnsupportedAudioFileException e) {
                              e.printStackTrace();
                    } catch (IOException e) {
                              e.printStackTrace();
                    } catch (LineUnavailableException e) {
                              e.printStackTrace();
                    }
          }

          /**
           *        Parse message for tasks.  If task exists in message, add link which when clicked opens task details.
           *
           *        @param message             message to be formatted
           *        @return textFlow             TextFlow with message + task number link
           */
          public TextFlow formatMessage(String message){
                    Pattern pattern = Pattern.compile("\\[\\d+\\]");
                    Matcher matcher = pattern.matcher(message);
                    TextFlow textFlow = null;
                    Text text = new Text(message);
                    text.setId("chatText");

                    while (matcher.find()){
                              String taskNum = matcher.group();
                              taskNum = taskNum.replace("[","");
                              taskNum = taskNum.replace("]", "");
                              Integer taskId = Integer.parseInt(taskNum);
                              Hyperlink taskLink = new Hyperlink("Task" + taskNum);
                              taskLink.setOnMouseClicked(( MouseEvent event) -> {
                                        for (Task task : tasks){
                                                  if (taskId==task.getId()){
                                                            try {
                                                                      taskDetailsPopup(task);
                                                            } catch (IOException e) {
                                                                      e.printStackTrace();
                                                            }
                                                  }
                                        }
                              });

                              text.setText(message);
                              textFlow = new TextFlow(text,taskLink);
                    }
                    if (textFlow==null){
                              textFlow = new TextFlow(text);
                    }
                    return textFlow;
          }

          /**
           *        Inject new chat user to message table
           *
           *        @param chatUser            ChatUser to be added to message table
           */
          public void updateMessageTable(ChatUser chatUser) {
                    Platform.runLater(() ->{
                              chatUsers.add(chatUser);
                              chatTable.setItems(chatUsers);
                    });
          }

          /**
           *        Set message table columns to map to Chat User
           */
          private void chatTableColumns(){
                    TableColumn<ChatUser,String> firstNameCol = chatTableFirstName;
                    firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                    TableColumn<ChatUser,String> lastNameCol = chatTableLastName;
                    lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                    chatTable.getColumns().setAll(firstNameCol,lastNameCol);
          }

          /**
           *        If messageToggleFlag equals 0, message panel is minimized.  Otherwise, it is maximized.
           *        messageToggleflag is switched.
           */
          @FXML
          public void toggleMessagePane() {
                    if (messageToggleFlag==0){
                              AnchorPane.setRightAnchor(locationScroll,40.0);
                              AnchorPane.setRightAnchor(bottomTabPane, 40.0);
                              AnchorPane.setRightAnchor(unassignedScrollPane, 40.0);
                              AnchorPane.setRightAnchor(scheduledScrollPane, 40.0);
                              AnchorPane.setRightAnchor(userLabelPane, 40.0);
                              AnchorPane.setRightAnchor(messagesPane, -235.0);
                              messagesPane.setBackground(new Background(new BackgroundFill(Color.web("#005cb2"),CornerRadii.EMPTY,Insets.EMPTY)));
                              messagesLabel.setTextFill(Paint.valueOf("#dddddd"));
                              messageToggleLeft.setVisible(true);
                              messageToggleRight.setVisible(false);
                              messageToggleFlag=1;
                    }else {
                              AnchorPane.setRightAnchor(locationScroll,275.0);
                              AnchorPane.setRightAnchor(bottomTabPane, 275.0);
                              AnchorPane.setRightAnchor(unassignedScrollPane, 275.0);
                              AnchorPane.setRightAnchor(scheduledScrollPane, 275.0);
                              AnchorPane.setRightAnchor(userLabelPane, 275.0);
                              AnchorPane.setRightAnchor(messagesPane, 0.0);
                              messagesPane.setBackground(new Background(new BackgroundFill(null,CornerRadii.EMPTY,Insets.EMPTY)));
                              messagesLabel.setTextFill(Paint.valueOf("#333333"));
                              messageToggleLeft.setVisible(false);
                              messageToggleRight.setVisible(true);
                              messageToggleFlag=0;

                              notificationLabel.setVisible(false);
                              notificationImage.setVisible(false);
                              notificationCount = 0;
                    }
          }

          /**
           *        When row is selected from messages table, that worker's chat is displayed and any sent messages
           *        will be directed to selected worker.
           */
          @FXML
          public void chatSelect(){
                    if (chatTable.getItems()!=null){
                              ChatUser user = (ChatUser) chatTable.getSelectionModel().getSelectedItem();
                              currentChat = user;
                              chatTitledPane.setText(currentChat.getFirstName() + " " + currentChat.getLastName());
                              Integer chatUserId = Integer.parseInt(user.getId());
                              ArrayList<AnchorPane> userChat;
                              if (chats.containsKey(chatUserId)) {
                                        userChat = chats.get(chatUserId);
                              } else {
                                        userChat = new ArrayList<>();
                                        chats.put(chatUserId,userChat);
                              }
                              textMessage.setDisable(false);
                              ObservableList<AnchorPane> chat = FXCollections.observableArrayList(userChat);
                              chatList.setItems(chat);
                    }
          }

          /**
           *        Uses ChatProducer to send ActiveMQ message of chat message upon clicking "Send Message" button.
           *        Then clears text message text area.
           */
          @FXML
          public void outgoingMessages(){
                    ChatProducer.sendMessage(currentUser.getId().toString(),currentChat.getId(),new Timestamp(System.currentTimeMillis()),textMessage.getText());
                    textMessage.setText("");
          }

          // ADD FORMS --------------------------------------------------------------------------------------------------------------------

          /**
           *        When addUser icon is clicked a new stage is created for user to create new worker or manager.
           *
           *        @Exception          IOException
           */
          @FXML
          private void addUser() throws IOException {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddUserView.fxml"));
                    Stage addUserStage = stageFactory.createNewStage(fxmlLoader, "Add User", mainPane);
                    addUserStage.show();
          }

          /**
           *        When addLocation icon is clicked a new stage is created for user to create new location .
           *
           *        @Exception          IOException
           */
          @FXML
          private void addLocation() throws IOException{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddLocationView.fxml"));
                    Stage addLocationStage = stageFactory.createNewStage(fxmlLoader, "Add Locaiton", mainPane);
                    addLocationStage.show();
          }

          /**
           *        When addTask icon is clicked a new stage is created for user to create new task.
           *
           *        @Exception          IOException
           */
          @FXML
          private void addTask() throws IOException{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddTaskView.fxml"));
                    Stage addTaskStage = stageFactory.createNewStage(fxmlLoader, "Add Task", mainPane);
                    AddTaskController addTaskController = fxmlLoader.getController();
                    addTaskController.setCurrentUser(currentUser);
                    addTaskStage.show();
          }

}
