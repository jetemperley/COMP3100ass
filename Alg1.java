
import java.util.*;

public class Alg1 implements Scheduler {
	
	
	
	public void schedule(Job j, Client client){
		
		List<Server> servers = client.getServers("Capable ", j.core, j.mem, j.disk);
		
		int lowest = -1;
		String type = "";
		String id = "";
		for (Server s : servers){
			
			int wait = client.estWait(s.type, s.id);
			if (wait < lowest || lowest == -1){
				lowest = wait;
				type = s.type;
				id = s.id;
			}
		}
		
		client.schd(j.jobID, type, id);
	}
	
	public void jobCompleted(JobComplete jc, Client c)
	{
		
	}
	
}
