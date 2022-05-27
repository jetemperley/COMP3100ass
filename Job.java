

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

class JobState {
	
	String id;
	String state;
	String subTime;
	String startTime;
	String estRun;
	String core;
	String mem;
	String disk;
	
	public JobState(String[] str){
		
		
		id = str[0];
		state = str[1];
		subTime = str[2];
		startTime = str[3];
		estRun = str[4];
		core = str[5];
		mem = str[6];
		disk = str[7];
	}

}
