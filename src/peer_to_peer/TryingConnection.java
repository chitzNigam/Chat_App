package peer_to_peer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TryingConnection {
	Socket s = null;
	Socket c = null;
	ServerSocket ss = null;
	String ip = "";
	Stage stage = null;
	Thread t;
	Button b;
	private volatile int isNotConnected = 0;
	
	private void runApplication() {
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater(new Runnable() {
		    @Override public void run() {
				new Peer(stage, s, c);
		    }
		});
	}
	
    public TryingConnection(String ip) {
		this.ip = ip;
	}
	
    private void connect(Label label) {
    	
    	t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(c == null) {
					try {
						c = new Socket(ip, 15400);
						if(c == null) {
							Thread.sleep(1000);
						}
					} catch (UnknownHostException e) {
						
						e.printStackTrace();
					} catch (IOException e) {
						 
						e.printStackTrace();
						isNotConnected--;
						break;
					} catch (InterruptedException e) {
						
						e.printStackTrace();
						isNotConnected--;
						break;
					}
				}
				
				isNotConnected++;
				System.out.println("Client connected");
				if(isNotConnected == 2) {
					Platform.runLater(new Runnable() {
					    @Override public void run() {
					        label.setText("Connected");
					        b.setVisible(false);
					    }
					});
					runApplication();
				}
			}
		});
    	t.start();
    	
		try {
			ss = new ServerSocket(15400);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		System.out.println("Server created");
    	
		try {
			s = ss.accept();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		isNotConnected++;
		System.out.println("Server accepted");
		if(isNotConnected == 2) {
			Platform.runLater(new Runnable() {
			    @Override public void run() {
			        label.setText("Connected");
			        b.setVisible(false);
			    }
			});
			runApplication();
		}
    }
   
    public void func(Stage primaryStage, GridPane gridPane, Label l, Button btn) {
    	stage = primaryStage;
    	l.setText("Trying to connect...");
    	b = new Button("Retry");
    	GridPane.setHalignment(b, HPos.CENTER);
    	btn.setVisible(false);
    	gridPane.add(b, 0, 2);
    	b.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				t.stop();
				try {
					if(ss!=null)
						ss.close();
					if(s!=null)
						s.close();
					if(c!=null)
						c.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new StartWindow().run(primaryStage);
			}
		});
    	Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				connect(l);
			}
		});
        t.start();
    }

}
