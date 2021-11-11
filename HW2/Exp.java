import java.lang.Math;

class Exp{
    public static void main(String[] args){
	//convert the arguments
	double lambda = Double.parseDouble(args[0]);
	int n = Integer.parseInt(args[1]);

	//Instatiate a new Exp instance
	Exp exp = new Exp();

	//Generate a random variable in the exponential distribution n times and print out the result.
	for (int i = 0; i<n; i++){
		double result = exp.getExp(lambda);
		System.out.println(result);
	}
    }
    
    public double getExp(double lambda){
	//Use the quantile function to generate a random variable that fits into the exponential function.
	//Q_X(p)= -(ln(1-p))/labda
	double p = Math.random();
	double num = -(Math.log(1-p));
	return (num/lambda);
    }
    

}
