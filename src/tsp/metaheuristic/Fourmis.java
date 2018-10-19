package tsp.metaheuristic;
import tsp.Instance; 
import tsp.Solution;
import java.util.ArrayList;
import java.util.List;


public class Fourmis extends Environnement{

 
   public Fourmis(Instance instance, long timeLimit) {
		 super(instance, timeLimit);
		 int n=this.getInstance().getNbCities();
		 this.VillesPasEncoreVisitées=new ArrayList<Integer>(n);
		 this.VillesVisitées=new ArrayList<Integer>(n);	
		 for (int init=0;init<n;init++) {
			 this.VillesPasEncoreVisitées.add(init);
		 }
		 this.TempsDeParcours=0;
		 this.EtatFourmi=0;
		/* this.borneMin=0;
		 this.evaporation=(float) 0.2;
		 this.pheromones=new float[n][n]; */
	}

   private List<Integer> VillesVisitées;        // toutes les villes visitées par la fourmi
   private List<Integer> VillesPasEncoreVisitées;    // toutes les villes encore à visiter
   private long TempsDeParcours;                // compteur de longueur du chemin parcouru
   private int EtatFourmi;  // état de la fourmi, en route, en retour (-1 Retour, 0 Rien; 1 En Route; 2 Arrivé; 3 Supprimée)
   private int VilleOrigine;
   private int VilleDestination;
   private long PositionSurArcActuel;
   private long LongueurArcActuel;
   
 

public void findNextSearchDestination() throws Exception { // détermination du prochain noeud à atteindre
	   switch(EtatFourmi){
       	case 0:{
            VillesVisitées.add(0);
            VillesPasEncoreVisitées.remove(0);          
            int Destination = getNearCity(0);
            EtatFourmi = 1;
            VilleOrigine = 0;
            VilleDestination = Destination;
          //PositionSurArcActuel = 0;
          //LongueurArcActuel =this.getInstance().getDistances(0,VilleDestination);
        }
        case 1:{ // la fourmi est sur le graphe
            this.TempsDeParcours+=this.getInstance().getDistances(this.VilleOrigine, this.VilleDestination);
            VillesVisitées.add(this.VilleDestination);
            VillesPasEncoreVisitées.remove(VilleDestination);  
            if (this.VillesPasEncoreVisitées.size() == 0){ // rien d'autre à visiter, on revient
                this.TempsDeParcours += this.getInstance().getDistances(VilleDestination, 0);
                this.EtatFourmi = -1;
                this.VilleOrigine=0;
                this.VilleDestination=this.VillesVisitées.size()-1; // on s'intéresse maintenant aux indexs des villes!
                //this.LongueurArcActuel=this.getInstance().getDistances(this.VillesVisitées.get(VilleOrigine), this.VillesVisitées.get(VilleDestination));
                //this.PositionSurArcActuel=this.LongueurArcActuel;                                  
            }
            else {
            	int Destination = getNearCity(this.VilleDestination);
            	this.VilleOrigine = this.VilleDestination;
            	this.VilleDestination = Destination; 
            	//this.LongueurArcActuel=this.getInstance().getDistances(VilleOrigine, VilleDestination);
            	//this.PositionSurArcActuel=0;
            }
        }
        case -1:{  // si la fourmi revient au nid
            if (this.VilleDestination == 0){
                // retournée au nid avec succès
                this.pheromones[this.VillesVisitées.get(VilleOrigine)][0]= super.facteur/this.TempsDeParcours;
                this.pheromones[0][this.VillesVisitées.get(VilleOrigine)]=super.facteur/this.TempsDeParcours;
                this.EtatFourmi=2;
            }
            else{
 
            // trouver la ville précédemment visitée et la passer en destination
            // mettre des phéromones sur l'arc parcouru
            this.pheromones[this.VillesVisitées.get(VilleOrigine)][this.VillesVisitées.get(VilleDestination)]= super.facteur/this.TempsDeParcours;
            this.pheromones[this.VillesVisitées.get(VilleDestination)][this.VillesVisitées.get(VilleOrigine)]= super.facteur/this.TempsDeParcours;
            this.VilleOrigine=this.VilleDestination;
            this.VilleDestination=this.VilleOrigine-1;
            //this.LongueurArcActuel=this.getInstance().getDistances(VilleOrigine, VilleDestination);
        	//this.PositionSurArcActuel=this.LongueurArcActuel;
        }
        }
}
}

  /* public void frame() throws Exception{ //Vérifié
	    switch(this.EtatFourmi){
	        case 1:{
	            this.TempsDeParcours ++;
	        }
	        case -1:{
	        	this.PositionSurArcActuel++;             
	            if (this.PositionSurArcActuel >= this.LongueurArcActuel) {
	            	this.findNextSearchDestination();
	            }
	        }
	        case 0:{
	            this.findNextSearchDestination();             
	        }
	    }
   }*/

private int getNearCity(int i) throws Exception {
	    // hasard sur les chemins restants, pondérés par les phéromones
	float TaillePheromone = 0;          
	for (int k = 0; k < this.VillesPasEncoreVisitées.size(); k++){
	        if (this.VillesPasEncoreVisitées.get(k) != i) {
	        	TaillePheromone += this.pheromones[i][ this.VillesPasEncoreVisitées.get(k)];
	        }
	}
	float found =(float) (Math.random()*TaillePheromone*1000)/1000;
	float Pheromones = 0;
	int j = 0;
	while (j < this.VillesPasEncoreVisitées.size()){
		Pheromones  += pheromones[VilleDestination][ this.VillesPasEncoreVisitées.get(j)];
	 
		if (Pheromones>found) {
			break;
		}
		else {
			j ++;
		}
	    if (j == this.VillesPasEncoreVisitées.size()){
	        // aucune solution acceptable, détruire la fourmi courante
	        Exception e=new Exception("Effacer");
	        throw e;
	    }
	}
	return this.VillesPasEncoreVisitées.get(j);
}



public Solution[] Solution(int NombreFourmis,Instance instance) throws Exception {
	 Solution[] s= new Solution[NombreFourmis];
	 double[] temps=new double[NombreFourmis];
	 for (int i=0; i<NombreFourmis; i++){// pour chaque fourmi
		 Fourmis f=new Fourmis(instance, 100000000);
		 while(true) {
			 try {
				 f.frame();
				 System.out.println(f.EtatFourmi);
				 System.out.println(f.VilleOrigine);
				 System.out.println(f.PositionSurArcActuel);
			 }catch(Exception e) {
				 if(e.getMessage().equals("Effacer")) { 
					 s[i]=new Solution(instance);
					 temps[i]=100000000;
				 }
				 else {
					 if(e.getMessage().equals("Enregistre")) {
						 Solution sol=new Solution(instance);
						 for(int k=0;k<f.VillesVisitées.size()-1;k++) {
							 sol.setCityPosition(f.VillesVisitées.get(k), f.VillesVisitées.get(k+1));
						 }
						 sol.setCityPosition(f.VillesVisitées.get(f.VillesVisitées.size()),0);
						 s[i]=sol;
						 temps[i]=s[i].evaluate();
					 }
				 }
			 }
		 }
	 }
	 
	        // on évapore les phéromones toutes les 20 itérations
	        // juste histoire de ne pas monopoliser toutes les ressources pour ça
	   /*     if (i % 20 == 0)
	            evaporate(); 
	    }
*/	//System.out.println(temps);
	return s;
	
}
}