package meetingDoctor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class LoginGui extends JFrame implements ActionListener {
   JPanel panel;
   JLabel user_label, password_label, message;
   JTextField userName_text;
   JPasswordField password_text;
   JButton submit, cancel;
   LoginGui() {
      // Username Label
      user_label = new JLabel();
      user_label.setText("Patient Name :");
      userName_text = new JTextField();
      // Password Label
      password_label = new JLabel();
      password_label.setText("Password :");
      password_text = new JPasswordField();
      // Submit
      submit = new JButton("SUBMIT");
      panel = new JPanel(new GridLayout(4, 1));
      panel.add(user_label);
      panel.add(userName_text);
      panel.add(password_label);
      panel.add(password_text);
      message = new JLabel();
      panel.add(message);
      panel.add(submit);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // Adding the listeners to components...
      submit.addActionListener(this);
      add(panel, BorderLayout.CENTER);
      setTitle("Patient Login !!!");
      setSize(450,350);
      setVisible(true);
   }
   public static void main(String[] args) {
      new LoginGui();
   }
   @Override
   public void actionPerformed(ActionEvent ae) {
      String userName = userName_text.getText();
      String password = password_text.getText();
      String newLine = System.getProperty("line.separator");
      if (userName.trim().equals("admin") && password.trim().equals("seng")) {
    	  
         message.setText(" Hello " + userName + " "+ newLine +"Welcome to SENG-696");
      } else {
         message.setText(" Invalid user... ");
      }
   }
}
