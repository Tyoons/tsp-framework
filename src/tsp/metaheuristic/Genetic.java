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
	
	/** On choisit le nombre d'individu */
	private static int nbIndividus = 50;
	
		// -----------------------------
		// ----- Initialization --------
		// -----------------------------
	
	public void getInitGene() throws Exception {
		int nbVilles = this.getInstance().getNbCities();
	
		/* On initialise la liste des villes deja� visitees */ 
		int[] visite = new int[nbVilles];
		
		/* On initialise les elements de cette liste */
		visite[0]=0;
		visite[nbVilles-1] = -1;

		
		/* On prend arbitrairement comme ville de départ la ville 0 */
		this.getSolution().setCityPosition(0, 0);
		
		/* i sera la variable des iterations */
		int i = 1;
		
		/* On boucle tant que toutes les villes n'ont pas �t� visit�e*/
		while(visite[nbVilles-1] == -1) {
			long[] voisins = new long[nbVilles-1]; // La liste des voisins de notre ville
			voisins = this.getInstance().getDistances()[i]; // On r�cup�re les distances entre la ville et ses voisins
			voisins[i] = 150000; // On set la distance de notre ville a inf (0 a la base)
			long min = 150000;
			int indice = i; // Indice qui servira � trouver le min
			for (int j=0;j<nbVilles-1;j++) { // On test tous les voisins 
				if (!testInt(j, visite) && voisins[j]<min) { // Si le sommet j n'a pas d�j� �t� visit� et que sa distance est < a notre min
					min = voisins[j]; 
					indice = j;
				}
			}
			visite[i]=indice; // On ajoute le sommet min aux sommets visit�
			this.getSolution().setCityPosition(i, indice); // On l'ajoute a la solution
			i++;
		}
	}
		
		// -----------------------------
		// -- Creation des N individu --
		// -----------------------------
		
		/** On creer N individu � partir de l'initialisation */
		
		public Solution[] creationPopulation() {
			Solution[] population = new Solution[nbIndividus];
			for (int i=0;i<nbIndividus;i++) {
				population[i] = this.getSolution();
			}
			return population;
		}
		
		
		// -----------------------------
		// -- Creation des N individu --  Aleatoire
		// -----------------------------
	
		public Solution[] creationPopulationAleatoire() throws Exception {
			int nbVilles = this.getInstance().getNbCities();
			Solution[] resultat = new Solution[nbIndividus];
			for (int i=0;i<nbIndividus;i++) {
				resultat[i].setCityPosition(0, 0);
				
				/* Tableau des villes */
				int[] villes = new int[nbVilles];
				for (int j=1;j<this.getInstance().getNbCities();j++) {
				}
				
				
			}
			return resultat;
		}
		
		// -----------------------------
		// ---- Melange genetique ------
		// -----------------------------
	
		public static Solution[] iterationGene(Solution[] parents, int nbSwap) throws Exception {
			Solution[] offspring = new Solution[nbIndividus];
			Solution[] famille = new Solution[2*nbIndividus];
			double[] valeurs = new double[2*nbIndividus];
			int nbVilles = parents[0].getInstance().getNbCities();
			
			/** On prepare la famille */
			for (int i = 0;i<nbIndividus;i++) {
				famille[i] = parents[i];
				famille[i+nbIndividus] = parents[i];
			}
			
			
			for (int i = nbIndividus;i<2*nbIndividus;i++) {
				int indiceSwap1 = 1 + (int)(Math.random() *(nbVilles - 1));
				int indiceSwap2 = 1 + (int)(Math.random() *(nbVilles - 1));
				for (int j=0;j<nbSwap;j++) {
					int ville1 = famille[i].getCity(indiceSwap1);
					int ville2 = famille[i].getCity(indiceSwap2);
					famille[i].setCityPosition(ville2, indiceSwap1);
					famille[i].setCityPosition(ville1, indiceSwap2);
				}
			}
			
			
			
			for (int i=0;i<2*nbIndividus;i++) {
				valeurs[i] = famille[i].evaluate();
			}
			
			
			/** On tri la liste des valeurs et des solution 
			 * en fonction de la valeur objective de la solution.
			*/
			for (int i=0;i<2*nbIndividus;i++) {
				double tempL;
				Solution tempS;
				for (int j=0;j<2*nbIndividus-i-1;j++) {
					if (valeurs[j]>valeurs[j+1]) {
						tempL= valeurs[j];
						valeurs[j] = valeurs[j+1];
						valeurs[j+1] = tempL;
						tempS = famille[j];
						famille[j] = famille[j+1];
						famille[j+1] = tempS;
					}
				}
				
			}
			
			//System.out.println(valeurs[nbIndividus+1]);
			
			for (int i = 0;i<nbIndividus;i++) {
				offspring[i] = famille[i];
			}
			
			return offspring;
		}
		
		
		// -----------------------------
		// -----------------------------
		// -----------------------------
		
	/** Fonction pour obtenir la meilleure solution dans une liste */
	
	private static Solution getMeilleurSol(Solution[] population) {
		long min = population[0].getObjectiveValue();
		Solution resultat = population[0];
		for (int i=1;i<population.length;i++) {
			if (population[i].getObjectiveValue() < min) {
				min = population[i].getObjectiveValue();
				resultat = population[i];
			}
		}
		return resultat;
	}
	
	/** Fonction pour tester si un entier est dans un tableau */
		
	private static boolean testInt(int element,int[] tableau) {
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
