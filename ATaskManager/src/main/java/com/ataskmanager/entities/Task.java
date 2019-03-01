package com.ataskmanager.entities;

import javax.persistence.*;
import java.sql.Timestamp;

/** Represents a Task
 *        Annotations for Hibernate
 * @author Adam Combs
 * @author Seth Wampole
 */
@Entity
@Table(name="ams_tasks")
public class Task {

          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          @Column(name="id")
          private Integer id;
          @Column(name="requester_id")
          private Integer requesterId;
          @Column(name="assigned_to")
          private Integer assignedTo;
          @Column(name="short_subject")
          private String shortSubject;
          @Column(name="long_description")
          private String longDescription;
          @Column(name="task_status")
          private String taskStatus;
          @Column(name="location_id")
          private Integer locationId;
          private Integer priority;
          @Column(name="due_date_time")
          private Timestamp dueDateTime;
          @Column(name="scheduled_date_time")
          private Timestamp scheduledDateTime;

          public Task() { }

          public Task(Integer requesterId, Integer assignedTo, String shortSubject, String longDescription, String taskStatus, Integer locationId, Integer priority, Timestamp dueDateTime, Timestamp scheduledDateTime) {
                    this.requesterId = requesterId;
                    this.assignedTo = assignedTo;
                    this.shortSubject = shortSubject;
                    this.longDescription = longDescription;
                    this.taskStatus = taskStatus;
                    this.locationId = locationId;
                    this.priority = priority;
                    this.dueDateTime = dueDateTime;
                    this.scheduledDateTime = scheduledDateTime;
          }

          public Task(Integer id, Integer requesterId, Integer assignedTo, String shortSubject, String longDescription, String taskStatus, Integer locationId, Integer priority, Timestamp dueDateTime, Timestamp scheduledDateTime) {
                    this.id = id;
                    this.requesterId = requesterId;
                    this.assignedTo = assignedTo;
                    this.shortSubject = shortSubject;
                    this.longDescription = longDescription;
                    this.taskStatus = taskStatus;
                    this.locationId = locationId;
                    this.priority = priority;
                    this.dueDateTime = dueDateTime;
                    this.scheduledDateTime = scheduledDateTime;
          }

          public Integer getId() {
                    return id;
          }

          public void setId(Integer id) {
                    this.id = id;
          }

          public Integer getAssignedTo() { return assignedTo; }

          public void setAssignedTo(Integer assignedTo) { this.assignedTo = assignedTo; }

          public Integer getRequesterId() {
                    return requesterId;
          }

          public void setRequesterId(Integer requesterId) {
                    this.requesterId = requesterId;
          }

          public String getShortSubject() {
                    return shortSubject;
          }

          public void setShortSubject(String shortSubject) {
                    this.shortSubject = shortSubject;
          }

          public String getLongDescription() {
                    return longDescription;
          }

          public void setLongDescription(String longDescription) {
                    this.longDescription = longDescription;
          }

          public String getTaskStatus() {
                    return taskStatus;
          }

          public void setTaskStatus(String status) {
                    this.taskStatus = status;
          }

          public Integer getLocationId() {
                    return locationId;
          }

          public void setLocationId(Integer locationId) {
                    this.locationId = locationId;
          }

          public Integer getPriority() {
                    return priority;
          }

          public void setPriority(Integer priority) {
                    this.priority = priority;
          }

          public Timestamp getDueDateTime() {
                    return dueDateTime;
          }

          public void setDueDateTime(Timestamp dueDateTime) {
                    this.dueDateTime = dueDateTime;
          }

          public Timestamp getScheduledDateTime() {
                    return scheduledDateTime;
          }

          public void setScheduledDateTime(Timestamp scheduledDateTime) {
                    this.scheduledDateTime = scheduledDateTime;
          }
}

