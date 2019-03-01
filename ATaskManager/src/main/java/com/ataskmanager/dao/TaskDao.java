package com.ataskmanager.dao;

import com.ataskmanager.entities.Task;
import com.ataskmanager.entities.User;
import com.ataskmanager.utils.ATMSessionFactory;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDao {
          private List<Task> tasks;
          private List<Task> unassignedTasks;
          private List<Task> scheduledTasks;
          private List<Task> assignedTasks;
          private List<Task> completedTasks;

          public List<Task> loadTasks(){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    List<Task> tasksList = new ArrayList<>();
                    try {
                              tx = session.beginTransaction();
                              CriteriaBuilder builder = session.getCriteriaBuilder();
                              CriteriaQuery<Task> criteriaQuery = builder.createQuery(Task.class);
                              criteriaQuery.from(Task.class);
                              tasksList = new ArrayList<>(session.createQuery(criteriaQuery).getResultList());
                    } catch (HibernateException he) {
                              if(tx!=null) tx.rollback();
                              he.printStackTrace();
                    } finally {
                              session.close();
                              sessionFactory.close();
                    }
                    return tasksList;
          }

          public void deleteTask(Task task){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    try {
                              tx = session.beginTransaction();
                              session.delete(task);
                              tx.commit();
                    } catch (HibernateException he){
                              if (tx!=null) tx.rollback();
                              he.printStackTrace();
                    } finally{
                              session.close();
                              sessionFactory.close();
                    }
          }

          public Task saveTask(Task task) {
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;

                    try {
                              tx = session.beginTransaction();
                              session.saveOrUpdate(task);
                              tx.commit();
                    } catch (HibernateException he){
                              if (tx!=null) tx.rollback();
                              he.printStackTrace();
                    } finally{
                              session.close();
                              sessionFactory.close();
                    }
                    return task;
          }

          public Task findById(Integer id){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    Task task = null;
                    try {
                              tx = session.beginTransaction();
                              task = session.get(Task.class, id);
                    } catch (HibernateException he) {
                              if (tx!=null)tx.rollback();
                              he.printStackTrace();
                    } finally {
                              session.close();
                              sessionFactory.close();
                    }
                    return task;
          }

          public List<Task> getArchivedTasks(int userId){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    List<Task> tasks = null;
                    try{
                              tx = session.beginTransaction();
                              CriteriaBuilder builder = session.getCriteriaBuilder();
                              CriteriaQuery<Task> criteria = builder.createQuery(Task.class);
                              Root<Task> task = criteria.from(Task.class);
                              criteria.select(task);
                              criteria.where(
                                      builder.equal(task.get("taskStatus"),"archived"),
                                      builder.equal(task.get("assignedTo"),userId)
                              );
                              tasks = new ArrayList<>(session.createQuery(criteria).getResultList());
                              tx.commit();
                    } catch (HibernateException he){
                              if(tx!=null)tx.rollback();
                              he.printStackTrace();
                    } finally {
                              session.close();
                              sessionFactory.close();
                    }
                    return tasks;
          }

          public List<String> getHours(){
                    ArrayList<String> hoursList = new ArrayList<>();
                    for (Integer i = 1; i <= 24; i++){
                              hoursList.add(i.toString());
                    }
                    return hoursList;
          }

          public void setTaskLists(){
                    Date currentDate = new Date();
                    unassignedTasks = new ArrayList<>();
                    scheduledTasks = new ArrayList<>();
                    assignedTasks = new ArrayList<>();
                    completedTasks = new ArrayList<>();
                    for (Task task : tasks){
                              Date dueDate = new Date(task.getDueDateTime().getTime());
                              if (currentDate.getTime()  > dueDate.getTime() + 86400*7*1000 && task.getTaskStatus().equalsIgnoreCase("COMPLETED")){
                                        task.setTaskStatus("ARCHIVED");
                                        saveTask(task);
                              }
                              if (!task.getTaskStatus().equalsIgnoreCase("ARCHIVED")) {
                                        if (task.getTaskStatus().equalsIgnoreCase("scheduled")) {
                                                  Timestamp ts = task.getScheduledDateTime();
                                                  Date scheduledDate = new Date(ts.getTime());
                                                  if (currentDate.after(scheduledDate)) {
                                                            task.setTaskStatus("OPEN");
                                                            saveTask(task);
                                                            assignedTasks.add(task);
                                                  } else {
                                                            scheduledTasks.add(task);
                                                  }
                                        } else if (task.getTaskStatus().equalsIgnoreCase("completed")) {
                                                  completedTasks.add(task);
                                        } else if (task.getAssignedTo() != null) {
                                                  assignedTasks.add(task);
                                        } else {
                                                  unassignedTasks.add(task);
                                        }
                              }
                    }
          }

          // GETTERS AND SETTERS
          public List<Task> getUnassignedTasks() {
                    return unassignedTasks;
          }

          public List<Task> getScheduledTasks() {
                    return scheduledTasks;
          }

          public List<Task> getAssignedTasksByWorker(User worker) {
                    List<Task> tasksList = new ArrayList<>();
                    for (Task task : assignedTasks){
                              if (task.getAssignedTo()!=null && task.getAssignedTo().equals(worker.getId()) && !task.getTaskStatus().equalsIgnoreCase("completed")){
                                        tasksList.add(task);
                              }
                    }
                    return tasksList;
          }

          public List<Task> getCompletedTasksByWorker(User worker) {
                    List<Task> taskList = new ArrayList<>();
                    for (Task task : completedTasks){
                              if (task.getAssignedTo()!=null && task.getAssignedTo().equals(worker.getId()) && task.getTaskStatus().equalsIgnoreCase("completed")){
                                        taskList.add(task);
                              }
                    }
                    return taskList;
          }

          public void setTasks(List<Task> tasks) {
                    this.tasks = tasks;
          }
}