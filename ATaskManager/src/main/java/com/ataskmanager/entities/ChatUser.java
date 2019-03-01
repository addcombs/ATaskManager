package com.ataskmanager.entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

/** Represents a ChatUser
 *
 *        Sets properties for chat table columns to link rows with chat users.
 *
 *        @author Adam Combs
 *        @author Seth Wampole
 */
public class ChatUser {
          private StringProperty firstName;
          private StringProperty lastName;
          private StringProperty id;
          private List<String> chat;

          public ChatUser() {
          }

          public ChatUser(String id, String firstName, String lastName){
                    setId(id);
                    setFirstName(firstName);
                    setLastName(lastName);
          }

          public void setId(String value){ idProperty().set(value); }

          public String getId() { return idProperty().get(); }

          public StringProperty idProperty() {
                    if (id == null) id = new SimpleStringProperty(this, "id");
                    return id;
          }

          public void setFirstName(String value) { firstNameProperty().set(value); }

          public String getFirstName() { return firstNameProperty().get(); }

          public StringProperty firstNameProperty() {
                    if (firstName == null) firstName = new SimpleStringProperty(this, "firstName");
                    return firstName;
          }

          public void setLastName(String value) { lastNameProperty().set(value); }

          public String getLastName() { return lastNameProperty().get(); }

          public StringProperty lastNameProperty() {
                    if (lastName == null) lastName = new SimpleStringProperty(this, "lastName");
                    return lastName;
          }

          public void setChat(List<String> chatList) { this.chat = chatList; }

          public List<String> getChat(){
                    return this.chat;
          }

}
