
import java.util.*;

public class LengthPriority implements Scheduler {
	
	List<String> all;
	
	public Alg2(Client c){
		all = c.getServers();
	}
	
	public void schedule(Job j, Client client)
	{
		
		List<String> avail = client.getServers(
			"Avail", j.core, j.mem, j.disk
		);
		
		List<String> capable = client.getServers
		(
			"Capable ", j.core, j.mem, j.disk
		);
		
		for (int i = 0; i < capable.size(); i++)
		{
			String[] serv
			if ()	
		}
		
		
		
		
	}
	
	public void jobCompleted(JobComplete jc, Client c)
	{
	
	}
	
	void reshuffle(){
		
	}
}
