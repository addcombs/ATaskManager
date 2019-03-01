package com.ataskmanager.messages;

import com.ataskmanager.ATaskManager;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;

import javax.jms.*;

/** Controller for the Add Location Form
 * @author Adam Combs
 * @author Seth Wampole
 */
public class ChatSubscription implements MessageListener {
          private static Connection connection;
          private static Session session;

          /**
           *        Subscribe to MESSAGE topic.
           *
           *        @param              user                String of user subscribing
           */
          public static void subscribe(String user)  {
                    try{
                              ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://messageq1.pyleco.loc:61616");
                              connection = factory.createConnection();
                              connection.setClientID(user);
                              connection.start();
                              session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                              Topic destination = session.createTopic("MESSAGE");
                              MessageConsumer consumer = session.createDurableSubscriber(destination,"Listener " + 1);
                              consumer.setMessageListener(new ChatSubscription());
                    } catch (JMSException e){
                              e.printStackTrace();
                    }
          }

          /**
           *        On message received, check is message type is MQTT message or Text Message.
           *        Extracts message.
           *        Sends message into main stage.
           *
           *        @param              message             Message received
           */
          @Override
          public void onMessage(Message message) {
                    if (message instanceof ActiveMQBytesMessage){
                              try {
                                        ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
                                        byte[] buffer = new byte[(int) bytesMessage.getBodyLength()];
                                        bytesMessage.readBytes(buffer);
                                        String textMessage  = new String(buffer);
                                        ATaskManager.getATMController().incomingMessages(textMessage);
                              } catch (Exception e) {
                                        e.printStackTrace();
                              }
                    } else {
                              try {
                                        TextMessage text = (TextMessage) message;
                                        ATaskManager.getATMController().incomingMessages(text.getText());
                              } catch (JMSException e) {
                                        e.printStackTrace();
                              }
                    }
          }

          /**
           *        Stops and closes connection.  Closes session.
           *
           *        @throws JMSException
           */
          public static void close() throws JMSException {
                    connection.stop();
                    connection.close();
                    session.close();
          }
}
