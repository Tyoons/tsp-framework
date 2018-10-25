package tsp.metaheuristic;

import java.util.ArrayList;

import tsp.Instance;
import tsp.TSPSolver;

public class Clustering extends TSPSolver  {

	public Clustering(Instance instance, long timeLimit) {
		super(instance, timeLimit);
		// TODO Auto-generated constructor stub
	}

	
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

	
	public static Instance nouveauGroupe(Instance inst, ArrayList<String> labels) throws Exception {
		Instance nouveauGroupe = new  Instance(inst.getFileName(), inst.getType());
		int i = 0;
		int k =0;
		int n = labels.size();
		double [] m_x = new double[n];
		double [] m_y = new double[n];
		long[][] m_distances = new long[n][n];
		String[] m_labels = new String[n];
		while(i<n) {
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

public static void retirer(Instance inst, ArrayList<String>labels )throws Exception {
	int i = 0;
	int k =0;
	int n = labels.size();
	int m = inst.getNbCities();
	int l = m-n;
	double [] m_x = new double[l];
	double [] m_y = new double[l];
	long[][] m_distances = new long[l][l];
	String[] m_labels = new String[l];
	
	while(i<n) {
		if(!inst.getLabel(k).equals(labels.get(i))) {
			m_x[k]=inst.getX(k);
			m_y[k] = inst.getY(k);
			m_labels[k] = inst.getLabel(k);
			k++;
			
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
	inst.setNbCities(n);
	inst.setM_distances(m_distances);
	inst.setM_x(m_x);
	inst.setM_y(m_y);
	inst.setM_labels(m_labels);
}

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

public static ArrayList<Instance> reduction(Instance inst) throws Exception{
	ArrayList<Instance> listeInstance = new ArrayList<Instance>();
	int nb_cities = inst.getNbCities();
	int nb_monde = listeInstance.size();
	int n = listeInstance.get(0).getNbCities();
	double dmoy = d_moy( listeInstance.get(0));
	boolean ajout = false;
	for (int i = 0; i<n;i++) {
		ArrayList<String> indexes = new ArrayList<String>();
		for(int j =0;j<n;j++) {
			
				for(int k =0; k< indexes.size();k++){
					if(listeInstance.get(0).getDistances(Integer.parseInt(indexes.get(k)),j)< dmoy/5) {
						indexes.add(listeInstance.get(0).getLabel(j));
						
					}
				}
			
			
			if(listeInstance.get(0).getDistances(i, j) < dmoy/5) {
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

}


