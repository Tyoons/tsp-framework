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

	}

   private List<Integer> VillesVisitées;        // toutes les villes visitées par la fourmi
   private List<Integer> VillesPasEncoreVisitées;    // toutes les villes encore à visiter
   private long TempsDeParcours;                // compteur de longueur du chemin parcouru
   private int EtatFourmi;  // état de la fourmi, en route, en retour (-1 Retour, 0 Rien; 1 En Route; 2 Arrivé; 3 Supprimée)
   private int VilleOrigine;
   private int VilleDestination;

   
 

public void findNextSearchDestination() throws Exception { // détermination du prochain noeud à atteindre
	if (this.EtatFourmi==0) {
            VillesVisitées.add(0);
            VillesPasEncoreVisitées.remove(this.VillesPasEncoreVisitées.lastIndexOf(0));          
            int Destination = getNearCity(0,this.VillesPasEncoreVisitées);
            EtatFourmi = 1;
            VilleOrigine = 0;
            VilleDestination = Destination;

        }
	else {
		if(this.EtatFourmi==1) { // la fourmi est sur le graphe
            this.TempsDeParcours+=this.getInstance().getDistances(this.VilleOrigine, this.VilleDestination);  
            if (this.VillesPasEncoreVisitées.size() == 1){ // rien d'autre à visiter, on revient
                this.TempsDeParcours += this.getInstance().getDistances(VilleDestination, 0);
                this.VillesVisitées.add(this.VillesPasEncoreVisitées.get(0));
                this.EtatFourmi = -1;
                this.VilleOrigine=0;
                this.VilleDestination=this.VillesVisitées.size()-1; // on s'intéresse maintenant aux indexs des villes!

            }
            else {
            	VillesVisitées.add(this.VilleDestination);
                VillesPasEncoreVisitées.remove(VillesPasEncoreVisitées.lastIndexOf(this.VilleDestination));
            	this.VilleOrigine = this.VilleDestination;
            	int Destination = getNearCity(this.VilleDestination,this.VillesPasEncoreVisitées);
            	this.VilleDestination = Destination; 

            }
        }
		else {
			if(this.EtatFourmi==-1) {
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

				}
			}
		}
}
}

private int getNearCity(int i,List<Integer> V) throws Exception {
	    // méthode de la règle aléatoire de transition proportionnelle
	double Poids = 0; 
	double[] VillesDisponibles=new double[V.size()];
	for (int k = 0; k < V.size(); k++){
		if(V.get(k)!=i) {
			Poids+= this.gamma+Math.pow(this.pheromones[i][V.get(k)],this.alpha)*Math.pow(1/this.getInstance().getDistances(i,V.get(k)),this.beta);
		}
	}
	for(int j=0;j<VillesDisponibles.length;j++){
		if(V.get(j)!=i) {
			VillesDisponibles[j]=(this.gamma+Math.pow(this.pheromones[i][V.get(j)],this.alpha)*Math.pow(1/this.getInstance().getDistances(i,V.get(j)),this.beta))/Poids;
			}
	}
	double proba=Math.random();
	int l=0;
	double sum=VillesDisponibles[0];
	if(VillesDisponibles.length>0) {
		while(l<VillesDisponibles.length && proba>sum) {
			l++;
			sum+=VillesDisponibles[l];
		}
		return V.get(l);
	}
	else {
		return V.get(0);
	}
}



public Solution[] Solution(int NombreFourmis) throws Exception {
	 Solution[] s= new Solution[NombreFourmis];
	 for (int i=0; i<NombreFourmis; i++){
		 Fourmis f=new Fourmis(this.getInstance(),this.getTimeLimit());
		 while(f.EtatFourmi!=2) {	
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
	Solution[] s=this.Solution(this.getInstance().getNbCities());
	int m=0;
	double min=s[0].evaluate();
	for(int i=1;i<s.length;i++){
		if(s[i].evaluate()<min) {
			m=i;
			min=s[i].evaluate();
			System.out.println("Nouvelle Meilleure fourmie:"+m);
		}
	}
	return s[m];
	
	
}
}