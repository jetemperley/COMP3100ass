
import java.util.*;

public class Alg2 implements Scheduler {
	
	
	
	public void schedule(String[] job, Client client){
		
		List<String> servers = client.getServers("Capable ", job[4], job[5], job[6]);
		
		
		
	}
	
	public void jobCompleted(String[] job, Client c){}
	
}
