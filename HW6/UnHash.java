import java.security.*;
import java.util.Timer;
/***************************************************/
/* CS-350 Fall 2021 - Homework 5 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a simple MD5 hash cracker. It takes in input  */
/*   an MD5 hash hexadecimal ASCII representation  */
/*   and uses a brute-force search to reverse the  */
/*   hash.                                         */
/*                                                 */
/***************************************************/

public class UnHash extends Thread{
    volatile private String hash = null;
    private int limit = 0;
    private long start;
    public boolean kill = false;
    public void run(){
        try{
            while (!kill || hash != null){

                if (hash != null){
                    unhash(hash);
                }

            }
        }
        catch (Exception e){

        }
    }
    public int unhash (String to_unhash) throws NoSuchAlgorithmException
    {
    	/* Construct a simple hasher class */
        Hash hasher = new Hash();
        /* Loop forever until a match is found */
        for(int cur = 0; ; ++cur) {
            if (limit!=0 && (System.nanoTime()-start)/1e6 > limit){
                System.out.println(hash);
                this.receiveHash(null);
                return cur;
            }
            String tmpHash = hasher.hash(cur);
            
            /* Does the current hash matches the target hash? */

            if(tmpHash.equals(to_unhash)){
            	 /* Found it! Return right away. */
                 this.receiveHash(null);

                 System.out.println(cur);
                 return cur;
            }
        }
    }

    public void receiveHash(String hash){
        this.hash = hash;
        this.start = System.nanoTime();
    }

    public boolean isIdle(){
        return (hash == null);
    }

    public void kill(){
        kill = true;
    }

    public void setLimit(int limit){
        this.limit = limit;
    }
    /* Class test procedure. Take a MD5 hash via system paramters and reverse it. */
    public static void main(String[] args) throws NoSuchAlgorithmException 
    {
        String to_unhash = args[0];
        
        /* Construct dehasher */
        UnHash dehasher = new UnHash();
        
        /* Reverse hash and print result in one go */
        System.out.println(dehasher.unhash(to_unhash));
    }
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
