
import java.util.*;

public class Alg1 implements Scheduler {
	
	
	
	public void schedule(String[] job, Client client){
		
		List<String> servers = client.getServers("Capable ", job[4], job[5], job[6]);
		
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
		
		client.schd(job[2], type, id);
	}
	
	public void jobCompleted(String[] job, Client c){}
	
}
