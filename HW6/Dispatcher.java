import java.io.*;
import java.security.*;
import java.util.*;
import java.nio.file.*;

/***************************************************/
/* CS-350 Fall 2021 - Homework 5 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a linear, serializer dispatcher for a list of */
/*   input MD5 hashes provied in input via a file  */
/*   path.                                         */
/*                                                 */
/***************************************************/

public class Dispatcher {

    public void dispatch (String fileName, int n, int limit) throws NoSuchAlgorithmException
    {
        /* The fileName parameter contains the full path to the input file */
        Path inputFile = Paths.get(fileName);
        UnHash[] dehasher = new UnHash[n];
        for (int i = 0; i< dehasher.length;i++){
            dehasher[i] = new UnHash();
            dehasher[i].start();
            dehasher[i].setLimit(limit);
        }

        LinkedList<String> queue = new LinkedList<String>();

	/* Attempt to open the input file, if it exists */
        if (Files.exists(inputFile)) {

	    /* It appears to exists, so open file */
            File fHandle = inputFile.toFile();
            
            /* Use a buffered reader to be a bit faster on the I/O */
            try (BufferedReader in = new BufferedReader(new FileReader(fHandle)))
            {

                String line;
		/* Pass each line read in input to the dehasher */                
                while((line = in.readLine()) != null){
	            queue.add(line);
                }
                while (!queue.isEmpty()){
                    for (int i=0; i<dehasher.length; i++){
                        if (dehasher[i].isIdle()){
                            dehasher[i].receiveHash(queue.remove());
                        }
                        if (queue.isEmpty()){
                            break;
                        }
                    }
                }
                for (int i=0; i<dehasher.length; i++){
                    dehasher[i].kill();
                } 
            } catch (FileNotFoundException e) {
                System.err.println("Input file does not exist.");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Unable to open input file for read operation.");
                e.printStackTrace();
            }

        } else {
            System.err.println("Input file does not exist. Exiting.");        	
        }

    }

    /* Entry point of the code */
    public static void main(String[] args) throws NoSuchAlgorithmException
    {
        String inputFile = args[0];
        int n = Integer.parseInt(args[1]);
        int limit = 0;
        if (args.length == 3){
            limit = Integer.parseInt(args[2]);
        }
        Dispatcher theDispatcher = new Dispatcher();
        theDispatcher.dispatch(inputFile, n, limit);
    }
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
