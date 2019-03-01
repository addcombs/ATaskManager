package com.ataskmanager;

import com.ataskmanager.controllers.MainController;
import com.ataskmanager.messages.ChatSubscription;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.jms.JMSException;
import java.io.IOException;

/**       Main Class extends JavaFX Application class
 * @author Adam Combs
 * @author Seth Wampole
 */
public class ATaskManager  extends Application {

          public static void main(String[] args) { launch(args); }
          private static MainController mainController;

          /**
           *        Overrides Application start method to create main stage and login stage.
           *        Attaches icon and stylesheet to stages.
           *        Adds listener for window resize.  Upon resize, main stage UI is refreshed to simulate responsive UI.
           *
           *        @param              atmStage                      rootStage
           *        @throws             IOException
           */
          @Override
          public void start(Stage atmStage) throws IOException {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ATMView.fxml"));
                    Parent rootNode = fxmlLoader.load();
                    mainController = fxmlLoader.getController();
                    Scene atmScene = new Scene(rootNode);
                    atmScene.getStylesheets().add(getClass().getResource("/css/atmStylez.css").toString());
                    atmStage.getIcons().add(new Image(getClass().getResource("/images/atm_icon.png").toString()));
                    atmStage.setTitle("Task Manager");
                    atmStage.setScene(atmScene);
                    atmStage.heightProperty().addListener((obs, oldValue, newValue) ->{
                              mainController.refreshUI();
                    });
                    atmStage.show();

                    FXMLLoader fxmlLoginLoader = new FXMLLoader(getClass().getResource( "/fxml/LoginView.fxml"));
                    Parent loginRoot= fxmlLoginLoader.load();
                    Stage loginStage = new Stage();
                    Scene loginScene = new Scene(loginRoot);
                    loginStage.initModality(Modality.APPLICATION_MODAL);
                    loginScene.getStylesheets().add(getClass().getResource("/css/atmStylez.css").toString());
                    loginStage.getIcons().add(new Image(getClass().getResource("/images/atm_icon.png").toString()));
                    loginStage.setTitle("Login");
                    loginStage.setScene(loginScene);
                    loginStage.show();
                    loginStage.setOnCloseRequest(event ->{
                              atmStage.close();
                    });
          }

          /**
           *        Overrides Application stop method.  Closes ChatSubscription.
           *        System exit closes JRE process.
           *
           *        @throws             JMSException
           */
          @Override
          public void stop() throws JMSException {
                    ChatSubscription.close();
                    System.exit(0);
          }

          /**
           *        Getter for MainController
           *
           *        @return             mainController
           */
          public static MainController getATMController(){
                    return mainController;
          }
}