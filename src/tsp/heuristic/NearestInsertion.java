package tsp.heuristic;
import java.util.ArrayList; 
import java.util.LinkedList;
import java.util.List;
import tsp.Instance;
import tsp.Solution;

import tsp.TSPSolver;


/**
 * Méthode heuristique de l'insertion du plus proche voisin
 * @author Valentin
 */
public class NearestInsertion extends TSPSolver{
	
	/**
	 * Tour: Une ArrayList<Integer> contenant les villes insérées une par une et qui la solution
	 */
	
	private ArrayList<Integer> Tour;
	
	/**
	 *VillesPasEncoreVisitées: Une ArrayList<Integer> des villes qui n'ont pas encore été insérées
	 */
	private ArrayList<Integer> VillesPasEncoreVisitées;
	
	/**
	 * Constructeur de la classe NearestInsertion à l'aide des paramètres de l'instance.
	 * Construction d'un Tour vide et initialisation de VillesPasEncoreVisitées avec toutes les villes du problèmes
	 * @param instance instance du problème
	 * @param timeLimit limite de temps de l'instance
	 */
	
	public NearestInsertion(Instance instance, long timeLimit) throws Exception {
		super(instance, timeLimit);
		this.Tour=new ArrayList<Integer>();
		int n=this.getInstance().getNbCities();
		this.VillesPasEncoreVisitées=new ArrayList<Integer>(n);
		for (int init=1;init<n;init++) {
			 this.VillesPasEncoreVisitées.add(init);
		}
	}	
	
	/**
	 * Initialisation de Tour en ajoutant la ville de départ (0), son plus proche voisin et la ville d'arrivée (0). Actualisation de VillesPasEncoreVisitées
	 */
	
	public void Initialisation() {
		Tour.add(0);
		Tour.add(getMin(0));
		Tour.add(0);
		this.VillesPasEncoreVisitées.remove(this.VillesPasEncoreVisitées.indexOf(getMin(0)));
	}
	
	/**
	 * Cherche la ville la plus proche de i en utilisant la matrice des distances de l'instance
	 * @param i Ville initiale
	 * @return Renvoie la ville la plus proche de la ville i
	 */
	
	public int getMin(int i) {
		long[] Distance=this.getInstance().getDistances()[i];
		long min=Distance[1];
		int j=1;
			for(int k=2;k<this.getInstance().getNbCities();k++) {
				if(Distance[k]<min) {
					min=Distance[k];
					j=k;
				}
			}
		return j;		
	}
	
	/**
	 * Cherche la ville de VillesPasEncoreVisitées la plus proche de n'importe quelle ville du Tour
	 * @param liste Liste des villes du Tour
	 * @param tab Liste des villes qui ne sont pas dans le Tour
	 * @return Renvoie la ville la plus proche des points de la liste
	 */
	
	public int Min(ArrayList<Integer> liste, ArrayList<Integer> tab){
		long min=1000;
		int j=-1;
		for(int l:liste){
			long[] Distance=this.getInstance().getDistances()[l];
			for(int t:tab) {
				if(Distance[t]<min){
					min=Distance[t];
					j=t;
				}
			}

		}
		return j;
	}
	
	/**
	 * Cherche dans le Tour l'arc et le point d'insertion de r qui minimise l'objectiveValue du Tour en insérant la ville r
	 * @param liste La liste des villes du Tour
	 * @param r La ville à insérer
	 * @return Le point d'insertion de la ville r dans la liste 
	 */
	
	public int InsertionArc(ArrayList<Integer> liste, int r){ //On cherche les extrémités de l'arc à modifier
		int PointInsertion= 0;
		long[][] Distances=this.getInstance().getDistances();
		long min=1000;
		for(int k=0;k<liste.size()-1;k++) {
			int i=liste.get(k);
			int j=liste.get(k+1);
			if(Distances[i][r]+Distances[r][j]-Distances[i][j]<min) { //Trouver le minimum de la somme des arcs (i,r) et (r,j) moins l'arc (i,j)
				min=Distances[i][r]+Distances[r][j]-Distances[i][j]; //Actualiser le minimum
				PointInsertion=i;
			}
		}
		return PointInsertion;		
	}
	
	/**
	 * Solution en insérant la ville la plus proche dans le Tour
	 * @return La solution de l'instance 
	 */
	public Solution Nearest() throws Exception {
		Initialisation();
		int i=3;
		while(i<=(this.getInstance().getNbCities())) {
			int NoeudSuivant=this.Min(Tour, VillesPasEncoreVisitées); //Ville à ajouter
			VillesPasEncoreVisitées.remove(VillesPasEncoreVisitées.indexOf(NoeudSuivant));
			int Point=this.InsertionArc(this.Tour, NoeudSuivant);
			int indice=this.Tour.indexOf(Point);
			this.Tour.add(indice+1, NoeudSuivant);
			i++;
		}
		
		Solution sol=new Solution(this.getInstance()); //Création de la solution avec le Tour final
		for(int k=0;k<this.Tour.size()-1;k++) {
			sol.setCityPosition(this.Tour.get(k), k);
		}
		return sol;
	}
	
	public Solution OptimisationInsertionNoeud(Solution solution) throws Exception {
		ArrayList<Integer> TourOpt=new ArrayList<Integer>();
		for(int k=0;k<this.getInstance().getNbCities()+1;k++) {
			TourOpt.add(k, solution.getCity(k));
		}
		boolean OptimisationPossible=true;
		int boucle=0;
		while(boucle<100 && OptimisationPossible) {
		    OptimisationPossible=false;
			for(int i=1;i<this.getInstance().getNbCities();i++) {
				long valeuropt=this.Evaluate(TourOpt);
				int Point=TourOpt.get(i);
				TourOpt.remove(TourOpt.indexOf(Point));
				int j=1;
				boolean continuer=true;
				while(continuer && j<this.getInstance().getNbCities()) {
					TourOpt.add(j, Point);
					if(this.Evaluate(TourOpt)<=valeuropt) {
						continuer=false;
						OptimisationPossible=true;
					}
					else {
						TourOpt.remove(j);
						j++;
						
					}
				}
			}
			boucle++;
		}
		for(int l=0;l<TourOpt.size()-1;l++) {
			solution.setCityPosition(TourOpt.get(l), l);
		}
		return solution;
	}
	
	public long Evaluate(ArrayList<Integer> liste) throws Exception {
		long valeur=0;
		for(int k=0;k<liste.size()-1;k++) {
			valeur+=this.getInstance().getDistances(liste.get(k),liste.get(k+1));
		}
		return valeur;
	}
}
	





