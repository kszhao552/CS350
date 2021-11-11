
/***************************************************/
/* CS-350 Fall 2021 - Homework 2 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a traffic sink. Any request that arrives at   */
/*   the sink is effectively released from the     */
/*   system.                                       */
/*                                                 */
/***************************************************/

class Sink extends EventGenerator {
    private Integer type;

    public Sink(Timeline timeline) {
	super(timeline);
    }
    
    @Override
    void receiveRequest(Event evt) {
	super.receiveRequest(evt);

	/* Print the occurrence of this event */
	System.out.println(evt.getRequest() + " DONE " + this.type + ": "+ evt.getTimestamp());	

    }

    void setType(Integer t){
        this.type = t;
    }
        
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
