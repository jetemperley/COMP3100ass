import java.io.*;
import java.net.*;

public class Client{
	public static void main (String[] args){
		try{
			BufferedReader read = 
				new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter port: ");
			String num = read.readLine();
			int port = Integer.parseInt(num);
			
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
			
		
			while (!inMsg.equals("QUIT")) {
				System.out.print("Send: ");
				outMsg = read.readLine();
				outMsg = outMsg + "\n";
				out.write(outMsg.getBytes());
				out.flush();
				System.out.println("Received: ");
				inMsg = in.readLine();
				System.out.println("    " + inMsg);
				while (in.ready()){
					inMsg = in.readLine();
					System.out.println("    " + inMsg);
				}
				
			}


			out.close();
			in.close();
		} catch (Exception e){
		}
	}
}

	
