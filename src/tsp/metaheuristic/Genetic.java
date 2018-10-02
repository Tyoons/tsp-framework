package tsp.metaheuristic;
import java.util.ArrayList;
import java.util.List;

import tsp.*;

public class Genetic extends TSPSolver{
	
	/**
	 * The Solution that will be returned by the program.
	 */
	private Solution m_solution;

	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;
	
	/** Initalise l'instance */
	public Genetic(Instance instance, long timeLimit) {
		super(instance, timeLimit);
	}
	
	
	public void getSolutionGene() throws Exception {
		int nbVilles = this.getInstance().getNbCities();
	
		/* On initialise la liste des villes deja  visitees */ 
		int[] visite = new int[nbVilles];
		
		/* On initialise les elements de cette liste */
		visite[0]=0;
		visite[nbVilles-1] = -1;

		
		/* On prend arbitrairement comme ville de dÃ©part la ville 0 */
		this.getSolution().setCityPosition(0, 0);
		
		/* i sera la variable des iterations */
		int i = 1;
		
		/* On boucle tant que toutes les villes n'ont pas été visitée*/
		while(visite[nbVilles-1] == -1) {
			long[] voisins = new long[nbVilles-1]; // La liste des voisins de notre ville
			voisins = this.getInstance().getDistances()[i]; // On récupère les distances entre la ville et ses voisins
			voisins[i] = 150000; // On set la distance de notre ville a inf (0 a la base)
			long min = 150000;
			int indice = i; // Indice qui servira à trouver le min
			for (int j=0;j<nbVilles-1;j++) { // On test tous les voisins 
				if (!testInt(j, visite) && voisins[j]<min) { // Si le sommet j n'a pas déjà été visité et que sa distance est < a notre min
					min = voisins[j]; 
					indice = j;
				}
			}
			visite[i]=indice; // On ajoute le sommet min aux sommets visité
			this.getSolution().setCityPosition(i, indice); // On l'ajoute a la solution
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
