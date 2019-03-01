package com.ataskmanager.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**       Creates new stages
 * @author Adam Combs
 * @author Seth Wampole
 */
public class StageFactory {

          /**
           *        Creates new modal stage, adds static icon and stylesheet, sets title, sets resizable to false.
           *
           *        @param              fxmlLoader          fxml file to load in stage
           *        @param              title                          title displayed on stage window title bar
           *        @param              mainPane              parent pane of new stage
           *
           *        @return             stage                         new Stag
           */
          public Stage createNewStage(FXMLLoader fxmlLoader, String title, AnchorPane mainPane) throws IOException {
                    Parent root = fxmlLoader.load();
                    Scene addScene = new Scene(root);
                    Stage addStage = new Stage();
                    addStage.getIcons().add(new Image(getClass().getResource("/images/atm_icon.png").toString()));
                    addScene.getStylesheets().add(getClass().getResource("/css/atmStylez.css").toString());
                    addStage.setTitle(title);
                    addStage.setScene(addScene);
                    addStage.setResizable(false);
                    addStage.initModality(Modality.WINDOW_MODAL);
                    addStage.initOwner(mainPane.getScene().getWindow());
                    return addStage;
          }
}
