//package io.emqx;
package org.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class Publish extends JFrame {
    private JPanel Publish;
    private JTextField inputField;
    private JLabel labelinput;
    private JButton addProduct;

    private static final String topic = "test/request";
    private static final String broker = "tcp://broker.emqx.io:1883";
    private static final String clientId = "ProductSender";

    Publish() {
        setTitle("Publish");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 150);
        Publish = new JPanel();
        Publish.setLayout(new GridLayout(3, 1));
        inputField = new JTextField();
        labelinput = new JLabel("Enter product name");
        //labelinput.setText("Enter product name");
        addProduct = new JButton();
        addProduct.setText("Send");
        addProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String product = inputField.getText().trim();
                send(product);
                inputField.setText("");
            }
        });

        Publish.add(labelinput);
        Publish.add(inputField);
        Publish.add(addProduct);
        add(Publish);
        setVisible(true);
    }

    public static void main(String[] args) {
        Publish p = new Publish();
    }

    public static void send(String lastName) {
        try {
            MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
            MqttMessage message = new MqttMessage(lastName.getBytes());
            message.setQos(2);
            client.publish(topic, message);
            client.disconnect();
        } catch (MqttException me) {
            System.out.println("IS ERROR: " + me.getMessage());
        }
    }

}
