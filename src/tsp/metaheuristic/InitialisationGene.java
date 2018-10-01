package tsp.metaheuristic;
import java.util.ArrayList;
import java.util.List;

import tsp.*;

public class InitialisationGene {
	
	/**
	 * The Solution that will be returned by the program.
	 */
	private Solution m_solution;

	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;
	
	/** Initalise l'instance */
	public InitialisationGene(Instance instance, long timeLimit) {
		m_instance = instance;
		m_solution = new Solution(m_instance);
		m_timeLimit = timeLimit;
	}
	
	
	public void getSolution() {
		
	}
	
}
