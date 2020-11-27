package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.jupiter.api.Test;

class ISPBusinessTest {
    Town t;
	
	@Before
	
	@Test
	public void testCensus() 
	{
		Town t = new Town(2,2);
        t.grid[0][0] = new Empty(t,0,0);
        t.grid[0][1] = new Empty(t,0,1);
        t.grid[1][0] = new Casual(t,1,0);
        t.grid[1][1] = new Casual(t,1,1);
        
		assertEquals(2, ISPBusiness.getProfit(t));
	}
}
