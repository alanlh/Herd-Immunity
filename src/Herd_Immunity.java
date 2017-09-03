/*
 * Herd Immunity Simulation
 * By Alan Hu
 * 
 * v1.0 7/25
 * v0.8.1 7/24
 * v0.8 7/23
 * 
 * Missing: Data sorting
 * Needs work: Contagion standards
 * Needs work: Efficiency
 */

import java.util.Scanner;

public class Herd_Immunity {
	
	static Scanner scan = new Scanner(System.in);


	public static void main(String[] args) {
		

		System.out.println("If you want to run a thorough simulation with n trials, please type a positive integer containing the number of trials you want.");
		System.out.println("Otherwise, hit enter.");
		
		int numbSims = isPositiveInt();
		
		
		if (numbSims != 0) // Simulating a set of randomly generated populations.
		{			
			simAll(numbSims); 
		}
		else // Simulating a single population according to user's inputs.
		{
			simOnce();
		}

	}
	
	public static void simAll(int numbSims)
	{
		
		Population[] trials = new Population[numbSims];
		
		for (int i = 0; i < numbSims; i ++) // Each loop goes through an entire simulation, rather than performing the steps of the simulations in parallel.
		{
			trials[i] = new Population();
			
			System.out.println("Running simulation number : " + (i+1) + " out of " + numbSims);
			
			double percentImmune = Math.pow(Math.random(),0.25); // Skewed upwards to reflect the fact that in many cases, the percent immunized > 50%.
			double percentSick = Math.pow(Math.random(),3)*(1-percentImmune);	// Skewed downwards because only a small proportion contract sicknesses, with notable exceptions.
			
			trials[i].contagious = (int) Math.floor(Math.random() * 10) + 1; // Even distribution
			trials[i].severity = (int) Math.floor(Math.random() * 10) + 1;
			
			trials[i].generatePopulation(percentSick, percentImmune);
			
			trials[i].simulation();
			
		}
		
		System.out.println("Finished simulations!");
		System.out.print("Compiling results"); // Nothing other than effect.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.print(".");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(".");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < numbSims; i ++) // Outputs basic data.
		{
			System.out.println("Simulation " + (i+1) + ": " + Math.round(trials[i].numberImmune/10000) + " percent started immune. " + Math.round(trials[i].sickList.size()/10000) + " percent started sick.");
			System.out.println("    From the remaining " + Math.round(trials[i].initialOpen/10000) + " percent, " + (100-Math.round(trials[i].numberSickened * 100/trials[i].initialOpen)) + " percent were protected.");
			System.out.println("    Parameters: " + trials[i].severity + " severity, " + trials[i].contagious + " contagious.");

		}
				
		// System.out.println("Would you like to rearrange the data?");
		
		// Unfinished
			
	}
	
	public static void simOnce() // Simulates once according to user's specifications.
	{
		Population trial = new Population(); // create object containing all aspects of population
		
		System.out.println("You are about to simulate the effects of herd immunity in a population with 1,000,000 people.");
		System.out.println("Please enter the percent of people immune to the disease. Your input should be a decimal between 0 and 100 exclusive.");
		double percentImmune = enterPosDouble(0, 100)/100; // Makes the user submit a decimal between 0 and 1.
		
		System.out.println("Please enter the percent of people already infected with the disease.");
		System.out.println("Your input should be between 0 and 100 exclusive, and the sum of this number and the previous should not be greater than 100.");
		double percentSick = enterPosDouble(0, 100-100*percentImmune)/100; // Makes the user submit a decimal between 0 and 100 AND makes sure that percentSick + percentImmune <= 100.

		System.out.println("Now, on a scale of 1 through 10, how contagious is the disease? 10 means the disease spreads very easily.");
		trial.contagious = enterPosInt(1, 10); // code for checking contagiousLevel is an int between 1 and 10. 
		
		System.out.println("Now, on a scale of 1 through 10, how severe is the disease? 10 means the disease lasts a long time.");
		trial.severity = enterPosInt(1, 10); // Code for checking severity is an int between 1 and 10.
		
		System.out.println("Running simulation... Please wait.");
	
		trial.generatePopulation(percentSick, percentImmune);
		
		if (trial.initialOpen == 0) // In the case that everyone is either immunized or sick, to prevent a division by 0 error below.
		{
			System.out.println("Wait a sec... there's no healthy people!");
			System.out.println("In that case... there's not much point in simulating anything, right?");
			return;
		}

		
		trial.simulation();
		
		System.out.println("Simulation complete. Results:");
		System.out.println("The population of 1,000,000 started off with " + trial.sickList.size() + " sick people, " + trial.initialOpen + " healthy but not immune people, and " + trial.numberImmune + " immune people.");
		System.out.println("At the end, " + trial.openList.size() + " people remained healthy, while the number of sick people increased by " + trial.numberSickened + ".");
		System.out.println("Or in other words, having " + trial.numberImmune/10000 +  " percent of people immunized protected " + 100*trial.openList.size()/trial.initialOpen + " percent of the unimmunized population.");

	}
	
	public static int isPositiveInt() // Determines if string is a int greater than 0. If so, returns the int. If not, returns 0. 
	{

		String input = scan.nextLine();

	    if (input.isEmpty() || input == "")
		{
			return 0;	
		}
	    int len = input.length();
	    for (int i = 0; i < len; i ++)
	    {
	    	char c = input.charAt(i);
	    	if (c < '0' || c > '9')
	    	{
	    		return 0;
	    	}
	    }
	    
			int result = Integer.parseInt(input);
			return result;
		
	}

	public static int enterPosInt (int min, int max) // Asks the user for a positive integer between a min and max value.
	{
		
		int x = min;
		while (true)
		{
			while (!scan.hasNextInt()) 
			{
		        System.out.println("That's not an integer! Try again:");
		        scan.next();
		    }
		    x = scan.nextInt();
		    
		    if (x >= min && x <= max)
		    {
		    	return x;
		    }
		    System.out.println("That's not between " + min + " and " + max + "! Try again:");
		}
				
	}

	public static double enterPosDouble ( double min, double max) // Asks the user for a double between a min and max value.
	{

		double x = min;
		while (true) 
		{
		    while (!(scan.hasNextDouble() || scan.hasNextInt())) 
		    {
		        System.out.println("That's not a decimal! Try again:");
		        scan.next();
		    }
		    x = scan.nextDouble();
		    
		    if (x > min && x < max)
		    {
		    	return x;
		    }
		    System.out.println("That's not between " + min + " and " + max + "! Try again:");
		} 
				
	}

}

