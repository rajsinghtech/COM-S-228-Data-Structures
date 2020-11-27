package edu.iastate.cs228.hw1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;


/**
 *  @author Raj Singh
 *
 */
public class Town {
	
	private int length, width;  //Row and col (first and second indices)
	public TownCell[][] grid;
	
	/**
	 * Constructor to be used when user wants to generate grid randomly, with the given seed.
	 * This constructor does not populate each cell of the grid (but should assign a 2D array to it).
	 * @param length
	 * @param width
	 */
	public Town(int length, int width) {
		//TODO: Write your code here.
		this.length = length;
		this.width = width;
		grid = new TownCell[length][width];
	}
	
	/**
	 * Constructor to be used when user wants to populate grid based on a file.
	 * Please see that it simple throws FileNotFoundException exception instead of catching it.
	 * Ensure that you close any resources (like file or scanner) which is opened in this function.
	 * @param inputFileName
	 * @throws FileNotFoundException
	 */
	public Town(String inputFileName) throws FileNotFoundException { //CHECKKKK
        //TODO: Write your code here.
        File f = new File(inputFileName);
        Scanner in = new Scanner(f); 
        int length = in.nextInt();
        int width = in.nextInt();
        this.length = length;
        this.width = width;
        grid = new TownCell[length][width];
        in.nextLine();
        
       

        for (int i = 0; i < length; i++) {
            String[] arr = in.nextLine().split(" ");
            for (int j = 0; j < arr.length; j++) {
                switch (arr[j]) {
                case "R":
                    grid[i][j] = new Reseller(this,i,j);
                    break;
                case "E":
                    grid[i][j] = new Empty(this,i,j);
                    break;
                case "C":
                    grid[i][j] = new Casual(this,i,j);
                    break;
                case "O":
                    grid[i][j] = new Outage(this,i,j);
                    break;
                case "S":
                    grid[i][j] = new Streamer(this,i,j);
                    break;
                case "R\t":
                    grid[i][j] = new Reseller(this,i,j);
                    break;
                case "E\t":
                    grid[i][j] = new Empty(this,i,j);
                    break;
                case "C\t":
                    grid[i][j] = new Casual(this,i,j);
                    break;
                case "O\t":
                    grid[i][j] = new Outage(this,i,j);
                    break;
                case "S\t":
                    grid[i][j] = new Streamer(this,i,j);
                    break;
                default:
                    break;
                }
            }
        }
        in.close();
    }
	
	/**
	 * Returns width of the grid.
	 * @return
	 */
	public int getWidth() {
		//TODO: Write/update your code here.
		return width;
	}
	
	/**
	 * Returns length of the grid.
	 * @return
	 */
	public int getLength() {
		//TODO: Write/update your code here.
		return length;
	}

	/**
	 * Initialize the grid by randomly assigning cell with one of the following class object:
	 * Casual, Empty, Outage, Reseller OR Streamer
	 */
	public void randomInit(int seed) { // CHECKKKKKKK
		Random rand = new Random(seed);
		for (int i = 0; i <= length - 1; i++)
		{
			for (int j = 0; j <= width - 1; j++)
			{
				int temp = rand.nextInt(5);
				if (temp == TownCell.RESELLER) // 0
				{
					//System.out.println('R');
					grid[i][j] = new Reseller(this, i, j);
				}
				else if (temp == TownCell.EMPTY) // 1
				{
					//System.out.println('E');

					grid[i][j] = new Empty(this, i, j);
				}
				else if (temp == TownCell.CASUAL) // 2
				{
					//System.out.println('C');

					grid[i][j] = new Casual(this, i, j);
				}
				else if (temp == TownCell.OUTAGE) // 3
				{
					//System.out.println('O');

					grid[i][j] = new Outage(this, i, j);
				}
				else if (temp == TownCell.STREAMER) // 4
				{
					//System.out.println('S');

					grid[i][j] = new Streamer(this, i, j);
				}
			}
		}
	}
	
	/**
	 * Output the town grid. For each square, output the first letter of the cell type.
	 * Each letter should be separated either by a single space or a tab.
	 * And each row should be in a new line. There should not be any extra line between 
	 * the rows.
	 */
	@Override
	public String toString() { // CHECKKKKKK
		String s = "";
		for (int i = 0; i <= getLength() - 1; i++) 
		{
			s = s + "\n";
			for (int j = 0; j <= getWidth() - 1; j++) 
			{
				if (grid[i][j].who().equals(State.CASUAL))
				{
					s = s + "C"  + " ";
				}
				else if (grid[i][j].who().equals(State.EMPTY))
				{
					s = s + "E" + " ";
				}
				else if (grid[i][j].who().equals(State.RESELLER))
				{
					s = s + "R" + " ";
				}
				else if (grid[i][j].who().equals(State.OUTAGE))
				{
					s = s + "O" + " ";
				}
				else if (grid[i][j].who().equals(State.STREAMER))
				{
					s = s + "S" + " ";
				}
			}
		}
		return s;
	}
}
