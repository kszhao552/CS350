import java.util.*;

public class Pirate {
    public void findTreasure(String fileName, int N, int timeout){
        	/* Construct the dispatcher with all the necessary parameters */
            Dispatcher theDispatcher = new Dispatcher(fileName, N, timeout) ;

            /* Start the work */
            try{
               theDispatcher.dispatch();
               
            }catch(InterruptedException e){
                System.out.println("Error");
            }
    }


    public static void main(String[] args) throws InterruptedException
    {
	/* Read path of input file */       
        String inputFile = args[0];

	/* Read number of available CPUs */
	int N = Integer.parseInt(args[1]);

	/* If it exists, read in timeout, default to 10 seconds otherwise */
	int timeoutMillis = 100000;
	if (args.length > 2) {
	    timeoutMillis = Integer.parseInt(args[2]);
	}
    Pirate pirate = new Pirate();
    pirate.findTreasure(inputFile, N, timeoutMillis);

    }
}

