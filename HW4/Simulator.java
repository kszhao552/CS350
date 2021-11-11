import java.lang.*;
import java.util.*; 

/***************************************************/
/* CS-350 Fall 2021 - Homework 3 - Code Solution   */
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
	int monitorCounter = 0;
	/* Main simulation loop */
	while(now < simTime) {
	    /* Fetch event from timeline */
	    Event evt = timeline.popEvent();
	    
		if (evt.getType() == EventType.MONITOR){
			monitorCounter++;
		}

	    /* Fast-forward time */
	    now = evt.getTimestamp();
	    
	    /* Extract block responsible for this event */
	    EventGenerator block = evt.getSource();

	    /* Handle event */
	    block.processEvent(evt);
	    
	}

	/* Print all the statistics */
	double q = 0;
	for (int i = 0; i < resources.size(); ++i) {
		q += resources.get(i).returnQ();
	    resources.get(i).printStats(now);
	}
	System.out.println("QTOT: " + q/monitorCounter);
	
    }
    
    /* Entry point for the entire simulation  */
    public static void main (String[] args) {

	/* Parse the input parameters */
	double simTime = Double.valueOf(args[0]);
	double lambda = Double.valueOf(args[1]);
	double ts0 = Double.valueOf(args[2]);
	double ts1 = Double.valueOf(args[3]);
	double ts2 = Double.valueOf(args[4]);
	double t1 = Double.valueOf(args[5]);
	double p1 = Double.valueOf(args[6]);
	double t2 = Double.valueOf(args[7]);
	double p2 = Double.valueOf(args[8]);
	double t3 = Double.valueOf(args[9]);
	double p3 = Double.valueOf(args[10]);
	int k2 = Integer.valueOf(args[11]);
	double p01 =Double.valueOf(args[12]);
	double p02 = Double.valueOf(args[13]);
	double p3out = Double.valueOf(args[14]);
	double p31 = Double.valueOf(args[15]);
	double p32 = Double.valueOf(args[16]);


	
	/* Create a new simulator instance */
	Simulator sim = new Simulator();
	
	/* Create the traffic source */
	Source trafficSource = new Source(sim.timeline, lambda);
	    
	/* Create a new traffic sink */
	Sink trafficSink = new Sink(sim.timeline);

	/* Create new single-cpu processing server */
	SimpleServer server0 = new SimpleServer(sim.timeline, ts0);
	MM2Server server1 = new MM2Server(sim.timeline, ts1);
	MM1KServer server2 = new MM1KServer(sim.timeline, ts2, k2);
	MG1Server server3 = new MG1Server(sim.timeline, t1, p1, t2, p2, t3, p3);


	/* Give some names to identify these servers when printing
	 * trace and statistics */
	server0.setName("S0");
	server1.setName("S1");
	server2.setName("S2");
	server3.setName("S3");
	
	/* Create two routing nodes */
	RoutingNode rn0 = new RoutingNode(sim.timeline);
	RoutingNode rn1 = new RoutingNode(sim.timeline);
	RoutingNode rn2 = new RoutingNode(sim.timeline);
	RoutingNode rn3 = new RoutingNode(sim.timeline);
	

	/* Establish routing */
	trafficSource.routeTo(server0);
	server0.routeTo(rn0);
	rn0.routeTo(server1, p01);
	rn0.routeTo(server2, p02);
	server1.routeTo(rn1);
	rn1.routeTo(server3, 1.0);
	server2.routeTo(rn2);
	rn2.routeTo(server3, 1.0);
	server3.routeTo(rn3);
	rn3.routeTo(server1, p31);
	rn3.routeTo(server2, p32);
	rn3.routeTo(trafficSink, p3out);

	/* Add resources to be monitored */
	sim.addMonitoredResource(server0);
	sim.addMonitoredResource(server1);
	sim.addMonitoredResource(server2);
	sim.addMonitoredResource(server3);
	sim.addMonitoredResource(trafficSink);
	
	/* Kick off simulation */
	sim.simulate(simTime);	
    }
    
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
