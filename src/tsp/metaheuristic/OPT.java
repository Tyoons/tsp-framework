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
	
	//------------------
	//--- Iterations ---
	//------------------
	
	public int[][] getMatriceSuivante(int[][] present) {
		
		int n = this.getInstance().getNbCities();
		
		
		// 1) Avoir les 3 edges
		int indiceSommet1 = (int)(Math.random() *(n - 1));
		int indiceSommet2 = getSuivant(indiceSommet1, present);
		int indiceSommet3 = (int)(Math.random() *(n - 1));
		while (indiceSommet1 == indiceSommet3) {
			indiceSommet3 =  (int)(Math.random() *(n - 1));
		}
		int indiceSommet4 = getSuivant(indiceSommet3, present);
		int indiceSommet5 = (int)(Math.random() *(n - 1));
		while (indiceSommet1 == indiceSommet5 || indiceSommet3 == indiceSommet5) {
			indiceSommet5 = (int)(Math.random() *(n - 1));
		}
		int indiceSommet6 = getSuivant(indiceSommet5, present);
		
		
		// 2) Tester toutes les distances et renvoyer la meilleure
		long min = this.poids(present);
		int[][] matMin = present;
		
		int[][] opt1 = copyTableau(present);
			opt1[indiceSommet1][indiceSommet2]=0;
			opt1[indiceSommet3][indiceSommet4]=0;
			opt1[indiceSommet1][indiceSommet3]=1;
			opt1[indiceSommet2][indiceSommet4]=1;
			long opt1Poids = this.poids(opt1);
		if (opt1Poids<min) {
			min = opt1Poids;
			matMin = opt1;
		}
		
		int[][] opt2 = copyTableau(present);
			opt2[indiceSommet3][indiceSommet4]=0;
			opt2[indiceSommet5][indiceSommet6]=0;
			opt2[indiceSommet3][indiceSommet5]=1;
			opt2[indiceSommet4][indiceSommet6]=1;
			long  opt2Poids = this.poids(opt2);
		if (opt2Poids<min) {
			min = opt2Poids;
			matMin = opt2;
		}
		
		int[][] opt3 = copyTableau(present);
			opt3[indiceSommet1][indiceSommet2]=0;
			opt3[indiceSommet3][indiceSommet4]=0;
			opt3[indiceSommet5][indiceSommet6]=0;
			opt3[indiceSommet1][indiceSommet4]=1;
			opt3[indiceSommet5][indiceSommet2]=1;
			opt3[indiceSommet3][indiceSommet6]=1;
			long  opt3Poids = this.poids(opt3);
		if (opt3Poids<min) {
			min = opt3Poids;
			matMin = opt3;
		}
		
		int[][] opt4 = copyTableau(present);
			opt4[indiceSommet1][indiceSommet2]=0;
			opt4[indiceSommet3][indiceSommet4]=0;
			opt4[indiceSommet5][indiceSommet6]=0;
			opt4[indiceSommet6][indiceSommet2]=1;
			opt4[indiceSommet3][indiceSommet4]=1;
			opt4[indiceSommet5][indiceSommet1]=1;
			long  opt4Poids = this.poids(opt4);
		if (opt4Poids<min) {
			min = opt4Poids;
			matMin = opt4;
		}		
		System.out.println("Tableau Initial");
		printMatrice(present);
		System.out.println("Indice 1 "+indiceSommet1+" Indice 2 "+indiceSommet2);
		System.out.println("Indice 3 "+indiceSommet3+" Indice 4 "+indiceSommet4);
		System.out.println("Indice 5 "+indiceSommet5+" Indice 6 "+indiceSommet6);
		System.out.println("Tableau 1");
		printMatrice(opt1);
		System.out.println("Tableau 2");
		printMatrice(opt2);
		
		System.out.println("Tableau 3");
		printMatrice(opt3);
		System.out.println("Tableau 4");
		printMatrice(opt4);
		return matMin;
	}
	
	//------------
	//--- Main ---
	//------------
	
	public Solution OPTMain(int iteration) throws Exception {
		int[][] matriceEdge = this.getInitOpt();
		printTableau(this.trajet(matriceEdge));
		
		for (int i=0;i<iteration;i++) {
			matriceEdge = this.getMatriceSuivante(matriceEdge);
			System.out.println("Final");
			printMatrice(matriceEdge);
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
		int indice = 1 ;
		int present = 0;
		trajet[0]=0;
		while (trajet[edge.length-1]==-1) {
			present = this.getSuivant(present, edge);
			trajet[indice]= present;
			printTableau(trajet);
			System.out.println(indice);
			indice++;
		}
		return trajet;
	}

	/** Renvoies le poids d'un trajet */
	public long poids(int[][] edge) {
		long[][] distance = this.getInstance().getDistances();
		long S = 0;
		for (int i=0;i<edge.length;i++) {
			for (int j=0;i<edge.length;i++) {
				S+= edge[i][j]*distance[i][j];
			}
		}
		return S;
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

	private int getSuivant(int sommetPresent, int[][] matriceEdge) {
		int indice = 0;
		while (matriceEdge[sommetPresent][indice] != 1) {
			indice++;
		}
		return indice;
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

