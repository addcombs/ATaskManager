package com.ataskmanager.dao;

import com.ataskmanager.entities.Location;
import com.ataskmanager.entities.User;
import com.ataskmanager.utils.ATMSessionFactory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class LocationDao {

          public void addNewLocation(Location location) {
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;

                    try {
                              tx = session.beginTransaction();
                              session.save(location);
                              tx.commit();
                    } catch (HibernateException he){
                              if (tx!=null) tx.rollback();
                              he.printStackTrace();
                    } finally {
                              session.close();
                              sessionFactory.close();
                    }
          }

          public void deleteLocation(Location location){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    try {
                              tx = session.beginTransaction();
                              session.delete(location);
                              tx.commit();
                    } catch (HibernateException he) {
                              if (tx!=null) tx.rollback();
                              he.printStackTrace();
                    } finally {
                              session.close();
                              sessionFactory.close();
                    }
          }

          public List<Location> getAllLocations(){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    List<Location> locations = new ArrayList<>();

                    try {
                              tx = session.beginTransaction();
                              CriteriaBuilder builder = session.getCriteriaBuilder();
                              CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
                              criteria.from(Location.class);
                              locations = new ArrayList<>(session.createQuery(criteria).getResultList());
                    } catch (HibernateException he){
                              if(tx!=null)tx.rollback();
                              he.printStackTrace();
                    } finally {
                              session.close();
                              sessionFactory.close();
                    }
                    return locations;
          }

          public Location getLocationByCode(String locationCode){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    Location location = new Location();

                    try{
                              tx = session.beginTransaction();
                              CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                              CriteriaQuery<Location> criteriaQuery = criteriaBuilder.createQuery(Location.class);
                              Root<Location> locationRoot= criteriaQuery.from(Location.class);
                              criteriaQuery.select(locationRoot);
                              criteriaQuery.where(
                                      criteriaBuilder.equal(locationRoot.get("locationCode"),locationCode)
                              );
                              location = session.createQuery(criteriaQuery).getSingleResult();
                    } catch (HibernateException he){
                              if(tx!=null)tx.rollback();
                              he.printStackTrace();
                    } finally {
                              session.close();
                              sessionFactory.close();
                    }
                    return location;
          }

          public Location getLocationById(Integer id){
                    SessionFactory sessionFactory = ATMSessionFactory.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;
                    Location location = new Location();

                    try {
                              tx = session.beginTransaction();
                              location = session.get(Location.class,id);
                    } catch (HibernateException he) {
                              if (tx!=null) tx.rollback();
                              he.printStackTrace();
                    } finally {
                              session.close();
                              sessionFactory.close();
                    }
                    return location;
          }


          public Callback<ListView<Location>, ListCell<Location>> cellFactory = new Callback<ListView<Location>, ListCell<Location>>() {
                    @Override
                    public ListCell<Location> call(ListView<Location> list){
                              return new ListCell<Location>(){
                                        @Override
                                        protected void updateItem(Location item, boolean empty){
                                                  super.updateItem(item,empty);
                                                  if (item == null || empty) {
                                                            setGraphic(null);
                                                  } else {
                                                            setText(item.getName());
                                                  }
                                        }
                              };
                    }
          };
}