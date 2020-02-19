package client_server.client;

import java.net.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*; 
  
class Client { 
	private Socket c;
	private PrintStream ps ;
	private BufferedReader br ;
	private Stage primaryStage;
	private Label label;
	private TextField ta;
	private Button button;
	private volatile String str;
	
	private void runServer() {
		Thread server = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					br
		            = new BufferedReader( 
		                new InputStreamReader( 
		                    c.getInputStream()));
					str="err";
					while(true) {
						try {
							while ((str = br.readLine()) != null) { 
								
							    if(!(str.equals("\n")||str.equals(""))) {
								    Platform.runLater(new Runnable() {
										@Override
										public void run() {
											label.setText(str);
											System.out.println("Done-"+str);
										}
									});
							    }
							}
						} catch(SocketException e) {
							
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}
				catch(Exception e) {
					e.printStackTrace();
				}

			}
		});
		server.start();
	}
	
	private void runClient() {
		String str2 = ta.getText();
		System.out.println(str2);
		 if(!(str.equals("\n")||str.equals(""))) 
			 ps.println(str2+"");
        //ps.flush();
	}
	
	public Client(Stage stage, Socket client)
	{
		primaryStage = stage;
		//System.out.println(primaryStage.getScene().getRoot().getChildrenUnmodifiable().toString());
		GridPane gp = (GridPane)primaryStage.getScene().getRoot();
		label = (Label)gp.getChildren().get(0);
		label.setText("yesss");
		ta = (TextField)gp.getChildren().get(1);
		ta.setVisible(true);
		button = (Button)gp.getChildren().get(2);
		button.setText("Send");
		button.setVisible(true);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				runClient();
			}
		});
		//s= server;
		c= client;
		Thread tw = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ps
					= new PrintStream(c.getOutputStream());
					ps.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				runServer();
				
			}
		});
		tw.start();
	}
  
}