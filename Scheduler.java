/*
	Author: Jacob Temperley
	Student number: 44816936
	Email: jacob.temperley@students.mq.edu.au

	This is the interface for the schedular algorithm, which 
	has a single method that takes a job and schedules it via 
	the client

*/

public interface Scheduler {
	// the scheduler takes a job and sends the scheduling decision
	// the job is a string array split by spaces
	public void schedule(String[] job, Client c);
	
}
