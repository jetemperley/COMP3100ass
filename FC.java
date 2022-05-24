
import java.util.*;

public class FC implements Scheduler {
	
	
	
	public void schedule(Job job, Client client){
		
		client.send("GETS Capable " + job.core +" " + job.mem +" " + job.disk);
		client.receive();

		client.send("OK");
		List<String> servers = client.receive();
		
		client.send("OK");
		client.receive();
		
		String[] srv = servers.get(0).split(" ");
		
		client.send("SCHD " +
			job.jobID + " " +
			srv[0] + " " +
			Integer.parseInt(srv[1]));
		client.receive();
		
		
	
	}
	
	public void jobCompleted(JobComplete job, Client c){}
	
}
