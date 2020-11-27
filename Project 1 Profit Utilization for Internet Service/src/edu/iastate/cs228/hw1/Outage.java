package edu.iastate.cs228.hw1;

public class Outage extends TownCell {

	public Outage (Town p, int r, int c) {
		super(p,r,c);
	}
	@Override
	public State who() {
		// TODO Auto-generated method stub
		return State.OUTAGE;
	}

	@Override
	public TownCell next(Town tNew) {
		// TODO Auto-generated method stub
		int nCensus[] = new int[NUM_CELL_TYPE]; 
		census(nCensus);
		nCensus[OUTAGE]--;

			return new Empty(tNew, row, col);
		
	}

}
