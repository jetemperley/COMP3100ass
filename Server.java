

public class Server(){
	
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
		type = serv[1];
		id = serv[2];
		state = serv[3];
		startTime = serv[4];
		core = serv[5];
		mem = serv[6];
		disk = serv[7];
		waiting = serv[8]; 
		running = serv[9];
		 
	}

}
