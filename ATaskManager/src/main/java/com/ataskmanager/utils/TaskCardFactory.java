package com.ataskmanager.utils;

import com.ataskmanager.entities.Task;
import com.ataskmanager.entities.User;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**       Creates GridPanes for tasks
 * @author Adam Combs
 * @author Seth Wampole
 */
public class TaskCardFactory {

          private List<User> users;

          /**
           *        Creates GridPane and sets all columns and rows for task
           *
           *        @param           task                Task for GridPane
           */
          public GridPane createTaskCard(Task task){
                    Date currentDate = new Date();
                    GridPane gridPane = new GridPane();
                    Date date = new Date(task.getDueDateTime().getTime());
                    String formattedDate = new SimpleDateFormat("MM/dd/yyyy',' k:mm").format(date);
                    if (currentDate.getTime() > date.getTime()){
                              gridPane.setId("cardGridPaneAlert");
                    } else if (currentDate.getTime() + 15*60*1000 > date.getTime()){
                              gridPane.setId("cardGridPaneWarning");
                    } else {
                              gridPane.setId("cardGridPane");
                    }
                    gridPane.add(new Label(task.getId().toString()), 0, 0);

                    Pane priorityPane = new Pane();
                    switch (task.getPriority()){
                              case 1:
                                        priorityPane.setId("highPriority");
                                        break;
                              case 2:
                                        priorityPane.setId("mediumPriority");
                                        break;
                              case 3:
                                        priorityPane.setId("lowPriority");
                                        break;
                              default:
                                        break;
                    }
                    gridPane.add(priorityPane, 1, 0);
                    gridPane.add(new Label (task.getShortSubject()), 0, 1);

                    for (User manager: users){
                              if (task.getRequesterId().equals(manager.getId())){
                                        String rFirstInitial = manager.getFirstName().substring(0,1);
                                        String rLastName = manager.getLastName();
                                        gridPane.add(new Label (rFirstInitial+" "+rLastName), 0 , 2);
                                        gridPane.add(new Label (formattedDate),0, 3);
                                        ColumnConstraints col1 = new ColumnConstraints();
                                        ColumnConstraints col2 = new ColumnConstraints();
                                        col1.setPercentWidth(90);
                                        col2.setPercentWidth(10);
                                        gridPane.getColumnConstraints().addAll(col1, col2);
                              }
                    }
                    return gridPane;
          }

          /**
           *        setter for Users list
           *
           *        @param           users               List of users
           */
          public void setUsers(List<User> users) {
                    this.users = users;
          }

}
