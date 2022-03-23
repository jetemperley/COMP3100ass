import java.io.*;
import java.net.*;
import java.util.*;

public class LRR{
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
			
			System.out.println("Send: ");
				outMsg = "HELO\n";
			System.out.print("    " + outMsg);	
			out.write(outMsg.getBytes());
			out.flush();
			
			System.out.println("Received: ");
			inMsg = (String) in.readLine();
			System.out.println("    " + inMsg);
			
			System.out.println("Send: ");
			outMsg = "AUTH jacob\n";
			System.out.print("    " + outMsg);	
			out.write(outMsg.getBytes());
			out.flush();
			
			System.out.println("Received: ");
			inMsg = (String) in.readLine();
			System.out.println("    " + inMsg);
			
			
			System.out.println("Send: ");
			outMsg = "REDY\n";
			System.out.print("    " + outMsg);	
			out.write(outMsg.getBytes());
			out.flush();
			
			System.out.println("Received: ");
			inMsg = (String) in.readLine();
			System.out.println("    " + inMsg);
			
			System.out.println("Send: ");
			outMsg = "GETS All\n";
			System.out.print("    " + outMsg);	
			out.write(outMsg.getBytes());
			out.flush();
			
			System.out.println("Received: ");
			inMsg = (String) in.readLine();
			System.out.println("    " + inMsg);
			
			System.out.println("Send: ");
			outMsg = "OK\n";
			System.out.print("    " + outMsg);	
			out.write(outMsg.getBytes());
			out.flush();
			
			
			List<String> names = new ArrayList();
			List<Integer> cores  = new ArrayList();
			List<Integer> ids  = new ArrayList();
			
			
			
			System.out.println("Received: ");
			inMsg = (String) in.readLine();
			String[] parts = inMsg.split(" ");
			names.add(parts[0]);
			cores.add(Integer.parseInt(parts[4]));
			ids.add(Integer.parseInt(parts[1]));
			System.out.println("    " + inMsg);
			while (in.ready()){
				inMsg = in.readLine();
				parts = inMsg.split(" ");
				names.add(parts[0]);
				cores.add(Integer.parseInt(parts[4]));
				ids.add(Integer.parseInt(parts[1]));
				System.out.println(
					names.get(names.size()) + " " +
					ids.get(ids.size()) + " " +
					cores.get(cores.size()));
			}


			out.close();
			in.close();
		} catch (Exception e){
		}
	}
}

	
