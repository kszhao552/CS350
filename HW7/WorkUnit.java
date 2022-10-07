/***************************************************/
/* CS-350 Fall 2021 - Homework 6 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements a descriptor */
/*   for a unit of work that needs to be processed */
/*   by the generic worker thread. The class also  */
/*   incorporates a field to save the result of    */
/*   the performed computation.                    */
/*                                                 */
/***************************************************/

public class WorkUnit {

    String hash;
    String result;
    int lower = -1;
    int higher = Integer.MAX_VALUE;

    /* Simple constructor to set the input hash */
    public WorkUnit (String hash) {
	this.hash = hash;
	this.result = null;
    }

    public WorkUnit (String hash, int lower, int higher){
        this.hash = hash;
        this.result = null;
        this.lower = lower;
        this.higher = higher;
    }

    public String getHash() {
	return hash;
    }

    /* These can be handy to generalize the boundaries of hash
     * cracking */
    public int getLowerBound() {
	return this.lower;
    }

    public int getUpperBound() {
	return this.higher;
    }

    public void setResult(String result) {
	this.result = result;
    }

    public String getResult() {
	return result;
    }

    /* Render this WorkUnit when printed */
    @Override
    public String toString() {
	if (this.result != null)
	    return this.result;
	else
	    return this.hash;
    }
    
    public boolean isInteger() {
    if (this.result != null)
	    return true;
	else
	    return false;
    }

}
