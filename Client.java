
/*
	Author: Jacob Temperley
	Student number: 44816936
	Email: jacob.temperley@students.mq.edu.au

	This class is designed to communicate with the 
	server simulate ds-server.exe and schedule jobs
	based on the supplied scheduler

*/

import java.io.*;
import java.net.*;
import java.util.*;

public class Client{

	private DataOutputStream out;
	private BufferedReader in;
	private Scheduler sched;
	private String[] job;


	Client(int port){
		Socket s = null;
		try {
			s = new Socket("localhost", port);

			if (!s.isConnected()){
				System.out.println("Could not connect");
				s.close();
				System.exit(1);
			}
			
			// create communication streams
			out = new DataOutputStream(s.getOutputStream());
			in = new BufferedReader(
					new InputStreamReader(s.getInputStream()));
		}catch (Exception e){
			System.out.println("Setup error");
			System.exit(1);
		}

		// do handshake with server
		send("HELO");
		receive();

		send("AUTH jacob");
		receive();

		send("REDY");
		job = receive().get(0).split(" ");

		// default scheduler is LLR
		sched = new LRR(this);

	}

	// send the message msg to the socket
	public void send(String msg){
		try {
			out.flush();
			msg = msg + "\n";
			out.write(msg.getBytes());
			out.flush();
		} catch (Exception e){
			System.out.println("send error");
		}
	}

	// listen for a message from the socket
	// blocks while waiting
	public List<String> receive(){
		List<String> lines = new ArrayList();
		try {

			String str = (String) in.readLine();
			if (str.split(" ")[0].equals("JCPL")){
				// if the message is a job completion 
				// then discard it and wait for a new message
				// TODO: refactor this in stage 2 for the scheduler to handle 
				send("REDY");
				return receive();

			}
			lines.add(str);

			// on a GETS for example, there may be many lines to read
			while (in.ready()){
				str = in.readLine();
				if (str.split(" ")[0].equals("JCPL")){
					continue;
				}
				lines.add(str);
			}
		} catch (Exception e){
			System.out.println("receiver error");
			e.printStackTrace();
		}
		return lines;
	}
	
	// close the connection
	public void close(){
		try {
			send("QUIT");
			receive();
			out.close();
			in.close();
		} catch (Exception e){
			System.out.println("close error");
		}
	}

	// chang the scheduler
	// TODO: stage 2, pass a scheduler in maybe
	void setScheduler(String type){
		if (type.equals("LRR")){
			sched = new LRR(this);
		}
	}

	// loop through the process of 
	// get a new job, shedule, repeat
	void scheduleAllJobs(){
		
		while (!job[0].equals("NONE")){
			sched.schedule(job, this);
			send("REDY");
			job = receive().get(0).split(" ");
		}

		close();
	}

	public static void main (String[] args){
		if (args.length == 0){
			System.out.println("Usage: Client <port number>");
			System.exit(1);
		}
		Client serv = new Client(Integer.parseInt(args[0]));
		serv.scheduleAllJobs();


	}
}
