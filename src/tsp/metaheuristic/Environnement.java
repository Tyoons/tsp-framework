package tsp.metaheuristic;

import tsp.Instance;
import tsp.TSPSolver;
/**
 * Cette classe permet aux fourmis 
 * @author Valentin
 *
 */
public class Environnement extends TSPSolver{
	
	public double[][] pheromones;
	public double gamma;
	public double beta;
	public double alpha;
	public double facteur;
	public double evaporation;
		 
    public Environnement(Instance instance, long timeLimit) {
		super(instance, timeLimit);
		int n=super.getInstance().getNbCities();
		this.pheromones=new double[n][n]; //Tableau des phéromones initialisé à 0.0
		this.gamma=1;
		this.beta=10;
		this.alpha=1;
		this.evaporation=1;
		this.facteur=1;
		
	}

			
		
	public void evaporate(){
		for (int i=0; i<this.getInstance().getNbCities(); i++) {
			for (int j=0; j<i; j++){
				pheromones[i][j] =pheromones[i][j]*(100-evaporation) /100;
		       /* if (pheromones[i][j] < borneMin) {
		        	pheromones[i][j] = borneMin;
		        }*/
		        pheromones[j][i] = pheromones[i][j];              
			}
		}
}
	
}
