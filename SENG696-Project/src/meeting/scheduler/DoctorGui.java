package meeting.scheduler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DoctorGui extends JFrame {
    private DoctorAgent myAgent;
    private JTextField dayField;

    DoctorGui(DoctorAgent a) {
        super(a.getLocalName());

        myAgent = a;

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 2));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(new JLabel("Day of Meeting:"));
        dayField = new JTextField(15);
        p.add(dayField);
        getContentPane().add(p, BorderLayout.CENTER);

        JButton addButton = new JButton("Request");
        addButton.addActionListener(ev -> {
            try {
                String day = dayField.getText().trim();
                myAgent.requestMeeting(Integer.parseInt(day)); // send new meeting day to agent
                dayField.setText("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(DoctorGui.this, "Invalid values. " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        p = new JPanel();
        p.add(addButton);
        getContentPane().add(p, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.doDelete();
            }
        });

        setResizable(false);
    }

    public void display() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth() / 2;
        int centerY = (int) screenSize.getHeight() / 2;
        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        setVisible(true);
    }
}
