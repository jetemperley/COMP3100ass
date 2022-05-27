
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


	Client(int port, String algo){
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
		
		switch (algo){
			case "fc":
				sched = new FC();
				break;
			case "LRR":
				sched = new LRR(this);
				break;
			case "Alg1":
				sched = new Alg1();
				break;
			case "Alg2":
				sched = new LengthPriority(this);
				break;
			case "check":
				sched = new Check(this);
				break;
		}
		

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
	
	public List<String> comm(String s){
		send(s);
		return receive();
	}
	
	public List<Server> getServers(){ 
		 
		return getServers("All", "", "", "");
	}
	
	public List<Server> getServers(String qual, String core, String mem, String disk){
		send("GETS " + qual + " " + core + " " + mem + " " + disk);
		receive();
		List<String> lst =  ok();
		ok();
		if (lst.get(0).equals("."))
			lst.remove(0);
		List<Server> srv = new ArrayList();
		
		for (String s : lst){
			// System.out.println(s);
			srv.add(new Server(s.split(" ")));
		}
		return srv;
	}
	
	public void migj(String jobID, String srcType, String srcID, 
		String tgtType, String tgtID)
	{
		send("MIGJ " + jobID + " " + srcType + " " + srcID + 
			" " + tgtType + " " + tgtID);
		receive();	
	}
	
	public List<String> ok(){
		send("OK");
		return receive();
	}
	
	public List<JobState> listJobs(String srvType, String srvID){
		send("LSTJ " + srvType + " " + srvID);
		receive();
		List<String> lst = ok();
		ok();
		List<JobState> jobs = new ArrayList();
		for (String s : lst){
			jobs.add(new JobState(s.split(" ")));
		}
		return jobs;
	}
	
	public void push(){
		send("PSHJ");
		receive();
		
	}
	
	public int estWait(String srvType, String srvId){
		send("EJWT " + srvType + " " + srvId);
		int wait = Integer.parseInt(receive().get(0));
		return wait;
	}	
	
	public void schd(String jobId, String type, String id) {
		send("SCHD " + jobId + " " + type + " " + id);
		receive();
	}
	
	// state 1 is waiting, 2 is running
	public int nJobs(String type, String id, String state){
		send("CNTJ " + type + " " + id + " " + state);
		int n = Integer.parseInt(receive().get(0));
		return n;
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
		
			// all the cmds we want to handle
			switch(job[0]) {
				
				// job completed
				case "JCPL":
					sched.jobCompleted(new JobComplete(job), this);
				break;
				
				// new job to schedule
				case "JOBN":
					sched.schedule(new Job(job), this);
				break;
				
				case "JOBP":
					sched.schedule(new Job(job), this);
				break;
				
			
			}
			
			send("REDY");
			job = receive().get(0).split(" ");
		}

		close();
	}

	public static void main (String[] args){
		// default arguments
		int pnum = 50000;
		String algo = "fc";
		
		// categorise arguments and overwrite defaults 
		for (int i = 0; i < args.length; i++){
			int type = catArg(args[i]);
			if (type == -1){
				algo = args[i];
			} else {
				pnum = type;
			}
		}
		
		Client serv = new Client(pnum, algo);
		serv.scheduleAllJobs();

	}
	
	static int catArg(String arg){
		try {
			int num = Integer.parseInt(arg);
			if (num < 0){
				System.out.println("Error: port was negative");
				System.out.println("Usage: Client <port number> <algorithm>");
				System.exit(1);
			}
			return num;
		} catch (Exception e){}
		return -1;
	}
}
