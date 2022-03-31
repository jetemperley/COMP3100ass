import java.io.*;
import java.net.*;
import java.util.*;

public class LRR{
	
	DataOutputStream out;
	BufferedReader in;
	String inMsg;
	
	
	LRR(int port){
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
			System.out.println("Setup error");
		}
		
	}
	
	public void send(String msg){
		try {
			out.flush();
			// System.out.println("Send: ");
			msg = msg + "\n";
			// System.out.print("    " + msg);	
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
			// System.out.println("Received: ");
			// System.out.println("    " + str);
			if (str.split(" ")[0].equals("JCPL")){
				// System.out.println("ignored");
				send("REDY");
				return receive();
				
			} 
			lines.add(str);
			
			while (in.ready()){
				str = in.readLine();
				if (str.split(" ")[0].equals("JCPL")){
					// System.out.println("discarded");
					continue;
				}
				lines.add(str);
				// System.out.println("    " + lines.get(lines.size()-1));
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
			// System.out.print("Enter port: ");
			// String num = read.readLine();
			// int port = Integer.parseInt(num);
			
			
			LRR serv = new LRR(50000);
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
			int largestSize = -1, index = -1;
			String[] entry = null;
			for (int i = 0; i < svrList.size(); i++){
				String str = svrList.get(i);
				String[] parts = str.split(" ");
				int size = Integer.parseInt(parts[4]);
				if (size > largestSize){
					entry = parts;
					largestSize = size;
					index = i;
				}
			}
			
			if (entry == null){
				System.out.println(
					"there are no servers to assign to");
				return; 
			}
			
			// get a list of the largest servers
			List<String[]> LRRServers = new ArrayList();
			for (int i = index; i < svrList.size(); i++){
				String str = svrList.get(i);
				String[] parts = str.split(" ");
				if (parts[0].equals(entry[0])){
					LRRServers.add(parts);
				} else {
					break;
				}
			}
			
			int current = 0;
			
			while (!job[0].equals("NONE")){
			
				int jobID = Integer.parseInt(job[2]);
				String[] currServ =
					LRRServers.get(current);
				
				
				serv.send("SCHD " + 
					jobID + " " + 
					currServ[0] + " " +
					Integer.parseInt(currServ[1]));
				serv.receive();
				serv.send("REDY");
				job = serv.receive().get(0).split(" ");
				
				current++;
				current = current%LRRServers.size();
			}
			
			// SCHD jobID serverType serverID
			// JOBN submitTime jobID estRuntime core memory disk
			
			serv.send("QUIT");
			serv.receive();
			serv.close();
			
			


		} catch (Exception e) {
			System.out.println("main error");
		}
	}
}

	
