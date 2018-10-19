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
   //private long PositionSurArcActuel;
   //private long LongueurArcActuel;
   
 

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
            break;
        }
        case 1:{ // la fourmi est sur le graphe
            this.TempsDeParcours+=this.getInstance().getDistances(this.VilleOrigine, this.VilleDestination);  
            if (this.VillesPasEncoreVisitées.size() == 0){ // rien d'autre à visiter, on revient
                this.TempsDeParcours += this.getInstance().getDistances(VilleDestination, 0);
                this.EtatFourmi = -1;
                this.VilleOrigine=0;
                this.VilleDestination=this.VillesVisitées.size()-1; // on s'intéresse maintenant aux indexs des villes!
                //this.LongueurArcActuel=this.getInstance().getDistances(this.VillesVisitées.get(VilleOrigine), this.VillesVisitées.get(VilleDestination));
                //this.PositionSurArcActuel=this.LongueurArcActuel;
                break;
            }
            else {
            	int Destination = getNearCity(this.VilleDestination);
            	System.out.println("Je cherche la prochaine ville:");
            	System.out.println("Je viens de:");
            	System.out.println(this.VilleOrigine);
            	System.out.println("Je vais à la ville:");
            	System.out.println(this.VilleDestination);
            	VillesVisitées.add(this.VilleDestination);
                VillesPasEncoreVisitées.remove(this.VilleDestination);
            	this.VilleOrigine = this.VilleDestination;
            	this.VilleDestination = Destination; 
            	//this.LongueurArcActuel=this.getInstance().getDistances(VilleOrigine, VilleDestination);
            	//this.PositionSurArcActuel=0;
            	break;
            }
        }
        case -1:{  // si la fourmi revient au nid
            if (this.VilleDestination == 0){
                // retournée au nid avec succès
                this.pheromones[this.VillesVisitées.get(VilleOrigine)][0]= super.facteur/this.TempsDeParcours;
                this.pheromones[0][this.VillesVisitées.get(VilleOrigine)]=super.facteur/this.TempsDeParcours;
                this.EtatFourmi=2;
                break;
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
            break;
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
	    // méthode de la règle aléatoire de transition proportionnelle
	double Poids = 0; 
	double[] VillesDisponibles=new double[this.VillesPasEncoreVisitées.size()];
	System.out.println("Il reste "+this.VillesPasEncoreVisitées.size()+" villes");
	for (int k = 0; k < this.VillesPasEncoreVisitées.size(); k++){
		if(this.VillesPasEncoreVisitées.get(k)!=i) {
			Poids+= this.gamma+Math.pow(this.pheromones[i][ this.VillesPasEncoreVisitées.get(k)],this.alpha)*Math.pow(1/this.getInstance().getDistances(i, this.VillesPasEncoreVisitées.get(k)),this.beta);
		}
		System.out.println(Poids);
	}
	for(int j=0;j<this.VillesPasEncoreVisitées.size();j++){
		if(this.VillesPasEncoreVisitées.get(j)!=i) {
			VillesDisponibles[j]=(this.gamma+Math.pow(this.pheromones[i][ this.VillesPasEncoreVisitées.get(j)],this.alpha)*Math.pow(1/this.getInstance().getDistances(i, this.VillesPasEncoreVisitées.get(j)),this.beta))/Poids;
		System.out.println(VillesDisponibles[j]);
	}
	}
	double proba=Math.random();
	int l=0;;
	while(l<this.VillesPasEncoreVisitées.size() && proba<VillesDisponibles[l]) {
		l++;
	}
	return this.VillesPasEncoreVisitées.get(l);
}



public Solution[] Solution(int NombreFourmis) throws Exception {
	 Solution[] s= new Solution[NombreFourmis];
	 for (int i=0; i<NombreFourmis; i++){
		 Fourmis f=new Fourmis(this.getInstance(),this.getTimeLimit());
		 while(f.EtatFourmi!=2) {
			 System.out.println("On est là:");
			 System.out.println(f.VillesVisitées);
			 
			 f.findNextSearchDestination();
			 
			 
		 }
		 Solution sol=new Solution(this.getInstance());
		 for(int k=0;k<f.VillesVisitées.size();k++) {
			sol.setCityPosition(f.VillesVisitées.get(k), k);
		}
		sol.setCityPosition(0,f.VillesVisitées.size());
		s[i]=sol;
		if(i%20==0) {
			this.evaporate();
		}
	 }
	return s;	 	
}

public Solution MeilleureFourmi() throws Exception {
	Solution[] s=this.Solution(500);
	int m=0;
	double min=s[0].evaluate();
	for(int i=1;i<s.length;i++){
		if(s[i].evaluate()<min) {
			m=i;
			min=s[i].evaluate();
			System.out.println(m);
		}
	}
	return s[m];
	
	
}
}