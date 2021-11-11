import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.LinkedList;

class Simulator{
    public static double arrival;
    public static double service;
    public static int arr =0 ;
    public static int start = 0;
    public static int done = 0;
    

    
    
    public static void main(String[] args){
	Double length = Double.parseDouble(args[0]);
	arrival = Double.parseDouble(args[1]);
	double service_time = Double.parseDouble(args[2]);

	service = 1/service_time;

	simulate(length);

    }

    public static void simulate(double time){
	Exp generator = new Exp();

	double qlen = 0;
	double wlen = 0;
	double tresp = 0;
	double twait = 0;
	
	double queue_length =0;
	double num_monitor=0;
	
	PriorityQueue<Event> timeline = new PriorityQueue<Event>(5, new Comparator <Event>(){
		public int compare(Event event1, Event event2){
		    if (event1.time > event2.time) return 1;
		    if (event1.time < event2.time) return -1;
		    return 0;
		}
	    });

	LinkedList<Request> queue = new LinkedList<Request>();

	double clock = 0;
	Event e=new Event("Birth", clock+generator.getExp(arrival));

	timeline.add(e);

	e = new Event("Monitor", clock+generator.getExp(arrival));
	timeline.add(e);

	Request r;
	
	while (clock < time){
	    e = timeline.remove();
	    clock = e.time;
	    switch (e.type){
	    case "Birth":
		r = new Request();
		r.arrival_time = clock;
		System.out.println("R" + arr + " ARR: " + r.arrival_time);
		arr+=1;
				   
		queue.add(r);
		if (queue.size() ==1){
		    r.start_time = clock;
		    System.out.println("R" + start + " START: " + r.start_time);
		    start+=1;
		    
		    e = new Event("Death", clock+generator.getExp(service));
		    timeline.add(e);
		}
		e = new Event("Birth", clock+generator.getExp(arrival));
		timeline.add(e);
		
		break;
	    case "Death":
		r = queue.remove();
		r.finish_time=clock;
		System.out.println("R" + done + " DONE: " + r.finish_time);
		done += 1;

		tresp += (r.finish_time-r.arrival_time);
		
		if (queue.size() > 0){
		    r = queue.get(0);
		    r.start_time = clock;
		    System.out.println("R" + start + " START: " + r.start_time);
		    start+=1;
		    twait += (r.start_time-r.arrival_time);
		    e = new Event("Death", clock+generator.getExp(service));
		    timeline.add(e);
		}
		
		break;
	    case "Monitor":
		queue_length += queue.size();
		num_monitor += 1;
		e = new Event("Monitor", clock + generator.getExp(arrival));
		timeline.add(e);
		break;
	    }
	}


	System.out.println("UTIL: " + ((tresp-twait)/time) );
	System.out.println("QLEN: "+ (((queue_length/num_monitor)+ ((tresp-twait)/time))) );
	System.out.println("WLEN: "+ ((queue_length/num_monitor) ));
	System.out.println("TRESP: "+ (tresp/done));
	System.out.println("TWAIT: " + (twait/done ) ); 
    } 
    
}
