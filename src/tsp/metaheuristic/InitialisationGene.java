package tsp.metaheuristic;
import java.util.ArrayList;
import java.util.List;

import tsp.*;

public class InitialisationGene extends TSPSolver{
	
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
		super(instance, timeLimit);
	}
	
	
	public void getSolutionGene() throws Exception {
		int nbVilles = this.getInstance().getNbCities();
	
		/* On initialise la liste des villes déjà visitées */ 
		int[] visite = new int[nbVilles];
		
		/* On initialise les éléments de cette liste */
		visite[0]=0;
		visite[nbVilles-1] = -1;

		
		/* On prend arbitrairement comme ville de départ la ville 0 */
		this.getSolution().setCityPosition(0, 0);
		
		/* i sera la variable des itérations */
		int i = 1;
		
		while(visite[nbVilles-1] == -1) {
			long[] voisins = new long[nbVilles-1];
			voisins = this.getInstance().getDistances()[i];
			voisins[i] = 150000;
			long min = 150000;
			int indice = i;
			for (int j=0;j<nbVilles-1;j++) {
				if (!testInt(j, visite) && voisins[j]<min) {
					min = voisins[j];
					indice = j;
				}
			}
			visite[i]=indice;
			//System.out.println(i);
			this.getSolution().setCityPosition(i, indice);
			i++;
		}
		
		
	}
	
	/* Fonction pour tester si un entier est dans un tableau */
	public static boolean testInt(int element,int[] tableau) {
		boolean in = false;
		int i = 0;
		while (i<tableau.length && in == false) {
			if (tableau[i]==element) {
				in = true; 
			}
			i++;
		}
		return in;
	}
	
}
