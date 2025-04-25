import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PictureForm {

    private JPanel mainPanel;
    private JSpinner upLimit;
    private JLabel UpperLimit;
    private JSpinner lowLimit;
    private JLabel lowerLimit;
    private JLabel countT;
    private JSpinner countTrap;
    private CanvasPanel canvasPanel;
    private JCheckBox toFill;
    private final int size = 750;

    private void createUIComponents() {
        canvasPanel = new CanvasPanel();
    }

    PictureForm() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(0xFEFBEF));

        lowerLimit = new JLabel("Нижняя граница");
        lowerLimit.setPreferredSize(new Dimension(100, 60));

        lowLimit = new JSpinner();
        lowLimit.setPreferredSize(new Dimension(100, 60));

        UpperLimit = new JLabel("Верхняя граница");
        UpperLimit.setPreferredSize(new Dimension(100, 60));

        upLimit = new JSpinner();
        upLimit.setPreferredSize(new Dimension(100, 60));

        countT = new JLabel("Число трапеций");

        countTrap = new JSpinner();
        countTrap.setPreferredSize(new Dimension(100, 40));

        toFill = new JCheckBox("Закрасить?");

        createUIComponents();
        canvasPanel.setPreferredSize(new Dimension(800, 800));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lowerLimit, gbc);

        gbc.gridx = 1;
        mainPanel.add(lowLimit, gbc);

        gbc.gridx = 2;
        mainPanel.add(toFill, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(UpperLimit, gbc);

        gbc.gridx = 1;
        mainPanel.add(upLimit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(countT, gbc);

        gbc.gridx = 1;
        mainPanel.add(countTrap, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(canvasPanel, gbc);

        lowLimit.setModel(new SpinnerNumberModel(0, 0, size, 1));
        upLimit.setModel(new SpinnerNumberModel(size, 0, size, 1));
        countTrap.setModel(new SpinnerNumberModel(10, 1, size, 1));

        lowLimit.addChangeListener(e -> reCreate());
        upLimit.addChangeListener(e -> reCreate());
        countTrap.addChangeListener(e -> reCreate());
        toFill.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                boolean isF = e.getStateChange() == ItemEvent.SELECTED;
                canvasPanel.updateFill(isF);
            }
        });
    }

    public void reCreate() {
        int a = (int) lowLimit.getValue();
        int b = (int) upLimit.getValue();
        int n = (int) countTrap.getValue();
        canvasPanel.updateParam(a, b, n);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("PictureForm");
        frame.setContentPane(new PictureForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(8000, 6000);
        frame.setMinimumSize(new Dimension(1750, 1080));
        frame.pack();
        frame.setVisible(true);
    }
}
