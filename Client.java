import java.io.*;
import java.net.*;
import java.util.*;

public class Client{

	DataOutputStream out;
	BufferedReader in;
	List<String[]> servers;
	Scheduler sched;


	Client(int port){
		try {
			Socket s = new Socket("localhost", port);

			if (s.isConnected()){
				System.out.println("Connected successfully");
			} else {
				System.out.println("Could not connect");
				s.close();
				return;
			}

			out = new DataOutputStream(s.getOutputStream());
			in = new BufferedReader(
					new InputStreamReader(s.getInputStream()));
		}catch (Exception e){
			System.out.println("Setup error");
		}

		send("HELO");
		receive();

		send("AUTH jacob");
		receive();

		send("REDY");
		receive().get(0).split(" ");

		// default scheduler is LLR
		sched = new LRR(this);



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
			// SCHD jobID serverType serverID
			// JOBN submitTime jobID estRuntime core memory disk
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
			send("QUIT");
			receive();
			out.close();
			in.close();
		} catch (Exception e){
			System.out.println("close error");
		}
	}

	void setScheduler(String type){
		if (type.equals("LRR")){
			sched = new LRR(this);
		}
	}

	void scheduleAllJobs(){
		send("REDY");
		String[] job = receive().get(0).split(" ");
		while (!job[0].equals("NONE")){
			sched.schedule(job, this);
			send("REDY");
			job = receive().get(0).split(" ");
		}

		close();
	}

	public static void main (String[] args){


		Client serv = new Client(50000);
		serv.scheduleAllJobs();


	}
}
