package edu.iastate.cs228.hw1;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Raj Singh
 *
 * The ISPBusiness class performs simulation over a grid 
 * plain with cells occupied by different TownCell types.
 *
 */
public class ISPBusiness {
	
	/**
	 * Returns a new Town object with updated grid value for next billing cycle.
	 * @param tOld: old/current Town object.
	 * @return: New town object.
	 */
	public static Town updatePlain(Town tOld) {
		Town tNew = new Town(tOld.getLength(), tOld.getWidth());
		//TODO: Write your code here.
		for (int i = 0; i <= tOld.getWidth() - 1; i++) // iterates through the old plain
		{
			for (int j = 0; j <= tOld.getWidth() - 1; j++)
			{
				 tNew.grid[i][j] = tOld.grid[i][j].next(tNew); 
				// creates the new plain after the 'next' method is employed
			}
		}
		return tNew;
	}
	
	/**
	 * Returns the profit for the current state in the town grid.
	 * @param town
	 * @return
	 */
	public static int getProfit(Town town) {
		//TODO: Write/update your code here.
		int profit = 0;
		for (int i = 0; i <= town.getLength() - 1; i++) 
		{
		
			for (int j = 0; j <= town.getWidth() - 1; j++) 
			{
				if(town.grid[i][j].who().equals(State.CASUAL)) {
					profit++;
				}
			}
		}
		return profit;
	}
	

	/**
	 * Main method. Interact with the user and ask if user wants to specify elements of grid
	 *  via an input file (option: 1) or wants to generate it randomly (option: 2).
	 *  
	 *  Depending on the user choice, create the Town object using respective constructor and
	 *  if user choice is to populate it randomly, then populate the grid here.
	 *  
	 *  Finally: For 12 billing cycle calculate the profit and update town object (for each cycle).
	 *  Print the final profit in terms of %. You should only print the integer part (Just print the 
	 *  integer value. Example if profit is 35.56%, your output should be just: 35).
	 *  
	 * Note that this method does not throws any exception, thus you need to handle all the exceptions
	 * in it.
	 * 
	 * @param args
	 * 
	 */
	public static void main(String []args) {
        //TODO: Write your code here.
        Scanner s = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter 1 to input a file");
        System.out.println("Enter 2 to Randomly generate a grid");
        int option = s.nextInt();  // Read user input
        int length = 0;
        int width = 0;
        int seed = 0;
        Town town;
        if (option == 1) {
            Scanner t2 = new Scanner(System.in);
            System.out.println("Enter filename");
            String filename = t2.nextLine();
            try {
                town = new Town(filename);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                t2.close();
                return;
            }
       

        }
        else if(option == 2) {
            Scanner t = new Scanner(System.in);
            System.out.println("Provide rows, cols and seed integer separated by space: ");
            length = t.nextInt();
            width = t.nextInt();
            seed = t.nextInt();
            town = new Town(length, width);
            town.randomInit(seed);
            t.close();
        }
        else {
            System.out.println("Invalid option selected. Exiting now");
            return;
        }
        
       

      // t.toString();
        int profit = 0;
        for(int i = 1; i <= 12; i++) {
            //System.out.println("Iteration: " + i);
            System.out.print(town.toString());
            System.out.print("\n");

           // UPDATE PROFIT AND PRINT PROFIT
            profit = getProfit(town);
        System.out.println("Profit: " + profit); 
       town =  updatePlain(town);
        }
   		int potential = town.getLength() * town.getWidth();
  		double percent = (profit * 100) / potential;
        System.out.println("Profit %: " + percent); 
	}
	
}


