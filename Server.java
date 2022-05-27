

public class Server{
	
	String type;
	String id;
	String state;
	String startTime;
	String core;
	String mem;
	String disk;
	String waiting;
	String running;
	
	public Server(String[] serv)
	{
		
		
		type = serv[0];
		id = serv[1];
		state = serv[2];
		startTime = serv[3];
		core = serv[4];
		mem = serv[5];
		disk = serv[6];
		waiting = serv[7]; 
		running = serv[8];
		 
	}

}
