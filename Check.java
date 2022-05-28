
import java.util.*;

public class Check implements Scheduler{
	
	List<Server> all;
	
	public Check(Client c){
		all = c.getServers();
	}
	
	public void schedule(Job j, Client client)
	{
		
		List<Server> avail = client.getServers("Avail", j.core, j.mem, j.disk);
		
		if (avail.size() >0){
			Server s = avail.get(0);
			client.schd(j.jobID, s.type, s.id);
		} else {
			List<Server> cape = client.getServers("Capable", j.core, j.mem, j.disk);
			Server s = cape.get(0);
			client.schd(j.jobID, s.type, s.id);
		}
			
		
	}
	
	public void jobCompleted(JobComplete jc, Client c)
	{
		for (Server s : all){
			if (c.nJobs(s.type, s.id, "1") > 1) {
				List<JobState> jobs =  c.listJobs(s.type, s.id);
				for (JobState job : jobs) {
					if (job.state.equals("1")) {
						List<Server> a = c.getServers("Avail", job.core, job.mem, job.disk);
						if (a.size() > 0){
							Server t = a.get(0);
							c.migrate(job.id, s.type, s.id, t.type, t.id);
						}
						
					}
				}
			}
		}
	}

}
