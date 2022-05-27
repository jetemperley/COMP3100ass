
import java.util.*;

public class LengthPriority implements Scheduler {
	
	List<Server> servers;
	
	
	public LengthPriority(Client c){
		servers = c.getServers();
	}
	
	public void schedule(Job j, Client client)
	{
		
		List<Server> avail = client.getServers(
			"Avail", j.core, j.mem, j.disk
		);
		
		
		if (avail.size() > 0) {
		// if there is an obvious fit, just use that one
			Server s = avail.get(0);
			client.schd(j.jobID, s.type, s.id);
		} else {
			reshuffle(j, client);
			List<Server> servs = client.getServers("Capable", j.core, j.mem, j.disk);
			Server shortServ = null;
			int shortest = -1;
			for (Server s : servs){
				int wait = client.estWait(s.type, s.id);
				if (shortest == -1 || wait < shortest){
					shortest = wait;
					shortServ = s;
				}
			}
			client.schd(j.jobID, shortServ.type, shortServ.id);
		}
		
	}
	
	public void jobCompleted(JobComplete jc, Client c)
	{
	
	}
	
	void reshuffle(Job j, Client client){
		List<Server> servs = client.getServers("Capable", j.core, j.mem, j.disk);
		Server longServ = null;
		int longest = -1;
		for (Server s : servs){
			int wait = client.estWait(s.type, s.id);
			if (longest == -1 || wait > longest){
				longest = wait;
				longServ = s;
			}
		}
		List<JobState> jobs = client.listJobs(longServ.type, longServ.id);
		for (JobState js : jobs){
			resched(js, longServ.type, longServ.id, client);
		}
	}
	void resched(JobState js, String type, String id, Client c){
		List<Server> servs = c.getServers("Capable", js.core, js.mem, js.disk);
		Server shortServ = null;
		int shortest = -1;
		for (Server s : servs){
			int wait = c.estWait(s.type, s.id);
			if (shortest == -1 || wait < shortest){
				shortest = wait;
				shortServ = s;
			}
		}
		c.migj(js.id, type, id, shortServ.type, shortServ.id);
	}
	
	
}
