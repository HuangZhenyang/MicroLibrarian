package MicrosoftImageCup;

//import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//import java.security.Provider.Service;

public class Server{

	
	public static void main(String[] args) {
		
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(6100);
			System.out.println(">>> Server start");
			
			while(true){
				Socket socket = serverSocket.accept();	
				System.out.println("Client IP:" + socket.getInetAddress());
				
				//BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//PrintWriter os = new PrintWriter(socket.getOutputStream());
				//BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
				
				//String string = is.readLine();
						
				MainService t = new MainService();
				
				t.setSocket(socket);
				t.start();
				
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
		
	}

}