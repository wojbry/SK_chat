import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class client_GUI extends JFrame implements Runnable {
	static client_GUI cl;
	// The client socket
	private static Socket clientSocket = null;
	// The output stream
	private static PrintStream os = null;
	// The input stream
	private static DataInputStream is = null;
	
	private static BufferedReader inputLine = null;
	private static boolean closed = false;
	static // The default port.
	int portNumber = 2222;
	// The default host.
	static String host = "localhost";
	static String username;
	JLabel label;
	JTextField sendTo;
	JLabel label2;
	JTextArea message;
	JLabel label3;
	final public JTextArea info = new JTextArea();;
	JButton send;
	JButton sendAll;
  
	public client_GUI(){
		setSize(700, 700);
		setLayout(new GridLayout(4,2));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		label = new JLabel("Send to:",JLabel.CENTER);
		sendTo = new JTextField(15);
		label2 = new JLabel("Message:",JLabel.CENTER);
		message = new JTextArea();
		label3 = new JLabel("Conversations:",JLabel.CENTER);
		//info = new JTextArea();
		info.setEditable(false);
	
		send = new JButton("SEND");
		sendAll = new JButton("SEND ALL");
		
		add(label);
		add(sendTo);
		add(label2);
		add(message);
		add(label3);
		add(info);
		add(send);
		add(sendAll);
	
		ActionListener sendAllListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = message.getText();
				if(text != null){
					getOs().println(text);
				}
				message.setText("");
				
			}
	
	
		};
		
		sendAll.addActionListener(sendAllListener);
		
		ActionListener sendListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				String receiver = sendTo.getText();
				String text = message.getText();
				if(text != null){
					getOs().println("@"+receiver+" "+text);
				}
				message.setText("");
				
			}
	
	
		};
		
		send.addActionListener(sendListener);
	
	   

	}
  
	public static void connect(){
		try {
			clientSocket = new Socket(host, portNumber);
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			setOs(new PrintStream(clientSocket.getOutputStream()));
			is = new DataInputStream(clientSocket.getInputStream());
	    	} catch (UnknownHostException e) {
	    		System.err.println("Don't know about host " + host);
	    	} catch (IOException e) {
	    		System.err.println("Couldn't get I/O for the connection to the host "
	    				+ host);
	    	}
	    
	}
  
	public static void main(String[] args) {
		connect();
		cl = new client_GUI();
		Login l = new Login(cl);
		l.setVisible(true);

		if (clientSocket != null && getOs() != null && is != null) {
			try {

	        /* Create a thread to read from the server. */
				new Thread(new client_GUI()).start();
				while (!closed) {
					getOs().println(inputLine.readLine().trim());
				}
	        /*
	         * Close the output stream, close the input stream, close the socket.
	         */
				getOs().close();
				is.close();
				clientSocket.close();
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}
	}

	public void run() {
    /*
     * Keep on reading from the socket till we receive "Bye" from the
     * server. Once we received that then we want to break.
     */
		String responseLine;
		try {
			while ((responseLine = is.readLine()) != null) {
				System.out.println(responseLine);
				cl.info.append(responseLine+"\n");
	        
	      
				if (responseLine.indexOf("*** Bye") != -1)
					break;
			}
			closed = true;
	    } catch (IOException e) {
	      System.err.println("IOException:  " + e);
	    }
	  }

	public static PrintStream getOs() {
		return os;
	}
	
	public static void setOs(PrintStream os) {
		client_GUI.os = os;
	}

}