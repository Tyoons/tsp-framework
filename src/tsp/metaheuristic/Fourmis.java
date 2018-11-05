package tsp.metaheuristic;
import tsp.Instance; 
import tsp.Solution;
import tsp.TSPSolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Méthode métaheuristique des colonies de foumis
 * @author Valentin 
 */
public class Fourmis extends TSPSolver{
	
	/**
	 *  Matrice représentant les phéromones déposés sur le graphe par les fourmis précédentes de taille NbCities()
	 */
	private double[][] pheromones;
	
	/**
	 * ArrayList des villes visitées par la fourmis pendant son parcours du graphe
	 */
	
	private List<Integer> VillesVisitées;
	
	/**
	 * Arraylist des villes du graphe qui n'ont pas encore été visitées par la fourmi
	 */
	
	private List<Integer> VillesPasEncoreVisitées; 
	
	/**
	 * Long qui calcule la distance parcourue par la foumi
	 */
	
	private long TempsDeParcours;  
	
	/**
	 * Entier qui représente l'état de la foumi 0 Création 1 En chemin -1 Revient 2 Arrivée
	 */
	
	private int EtatFourmi; 
	
	/**
	 * Entier correspondant au numéro de la ville d'origine de la fourmi à chaque étape
	 */
	
	private int VilleOrigine;
	
	/**
	 * Entier correspondant au numéro de la ville destination de la fourmi à chaque étape
	 */
	
	private int VilleDestination;
	
	/**
	 * Paramètre de l'algorithme agissant sur l'importance de la visibilité des villes
	 */
	
	private double alpha;
	
	/**
	 * Paramètre de l'algorithme agissant sur l'importance des phéromones
	 */
	
	private double beta;
	
	/**
	 * Paramètre permettant à la fourmi choisir des villes sans phéromone
	 */
	
	private double gamma;
	
	/**
	 * Double agissant sur la taille des phéromones déposés par la fourmi
	 */
	
	private double facteur;
	
	/**
	 * Double représentant la probabilité pour le choix de la ville suivante
	 */
	private double probabilité;
	/**
	 * Pourcentage d'évaporation des phéromones sur le graphe
	 */
	
	private double evaporation;
	
	/**
	 * Entier représentant le nombre de fourmis dans la colonie
	 */
	private int nombre;
	
	/**
	 * Constructeur d'une fourmi avec les paramètres alpha, beta, gamma, facteur et evaporation arbitrairement initialisés
	 * Etat de la fourmi est initalisée à 0 ainsi que TempsDeParcours
	 * Construction d'une ArrayList VillesVisitées vide et d'une ArrayList VillePasEncoreVisitées avec toutes les villes du problème
	 * @param instance instance du problème
	 * @param timeLimit limite de temps de l'instance
	 * @param pheromones matrice représentant les phéromones déposés sur le graphe par les fourmis précédentes
	 */
   public Fourmis(Instance instance, long timeLimit, double[][] pheromones) {
	   super(instance, timeLimit);
	   int n=this.getInstance().getNbCities();
	   this.VillesPasEncoreVisitées=new ArrayList<Integer>(n);
	   this.VillesVisitées=new ArrayList<Integer>(n);	
	   for (int init=0;init<n;init++) {
		   this.VillesPasEncoreVisitées.add(init);
	   }
	   this.pheromones=pheromones;
	   this.TempsDeParcours=0;
	   this.EtatFourmi=0;
	   this.gamma=0.0001;
	   this.beta=2;
	   this.alpha=1;
	   this.evaporation=50;
	   this.facteur=1000;
	   this.probabilité=0.4;
	   this.nombre=(int) (1000000000/Math.pow(this.getInstance().getNbCities(),2));
			
	}
  
   /**
    * Simule une étape de parcours du graphe par la fourmi suivant son état. 
    * Cette fonction actualise les paramètres de la fourmi: VilleOrigine, VilleDestination, VillesPasEncoreVisitées, VillesVisitées, TempsDeParcours et EtatFourmi
    * @throws Exception
    */
   public void findNextSearchDestination() throws Exception {
	   if (this.EtatFourmi==0) { //Fourmi créée
            VillesVisitées.add(0);
            VillesPasEncoreVisitées.remove(this.VillesPasEncoreVisitées.lastIndexOf(0));          
            int Destination = getNearCity(0,this.VillesPasEncoreVisitées); //Détermination du prochain noeud à atteindre
            EtatFourmi = 1; //En chemin
            VilleOrigine = 0;
            VilleDestination = Destination;

        }
	else {
		if(this.EtatFourmi==1) { //Fourmi est en chemin sur le graphe
            this.TempsDeParcours+=this.getInstance().getDistances(this.VilleOrigine, this.VilleDestination); //Actualisation TempsDeParcours 
            if (this.VillesPasEncoreVisitées.size() == 1){ //Rien d'autre à visiter, elle revient vers le point de départ
                this.TempsDeParcours += this.getInstance().getDistances(VilleDestination, 0); //Ajout de la dernière distance et dernière ville
                this.VillesVisitées.add(this.VillesPasEncoreVisitées.get(0));
                this.EtatFourmi = -1; //Elle rentre
                this.VilleOrigine=0;
                this.VilleDestination=this.VillesVisitées.size()-1; //On s'intéresse maintenant aux indexs des villes!

            }
            else {
            	VillesVisitées.add(this.VilleDestination); //Fourmi visite la ville destination
                VillesPasEncoreVisitées.remove(VillesPasEncoreVisitées.lastIndexOf(this.VilleDestination));
            	this.VilleOrigine = this.VilleDestination; 
            	int Destination = getNearCity(this.VilleDestination,this.VillesPasEncoreVisitées); //Prochaine ville
            	this.VilleDestination = Destination; 

            }
        }
		else {
			if(this.EtatFourmi==-1) { //Fourmi revient au nid
				if (this.VilleDestination == 0){ //Arrivée au nid
					this.pheromones[this.VillesVisitées.get(VilleOrigine)][0]+=this.facteur/this.TempsDeParcours; //Actualisation des phéromones
					this.pheromones[0][this.VillesVisitées.get(VilleOrigine)]+=this.facteur/this.TempsDeParcours;
					this.EtatFourmi=2; //Terminus
				}
				else{ //Mettre des phéromones sur l'arc parcouru
					this.pheromones[this.VillesVisitées.get(VilleOrigine)][this.VillesVisitées.get(VilleDestination)]= this.facteur/this.TempsDeParcours;
					this.pheromones[this.VillesVisitées.get(VilleDestination)][this.VillesVisitées.get(VilleOrigine)]= this.facteur/this.TempsDeParcours;
					this.VilleOrigine=this.VilleDestination; //Trouver la ville précédemment visitée et la passer en destination
					this.VilleDestination=this.VilleOrigine-1;
				}
			}
		}
	}
}
/**
 * Détermine la prochaine ville que doit visiter la fourmi. Selon la probabilité soit la ville disponible la plus proche est choisie, soit utilisation de la matrice des phéromones, les distances et les paramètres alpha, beta et gamma pour appliquer la méthode de la règle aléatoire de transition proportionnelle
 * @param i ville origine
 * @param V une liste des villes à visiter
 * @return la prochaine destination de la fourmi
 * @throws Exception
 */
private int getNearCity(int i,List<Integer> V) throws Exception {
	double test=Math.random();
	if (test<this.probabilité) {
		double Poids = 0; 
		double[] VillesDisponibles=new double[V.size()]; //Tableau de probabilité
		for (int k = 0; k < V.size(); k++){
			if(V.get(k)!=i) { //Méthode de la règle aléatoire de transition proportionnelle
				Poids+= this.gamma+Math.pow(this.pheromones[i][V.get(k)],this.alpha)*Math.pow(1/this.getInstance().getDistances(i,V.get(k)),this.beta);
			}
		}
		for(int j=0;j<VillesDisponibles.length;j++){
			if(V.get(j)!=i) { //Affecter une probabilité pj à la ville potentielle j
				VillesDisponibles[j]=(this.gamma+Math.pow(this.pheromones[i][V.get(j)],this.alpha)*Math.pow(1/this.getInstance().getDistances(i,V.get(j)),this.beta))/Poids;
			}
		}
		double proba=Math.random();
		int l=0; //Détermination aléatoire de la ville suivante en utilisant les probabilités
		double sum=VillesDisponibles[0];
		while(l<VillesDisponibles.length && proba>sum) {
			l++;
			sum+=VillesDisponibles[l];
		}
		return V.get(l);
	}
	else { //Plus proche voisin
		long[] Distance=this.getInstance().getDistances()[i];
		long min=Distance[1];
		int j=V.get(0);
			for(int k:V) {
				if(Distance[k]<min) {
					min=Distance[k];
					j=k;
				}
			}
		return j;		
	}
}

/**
 * Envoie NombreFourmis fourmis dans le graphe, actualise les phéromones sur le graphe à chaque passage et créer un tableau de NombreFourmis solutions
 * @param NombreFourmis nombre de fourmis à créer et envoyer dans le graphe
 * @return un tableau de NombreFourmis solutions
 * @throws Exception
 */

public Solution[] Solution(int NombreFourmis) throws Exception {
	double[][] pheromonescolonie=new double[this.getInstance().getNbCities()][this.getInstance().getNbCities()]; //création d'une matrice vierge
	Solution[] s= new Solution[NombreFourmis];
	 for (int i=0; i<NombreFourmis; i++){
		 Fourmis f=new Fourmis(this.getInstance(),this.getTimeLimit(), pheromonescolonie); //Envoie d'une fourmi avec comme phéromones pheromonescolonie
		 while(f.EtatFourmi!=2) { //Tant que la fourmi est sur le graphe et n'est pas arrivée au nid
			 f.findNextSearchDestination();//Coeur de l'algorithme	 
		 }
		 Solution sol=new Solution(this.getInstance()); //Création de la solution i
		 for(int k=0;k<f.VillesVisitées.size();k++) {
			sol.setCityPosition(f.VillesVisitées.get(k), k);
		}
		sol.setCityPosition(0,f.VillesVisitées.size());
		s[i]=sol;
		pheromonescolonie=this.pheromones; //Actualisation des phéromones de la colonie
		if(i%20==0) { //On évapore les phéromones pour éviter une stagnation
			this.evaporate();
		}
	 }
	return s;	 	
}

/**
 * Sélectionne et renvoie la meilleur solution trouvée par la colonie de fourmis
 * @return la meilleure solution du tableau de solution
 * @throws Exception
 */
public Solution MeilleureFourmi() throws Exception {
	Solution[] s=this.Solution(this.nombre); //
	int m=0;
	double min=s[0].evaluate(); //Comparaison des ObjectiveValues des solutions
	for(int i=1;i<s.length;i++){
		if(s[i].evaluate()<min) {
			m=i;
			min=s[i].evaluate();
			System.out.println("Meilleure fourmi: "+m+" avec une distance de: "+s[m].evaluate());
		}
	}
	return s[m];	
}

/**
 * Evaporation des phéromones de evaporation pourcent
 */
public void evaporate(){
	for (int i=0; i<this.getInstance().getNbCities(); i++) {
		for (int j=0; j<i; j++){
			pheromones[i][j] =pheromones[i][j]*(100-evaporation) /100;
	        pheromones[j][i] = pheromones[i][j];              
		}
	}
}
}