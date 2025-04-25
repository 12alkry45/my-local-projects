package org.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Subscribe extends JFrame implements MqttCallback {
    private static List<String> products = new ArrayList<>();
    private JTextArea area2;
    private JPanel Subscribe;
    private static final String topic = "test/request";
    private static final String broker = "tcp://broker.emqx.io:1883";
    private static final String clientId = "ProductGet";

    public Subscribe() {
        init();
        connect();
    }

    private void init() {
        setTitle("Final product's list");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        Subscribe = new JPanel();
        Subscribe.setLayout(new GridLayout(1, 1));
        area2 = new JTextArea();
        area2.setEditable(false);
        JScrollPane scroll = new JScrollPane(area2);
        Subscribe.add(scroll, BorderLayout.CENTER);
        add(Subscribe);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Subscribe();
        });
    }

    private void connect() {
        try {
            MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.setCallback(this);
            client.connect();
            client.subscribe(topic, 2);
        } catch (MqttException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void connectionLost(Throwable cause) {
        System.out.println("lost connection");
    }

    public void messageArrived(String topic, MqttMessage message) {
        SwingUtilities.invokeLater(
                () -> {
                    try {
                        String p = new String(message.getPayload());
                        products.add(p);
                        updateListArea();
                    } catch (Exception e) {
                        System.out.println("ERROR " + e.getMessage());
                    }
                });
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    private void updateListArea() {
        SwingUtilities.invokeLater(() -> {
            StringBuilder buildList = new StringBuilder();
            for (int i = 0; i < products.size(); i++) {
                buildList.append("Number ").append(i + 1).append(" is ").append(products.get(i)).append(";\n");
            }
            area2.setText(buildList.toString());
        });
    }

}
