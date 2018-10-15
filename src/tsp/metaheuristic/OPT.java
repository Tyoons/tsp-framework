package tsp.metaheuristic;
import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.TSPSolver;


public class OPT extends TSPSolver {
	/**
	 * The Solution that will be returned by the program.
	 */
	private Solution m_solution;

	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;
	
	/** Initalise l'instance */
	public OPT(Instance instance, long timeLimit) {
		super(instance, timeLimit);
	}
}
