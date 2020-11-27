package edu.iastate.cs228.hw1;

public class Empty extends TownCell {

	public Empty (Town p, int r, int c) {
		super(p,r,c);
	}
	@Override
	public State who() {
		// TODO Auto-generated method stub
		return State.EMPTY;
	}

	@Override
	public TownCell next(Town tNew) {
		// TODO Auto-generated method stub
		int nCensus[] = new int[NUM_CELL_TYPE]; 
		census(nCensus);
		nCensus[EMPTY]--;
		if (nCensus[EMPTY] + nCensus[OUTAGE] <= 1) {
				return new Reseller(tNew, row, col);
		}
		else
		{
			return new Casual(tNew, row, col);
		}
		
	}

}
