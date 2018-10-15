package tsp.metaheuristic;
import tsp.Instance;
import tsp.Solution;
import tsp.TSPSolver;
import java.util.ArrayList;
import java.util.List;


public class Fourmis extends TSPSolver{

 
   public Fourmis(Instance instance, long timeLimit) {
		super(instance, timeLimit);
		// TODO Auto-generated constructor stub
	}

   private List<Integer> VillesVisitées;        // toutes les villes visitées par la fourmi
   private List<Integer> VillesPasEncoreVisitées;    // toutes les villes encore à visiter
   private long TempsDeParcours;                // compteur de longueur du chemin parcouru
   private int EtatFourmi;  // état de la fourmi, en route, en retour (-1 Retour, 0 Rien; 1 En Route)
   private int VilleOrigine;
   private int VilleDestination;
   private long PositionSurArcActuel;
   private long LongueurArcActuel;
   private float[][] pheromones;
   private float evaporation;
   private float borneMin;
   
   

   public Fourmis(Instance instance, long timeLimit) {
	   this.VillesPasEncoreVisitées=this.getInstance().getNbCities()[]
}

public void findNextSearchDestination() throws Exception { // détermination du prochain nœud à atteindre
	   switch(EtatFourmi){
       	case 0:{
            VillesVisitées.add(0);
            VillesPasEncoreVisitées.remove(0);          
            int Destination = getNearCity(0);
            EtatFourmi = 1;
            VilleOrigine = 0;
            VilleDestination = Destination;
            PositionSurArcActuel = 0;
            LongueurArcActuel =this.getInstance().getDistances(0,VilleDestination);
            break;
        }
        case 1:{ // si la fourmi cherche son chemin dans le graphe
            // on a atteint currentDestination           
            this.TempsDeParcours+=this.getInstance().getDistances(this.VilleOrigine, this.VilleDestination);
            VillesVisitées.add(this.VilleDestination);
            VillesPasEncoreVisitées.remove(VilleDestination);  
            if (this.VillesPasEncoreVisitées.size() == 0){
                // plus rien à visiter, le chemin est complet, on revient vers le nid
                this.TempsDeParcours += this.getInstance().getDistances(VilleDestination, 0);
                this.EtatFourmi = -1;
                this.VilleOrigine=this.VillesVisitées.size()-1;
                this.VilleDestination=this.VillesVisitées.size()-2;
                this.LongueurArcActuel=this.getInstance().getDistances(this.VillesVisitées.get(VilleOrigine), this.VillesVisitées.get(VilleDestination));
                this.PositionSurArcActuel=this.LongueurArcActuel;                                  
            }
            else {
            	int Destination = getNearCity(this.VilleDestination);
            	this.VilleOrigine = this.VilleDestination;
            	this.VilleDestination = Destination; 
            	this.LongueurArcActuel=this.getInstance().getDistances(VilleOrigine, VilleDestination);
            	this.PositionSurArcActuel=0;
            }
            break;
        }
        case -1:{  // si la fourmi revient au nid
            if (this.VilleDestination == 0){
                // retournée au nid avec succès
                this.pheromones[this.VillesVisitées.get(VilleOrigine)][this.VillesVisitées.get(VilleDestination)]= this.TempsDeParcours;
 
                // sauver le résultat, changer de fourmi
              /*  antException e;
                e.a = this;
                e.state = antException::TO_REGISTER;
                throw e;*/ //jsp comment on fait ça!
            }
 
            // trouver la ville précédemment visitée et la passer en destination
            // mettre des phéromones sur l'arc parcouru
            this.pheromones[this.VillesVisitées.get(VilleOrigine)][this.VillesVisitées.get(VilleDestination)]= this.TempsDeParcours;
            this.VilleOrigine=this.VilleDestination;
            this.VilleDestination=this.VilleOrigine-1;
            this.LongueurArcActuel=this.getInstance().getDistances(VilleOrigine, VilleDestination);
        	this.PositionSurArcActuel=this.LongueurArcActuel;
            break;
        }
    }   
}

   public void frame() throws Exception{
	    switch(this.EtatFourmi){
	        case 1:{
	            this.TempsDeParcours ++;
	        }
	        case -1:{
	        	this.PositionSurArcActuel++;             
	            if (this.PositionSurArcActuel >= this.LongueurArcActuel) {
	            	this.findNextSearchDestination();
	            }
	            break; 
	        }
	        case 0:{
	            this.findNextSearchDestination();
	            break;              
	        }
	    }
   }

private int getNearCity(int i) {
	    // hasard sur les chemins restants, pondérés par les phéromones
	float TaillePheromone = 0;          
	for (int k = 0; k < this.VillesPasEncoreVisitées.size(); k++){
	        if (this.VillesPasEncoreVisitées.get(k) != i) {
	        	TaillePheromone += this.pheromones[i][ this.VillesPasEncoreVisitées.get(k)];
	        }
	}
	float found =(float) (Math.random()*TaillePheromone);
	float Pheromones = 0;
	int j = 0;
	while (j < this.VillesPasEncoreVisitées.size()){
		if (this.VillesPasEncoreVisitées.get(j) == i){
			j++;
	    }
		Pheromones  += pheromones[VilleDestination][ this.VillesPasEncoreVisitées.get(j)];
	 
		if (Pheromones>found) {
			break;
		}
		else {
			j ++;
		}
	    if (j == this.VillesPasEncoreVisitées.size()){
	        // aucune solution acceptable, détruire la fourmi courante
	        /*antException e;
	        e.a = this;
	        e.state = antException::TO_DELETE;
	        throw e;*/
	    }
	}
	return this.VillesPasEncoreVisitées.get(j);
}

public void evaporate(){
    for (int i=0; i<this.getInstance().getNbCities(); i++)
        for (int j=0; j<i; j++){
            pheromones[i][j] = (float)(pheromones[i][j]*(100-evaporation) /100);
            if (pheromones[i][j] < borneMin) {
                pheromones[i][j] = borneMin;
            }
            pheromones[j][i] = pheromones[i][j];              
        }
}

public static Solution[] Solution(int NombreFourmis) {
	 for (int i=0; i<NombreFourmis; i++){// pour chaque fourmi
		 Fourmis f=new Fourmis();
	        std::list<ant*>::iterator it = ants.begin();
	        while (it != ants.end()){
	            try{
	                 (*it)->frame();
	            }catch(antException &e){
	                if (e.state == antException::TO_REGISTER)
	                    notifySolution(e.a->tmpVisitedLength, e.a->visitedCities);    
	 
	                if(bestLength <= data.optimalLength)
	                     return;              
	 
	                // on crée une nouvelle fourmi pour remplacer la fourmi courante
	                *it = new ant(data);     
	                delete e.a;                                         
	            }
	            it++;
	        }       
	 
	        // on évapore les phéromones toutes les 20 itérations
	        // juste histoire de ne pas monopoliser toutes les ressources pour ça
	        if (i % 20 == 0)
	            evaporate(); 
	    }

	return null;
	
}

}