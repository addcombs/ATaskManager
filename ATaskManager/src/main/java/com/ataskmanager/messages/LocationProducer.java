package com.ataskmanager.messages;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class LocationProducer {
          private static Connection connection;
          private static Session session;

          public static void sendMessage(String message) {
                    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://messageq1.pyleco.loc:61616");
                    try{
                              connection = connectionFactory.createConnection();
                              connection.start();
                              session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
                              Destination destination = session.createTopic("LOCATION");
                              MessageProducer producer = session.createProducer(destination);
                              producer.send(session.createTextMessage(message));
                              System.out.println(message);
                              connection.close();
                              session.close();
                    } catch (JMSException e){
                              e.printStackTrace();
                    }
          }
}