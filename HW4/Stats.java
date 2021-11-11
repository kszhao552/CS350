/***************************************************/
/* CS-350 Fall 2021 - Homework 3 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements just a       */
/*   container of statistics. It is meant to be    */
/*   extensible if future simulators require       */
/*   additional metrics to be tracked.             */
/*                                                 */
/***************************************************/

public class Stats {
    public Double arrival;
    public Double serviceStart;
    public Double departure;
    
    public Stats () {
	this.arrival = new Double(0);
	this.serviceStart = new Double(0);
	this.departure = new Double(0);
    }
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
