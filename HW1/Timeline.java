class Timeline{
    
    public Node timeline;
    public Node last;
    
    public static void main(String[] args){
	//convert the arguments.
	double lambdaA = Double.parseDouble(args[0]);
	double lambdaB = Double.parseDouble(args[1]);
	int T = Integer.parseInt(args[2]);

	//Instantiate a new exp and timeline object to generate times and store them.
	Exp exp = new Exp();
	Timeline events = new Timeline();

	//temporary variable to store the current time for processes.
	double temp = 0;

	//Add initial process at time = 0 for both types.
	events.addToTimeline(new Event('A', 0));
	events.addToTimeline(new Event('B', 0));

	//Generate processes for type A and add them to the timeline until the time reaches T.
	while (temp <=T){
	    temp += exp.getExp(lambdaA);
	    if (temp > T){
		temp = 0;
		break;
	    }
	    events.addToTimeline(new Event('A', temp));
		  
	}
	events.last = events.timeline;
	//Do the same for type B events.
	while (temp <=T){
	    temp += exp.getExp(lambdaB);
	    if (temp >T){
		break;
	    }
	    events.addToTimeline(new Event('B', temp));
	}

	//Counters for how many A and B events have occurred.
	int countA = 0;
	int countB = 0;

	//While events still exist in the timeline, pop them out and print out their type, number and timestamp.
	while (events.timeline != null){
	    Event recent = events.popNext();
	    

	    if (recent.type == 'A'){
		System.out.println(recent.type+""+countA+ ": "+ recent.timestamp);
		countA += 1;
	    }
	    else{
		System.out.println(recent.type+""+countB+ ": "+ recent.timestamp);
		countB += 1;
	    }
	}
    }


    //Node class to store data
    class Node {
	Event event;
	Node next;
       
	public Node(Event event){
	    this.event = event;
	    this.next = null;
	}

    }

    //TODO: Speed up computing time by storing the previous node added and reset back to beginning when A is finished.
    //Current add traverses the entire list from start which is slow.
    
    //Timeline class that acts like a linked list
    public Timeline(){
	this.timeline = null;
	this.last = null;
    }

    public void addToTimeline(Event evtToAdd){
	//Create a node for the event.
	Node node = new Node(evtToAdd);
	
	//If the timeline is empty, set the event as the first event
	if (this.last == null){
	    this.timeline = node;
	    this.last = node;
	}
	//Else traverse the timeline
	else{
	    Node cur = this.last;
	    Node prev = null;
	    while (cur != null){

		//If the current event is later than the current one, add it.
		if (cur.event.timestamp > node.event.timestamp){

		    //If the current node is the first node, set new event as the timeline start.
		    if (prev == null){
			node.next = this.timeline;
			this.timeline = node;
			this.last = node;
			return;
		    }
		    //otherwise, set the node's next to the current and set the previious node's next to current.
		    else{
			node.next = cur;
			prev.next = node;
			this.last = node;
			return;
		    }
		}
		
		//advance the current node by one.
		prev = cur;
	        cur = cur.next;
	    }

	    //If here, then the event is last.
	    prev.next = node;
	    this.last = node;
	}

    }
    public Event popNext(){
	//get the first event and set the timeline to the next one
	Node node = this.timeline;
	this.timeline = this.timeline.next;

	//return the first event
	return node.event;
    }
}
