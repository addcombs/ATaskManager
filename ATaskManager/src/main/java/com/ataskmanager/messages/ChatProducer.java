package com.ataskmanager.messages;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.sql.Timestamp;

/** Send message to ActiveMQ broker for topic MESSAGE
 * @author Adam Combs
 * @author Seth Wampole
 */
public class ChatProducer {
          private static Connection connection;
          private static Session session;

          /**
           *        Send message with senderId, recipId, timestamp, message
           *
           *        @param              senderId                      String of sender's id
           *        @param              recipId                          String of recipient's id
           *        @param              timestamp                 Timestamp of message
           *        @param              message                       String of chat message
           */
          public static void sendMessage(String senderId, String recipId, Timestamp timestamp, String message) {
                    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://messageq1.pyleco.loc:61616");
                    try{
                              connection = connectionFactory.createConnection();
                              connection.start();
                              session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
                              Destination destination = session.createTopic("MESSAGE");
                              MessageProducer producer = session.createProducer(destination);
                              String textMessage = senderId + ";" + recipId + ";" + timestamp.toString() + ";" + message;
                              producer.send(session.createTextMessage(textMessage));
                              connection.close();
                              session.close();
                    } catch (JMSException e){
                              e.printStackTrace();
                    }
          }
}
