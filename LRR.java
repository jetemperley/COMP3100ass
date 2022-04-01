import java.util.*;

class LRR implements Scheduler{

	List<String> servers;
	List<String[]> largest;
	int current = 0;


	LRR(Client client){
		client.send("GETS All");
		client.receive();

		client.send("OK");
		servers = client.receive();

		client.send("OK");
		client.receive();

		// find the largest server type
		int largestSize = -1, index = -1;
		String[] entry = null;
		for (int i = 0; i < servers.size(); i++){
			String[] parts = servers.get(i).split(" ");
			int size = Integer.parseInt(parts[4]);
			if (size > largestSize){
				entry = parts;
				largestSize = size;
				index = i;
			}
		}

		// get a list of the largest servers
		largest = new ArrayList();
		for (int i = index; i < servers.size(); i++){

			String[] parts = servers.get(i).split(" ");
			if (parts[0].equals(entry[0])){
				largest.add(parts);
			}
		}

		if (entry == null){
			System.out.println(
				"there are no servers to assign to");
			System.exit(1);
		}
	}

	public void schedule(String[] job, Client client){

		int jobID = Integer.parseInt(job[2]);
		String[] currServ =	largest.get(current);


		client.send("SCHD " +
			jobID + " " +
			currServ[0] + " " +
			Integer.parseInt(currServ[1]));
		client.receive();

		current++;
		current = current%largest.size();

	}
}
