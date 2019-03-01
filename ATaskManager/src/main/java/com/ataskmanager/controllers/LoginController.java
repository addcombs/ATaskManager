package com.ataskmanager.controllers;

import com.ataskmanager.ATaskManager;
import com.ataskmanager.dao.UserDao;
import com.ataskmanager.entities.User;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Controller for the Login Stage
 * @author Adam Combs
 * @author Seth Wampole
 */
public class LoginController{

          @FXML private TextField username;
          @FXML private TextField password;
          @FXML private Label message;

          private UserDao userDao = new UserDao();

          /**
           *        On submit of login.
           *        Attempts to get user from database with username and password.
           *        If user is null, login is invalid.  Otherwise, user is authenticated, login stage is closed, and user is injected to main stage.
           */
          @FXML
          private void onSubmit() {
                    User user = userDao.verifyUser(username.getText(),password.getText());

                    if (user.getUsername()==null){
                              message.setText("Invalid username/password combination.");
                    } else{
                              Stage stage = (Stage) message.getScene().getWindow();
                              stage.close();
                              ATaskManager.getATMController().loggedIn(userDao.getUserByUsername(user.getUsername()));
                    }
          }
}
