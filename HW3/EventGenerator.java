
/***************************************************/
/* CS-350 Fall 2021 - Homework 2 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a generic simulator that produces events.     */
/*   depending on the object extendind this class  */
/*   one might define a source, sink, or server.   */
/*                                                 */
/***************************************************/

class EventGenerator {

    protected Timeline timeline;
    protected EventGenerator next;
    protected EventGenerator next2;

    public EventGenerator(Timeline timeline) {
	this.timeline = timeline;
    }
    
    /* Function that is used to signify the arrival of a new request
     * to the event generator */
    void receiveRequest(Event evt) {
	Request req = evt.getRequest();
	req.moveTo(this);	    
    }

    /* Function that is used to signify the departure of a new request
     * from the event generator */
    void releaseRequest(Event evt) {}

    /* Function to set the next hop for requests departing from this
     * event generetor block */
    void routeTo(EventGenerator next) {
	this.next = next;
    }

    void routeTo2(EventGenerator next){
        this.next2 = next;
    }

    /* Connect EventGenerator to corresponding timeline if not done by
     * the constructur */
    void setTimeline(Timeline timeline) {
	this.timeline = timeline;
    }

    /* Generic event processing logic for a typical event generator
     * block --- a monitor will have to override this method. */
    void processEvent(Event evt) {
	if (evt.getType() == EventType.BIRTH) {
	    receiveRequest(evt);
	} else if (evt.getType() == EventType.DEATH) {
	    releaseRequest(evt);
	}
    }

    /* When no rate is set, default to some value */
    Double getRate() {
	return Double.POSITIVE_INFINITY;
    }

    /* Sub-class will have to implement this if needed */
    void executeSnapshot() {
	/* Do nothing */
    }

    /* Sub-class will have to implement this if needed */
    void printStats(Double time) {
	/* Do nothing */
    }

    void printUtil(double time){
    /* Do nothing */
    }

    void printQlen(){
        /* Do nothing */
    }

    Double getW(){
        return 0.0;
    }
    Double getTw(){
        return 0.0;
    }
    Double getTq(){
        return 0.0;
    }
    Integer getReqs(){
        return 0;
    }
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
