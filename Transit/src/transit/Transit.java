package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {
		int busCounter = busStops.length-1;
		int trainCounter = trainStations.length-1;
		TNode nodeLoc = new TNode();
		TNode nodeBus = new TNode();
		TNode nodeTrain = new TNode();
		
		for (int i = locations.length-1; i >= 0; i--) {
			TNode nextLoc = nodeLoc;
			if (i == locations.length-1) nextLoc = null;
			nodeLoc = new TNode(locations[i], nextLoc, null);
			
			if (busCounter >= 0 && locations[i] == busStops[busCounter]) {
				if (busCounter == busStops.length-1) {
					nodeBus = new TNode(busStops[busCounter], null, nodeLoc);
				}
				else {
					TNode nextBus = nodeBus;
					nodeBus = new TNode(busStops[busCounter], nextBus, nodeLoc);
				}
				busCounter--;
			}
			
			if (trainCounter >= 0 && locations[i] == trainStations[trainCounter]) {
				if (trainCounter == trainStations.length-1) {
					nodeTrain = new TNode(trainStations[trainCounter], null, nodeBus);
				}
				else {
					TNode nextTrain = nodeTrain;
					nodeTrain = new TNode(trainStations[trainCounter], nextTrain, nodeBus);
				}
				trainCounter--;
			}
		}
		TNode nextLoc = nodeLoc;
		nodeLoc = new TNode(0, nextLoc, null);
		TNode nextBus = nodeBus;
		nodeBus = new TNode(0, nextBus, nodeLoc);
		TNode nextTrain = nodeTrain;
		nodeTrain = new TNode(0, nextTrain, nodeBus);
		this.trainZero = nodeTrain;
	}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
	    TNode node = trainZero;
		while (node.getNext().getLocation() != station) {
			node = node.getNext();
			if (node.getNext() == null) break;
		}
		if (node.getNext() != null) {
			node.setNext(node.getNext().getNext());
		}
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
		TNode node = trainZero.getDown();
		TNode walkingLayer = trainZero.getDown().getDown();
		while (node.getNext() != null && node.getNext().getLocation() < busStop) {
			node = node.getNext();
			walkingLayer = walkingLayer.getNext();
		}
		if (node.getNext() != null && node.getNext().getLocation() != busStop) {
			while (walkingLayer.getLocation() != busStop) {
				walkingLayer = walkingLayer.getNext();
			}
			TNode newStop = new TNode(busStop, node.getNext(), walkingLayer);
			node.setNext(newStop);
		}
		else if (node.getNext() == null && node.getLocation() != busStop) {
			while (walkingLayer.getLocation() != busStop) {
				walkingLayer = walkingLayer.getNext();
			}
			TNode newStop = new TNode(busStop, node.getNext(), walkingLayer);
			node.setNext(newStop);
		}
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {
		ArrayList<TNode> path = new ArrayList<TNode>();
		TNode node = trainZero;
		while (node.getLocation() != destination) {
			path.add(node);
			if (node.getNext() != null && node.getNext().getLocation() <= destination) node = node.getNext();
			else node = node.getDown();
		}
		path.add(node);
		while (node.getDown() != null) {
			node = node.getDown();
			path.add(node);
		}
	    return path;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {	
		//create trainStations[]
		int counting = 0;
		TNode ptrNode = trainZero.getNext();
		while (ptrNode.getNext() != null) {
			counting++;
			ptrNode = ptrNode.getNext();
		}
		int[] trainStations = new int[counting+1];
		ptrNode = trainZero.getNext();
		for (int i = 0; i < trainStations.length; i++) {
			trainStations[i] = ptrNode.getLocation();
			ptrNode = ptrNode.getNext();
		}
		//create busStops[]
		counting = 0;
		ptrNode = trainZero.getDown().getNext();
		while (ptrNode.getNext() != null) {
			counting++;
			ptrNode = ptrNode.getNext();
		}
		int[] busStops = new int[counting+1];
		ptrNode = trainZero.getDown().getNext();
		for (int i = 0; i < busStops.length; i++) {
			busStops[i] = ptrNode.getLocation();
			ptrNode = ptrNode.getNext();
		}
		//create locations[]
		counting = 0;
		ptrNode = trainZero.getDown().getDown().getNext();
		while (ptrNode.getNext() != null) {
			counting++;
			ptrNode = ptrNode.getNext();
		}
		int[] locations = new int[counting+1];
		ptrNode = trainZero.getDown().getDown().getNext();
		for (int i = 0; i < locations.length; i++) {
			locations[i] = ptrNode.getLocation();
			ptrNode = ptrNode.getNext();
		}

		//makeList code
		int busCounter = busStops.length-1;
		int trainCounter = trainStations.length-1;
		TNode nodeLoc = new TNode();
		TNode nodeBus = new TNode();
		TNode nodeTrain = new TNode();
		for (int i = locations.length-1; i >= 0; i--) {
			TNode nextLoc = nodeLoc;
			if (i == locations.length-1) nextLoc = null;
			nodeLoc = new TNode(locations[i], nextLoc, null);
			
			if (busCounter >= 0 && locations[i] == busStops[busCounter]) {
				if (busCounter == busStops.length-1) {
					nodeBus = new TNode(busStops[busCounter], null, nodeLoc);
				}
				else {
					TNode nextBus = nodeBus;
					nodeBus = new TNode(busStops[busCounter], nextBus, nodeLoc);
				}
				busCounter--;
			}
			
			if (trainCounter >= 0 && locations[i] == trainStations[trainCounter]) {
				if (trainCounter == trainStations.length-1) {
					nodeTrain = new TNode(trainStations[trainCounter], null, nodeBus);
				}
				else {
					TNode nextTrain = nodeTrain;
					nodeTrain = new TNode(trainStations[trainCounter], nextTrain, nodeBus);
				}
				trainCounter--;
			}
		}
		TNode nextLoc = nodeLoc;
		nodeLoc = new TNode(0, nextLoc, null);
		TNode nextBus = nodeBus;
		nodeBus = new TNode(0, nextBus, nodeLoc);
		TNode nextTrain = nodeTrain;
		nodeTrain = new TNode(0, nextTrain, nodeBus);
		return nodeTrain;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {
		//create scooter nodes
		TNode downPtr = new TNode();
		TNode node = new TNode();
		TNode nextNode = new TNode();
		for (int i = scooterStops.length-1; i >= 0; i--) {
			downPtr = trainZero.getDown().getDown();
			for (int j = 0; j < scooterStops[i]; j++) {
				downPtr = downPtr.getNext();
			}
			if (i != scooterStops.length-1) nextNode = node;
			node = new TNode();
			node.setLocation(scooterStops[i]);
			if (i == scooterStops.length-1) node.setNext(null);
			else node.setNext(nextNode);
			node.setDown(downPtr);			
		}
		nextNode = node;
		node = new TNode();
		node.setLocation(0);
		node.setNext(nextNode);
		node.setDown(trainZero.getDown().getDown());
		
		//set bus stop down to scooter nodes
		downPtr = trainZero.getDown();
		do {
			while (downPtr.getLocation() != node.getLocation()) {
				node = node.getNext();
			}
			downPtr.setDown(node);
			downPtr = downPtr.getNext();
		} while (downPtr.getNext() != null);
	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
