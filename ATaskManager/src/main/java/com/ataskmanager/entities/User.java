package com.ataskmanager.entities;

import javax.persistence.*;

/** Represents a User (Workers and Managers)
 *        Annotations for Hibernate
 * @author Adam Combs
 * @author Seth Wampole
 */
@Entity
@Table(name="ams_users")
public class User {

          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          private Integer id;
          private String username;
          private String password;
          @Column(name="first_name")
          private String firstName;
          @Column(name="last_name")
          private String lastName;
          @Column (name="location_id")
          private Integer locationId;
          @Column (name="online_status")
          private Integer onlineStatus;
          private String role;

          public User(){
          }

          public User(String username, String password, String firstName, String lastName, Integer locationId, Integer onlineStatus, String role) {
                    this.username = username;
                    this.password = password;
                    this.firstName = firstName;
                    this.lastName = lastName;
                    this.locationId = locationId;
                    this.onlineStatus = onlineStatus;
                    this.role = role;
          }

          public User(Integer id, String username, String password, String firstName, String lastName, Integer locationId, Integer onlineStatus, String role) {
                    this.id = id;
                    this.username = username;
                    this.password = password;
                    this.firstName = firstName;
                    this.lastName = lastName;
                    this.locationId = locationId;
                    this.onlineStatus = onlineStatus;
                    this.role = role;
          }

          public Integer getId() {
                    return id;
          }

          public void setId(Integer id) {
                    this.id = id;
          }

          public String getUsername() {
                    return username;
          }

          public void setUsername(String username) {
                    this.username = username;
          }

          public String getPassword() { return password; }

          public void setPassword(String password) {this.password = password; }

          public String getFirstName() {
                    return firstName;
          }

          public void setFirstName(String firstName) {
                    this.firstName = firstName;
          }

          public String getLastName() {
                    return lastName;
          }

          public void setLastName(String lastName) {
                    this.lastName = lastName;
          }

          public Integer getLocationId() {
                    return locationId;
          }

          public void setLocationId(Integer locationId) {
                    this.locationId = locationId;
          }

          public Integer getOnlineStatus() {
                    return onlineStatus;
          }

          public void setOnlineStatus(Integer onlineStatus) {
                    this.onlineStatus = onlineStatus;
          }

          public String getRole() {
                    return role;
          }

          public void setRole(String role) {
                    this.role = role;
          }
}
