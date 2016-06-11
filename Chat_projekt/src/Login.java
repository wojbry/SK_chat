import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class Login extends JFrame{
	String name;
	client_GUI Gui;
	public Login(client_GUI g){
		setSize(400, 200);
		setLayout(new GridLayout(3,1));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JLabel label = new JLabel("LOGIN:",JLabel.CENTER);
		JTextField login = new JTextField(15);
		JButton ok = new JButton("ZALOGUJ");
		add(label);
		add(login);
		add(ok);
ActionListener oklistener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				name = login.getText();
				if(name != null){
					g.username = name;
					
					dispose();
					g.setVisible(true);
					g.getOs().println(name);
				}
				
			}
	
	
		};
		
		ok.addActionListener(oklistener);
	}

}
