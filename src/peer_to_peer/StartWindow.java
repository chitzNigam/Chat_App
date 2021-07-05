package peer_to_peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
 
public class StartWindow extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	run(primaryStage);
    }
    
    public void run(Stage primaryStage) {
    	GridPane gridPane = new GridPane();
 
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(200, 00, 200, 200));                                             
        
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        ColumnConstraints columnOneConstraints = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.CENTER);

        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);                                                                                  //Initial UI

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
    	
        primaryStage.setTitle("Chat Application");
        Button btn = new Button();
        Label l = new Label("Enter the IP address of your friend");                                                                       // IP for connection
        Label ipl = new Label("");
        TextField tf = new TextField("0");
        btn.setText("OK");
        btn.setOnAction(new EventHandler<ActionEvent>() {                                                                   // Button actions after IP
 
            @Override
            public void handle(ActionEvent event) {                                                                         
                System.out.println(tf.getText());
                btn.setVisible(false);
                tf.setVisible(false);
                ipl.setVisible(false);
                new TryingConnection(tf.getText()).func(primaryStage,gridPane,l,btn);                                   // Creating peer connection to the other person
            }
        });
        GridPane.setHalignment(btn, HPos.CENTER);
        GridPane.setHalignment(l, HPos.CENTER);
        GridPane.setHalignment(ipl, HPos.CENTER);
        GridPane.setHalignment(tf, HPos.CENTER);
        gridPane.add(l, 0, 0);
        gridPane.add(tf,0, 1);
        gridPane.add(btn, 0, 2);
        gridPane.add(ipl, 0, 3);
        try {
			ipl.setText("Your IP address is :"+InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        primaryStage.setScene(new Scene(gridPane, 300,300));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
    }
}