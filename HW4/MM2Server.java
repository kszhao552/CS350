import java.util.*; 

/***************************************************/
/* CS-350 Fall 2021 - Homework 3 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a single-processor server with an infinite    */
/*   request queue and exponentially distributed   */
/*   service times, i.e. a x/M/1 server.           */
/*                                                 */
/***************************************************/

class MM2Server extends EventGenerator {

    private LinkedList<Request> theQueue = new LinkedList<Request>();
    private Double servTime;
    private String name = null;
    
    /* Statistics of this server --- to construct rolling averages */
    private Double cumulQ = new Double(0);
    private Double cumulW = new Double(0);
    private Double cumulTq = new Double(0);
    private Double cumulTw = new Double(0);
    private Double busyTime1 = new Double(0);
    private Double busyTime2 = new Double(0);
    private int snapCount = 0;
    private int servedReqs = 0;
    
	Request r1 = null;
	Request r2 = null;

    public MM2Server (Timeline timeline, Double servTime) {
	super(timeline);

	/* Initialize the average service time of this server */
	this.servTime = servTime;
    }

    /* Given a name to this server that will be used for the trace and
     * statistics */
    public void setName (String name) {
	this.name = name;
    }
    
    /* Internal method to be used to simulate the beginning of service
     * for a queued/arrived request. */
    private void __startService(Event evt, Request curRequest, int hold) {
	    Event nextEvent = new Event(EventType.DEATH, curRequest,
					evt.getTimestamp() + Exp.getExp(1/this.servTime), this);
		
	    curRequest.recordServiceStart(evt.getTimestamp());
	    cumulTw += curRequest.getServiceStart() - curRequest.getArrival();
	    
		if (hold == 2){
			r2 = curRequest;
		}else{
			r1 = curRequest;
		}

	    /* Print the occurrence of this event */
	    System.out.println(curRequest + " START" +
			       (this.name != null ? " " + this.name +"," + hold: "")  +
			       ": " + evt.getTimestamp());
	    
	    super.timeline.addEvent(nextEvent);	    	
    }
    
    @Override
    void receiveRequest(Event evt) {
	super.receiveRequest(evt);

	Request curRequest = evt.getRequest();

	curRequest.recordArrival(evt.getTimestamp());
	
	/* Upon receiving the request, check the queue size and act
	 * accordingly */
	if(r1 == null && r2 == null) {
	    __startService(evt, curRequest, Math.random()<.5 ? 1: 2);
	}
	else if (r1 == null){
		__startService(evt, curRequest, 1);

	}else if(r2 == null){
		__startService(evt, curRequest, 2);
	}
	else{
		theQueue.add(curRequest);
	}	
    }

    @Override
    void releaseRequest(Event evt) {
	/* What request we are talking about? */
	Request curRequest = evt.getRequest();
	int temp = 1;
	if (r2 != null && curRequest.id == r2.id){
		temp = 2;
	}

		System.out.println(evt.getRequest() + " DONE "
		+ this.name + "," + temp
		+ ": " + evt.getTimestamp());	

	/* If the following is not true, something is wrong */
	//assert curRequest == queueHead;

	curRequest.recordDeparture(evt.getTimestamp());
	
	/* Update busyTime */
	if (temp ==1){
		r1 = null;
		busyTime1 += curRequest.getDeparture() - curRequest.getServiceStart();
	}else{
		r2=null;
		busyTime2 += curRequest.getDeparture() - curRequest.getServiceStart();
	}
	/* Update cumulative response time at this server */
	cumulTq += curRequest.getDeparture() - curRequest.getArrival();
	
	/* Update number of served requests */
	servedReqs++;
	
	assert super.next != null;
	super.next.receiveRequest(evt);
	
		
	/* Any new request to put into service?  */
	if(!theQueue.isEmpty()) {
	    Request nextRequest = theQueue.removeFirst();

	    if(r1 == null && r2 == null) {
			__startService(evt, nextRequest, Math.random()<.5 ? 1: 2);
		}
		else if (r1 == null){
			__startService(evt, nextRequest, 1);
	
		}else if(r2 == null){
			__startService(evt, nextRequest, 2);
		}		    
	}
	
    }

    @Override
    Double getRate() {
	return 1/this.servTime;
    }

    @Override
    void executeSnapshot() {
	snapCount++;
	int util =0;
	if (r1!= null){
		util +=1;
	}
	if (r2!= null){
		util += 1;
	}
	cumulQ += theQueue.size() + util;
	cumulW += Math.max(theQueue.size()-1, 0);
    }

    @Override
    void printStats(Double time) {
	if (this.name == null) {
	    // System.out.println("UTIL: " + busyTime/time);
	    System.out.println("QLEN: " + cumulQ/snapCount);
	    System.out.println("TRESP: " + cumulTq/servedReqs);
	} else {
		System.out.println();
		System.out.println(this.name + ",1 UTIL: " + busyTime1/time);
	    System.out.println(this.name + ",2 UTIL: " + busyTime2/time);
	    System.out.println(this.name + " QLEN: " + cumulQ/snapCount);
		System.out.println(this.name + " TRESP: " + cumulTq/servedReqs);

	}
    }

    
    @Override
    public String toString() {
	return (this.name != null ? this.name : "");
    }

	@Override
	public double returnQ(){
        return cumulQ;
    }
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
