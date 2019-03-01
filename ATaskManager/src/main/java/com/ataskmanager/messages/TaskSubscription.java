package com.ataskmanager.messages;

import com.ataskmanager.ATaskManager;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;

import javax.jms.*;

public class TaskSubscription implements MessageListener {
          private static Connection connection;
          private static Session session;

          public static void subscribe(String user)  {
                    try{
                              ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://messageq1.pyleco.loc:61616");
                              connection = factory.createConnection();
                              connection.setClientID(user);
                              connection.start();
                              session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                              Topic destination = session.createTopic("TASK");
                              MessageConsumer consumer = session.createConsumer(destination);
                              consumer.setMessageListener(new TaskSubscription());
                    } catch (JMSException e){
                              e.printStackTrace();
                    }
          }

          @Override
          public void onMessage(Message message) {
                    if (message instanceof ActiveMQBytesMessage){
                              try {
                                        ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
                                        byte[] buffer = new byte[(int) bytesMessage.getBodyLength()];
                                        bytesMessage.readBytes(buffer);
                                        String textMessage  = new String(buffer);
                                        ATaskManager.getATMController().refreshTasks();
                                        ATaskManager.getATMController().refreshUI();
                              } catch (Exception e) {
                                        e.printStackTrace();
                              }
                    } else {
                              ATaskManager.getATMController().refreshTasks();
                              ATaskManager.getATMController().refreshUI();
                    }
          }

          public static void close() throws JMSException {
                    connection.close();
                    session.close();
          }
}