package client_server.client;

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
	String ip = "";
	Stage stage = null;
	Thread t;
	Button b;
	
	private void runApplication() {
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater(new Runnable() {
		    @Override public void run() {
				new Client(stage, s);
		    }
		});
	}
	
    public TryingConnection(String ip) {
		this.ip = ip;
	}
	
    private void connect(Label label) {
    	
		while(s == null) {
			try {
				s = new Socket(ip, 15414);
				if(s == null) {
					Thread.sleep(1000);
				}
			} catch (UnknownHostException e) {
				
				e.printStackTrace();
			
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			System.out.println("server connected");
			Platform.runLater(new Runnable() {
			    @Override public void run() {
			        label.setText("Server Connected");
			        b.setVisible(false);
			    }
			});
			runApplication();
		}
	
    }
   
    public void func(Stage primaryStage, GridPane gridPane, Label l, Button btn) {
    	stage = primaryStage;
    	l.setText("Trying to connect to Server...");
    	b = new Button("Retry");
    	GridPane.setHalignment(b, HPos.CENTER);
    	btn.setVisible(false);
    	gridPane.add(b, 0, 2);
    	b.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				t.stop();
				try {
					if(s!=null)
						s.close();
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
