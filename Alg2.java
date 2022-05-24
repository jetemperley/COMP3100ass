
import java.util.*;

public class Alg2 implements Scheduler {
	
	List<String> all;
	
	public Alg2(Client c){
		all = c.getServers();
	}
	
	public void schedule(Job j, Client client)
	{
		
		List<String> servers = client.getServers("Capable ", j.core, j.mem, j.disk);
		
		
		
	}
	
	public void jobCompleted(JobComplete jc, Client c)
	{
	
	}
	
	void reshuffle(){
		
	}
}
