package tsp.metaheuristic;
import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.TSPSolver;


public class OPT extends TSPSolver {
	/**
	 * The Solution that will be returned by the program.
	 */
	private Solution m_solution;

	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;

	
	/** Initalise l'instance */
	public OPT(Instance instance, long timeLimit) {
		super(instance, timeLimit);
	}
	
	//-----------------------
	//--- Iinitialisation ---
	//-----------------------
	
	/** Change m_solution pour qu'il soit égal au PCC */
	public void	initOPT() throws Exception {
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
	
	/** Cree la matrice des edges  avec 1 pour un edge et 0 pour rien*/
	public int[][] getInitOpt() throws Exception {
		this.initOPT();
		int n = this.getInstance().getNbCities();
		int[][] matriceEdge = new int[n][n];
		for (int i=0; i<n-1;i++) {
			matriceEdge[this.getSolution().getCity(i)][this.getSolution().getCity(i+1)] = 1;
			matriceEdge[this.getSolution().getCity(i+1)][this.getSolution().getCity(i)] = 1;
		}
		matriceEdge[this.getSolution().getCity(n-1)][0] = 1;
		matriceEdge[0][this.getSolution().getCity(n-1)] = 1;
		return matriceEdge;
		}
	
	public int[][] getInitOptPlusGene() throws Exception {
		Genetic initialisation = new Genetic(this.getInstance(),this.getTimeLimit());
		initialisation.getInitGene();
		initialisation.getSolution().evaluate();
		
		Solution[] population = initialisation.creationPopulation();
		for (int i=0;i<250;i++) {
			population = Genetic.iterationGene(population);
		}
		this.m_solution = population[0];
		
		int n = this.getInstance().getNbCities();
		int[][] matriceEdge = new int[n][n];
		for (int i=0; i<n-1;i++) {
			matriceEdge[this.getSolution().getCity(i)][this.getSolution().getCity(i+1)] = 1;
			matriceEdge[this.getSolution().getCity(i+1)][this.getSolution().getCity(i)] = 1;
		}
		matriceEdge[this.getSolution().getCity(n-1)][0] = 1;
		matriceEdge[0][this.getSolution().getCity(n-1)] = 1;
		return matriceEdge;
		}
	
	//------------------
	//--- Iterations ---
	//------------------
	
	public int[][] getMatriceSuivante(int[][] present) throws Exception {
		
		int n = this.getInstance().getNbCities();
		int[] trajet = trajet(present);
		
		// 1) Avoir les 3 edges
		int indiceSommet1 = (int)(Math.random() *(n - 1));
		
		int indiceSommet3 = (int)(Math.random() *(n - 1));
		while (indiceSommet1 == indiceSommet3) {
			indiceSommet3 =  (int)(Math.random() *(n - 1));
		}
		int indiceSommet5 = (int)(Math.random() *(n - 1));
		while (indiceSommet1 == indiceSommet5 || indiceSommet3 == indiceSommet5) {
			indiceSommet5 = (int)(Math.random() *(n - 1));
		}
		
		int[] tIndiceSommet = new int[3];
		while (trajet[tIndiceSommet[0]] != indiceSommet1) {
			tIndiceSommet[0] = tIndiceSommet[0]+1;
		}

		while (trajet[tIndiceSommet[1]] != indiceSommet3) {
			tIndiceSommet[1] = tIndiceSommet[1] + 1;
		}
	
		while (trajet[tIndiceSommet[2]] != indiceSommet5) {
			tIndiceSommet[2] = tIndiceSommet[2] + 1;
		}
		
		for (int i=0;i<3;i++) {
			int temp;
			for(int j=0;j<3-i-1;j++) {
				if(tIndiceSommet[j]>tIndiceSommet[j+1]) {
					temp = tIndiceSommet[j];
					tIndiceSommet[j] = tIndiceSommet[j+1];
					tIndiceSommet[j+1] = temp;
				}
			}
		}
	
		indiceSommet1 = trajet[tIndiceSommet[0]];
		indiceSommet3 = trajet[tIndiceSommet[1]];
		indiceSommet5 = trajet[tIndiceSommet[2]];
		
		int indiceSommet2 = getSuivant(indiceSommet1, present);
		int indiceSommet4 = getSuivant(indiceSommet3, present);
		int indiceSommet6 = getSuivant(indiceSommet5, present);
		
		
		// 2) Tester toutes les distances et renvoyer la meilleure
		long min = this.poids(present);
		int[][] matMin = present;
		
		int[][] opt1 = copyTableau(present);
			opt1[indiceSommet1][indiceSommet2]=0;
			opt1[indiceSommet3][indiceSommet4]=0;
			opt1[indiceSommet2][indiceSommet1]=0;
			opt1[indiceSommet4][indiceSommet3]=0;
			opt1[indiceSommet1][indiceSommet3]=1;
			opt1[indiceSommet2][indiceSommet4]=1;
			opt1[indiceSommet3][indiceSommet1]=1;
			opt1[indiceSommet4][indiceSommet2]=1;
			long opt1Poids = this.poids(opt1);
		if (opt1Poids<min) {
			min = opt1Poids;
			matMin = opt1;
		}
		
		int[][] opt2 = copyTableau(present);
			opt2[indiceSommet3][indiceSommet4]=0;
			opt2[indiceSommet4][indiceSommet3]=0;
			opt2[indiceSommet5][indiceSommet6]=0;
			opt2[indiceSommet6][indiceSommet5]=0;
			opt2[indiceSommet3][indiceSommet5]=1;
			opt2[indiceSommet5][indiceSommet3]=1;
			opt2[indiceSommet4][indiceSommet6]=1;
			opt2[indiceSommet6][indiceSommet4]=1;
			long  opt2Poids = this.poids(opt2);
		if (opt2Poids<min) {
			min = opt2Poids;
			matMin = opt2;
		}
		
		int[][] opt3 = copyTableau(present);
		opt3[indiceSommet1][indiceSommet2]=0;
		opt3[indiceSommet2][indiceSommet1]=0;
		opt3[indiceSommet5][indiceSommet6]=0;
		opt3[indiceSommet6][indiceSommet5]=0;
		opt3[indiceSommet1][indiceSommet5]=1;
		opt3[indiceSommet5][indiceSommet1]=1;
		opt3[indiceSommet2][indiceSommet6]=1;
		opt3[indiceSommet6][indiceSommet2]=1;
		long  opt3Poids = this.poids(opt3);
	if (opt3Poids<min) {
		min = opt3Poids;
		matMin = opt3;
	}
		
		int[][] opt4 = copyTableau(present);
			opt4[indiceSommet1][indiceSommet2]=0;
			opt4[indiceSommet2][indiceSommet1]=0;
			opt4[indiceSommet3][indiceSommet4]=0;
			opt4[indiceSommet4][indiceSommet3]=0;
			opt4[indiceSommet5][indiceSommet6]=0;
			opt4[indiceSommet6][indiceSommet5]=0;
			opt4[indiceSommet1][indiceSommet4]=1;
			opt4[indiceSommet4][indiceSommet1]=1;
			opt4[indiceSommet2][indiceSommet6]=1;
			opt4[indiceSommet6][indiceSommet2]=1;
			opt4[indiceSommet3][indiceSommet5]=1;
			opt4[indiceSommet5][indiceSommet3]=1;
			long  opt4Poids = this.poids(opt4);
		if (opt4Poids<min) {
			min = opt4Poids;
			matMin = opt4;
		}
		
		int[][] opt5 = copyTableau(present);
			opt5[indiceSommet1][indiceSommet2]=0;
			opt5[indiceSommet2][indiceSommet1]=0;
			opt5[indiceSommet3][indiceSommet4]=0;
			opt5[indiceSommet4][indiceSommet3]=0;
			opt5[indiceSommet5][indiceSommet6]=0;
			opt5[indiceSommet6][indiceSommet5]=0;
			opt5[indiceSommet1][indiceSommet4]=1;
			opt5[indiceSommet4][indiceSommet1]=1;
			opt5[indiceSommet2][indiceSommet5]=1;
			opt5[indiceSommet5][indiceSommet2]=1;
			opt5[indiceSommet3][indiceSommet6]=1;
			opt5[indiceSommet6][indiceSommet3]=1;
			long  opt5Poids = this.poids(opt5);
		if (opt5Poids<min) {
			min = opt5Poids;
			matMin = opt5;
		}	
		
		int[][] opt6 = copyTableau(present);
			opt6[indiceSommet1][indiceSommet2]=0;
			opt6[indiceSommet2][indiceSommet1]=0;
			opt6[indiceSommet3][indiceSommet4]=0;
			opt6[indiceSommet4][indiceSommet3]=0;
			opt6[indiceSommet5][indiceSommet6]=0;
			opt6[indiceSommet6][indiceSommet5]=0;
			opt6[indiceSommet1][indiceSommet3]=1;
			opt6[indiceSommet3][indiceSommet1]=1;
			opt6[indiceSommet2][indiceSommet5]=1;
			opt6[indiceSommet5][indiceSommet2]=1;
			opt6[indiceSommet4][indiceSommet6]=1;
			opt6[indiceSommet6][indiceSommet4]=1;
			long  opt6Poids = this.poids(opt6);
		if (opt6Poids<min) {
			min = opt6Poids;
			matMin = opt6;
		}
		
		int[][] opt7 = copyTableau(present);
			opt7[indiceSommet1][indiceSommet2]=0;
			opt7[indiceSommet2][indiceSommet1]=0;
			opt7[indiceSommet3][indiceSommet4]=0;
			opt7[indiceSommet4][indiceSommet3]=0;
			opt7[indiceSommet5][indiceSommet6]=0;
			opt7[indiceSommet6][indiceSommet5]=0;
			opt7[indiceSommet1][indiceSommet3]=1;
			opt7[indiceSommet3][indiceSommet1]=1;
			opt7[indiceSommet2][indiceSommet5]=1;
			opt7[indiceSommet5][indiceSommet2]=1;
			opt7[indiceSommet4][indiceSommet6]=1;
			opt7[indiceSommet6][indiceSommet4]=1;
			long  opt7Poids = this.poids(opt7);
		if (opt7Poids<min) {
			min = opt7Poids;
			matMin = opt7;
		}	
		
		
		/**System.out.println("Tableau Initial");
		printMatrice(present);
		printTableau(trajet);
		System.out.println("Indice "+indiceSommet1+" Indice "+indiceSommet2);
		System.out.println("Indice "+indiceSommet3+" Indice "+indiceSommet4);
		System.out.println("Indice "+indiceSommet5+" Indice "+indiceSommet6);
		
		/**
		System.out.println("Tableau 1");
		printMatrice(opt1);
		printTableau(trajet(opt1));
		System.out.println(opt1Poids);
		
		System.out.println("Tableau 2");
		printMatrice(opt2);
		printTableau(trajet(opt2));
		System.out.println(opt2Poids);
		
		System.out.println("Tableau 3");
		printMatrice(opt3);
		printTableau(trajet(opt3));
		System.out.println(opt3Poids);
		
		
		System.out.println("Tableau 4");
		printMatrice(opt4);
		printTableau(trajet(opt4));
		System.out.println(opt4Poids);
		
		System.out.println("Min"+min); */
		return matMin;
	}
	
	//------------
	//--- Main ---
	//------------
	
	public Solution OPTMain(int iteration) throws Exception {
		int[][] matriceEdge = this.getInitOpt();
		
		for (int i=0;i<iteration;i++) {
			matriceEdge = this.getMatriceSuivante(matriceEdge);
			//System.out.println("Final");
			//printMatrice(matriceEdge);
		}
		
		int[] tableauSolution = this.trajet(matriceEdge);
		for (int i=0;i<tableauSolution.length;i++) {
			this.getSolution().setCityPosition(tableauSolution[i], i);
		}
		this.getSolution().setCityPosition(0, tableauSolution.length);
		
		return this.getSolution();
	}
	
	//-------------------------
	//--- Fonctions Annexes ---
	//-------------------------
	
	/** Test si un element est dans un tableau  */
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

	/** Transforme la matrice des edge en trajet des villes */
	public int[] trajet(int[][] edge) throws Exception {
		int[] trajet = new int[edge.length];
		trajet[edge.length-1]=-1;
		int present = 0;
		int prec=0;
		int iteration=0;
		int indice;
		while (trajet[edge.length-1] == -1 && iteration<edge.length) {
			indice = 0;
			while(edge[present][indice]== 0 || indice==prec) {
				indice++;
			}
			trajet[iteration]=present;
			prec=present;
			present=indice;
			iteration++;
		}
		return trajet;
	}

	/** Renvoies le poids d'un trajet */
	public long poids(int[][] edge) {
		long[][] distance = this.getInstance().getDistances();
		long S = 0;
		for (int i=0;i<edge.length;i++) {
			for (int j=0;j<edge.length;j++) {
				S= S+edge[i][j]*distance[i][j];
			}
		}
		return S/2;
	}
	
	private static void printTableau(int[] tab) {
		String s = "{";
		for (int i=0;i<tab.length-1;i++) {
			s+=tab[i]+",";
		}
		s+=tab[tab.length-1]+"}";
		System.out.println(s);
	}
	
	private static void printMatrice(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
		    for (int j = 0; j < matrix[i].length; j++) {
		        System.out.print(matrix[i][j] + " ");
		    }
		    System.out.println();
		}
	}

	private int getSuivant(int sommetPresent, int[][] matriceEdge) throws Exception {
		int indice = 0;
		int[] trajet = trajet(matriceEdge);
		while (trajet[indice] != sommetPresent) {
			indice++;
		}
		return trajet[(indice+1)%trajet.length];
	}

	private int[][] copyTableau(int[][] tableau) {
		int[][] res = new int[tableau.length][tableau.length];
		for (int i=0;i<tableau.length;i++) {
			for (int j=0;j<tableau.length;j++) {
				res[i][j] = tableau[i][j];
			}
		}
		return res;
	}
	
}

