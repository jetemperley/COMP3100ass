

public class Job {
	
	String subTime;
	String jobID;
	String estRuntime; 
	String core;
	String mem; 
	String disk;
	
	public Job(String[] job){
		subTime = job[1];
		jobID = job[2];
		estRuntime = job[3];
		core = job[4];
		mem = job[5];
		disk = job[6];
	}

}

class JobComplete {


	String finTime;
	String jobID;
	String servType;
	String servID;
	
	JobComplete(String[] jcpl){
		finTime = jcpl[1];
		jobID = jcpl[2];
		servType = jcpl[3];
		servID = jcpl [4];
	}
}
