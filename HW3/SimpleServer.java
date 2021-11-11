import java.util.*; 

/***************************************************/
/* CS-350 Fall 2021 - Homework 2 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a single-processor server with an infinite    */
/*   request queue and exponentially distributed   */
/*   service times, i.e. a x/M/1 server.           */
/*                                                 */
/***************************************************/

class SimpleServer extends EventGenerator {

    private LinkedList<Request> theQueue = new LinkedList<Request>();
    private Double servTime;

	private Integer id;
    private static Integer _id = new Integer(0);

    /* Statistics of this server --- to construct rolling averages */
    private Double cumulQ = new Double(0);
    private Double cumulW = new Double(0);
    private Double cumulTq = new Double(0);
    private Double cumulTw = new Double(0);
    private Double busyTime = new Double(0);
    private int snapCount = 0;
    private int servedReqs = 0;
    
    public SimpleServer (Timeline timeline, Double servTime) {
	super(timeline);
	/* Initialize the average service time of this server */
	this.servTime = servTime;
	this.id = _id;
	_id++;
    }

    /* Internal method to be used to simulate the beginning of service
     * for a queued/arrived request. */
    private void __startService(Event evt, Request curRequest) {
	    Event nextEvent = new Event(EventType.DEATH, curRequest,
					evt.getTimestamp() + Exp.getExp(1/this.servTime), this);

	    curRequest.recordServiceStart(evt.getTimestamp());
		curRequest.setTreatment(true);
	    cumulTw += curRequest.getServiceStart() - curRequest.getArrival();
	    
	    /* Print the occurrence of this event */
	    System.out.println(curRequest + " START " + this.id + ": " + evt.getTimestamp());
	    
	    super.timeline.addEvent(nextEvent);	    	
    }
    
    @Override
    void receiveRequest(Event evt) {
	super.receiveRequest(evt);

	Request curRequest = evt.getRequest();

	curRequest.recordArrival(evt.getTimestamp());
	
	Request.updateRuns();

	if (curRequest.getTreatment()){
		System.out.println(curRequest + " NEXT " + this.id + ": " + evt.getTimestamp());
	}

	/* Upon receiving the request, check the queue size and act
	 * accordingly */
	if(theQueue.isEmpty()) {
	    __startService(evt, curRequest);
	}
	    
	theQueue.add(curRequest);	
    }

    @Override
    void releaseRequest(Event evt) {
	/* What request we are talking about? */
	Request curRequest = evt.getRequest();

	/* Remove the request from the server queue */
	Request queueHead = theQueue.removeFirst();

	/* If the following is not true, something is wrong */
	assert curRequest == queueHead;

	curRequest.recordDeparture(evt.getTimestamp());
	
	/* Update busyTime */
	busyTime += curRequest.getDeparture() - curRequest.getServiceStart();

	/* Update cumulative response time at this server */
	cumulTq += curRequest.getDeparture() - curRequest.getArrival();
	
	/* Update number of served requests */
	servedReqs++;
	
	assert super.next != null;
	super.next.receiveRequest(evt);
	
	/* Any new request to put into service?  */
	if(!theQueue.isEmpty()) {
	    Request nextRequest = theQueue.peekFirst();

	    __startService(evt, nextRequest);	    
	}
	
    }

    @Override
    Double getRate() {
	return 1/this.servTime;
    }

	@Override
	Double getTq(){
		return cumulTq;
	}
	@Override
	Double getTw(){
		return cumulTw;
	}
	@Override
	Double getW(){
		return cumulW;
	}

	@Override
	Integer getReqs(){
		return servedReqs;
	}

    @Override
    void executeSnapshot() {
	snapCount++;
	cumulQ += theQueue.size();
	cumulW += Math.max(theQueue.size()-1, 0);
    }

    @Override
    void printStats(Double time) {
	System.out.println("TRESP: " + cumulTq/servedReqs);
	System.out.println("TWAIT: " + cumulTw/servedReqs);
	System.out.println("WLEN: " + cumulW/snapCount);
    }
    
	@Override
	void printUtil(double time){
		System.out.println("UTIL " + this.id +  ": " + busyTime/time);
	}

	@Override
	void printQlen(){
		System.out.println("QLEN " +this.id + ": " + cumulQ/snapCount);
	}

}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
