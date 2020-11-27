package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.jupiter.api.Test;

class TownCellTest {
    Town t;
	
	@Before
	public void initialize() throws FileNotFoundException
	{
		t = new Town("ISP4x4.txt");
	}
	
	@Test
	public void testCensus() 
	{
		String str = t.grid[1][1].next(t).who().toString();
		assertEquals("", State.EMPTY.toString(), str); 
	}
}
