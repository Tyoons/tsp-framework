package tsp.metaheuristic;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.TSPSolver;

public class Clustering extends TSPSolver  {

	public Clustering(Instance instance, long timeLimit) {
		super(instance, timeLimit);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param inst une instance dont on vuet calculer la distance moyenne entre les sommets
	 * @return  un double : la dstance moyenne
	 */
	public static double d_moy(Instance inst) {
		long[][] distances = inst.getDistances();
		double dmoy;
		dmoy = 0;
		int n = inst.getNbCities();
		for(int i = 0; i<n; i++) {
			for (int j=0; j<i;j++) {
				dmoy+=distances [i][j];
			}
		}
		
		
	return dmoy/(n*(n+1)/2);
	}
/**
 * 
 * @param inst : l'instance dont on veut calculer le barycentre
 * @return un tableau de doubles : barycentre[0] contient la position en X et barycentre[1] celle en Y
 * @throws Exception
 */
	public static double[] barycentre(Instance inst) throws Exception {
		double [] barycentre= new double[2];
		double X = 0;
		double Y = 0;
		int n = inst.getNbCities();
		for(int i =0;i<n;i++) {
			 X+= inst.getX(i);
			 Y+= inst.getY(i);
		}
		X = X/n;
		Y = Y/n;
		barycentre[0] = X;
		barycentre[1] = Y;
		return barycentre;
	}

	/**
	 * 
	 * @param inst l'instance dont on veut copier certains indices
	 * @param labels les indices qui nous nterressent
	 * @return une instance qui ne contient que les sommets donc le nom correspond à labels
	 * @throws Exception
	 */
	public static Instance nouveauGroupe(Instance inst, ArrayList<String> labels) throws Exception {
		Instance nouveauGroupe = new  Instance(inst.getFileName(), inst.getType());
		int i = 0;
		int k =0;
		int n = labels.size();
		double [] m_x = new double[n];
		double [] m_y = new double[n];
		long[][] m_distances = new long[n][n];
		String[] m_labels = new String[n];
		while(i<n && k<inst.getNbCities()) {
			if(nouveauGroupe.getLabel(k).equals(labels.get(i))) {
				m_x[i]=nouveauGroupe.getX(k);
				m_y[i] = nouveauGroupe.getY(k);
				m_labels[i] = labels.get(i);
				i++;
				k++;
			}
			else {
				k++;
			}
		}
		for(i=0; i<n; i++) {
			for(int j=i;j<n;j++) {
				
				double dx = m_x[i] - m_x[j];
				double dy = m_y[i] - m_y[j];
				m_distances[i][j]= (long) Math.rint(Math.hypot(dx, dy));
				m_distances[j][i]= (long) Math.rint(Math.hypot(dx, dy));
			}
		}
		nouveauGroupe.setNbCities(n);
		nouveauGroupe.setM_distances(m_distances);
		nouveauGroupe.setM_x(m_x);
		nouveauGroupe.setM_y(m_y);
		nouveauGroupe.setM_labels(m_labels);
		return nouveauGroupe;
		
	}
/**
 * 
 * @param inst l'instance dont on veut retier les sommets choisis
 * @param labels les noms des sommets à retirer
 * @throws Exception
 */
public static void retirer(Instance inst, ArrayList<String>labels )throws Exception {
	int i = 0;
	int k =0;
	int w = 0;
	int n = labels.size();
	int m = inst.getNbCities();
	int l = m-n;
	double [] m_x = new double[l];
	double [] m_y = new double[l];
	long[][] m_distances = new long[l][l];
	String[] m_labels = new String[l];
	
	while(i<n && k<m && w<l) {
		if(!inst.getLabel(k).equals(labels.get(i))) {
			m_x[w]=inst.getX(k);
			m_y[w] = inst.getY(k);
			m_labels[w] = inst.getLabel(k);
			k++;
			w++;
			
		}
		else {
			k++;
			i++;
		}
	}
	for(i=0; i<l; i++) {
		for(int j=i;j<l;j++) {
			
			double dx = m_x[i] - m_x[j];
			double dy = m_y[i] - m_y[j];
			m_distances[i][j]= (long) Math.rint(Math.hypot(dx, dy));
			m_distances[j][i]= (long) Math.rint(Math.hypot(dx, dy));
		}
	
}
	inst.setNbCities(l);
	inst.setM_distances(m_distances);
	inst.setM_x(m_x);
	inst.setM_y(m_y);
	inst.setM_labels(m_labels);
}
/**
 * 
 * @param inst l'instance à laquelle on veut ajouter un nouveau groupe de sommets
 * @param groupe l'instance qui correspond aux groupe de sommets à ajouter
 * @param indice le noms que l'on va donner à ce nouveau groupe
 * @throws Exception
 */
public static void ajouter(Instance inst, Instance groupe, int indice) throws Exception {
	int n = inst.getNbCities()+1;
	double[] barycentre = barycentre(groupe);
	double [] m_x = new double[n];
	double [] m_y = new double[n];
	long[][] m_distances = new long[n][n];
	String[] m_labels = new String[n];
	for(int i =0; i<n-1;i++) {
		m_x[i]=inst.getX(i);
		m_y[i] = inst.getY(i);
		m_labels[i] = inst.getLabel(i);
	}
	m_x[n-1] = barycentre[0];
	m_y[n-1] = barycentre[1];
	m_labels[n-1] = indice+"";
	for(int i=0; i<n; i++) {
		for(int j=i;j<n;j++) {
			
			double dx = m_x[i] - m_x[j];
			double dy = m_y[i] - m_y[j];
			m_distances[i][j]= (long) Math.rint(Math.hypot(dx, dy));
			m_distances[j][i]= (long) Math.rint(Math.hypot(dx, dy));
		}
}
	inst.setNbCities(n);
	inst.setM_distances(m_distances);
	inst.setM_x(m_x);
	inst.setM_y(m_y);
	inst.setM_labels(m_labels);
	
}
/**
 * 
 * @param inst l'instance que l'on veut réduire
 * @return une liste d'instances. La première correspond au niveau le plus "dézoomé" et les suivantes correspondent aux petits mondes suivants.
 * @throws Exception
 */
public static ArrayList<Instance> reduction(Instance inst) throws Exception{
	ArrayList<Instance> listeInstance = new ArrayList<Instance>();
	listeInstance.add(inst);
	int nb_cities = inst.getNbCities();
	int nb_monde = listeInstance.size();
	int n = listeInstance.get(0).getNbCities();
	double dmoy = d_moy( listeInstance.get(0));
	boolean ajout = false;
	for (int i = 0; i<n;i++) {
		ArrayList<String> indexes = new ArrayList<String>();
		for(int j =i+1;j<n;j++) {
			
				for(int k =0; k< indexes.size();k++){
					if(listeInstance.get(0).getDistances(Integer.parseInt(indexes.get(k)),j)< dmoy/10) {
						System.out.println(listeInstance.get(0).getLabel(j) + "," +j + "," + listeInstance.get(0).getNbCities());

						if(!indexes.contains(listeInstance.get(0).getLabel(j)) &&  Integer.parseInt(listeInstance.get(0).getLabel(j)) <n ){
						indexes.add(listeInstance.get(0).getLabel(j));
					}
					}
				}
			
			
			if(listeInstance.get(0).getDistances(i, j) < dmoy/10 && !indexes.contains(listeInstance.get(0).getLabel(j))  &&  Integer.parseInt(listeInstance.get(0).getLabel(j)) <n ) {
				indexes.add(listeInstance.get(0).getLabel(j));
				ajout = true;
				
			}
		}
			if(ajout) {
				indexes.add(0,listeInstance.get(0).getLabel(i));
				
			
		
		
		listeInstance.add(nouveauGroupe(listeInstance.get(0),indexes));
		nb_monde = listeInstance.size();
		retirer(listeInstance.get(0),indexes);
		ajouter(listeInstance.get(0),listeInstance.get(listeInstance.size()-1),nb_monde + nb_cities);
		n = listeInstance.get(0).getNbCities();
			}
	}
	
	return listeInstance;
	
}
/**
 * 
 * @param solutions Le tableau des solutions à assembler
 * @param inst L'insatnce à partir de laquelle les solutions ont été calculéés
 * @return une solution qui correspond à l'assemblage des solutions : on raccorde un monde au suivant par le plus proche sommet,
 *  dans l'ordre défini par la solution "dézoommée" stockée dans Solution[0];
 * @throws Exception
 */
public static Solution assemblage(Solution[] solutions, Instance inst) throws Exception {
	Solution sol = new Solution(inst);
	Solution mere = solutions[0];
	int k =1;
	int pos = 0;
	for( int i =0; i< mere.getInstance().getNbCities(); i++) {
		if(mere.getCity(i)<inst.getNbCities()) {
			sol.setCityPosition(mere.getCity(i), pos);
			pos++;
		}
		else {
			Solution groupe = solutions [k];
			int [] ordonne = new int[groupe.getInstance().getNbCities()];
			ordonne = ordonner( inst,groupe, sol.getCity(i-1));
			k++;
			for(int ind = 0; ind<ordonne.length; ind++) {
				sol.setCityPosition(ordonne[ind], pos);
				pos++;
			}
		}
	}
	return sol;
}
/**
 * 
 * @param inst l'instance à partir de laquelle la solution à été établie
 * @param solution la solution d'un monde que l'on veut raccorder à la solution dézoomée
 * @param precedent le sommet précédent dans la solution dézoomée
 * @return une liste d'indices qui commence par le sommet le plus proche de precedent,
 * et qui contient tous les sommets dans l'ordre établit par la résolution
 * @throws Exception
 */
public static int[] ordonner ( Instance inst,Solution solution, int precedent) throws Exception {
	int n = solution.getInstance().getNbCities();
	int[] ordonne = new int[n];
	long distance = 0;
	long distanceMin = inst.getDistances(solution.getCity(0), precedent);
	int indice = 0;
	for(int i =0 ; i<n; i++) {
		distance = inst.getDistances(solution.getCity(i), precedent);
		if(distance< distanceMin) {
			distanceMin = distance;
			indice = i;
		}
	}
	for(int i = 0; i<n; i++) {
		ordonne[i] = solution.getCity(indice);
		indice = (indice+1)%n;
	}
	return ordonne;
}
}


