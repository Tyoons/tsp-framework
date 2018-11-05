package tsp.heuristic;
import java.util.ArrayList; 
import java.util.LinkedList;
import java.util.List;
import tsp.Instance;
import tsp.Solution;

import tsp.TSPSolver;

public class NearestInsertion extends TSPSolver{

	public NearestInsertion(Instance instance, long timeLimit) throws Exception {
		super(instance, timeLimit);
		this.Tour=new ArrayList<Integer>();
		int n=this.getInstance().getNbCities();
		this.VillesPasEncoreVisitées=new ArrayList<Integer>(n);
		for (int init=1;init<n;init++) {
			 this.VillesPasEncoreVisitées.add(init);
		}
	}
		
	private ArrayList<Integer> Tour;
	private ArrayList<Integer> VillesPasEncoreVisitées;
	
	
	public void Initialisation() { //Initialisation du sous-tour
		Tour.add(0);
		Tour.add(getMin(0));
		Tour.add(0);
		this.VillesPasEncoreVisitées.remove(this.VillesPasEncoreVisitées.indexOf(getMin(0)));
	}
	
	public int InsertionArc(ArrayList<Integer> liste, int r){ //On cherche les extrémités de l'arc à modifier
		int PointInsertion= 0;
		long[][] Distances=this.getInstance().getDistances();
		long min=1000;
		for(int k=0;k<liste.size()-1;k++) {
			int i=liste.get(k);
			int j=liste.get(k+1);
			if(Distances[i][r]+Distances[r][j]-Distances[i][j]<min) {
				PointInsertion=i;
			}
		}
		return PointInsertion;		
	}
	
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
	
	public Solution Nearest() throws Exception {
		Initialisation();
		int i=3;
		while(i<=(this.getInstance().getNbCities())) {
			int NoeudSuivant=this.Min(Tour, VillesPasEncoreVisitées);
			VillesPasEncoreVisitées.remove(VillesPasEncoreVisitées.indexOf(NoeudSuivant));
			int Point=this.InsertionArc(this.Tour, NoeudSuivant);
			int indice=this.Tour.indexOf(Point);
			this.Tour.add(indice+1, NoeudSuivant);
			i++;
		}
		
		Solution sol=new Solution(this.getInstance());
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
	





