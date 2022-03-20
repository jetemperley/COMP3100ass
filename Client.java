import java.io.*;
import java.net.*;

public class Client{
		public static void main (String[] args){
			try{
				BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Enter port: ");
				// String num = read.readLine();
				int port = 50000; // Integer.parseInt(num);
				
				Socket s = new Socket("localhost", port);
				
				if (s.isConnected()){
					System.out.println("Connected successfully");
				} else {
					System.out.println("Could not connect");
					s.close();
					return;
				}
				
				DataOutputStream out = 
					new DataOutputStream(s.getOutputStream());
				BufferedReader in = 
					new BufferedReader(new InputStreamReader(s.getInputStream()));
					
				String outMsg = "", inMsg = "";
				
				outMsg = "HELO\n";
				System.out.print("Send: " + outMsg);
				out.write(outMsg.getBytes());
				inMsg = (String) in.readLine();
				System.out.println("Received: " + inMsg);
				
				outMsg = "AUTH jacob\n";
				System.out.print("Send: " + outMsg);
				out.write(outMsg.getBytes());
				inMsg = (String) in.readLine();
				System.out.println("Received: " + inMsg);
				
				outMsg = "REDY\n";
				System.out.print("Send: " + outMsg);
				out.write(outMsg.getBytes());
				inMsg = (String) in.readLine();
				System.out.println("Received: " + inMsg);
				
				outMsg = "QUIT\n";
				System.out.print("Send: " + outMsg);
				out.write(outMsg.getBytes());
				inMsg = (String) in.readLine();
				System.out.println("Received: " + inMsg);
				
				while (!inMsg.equals("QUIT")) {
					System.out.print("Send: ");
					outMsg = read.readLine();
					outMsg = outMsg + "\n";
					out.write(outMsg.getBytes());
					inMsg = (String) in.readLine();
					System.out.println("Received: " + inMsg);
					
				}


				out.close();
				in.close();
			} catch (Exception e){
			}
		}
}

	
