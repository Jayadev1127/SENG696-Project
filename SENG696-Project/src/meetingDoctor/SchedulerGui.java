
package meetingDoctor;

import jade.core.AID;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SchedulerGui extends JFrame {	
	private DoctorConfirmingAgent myAgent;
	
	private JTextField nameField,ageField, phnumField,adrsField,titleField, priceField;
	
	SchedulerGui(DoctorConfirmingAgent a) {
		super(a.getLocalName());
		
		myAgent = a;
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		p.add(new JLabel("Name:"));
		nameField = new JTextField(15);
		p.add(nameField);
		p.add(new JLabel("Age:"));
		ageField = new JTextField(15);
		p.add(ageField);
		p.add(new JLabel("Phone Number:"));
		phnumField = new JTextField(15);
		p.add(phnumField);
		p.add(new JLabel("Address:"));
		adrsField = new JTextField(15);
		p.add(adrsField);
		p.add(new JLabel("Patientsymptoms:"));
		titleField = new JTextField(15);
		p.add(titleField);
		p.add(new JLabel("Approx amount of days with persistent symptoms:"));
		priceField = new JTextField(15);
		p.add(priceField);
		
		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String title = titleField.getText().trim();
					String price = priceField.getText().trim();
					String name = nameField.getText().trim();
					String age = ageField.getText().trim();
					String phnum = phnumField.getText().trim();
					String adrs = adrsField.getText().trim();
					
					myAgent.updateCatalogue(title, Integer.parseInt(price));
					//myAgent.updateCatalogue(title, Integer.parseInt(age));
					//myAgent.updateCatalogue(title, Integer.parseInt(phnum));
					titleField.setText("");
					priceField.setText("");
					ageField.setText("");
					nameField.setText("");
					adrsField.setText("");
					phnumField.setText("");
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(SchedulerGui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		p = new JPanel();
		p.add(addButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		// Make the agent terminate when the user closes 
		// the GUI using the button on the upper right corner	
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myAgent.doDelete();
			}
		} );
		
		setResizable(false);
	}
	
	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}	
}
