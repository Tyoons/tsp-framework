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
	
	/** 
	 * Change m_solution pour qu'il soit égal au PCC
	 *  */
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
	
	/** 
	 * Cree la matrice des edges  avec 1 pour un edge et 0 pour rien
	 * 
	 * @return La matrice de 0 et 1 associée au PCC
	 * @throws Exception
	 * */
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
	
	/**
	 * 
	 * @param Init
	 * 				La solution d'initialisation
	 * @return La matrice de 1 et de 0 associee a l initialisation
	 * @throws Exception
	 */
	public int[][] getInitOptPlusGene(Solution Init) throws Exception {
		
		int n = this.getInstance().getNbCities();
		int[][] matriceEdge = new int[n][n];
		for (int i=0; i<n-1;i++) {
			matriceEdge[Init.getCity(i)][Init.getCity(i+1)] = 1;
			matriceEdge[Init.getCity(i+1)][Init.getCity(i)] = 1;
		}
		matriceEdge[Init.getCity(n-1)][0] = 1;
		matriceEdge[0][Init.getCity(n-1)] = 1;
		return matriceEdge;
		}
	
	//------------------
	//--- Iterations ---
	//------------------
	
	/**
	 * 
	 * @param present
	 * 	La matrice de la solution actuelle 
	 * @return La matrice après une itération de 3 OPT
	 * @throws Exception
	 */
	public int[][] getMatriceSuivante(int[][] present) throws Exception {
		
		int n = this.getInstance().getNbCities();
		int[] trajet = trajet(present);
		
		// 1) Avoir les 3 edges
		int indiceSommet1 = (int)(Math.random() *(n));
		
		int indiceSommet3 = (int)(Math.random() *(n));
		while (indiceSommet1 == indiceSommet3) {
			indiceSommet3 =  (int)(Math.random() *(n));
		}
		int indiceSommet5 = (int)(Math.random() *(n));
		while (indiceSommet1 == indiceSommet5 || indiceSommet3 == indiceSommet5) {
			indiceSommet5 = (int)(Math.random() *(n));
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
	
		indiceSommet1 = trajet[tIndiceSommet[0]%n];
		indiceSommet3 = trajet[tIndiceSommet[1]%n];
		indiceSommet5 = trajet[tIndiceSommet[2]%n];
		
		int indiceSommet2 = getSuivant(indiceSommet1, present);
		int indiceSommet4 = getSuivant(indiceSommet3, present);
		int indiceSommet6 = getSuivant(indiceSommet5, present);
		
			
		long[] distance = new long[8];
		
		distance[0] = this.getInstance().getDistances(indiceSommet1, indiceSommet2)
				+this.getInstance().getDistances(indiceSommet3, indiceSommet4)
				+this.getInstance().getDistances(indiceSommet5, indiceSommet6);
		distance[1] = this.getInstance().getDistances(indiceSommet1, indiceSommet3)
				+this.getInstance().getDistances(indiceSommet2, indiceSommet4)
				+this.getInstance().getDistances(indiceSommet5, indiceSommet6);
		distance[2] = this.getInstance().getDistances(indiceSommet1, indiceSommet2)
				+this.getInstance().getDistances(indiceSommet3, indiceSommet5)
				+this.getInstance().getDistances(indiceSommet4, indiceSommet6);
		distance[3] = this.getInstance().getDistances(indiceSommet1, indiceSommet5)
				+this.getInstance().getDistances(indiceSommet3, indiceSommet4)
				+this.getInstance().getDistances(indiceSommet2, indiceSommet6);
		distance[4] = this.getInstance().getDistances(indiceSommet1, indiceSommet4)
				+this.getInstance().getDistances(indiceSommet3, indiceSommet5)
				+this.getInstance().getDistances(indiceSommet2, indiceSommet6);
		distance[5] = this.getInstance().getDistances(indiceSommet1, indiceSommet4)
				+this.getInstance().getDistances(indiceSommet3, indiceSommet6)
				+this.getInstance().getDistances(indiceSommet5, indiceSommet2);
		distance[6] = this.getInstance().getDistances(indiceSommet1, indiceSommet3)
				+this.getInstance().getDistances(indiceSommet6, indiceSommet4)
				+this.getInstance().getDistances(indiceSommet5, indiceSommet2);
		distance[7] = this.getInstance().getDistances(indiceSommet1, indiceSommet3)
				+this.getInstance().getDistances(indiceSommet6, indiceSommet4)
				+this.getInstance().getDistances(indiceSommet5, indiceSommet2);
		
		int indiceMin = 0;
		
		for (int i=1;i<8;i++) {
			if (distance[i]<distance[indiceMin]) {
				indiceMin = i;
			}
		}
		
		if (1 == indiceMin) {
			present[indiceSommet1][indiceSommet2]=0;
			present[indiceSommet3][indiceSommet4]=0;
			present[indiceSommet2][indiceSommet1]=0;
			present[indiceSommet4][indiceSommet3]=0;
			present[indiceSommet1][indiceSommet3]=1;
			present[indiceSommet2][indiceSommet4]=1;
			present[indiceSommet3][indiceSommet1]=1;
			present[indiceSommet4][indiceSommet2]=1;
		}
		
		if (2 == indiceMin) {
			present[indiceSommet3][indiceSommet4]=0;
			present[indiceSommet4][indiceSommet3]=0;
			present[indiceSommet5][indiceSommet6]=0;
			present[indiceSommet6][indiceSommet5]=0;
			present[indiceSommet3][indiceSommet5]=1;
			present[indiceSommet5][indiceSommet3]=1;
			present[indiceSommet4][indiceSommet6]=1;
			present[indiceSommet6][indiceSommet4]=1;
		}
		
		if (3 == indiceMin) {
		present[indiceSommet1][indiceSommet2]=0;
		present[indiceSommet2][indiceSommet1]=0;
		present[indiceSommet5][indiceSommet6]=0;
		present[indiceSommet6][indiceSommet5]=0;
		present[indiceSommet1][indiceSommet5]=1;
		present[indiceSommet5][indiceSommet1]=1;
		present[indiceSommet2][indiceSommet6]=1;
		present[indiceSommet6][indiceSommet2]=1;
		}
		
		if (4 == indiceMin) {
			present[indiceSommet1][indiceSommet2]=0;
			present[indiceSommet2][indiceSommet1]=0;
			present[indiceSommet3][indiceSommet4]=0;
			present[indiceSommet4][indiceSommet3]=0;
			present[indiceSommet5][indiceSommet6]=0;
			present[indiceSommet6][indiceSommet5]=0;
			present[indiceSommet1][indiceSommet4]=1;
			present[indiceSommet4][indiceSommet1]=1;
			present[indiceSommet2][indiceSommet6]=1;
			present[indiceSommet6][indiceSommet2]=1;
			present[indiceSommet3][indiceSommet5]=1;
			present[indiceSommet5][indiceSommet3]=1;
		}
		
		if (5 == indiceMin) {
			present[indiceSommet1][indiceSommet2]=0;
			present[indiceSommet2][indiceSommet1]=0;
			present[indiceSommet3][indiceSommet4]=0;
			present[indiceSommet4][indiceSommet3]=0;
			present[indiceSommet5][indiceSommet6]=0;
			present[indiceSommet6][indiceSommet5]=0;
			present[indiceSommet1][indiceSommet4]=1;
			present[indiceSommet4][indiceSommet1]=1;
			present[indiceSommet2][indiceSommet5]=1;
			present[indiceSommet5][indiceSommet2]=1;
			present[indiceSommet3][indiceSommet6]=1;
			present[indiceSommet6][indiceSommet3]=1;
		}
		
		if (6 == indiceMin) {
			present[indiceSommet1][indiceSommet2]=0;
			present[indiceSommet2][indiceSommet1]=0;
			present[indiceSommet3][indiceSommet4]=0;
			present[indiceSommet4][indiceSommet3]=0;
			present[indiceSommet5][indiceSommet6]=0;
			present[indiceSommet6][indiceSommet5]=0;
			present[indiceSommet1][indiceSommet3]=1;
			present[indiceSommet3][indiceSommet1]=1;
			present[indiceSommet2][indiceSommet5]=1;
			present[indiceSommet5][indiceSommet2]=1;
			present[indiceSommet4][indiceSommet6]=1;
			present[indiceSommet6][indiceSommet4]=1;
		}
		
		if (7 == indiceMin) {
			present[indiceSommet1][indiceSommet2]=0;
			present[indiceSommet2][indiceSommet1]=0;
			present[indiceSommet3][indiceSommet4]=0;
			present[indiceSommet4][indiceSommet3]=0;
			present[indiceSommet5][indiceSommet6]=0;
			present[indiceSommet6][indiceSommet5]=0;
			present[indiceSommet1][indiceSommet3]=1;
			present[indiceSommet3][indiceSommet1]=1;
			present[indiceSommet2][indiceSommet5]=1;
			present[indiceSommet5][indiceSommet2]=1;
			present[indiceSommet4][indiceSommet6]=1;
			present[indiceSommet6][indiceSommet4]=1;
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
		return present;
	}
	
	//------------
	//--- Main ---
	//------------
	
	/**
	 * 
	 * @param dureeMs
	 * la duree pendant laquelle on fait trourner l algo
	 * @return la solution apres dureeMs Millisecondes d iterations de 3 opt
	 * @throws Exception
	 */
	public Solution OPTMain(long dureeMs) throws Exception {
		
		int[][] matriceEdge = this.getInitOpt();
		
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		int nbIterations = 0;
		int iterations = 0;
		long poids;
		do
		{
			poids = poids(matriceEdge);
			matriceEdge = this.getMatriceSuivante(matriceEdge);
			if (poids > poids(matriceEdge)) {
				iterations = 0;
				//System.out.println("non");
			} else {
				iterations++;
				//System.out.println("oui");
			}
			nbIterations++;
			spentTime = System.currentTimeMillis() - startTime;
		}while(spentTime < (dureeMs));
		// j'ai commenté ton systeme.out.println on doit pas en faire 
		// Mayeul
			//System.out.println(nbIterations);
			//System.out.println("Final");
			//printMatrice(matriceEdge);
		
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
	

	/**
	 * 
	 * @param element
	 * @param tableau
	 * @return True si l element est dans le tableau
	 * 		   false sinon
	 */
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

	/**
	 * 
	 * @param edge 
	 * La matrice correspondant au trajet
	 * @return Un tableau ranger dans l'ordre de passage du voyageur
	 * @throws Exception
	 */
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

	/**
	 * 
	 * @param edge
	 * La matrice correspondant au trajet
	 * @return La distance du trajet 
	 */
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
	
	/** Affiche le tableau dans la console
	 * 
	 * @param tab 
	 * Le tableau en question  
	 */
	private static void printTableau(long[] tab) {
		String s = "{";
		for (int i=0;i<tab.length-1;i++) {
			s+=tab[i]+",";
		}
		s+=tab[tab.length-1]+"}";
		System.out.println(s);
	}
	
	/**
	 * Affiche la matrice dans la console
	 * @param matrix 
	 * La matrice en question 
	 */
	private static void printMatrice(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
		    for (int j = 0; j < matrix[i].length; j++) {
		        System.out.print(matrix[i][j] + " ");
		    }
		    System.out.println();
		}
	}
	
	/**
	 * 
	 * @param sommetPresent 
	 *  Le sommet present du voyageur
	 * @param matriceEdge 
	 *  La matrice du trajet 
	 * @return L indice de la ville suivante
	 * @throws Exception
	 */
	private int getSuivant(int sommetPresent, int[][] matriceEdge) throws Exception {
		int indice = 0;
		int[] trajet = trajet(matriceEdge);
		while (trajet[indice] != sommetPresent) {
			indice++;
		}
		return trajet[(indice+1)%trajet.length];
	}

	/**
	 * 
	 * @param tableau
	 * @return une copie de ce tableau
	 */
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

