import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Window extends JFrame{
    JPanel Window;
    JLabel textLabel1;
    JLabel textLabel2;
    JPanel inputPanel;
    JPanel outputPanel;
    JTextArea input;
    JTextArea output;
    JButton gen;
    String[] lines;
    public Window() {
        super("Parser");
        setSize(1000, 800);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Window = new JPanel();
        inputPanel =  new JPanel();
        outputPanel =  new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        outputPanel.setLayout(new GridBagLayout());
        textLabel1 = new JLabel("Input Code");
        textLabel2 = new JLabel("Output Code");
        input = new JTextArea("Input", 500, 50);
        output = new JTextArea("Output", 500, 50);
        output.setEditable(false);
        gen = new JButton("GENERATE");
        Window.setLayout(new GridLayout(1,2));

        gen.addActionListener(e -> readText());

        InitLeftPanel();
        InitRightPanel();

        Window.add(inputPanel);
        Window.add(outputPanel);
        add(Window);
        setVisible(true);
    }
    public void InitLeftPanel(){
        GridBagConstraints gbc = new GridBagConstraints();
        //inputTextPanel
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5,5,5,5);
        inputPanel.add(textLabel1, gbc);
        //buttonOfGenerate
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(gen);
        //scrollingPanel
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(input);
        inputPanel.add(scrollPane, gbc);
    }
    public void InitRightPanel(){
        GridBagConstraints gbc2 = new GridBagConstraints();
        //outputTextPanel
        gbc2.gridx = 0; gbc2.gridy = 0; gbc2.anchor = GridBagConstraints.WEST;
        gbc2.insets = new Insets(5,5,5,5);
        outputPanel.add(textLabel2, gbc2);
        //scrollingPanel
        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.weightx = 1.0; gbc2.weighty = 1.0;
        gbc2.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane2 = new JScrollPane(output);
        outputPanel.add(scrollPane2, gbc2);
    }
    public void readText(){
        String fullText = input.getText();
        lines = fullText.split("\n");
        output.setText("");
        Parser p = new Parser(lines);
        String fullparse = p.sb.toString();
        output.setText(fullparse);
    }
    public static void main(String[] args){
        Window w = new Window();
    }
}
