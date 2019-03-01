package com.ataskmanager.messages;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/** Controller for the Add Location Form
 * @author Adam Combs
 * @author Seth Wampole
 */
public class NewUserProducer {

          /**
           *        When new user is created.
           *        Retained "False" message is sent to STATUS.username topic with MQTT protocol.
           *
           *        @param              username            String username of new user.
           */
          public static void publishMQTT(String username){
                    String broker = "tcp://messageq1.pyleco.loc:1883";
                    String clientId = "newuser_"+username;
                    String topic = "STATUS/"+username;
                    String message = "false";
                    MemoryPersistence memoryPersistence = new MemoryPersistence();

                    try{
                              MqttClient mqttClient = new MqttClient(broker, clientId, memoryPersistence);
                              MqttConnectOptions connectOptions = new MqttConnectOptions();
                              connectOptions.setCleanSession(true);
                              mqttClient.connect(connectOptions);

                              MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                              mqttMessage.setRetained(true);
                              System.out.println("Topic" + topic + " Message: " + mqttMessage);

                              mqttClient.publish(topic, mqttMessage);

                              mqttClient.disconnect();;

                    } catch(MqttException me){
                              me.printStackTrace();
                    }
          }
}
