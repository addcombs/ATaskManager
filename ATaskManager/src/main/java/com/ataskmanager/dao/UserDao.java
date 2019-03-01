package com.ataskmanager.dao;

import com.ataskmanager.utils.ATMSessionFactory;
import com.ataskmanager.entities.User;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

          public List<User> loadUsers(){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    List<User> usersList = new ArrayList<>();
                    try{
                              tx = session.beginTransaction();
                              CriteriaBuilder builder = session.getCriteriaBuilder();
                              CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
                              criteriaQuery.from(User.class);
                              usersList = new ArrayList<>(session.createQuery(criteriaQuery).getResultList());
                    } catch (HibernateException he) {
                              if(tx!=null) tx.rollback();
                              he.printStackTrace();
                    } finally{
                              session.close();
                              sessionFactory.close();
                    }
                    return usersList;
          }

          public void saveUser(User user){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    try {
                              tx = session.beginTransaction();
                              session.saveOrUpdate(user);
                              tx.commit();
                    } catch (HibernateException he){
                              if (tx!=null) tx.rollback();
                              he.printStackTrace();
                    } finally{
                              session.close();
                              sessionFactory.close();
                    }
          }

          public void deleteUser(User user){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    try {
                              tx = session.beginTransaction();
                              session.delete(user);
                              tx.commit();
                    } catch (HibernateException he){
                              if (tx!=null) tx.rollback();
                              he.printStackTrace();
                    } finally{
                              session.close();
                              sessionFactory.close();
                    }
          }

          public User verifyUser(String username, String password) {
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    User user = new User();

                    try{
                              tx = session.beginTransaction();
                              CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                              CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
                              Root<User> userRoot = criteriaQuery.from(User.class);
                              criteriaQuery.select(userRoot);
                              criteriaQuery.where(
                                      criteriaBuilder.equal(userRoot.get("username"), username)
                              );
                              user = session.createQuery(criteriaQuery).getSingleResult();
                              tx.commit();
                    } catch (Exception e){
                              if (tx != null) tx.rollback();
                              e.printStackTrace();
                    } finally{
                              session.close();
                              sessionFactory.close();
                    }
                    if (user.getPassword()==null || password==null || !user.getPassword().equals(password)){
                              user = new User();
                    }
                    return user;
          }

          public User getUserByUsername(String username){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    User user = new User();

                    try{
                              tx = session.beginTransaction();
                              CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                              CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
                              Root<User> thisUser = criteriaQuery.from(User.class);
                              criteriaQuery.select(thisUser);
                              criteriaQuery.where(
                                      criteriaBuilder.equal(thisUser.get("username"), username)
                              );
                              user = session.createQuery(criteriaQuery).getSingleResult();
                              tx.commit();
                    } catch (Exception e){
                              if (tx != null) tx.rollback();
                              e.printStackTrace();
                    } finally{
                              session.close();
                              sessionFactory.close();
                    }
                    return user;
          }

          public User getUserById(Integer id){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    User user = new User();

                    try {
                              tx = session.beginTransaction();
                              user = session.get(User.class,id);
                    } catch (HibernateException he) {
                              if (tx!=null) tx.rollback();
                              he.printStackTrace();
                    } finally {
                              session.close();
                              sessionFactory.close();
                    }
                    return user;
          }

          public Callback<ListView<User>, ListCell<User>> cellFactory = new Callback<>() {
                    @Override
                    public ListCell<User> call(ListView<User> list) {
                              return new ListCell<User>() {
                                        @Override
                                        protected void updateItem(User item, boolean empty) {
                                                  super.updateItem(item, empty);
                                                  if (item == null || empty) {
                                                            setGraphic(null);
                                                  } else {
                                                            setText(item.getFirstName() + " " + item.getLastName());
                                                  }
                                        }
                              };
                    }
          };

}