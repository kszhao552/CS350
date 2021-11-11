import java.lang.*;
import java.util.*; 

/***************************************************/
/* CS-350 Fall 2021 - Homework 2 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a simulator where a single source of events   */
/*   is connected to a single exit point, with a   */
/*   single-processor server in the middle.        */
/*                                                 */
/***************************************************/

public class Simulator {

    /* These are the resources that we intend to monitor */
    private LinkedList<EventGenerator> resources = new LinkedList<EventGenerator>();

    /* Timeline of events */
    private Timeline timeline = new Timeline();

    /* Simulation time */    
    private Double now;
    
	private Double twait = new Double(0);
	private Double tresp = new Double(0);
	private Double wlen = new Double(0);
	private Integer servedReqs = new Integer(0);
	
    public void addMonitoredResource (EventGenerator resource) {
	this.resources.add(resource);
    }

    /* This method creates a new monitor in the simulator. To collect
     * all the necessary statistics, we need at least one monitor. */
    private void addMonitor() {
	/* Scan the list of resources to understand the granularity of
	 * time scale to use */
	Double monRate = Double.POSITIVE_INFINITY;

	for (int i = 0; i < resources.size(); ++i) {
	    Double rate = resources.get(i).getRate();
	    if (monRate > rate) {
		monRate = rate;
	    }
	}

	/* If this fails, something is wrong with the way the
	 * resources have been instantiated */
	assert !monRate.equals(Double.POSITIVE_INFINITY);

	/* Create a new monitor for this simulation */
	Monitor monitor = new Monitor(timeline, monRate, resources);

    }
    
    public void simulate (Double simTime) {

	/* Rewind time */
	now = new Double(0);

	/* Add monitor to the system */
	addMonitor();
	
	/* Main simulation loop */
	while(now < simTime) {
	    /* Fetch event from timeline */
	    Event evt = timeline.popEvent();
	    
	    /* Fast-forward time */
	    now = evt.getTimestamp();
	    
	    /* Extract block responsible for this event */
	    EventGenerator block = evt.getSource();

	    /* Handle event */
	    block.processEvent(evt);
	    
	}

	/* Print all the statistics */
	for (int i = 0; i < resources.size(); ++i) {
	    resources.get(i).printUtil(now);
	}
	for (int i = 0; i < resources.size(); ++i) {
	    resources.get(i).printQlen();
	}
	for (int i = 0; i < resources.size(); ++i) {
		tresp += resources.get(i).getTq();
		twait += resources.get(i).getTw();
		servedReqs += resources.get(i).getReqs();

	}
	servedReqs = servedReqs/2;
	System.out.println("TRESP: " + tresp/Request.getUnique());
	System.out.println("TWAIT: " + twait/Request.getUnique());
	System.out.println("RUNS: " + Request.getRuns()/Request.getUnique());
	System.out.println(Request.getRuns());

    }
    
    /* Entry point for the entire simulation  */
    public static void main (String[] args) {

	/* Parse the input parameters */
	double simTime = Double.valueOf(args[0]);
	double lambda = Double.valueOf(args[1]);
	double servTime0 = Double.valueOf(args[2]);
	double servTime1 = Double.valueOf(args[3]);
	double p0 = Double.valueOf(args[4]);
	double p1 = Double.valueOf(args[5]);


	/* Create a new simulator instance */
	Simulator sim = new Simulator();
	
	/* Create the traffic source */
	Source trafficSource = new Source(sim.timeline, lambda);
	    
	/* Create a new traffic sink */
	Sink trafficSink0 = new Sink(sim.timeline);
	trafficSink0.setType(0);
	Sink trafficSink1 = new Sink(sim.timeline);
	trafficSink1.setType(1);
	
	Split trafficSplit0 = new Split(sim.timeline, p0);
	Split trafficSplit1 = new Split(sim.timeline, p1);

	/* Create new single-cpu processing server */
	SimpleServer server0 = new SimpleServer(sim.timeline, servTime0);
	SimpleServer server1 = new SimpleServer(sim.timeline, servTime1);

	/* Establish routing */
	trafficSource.routeTo(server0);
	server0.routeTo(trafficSplit0);
	trafficSplit0.routeTo(trafficSink0);
	trafficSplit0.routeTo2(server1);
	server1.routeTo(trafficSplit1);
	trafficSplit1.routeTo(server0);
	trafficSplit1.routeTo2(trafficSink1);

	/* Add resources to be monitored */
	sim.addMonitoredResource(server0);
	sim.addMonitoredResource(server1);
	
	/* Kick off simulation */
	sim.simulate(simTime);	
    }
    
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
