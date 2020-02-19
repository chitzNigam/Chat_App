package client_server.server;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

import javafx.application.Platform;

public class Server {
	
	private ServerSocket ss;
	private Socket s1,s2;
	private PrintStream ps1,ps2 ;
	private BufferedReader br1,br2 ;
	private boolean abort1 = false;
	private boolean abort2 = false;
	private boolean flag = false;
	private volatile String str,str2;
	private Thread t1,t2;
	private Runnable r1,r2;
	
	public Server() {
		showIP();
		runServer(15414);
	}
	
	private synchronized void sendText(PrintStream p, String s) {
		p.print(s);
	}
	
	private void showIP() {
		//Create a new Dialog box to show the Ip using javafx
		
		//new JDialog(new JFrame("Your IP Address"), "Test").show();
		try {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
				
						JOptionPane.showMessageDialog(null, "IP Address of Server is :"+InetAddress.getLocalHost().getHostAddress());
					}
					catch(UnknownHostException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
			
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
	}
	
	public void runServer(int port) {
		try{
			ss = new ServerSocket(port);
			s1 = ss.accept();
			startFirstThread();
			s2 = ss.accept();
			startSecondThread();
			flag=true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
	}
	private void startSecondThread() {
		try {
			r2 = new Runnable() {
				
				@Override
				public void run() {
					try {
						br2
			            = new BufferedReader( 
			                new InputStreamReader( 
			                    s2.getInputStream()));
						ps2
						= new PrintStream(s2.getOutputStream());
						ps2.flush();
					}catch(Exception e) {
						e.printStackTrace();
					}
					while(!flag);
					sendText(ps2,"Connected");
					
					while(!abort1) {
						try {
							str ="err";
							while ((str = br2.readLine()) != null) { 
								
							    if(!(str.equals("\n")||str.equals(""))) {
								    Platform.runLater(new Runnable() {
										@Override
										public void run() {
											sendText(ps1, str);
											System.out.println("Done-"+str);
										}
									});
							    }
							}
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
			t2 = new Thread(r2);
			t2.start();
		}
		catch(Exception e) {
			
		}
		
	}

	private void startFirstThread() {
		try {
			r1 =new Runnable() {
				
				@Override
				public void run() {
					try {
						br1
			            = new BufferedReader( 
			                new InputStreamReader( 
			                    s1.getInputStream()));
						ps1
						= new PrintStream(s1.getOutputStream());
						ps1.flush();
					}catch(Exception e) {
						e.printStackTrace();
					}
					
					while(!flag);
					sendText(ps2,"Connected");
				
					
					while(!abort2) {
						try {
							str2 ="err";
							while ((str2 = br1.readLine()) != null) { 
								
							    if(!(str2.equals("\n")||str2.equals(""))) {
								    Platform.runLater(new Runnable() {
										@Override
										public void run() {
											sendText(ps2, str2);
											System.out.println("Done-"+str2);
										}
									});
							    }
							}
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
			t1= new Thread(r1);
			t1.start();
		}
		catch(Exception e) {
			
		}
	}

	public static void main(String srgs[]) {
		new Server();
	}
}
