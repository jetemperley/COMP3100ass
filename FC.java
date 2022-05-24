
import java.util.*;

public class FC implements Scheduler {
	
	
	
	public void schedule(String[] job, Client client){
		
		client.send("GETS Capable " + job[4] +" " +job[5] +" " +job[6]);
		client.receive();

		client.send("OK");
		List<String> servers = client.receive();
		
		client.send("OK");
		client.receive();
		
		String[] srv = servers.get(0).split(" ");
		int jobID = Integer.parseInt(job[2]);
		
		client.send("SCHD " +
			jobID + " " +
			srv[0] + " " +
			Integer.parseInt(srv[1]));
		client.receive();
		
		
	
	}
	
	public void jobCompleted(String[] job, Client c){}
	
}
