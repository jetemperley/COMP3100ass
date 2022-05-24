
import java.util.*;

public class Alg1 implements Scheduler {
	
	
	
	public void schedule(Job j, Client client){
		
		List<String> servers = client.getServers("Capable ", j.core, j.mem, j.disk);
		
		int lowest = -1;
		String type = "";
		String id = "";
		for (String s : servers){
			String[] serv = s.split(" ");
			int wait = client.estJobWaitTime(serv[0], serv[1]);
			if (wait < lowest || lowest == -1){
				lowest = wait;
				type = serv[0];
				id = serv[1];
			}
		}
		
		client.schd(j.jobID, type, id);
	}
	
	public void jobCompleted(JobComplete jc, Client c)
	{
		
	}
	
}
