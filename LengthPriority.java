
import java.util.*;

public class LengthPriority implements Scheduler {
	
	List<Server> servers;
	
	
	public Alg2(Client c){
		List<String> all = c.getServers();
		servers = new ArrayList();
		for (String s : all){
			servers.add(new Server(s.split(" ")));
		}
	}
	
	public void schedule(Job j, Client client)
	{
		
		List<String> servs = client.getServers
		(
			"Avail", j.core, j.mem, j.disk
		);
		
		
		
		
		servs = client.getServers
		(
			"Capable ", j.core, j.mem, j.disk
		);
		
		for (int i = 0; i < capable.size(); i++)
		{
			Server serv = capable.get(i).split(" ");
			if ()	
		}
		
		
		
		
	}
	
	public void jobCompleted(JobComplete jc, Client c)
	{
	
	}
	
	void reshuffle(){
		
	}
}
