import java.util.*; 

/***************************************************/
/* CS-350 Fall 2021 - Homework 3 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a traffic split-point with single-input and   */
/*   multiple outputs.                             */
/*                                                 */
/***************************************************/

class RoutingNode extends EventGenerator {

    private HashMap<EventGenerator, Double> routingTable = new HashMap<EventGenerator, Double>();
    
    public RoutingNode (Timeline timeline) {
	super(timeline);	
    }

    @Override
    public void routeTo(EventGenerator next) {
	routeTo(next, new Double(1));
    }
    
    public void routeTo(EventGenerator next, Double probability) {
	/* Always assume that the same destination does not exist
	 * twice in the routing table */
	assert !routingTable.containsKey(next);

	/* Add destination to routing table */
	routingTable.put(next, probability);

	/* Perform a sanity check that the total probability has not
	 * exceeded 1 */
	Double totalP = new Double(0);

	for(Map.Entry<EventGenerator, Double> entry : routingTable.entrySet()) {
	    totalP += entry.getValue();
	}

	assert totalP <= 1;
    }

    @Override
    void receiveRequest(Event evt) {
	Request curRequest = evt.getRequest();

	/* Find out where to route to with a dice roll */
	Double dice = Math.random();

	/* Identify the destination with CDF calculation */
	Double cumulP = new Double(0);

	EventGenerator nextHop = null;

	for(Map.Entry<EventGenerator, Double> entry : routingTable.entrySet()) {
	    cumulP += entry.getValue();

	    if (dice < cumulP) {
		nextHop = entry.getKey();
		break;
	    }
	}

	/* Print the occurrence of this event */
	if (!nextHop.toString().equals(""))
	    System.out.println(evt.getRequest() + " FROM " + evt.getRequest().getLastServer() + " TO " + nextHop  + ": " + evt.getTimestamp());
	
	assert nextHop != null;

	nextHop.receiveRequest(evt);
    }
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
