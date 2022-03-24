import java.io.*;
import java.net.*;
import java.util.*;

public class LRR2{
	
	DataOutputStream out;
	BufferedReader in;
	String inMsg;
	
	
	LRR2(int port){
		try {
			inMsg = "";
			Socket s = new Socket("localhost", port);
				
			if (s.isConnected()){
				System.out.println("Connected successfully");
			} else {
				System.out.println("Could not connect");
				s.close();
				return;
			}
		
			out = 
				new DataOutputStream(s.getOutputStream());
			in = 
				new BufferedReader(new InputStreamReader(s.getInputStream()));
		}catch (Exception e){
			System.out.println("setup error");
		}
		
	}
	
	public void send(String msg){
		try {
			out.flush();
			System.out.println("Send: ");
			msg = msg + "\n";
			System.out.print("    " + msg);	
			out.write(msg.getBytes());
			out.flush();
		} catch (Exception e){
			System.out.println("send error");
		}
	}
	
	public List<String> receive(){
		List<String> lines = new ArrayList();
		try {
			
			String str = (String) in.readLine();
			System.out.println("Received: ");
			System.out.println("    " + str);
			if (str.split(" ")[0].equals("JCPL")){
				System.out.println("ignored");
				send("REDY");
				return receive();
				
			} 
			lines.add(str);
			
			while (in.ready()){
				str = in.readLine();
				if (str.split(" ")[0].equals("JCPL")){
					System.out.println("discarded");
					continue;
				}
				lines.add(str);
				System.out.println("    " + lines.get(lines.size()-1));
			}
		} catch (Exception e){
			System.out.println("receiver error");
			e.printStackTrace();
		}
		return lines;
	}
	
	public void close(){
		try {
			out.close();
			in.close();
		} catch (Exception e){
			System.out.println("close error");
		}
	}
	
	public static void main (String[] args){
		try{
			BufferedReader read = 
				new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter port: ");
			String num = read.readLine();
			int port = Integer.parseInt(num);
			
			
			LRR2 serv = new LRR2(port);
			serv.send("HELO");
			serv.receive();
			
			serv.send("AUTH jacob");
			serv.receive();
			
			serv.send("REDY");
			String[] job = serv.receive().get(0).split(" ");
			
			serv.send("GETS All");
			serv.receive();
			
			serv.send("OK");
			List<String> svrList = 
				serv.receive();
			serv.send("OK");
			serv.receive();
			
			// get the largest server from the list
			int largestSize = -1;
			String[] entry = null;
			for (String str : svrList){
				String[] parts = str.split(" ");
				int size = Integer.parseInt(parts[4]);
				if (size > largestSize){
					entry = parts;
					largestSize = size;
				}
			}
			
			
			if (entry == null){
				System.out.println(
					"there are no servers to assign to");
				return; 
			}
			
			int svrID = Integer.parseInt(entry[1]);
			String svrType = entry[0];
			
			while (!job[0].equals("NONE")){
				int jobID = Integer.parseInt(job[2]);
				
				serv.send("SCHD " + 
					jobID + " " + 
					svrType + " " +
					svrID);
				serv.receive();
				serv.send("REDY");
				job = serv.receive().get(0).split(" ");
			}
			
			// SCHD jobID serverType serverID
			// JOBN submitTime jobID estRuntime core memory disk
			
			serv.send("QUIT");
			serv.receive();
			serv.close();
			
			/*
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
			*/


		} catch (Exception e) {
			System.out.println("main error");
		}
	}
}

	
