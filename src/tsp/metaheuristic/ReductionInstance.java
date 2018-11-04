package tsp.metaheuristic;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;




import tsp.Instance;
import tsp.Solution;
import tsp.TSPSolver;

public class ReductionInstance  extends TSPSolver {

	/**
	 * The Solution that will be returned by the program.
	 */
	private Solution m_solution;

	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;
	
	/** the list of instances */
	private ArrayList<Instance> ListeInstances;
	
	public ReductionInstance(Instance instance, long timeLimit) {
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
	public static Instance CreerCopieIndices (Instance inst, ArrayList<String> indices, int numeroGroupe) throws IOException {
		String nom = inst.getFileName();
		nom += numeroGroupe;
		try {
			PrintWriter fille = new PrintWriter(new FileWriter(new File(nom)));
			try (BufferedReader mere = new BufferedReader(new FileReader(inst.getFileName())))
	        {
	            String line;
	            String aecrire = "DIMENSION: " + indices.size() ;
	            fille.println("copie"+ numeroGroupe);
	            fille.println(aecrire);
	            do
	    		{
	    			line = mere.readLine();
	    		} while (!line.startsWith("DIMENSION"));
	     
	            
	    			line = mere.readLine();
	    			fille.println(line);
	    			line = mere.readLine();
	    			fille.println(line);
	    			// on recopie les deux lignes qui spécifient
	    			int i = 0;
	            while (!(line = mere.readLine()).startsWith("EOF") && i<indices.size()) {
	            	// on parcourt toutes les lignes
	            	
	            	
	            	if(indices.get(i).equals(line.split(" ")[0])) {
	           // on ne recopie que les lignes qui nous interressent
	            		fille.println(line);
	            		i++;
	            	}     
	            }
	            mere.close();
	        } 
			catch (IOException e) {
	            e.printStackTrace();
	        }
			fille.print("EOF" ); // end of file
			fille.close();
			}
			catch (IOException e)
			{
			e.printStackTrace();
			}
		Instance copie = new Instance(nom,inst.getType());
		return copie;
		
	}
	public static Instance  CreerCopie(Instance inst) throws IOException {
		
		String nom = inst.getFileName();
		nom += "_c";
		try {
			PrintWriter fille = new PrintWriter(new FileWriter(new File(nom)));
			try (BufferedReader mere = new BufferedReader(new FileReader(inst.getFileName())))
	        {
				String line ;
				fille.println("copie");
	            while (!(line = mere.readLine()).startsWith("EOF")) {
	            	// on parcourt toutes les lignes
	            		fille.println(line);
	            	}     
	            
	            mere.close();
	        } 
			catch (IOException e) {
	            e.printStackTrace();
	        }
			fille.print("EOF" ); // end of file
			fille.close();
			
		}
			
			catch (IOException e)
			{
			e.printStackTrace();
			}
		Instance copie = new Instance(nom,inst.getType());
		return copie;
	}
	
	public static Instance AjouterGroupe(Instance parent, Instance ajout, int indice) throws Exception {
		String nom = parent.getFileName();
		nom += "_";
		double[] barycentre = barycentre(ajout);
		try {
			PrintWriter fille = new PrintWriter(new FileWriter(new File(nom)));
			try (BufferedReader mere = new BufferedReader(new FileReader(parent.getFileName())))
	        {
	            String line;
	            String aecrire = "DIMENSION : " + (parent.getNbCities()+1);
	            fille.println(aecrire);
	            do
	    		{
	    			line = mere.readLine();
	    		} while (!line.startsWith("DIMENSION"));
	            
	    			line = mere.readLine();
	    			line = mere.readLine();
	    			fille.println(line);
	    			line = mere.readLine();
	    			fille.println(line);
	            while (!(line = mere.readLine()).startsWith("EOF")) {
	            	// on parcourt toutes les lignes
	            		fille.println(line);
	            	}     
	            
	            mere.close();
	        } 
			catch (IOException e) {
	            e.printStackTrace();
	        }
			fille.println(indice + " "+barycentre[0] + " "+barycentre[1]);
			fille.print("EOF" ); // end of file
			fille.close();
			
		}
			
			catch (IOException e)
			{
			e.printStackTrace();
			}
		Instance copie = new Instance(nom,parent.getType());
		return copie;
	
}
	public static Instance effacerIndices(Instance inst,ArrayList<String> indices) throws IOException {
		String nom = inst.getFileName() + "tortue";
		String line;
		try {
			PrintWriter fille = new PrintWriter(new FileWriter(new File(nom)));
			try (BufferedReader mere = new BufferedReader(new FileReader(inst.getFileName())))
	        {
				String aecrire = "DIMENSION: " + (inst.getNbCities()- indices.size()) ;
	            fille.println(aecrire);
				do
	    		{
	    			line = mere.readLine();
	    			fille.println(line);
	    		} while (!line.startsWith("DIMENSION"));
	        
				
	            
	    			line = mere.readLine();
	    			fille.println(line);
	    			line = mere.readLine();
	    			fille.println(line);
	    			System.out.println(line);
				int i = 0;
				while (!(line = mere.readLine()).startsWith("EOF")&& i<  indices.size() ) {
	            	// on parcourt toutes les lignes
					if((Integer.parseInt(indices.get(i)))!= Integer.parseInt(line.split(" ")[0])) {
						System.out.println(indices.get(i));
						System.out.println(indices.get(i).getClass());
						System.out.println(line.split(" ")[0] + "indces");
	            		fille.println(line);
	            	}
					else {
						System.out.println(indices.get(i));
						i++;
					}
					
				}
	            mere.close();
				 } 
				catch (IOException e) {
		            e.printStackTrace();
		        }
				fille.print("EOF" ); // end of file
				fille.close();
				}
				catch (IOException e)
				{
				e.printStackTrace();
				}
		Instance copie = new Instance(nom,inst.getType());
		return copie;
	}
	
	public static ArrayList<Instance> reduction(Instance inst) throws Exception{
		ArrayList<Instance> listeInstance = new ArrayList<Instance>();
		int n = listeInstance.get(0).getNbCities();
		double dmoy = d_moy( listeInstance.get(0));
		boolean ajout = false;
		for (int i = 0; i<n;i++) {
			ArrayList<String> indexes = new ArrayList<String>();
			for(int j =i;j<n;j++) {
				if(listeInstance.get(0).getDistances(i, j) < dmoy/5) {
					indexes.add(listeInstance.get(0).getLabel(j).toString());
					ajout = true;
				}
			}
				if(ajout) {
					indexes.add(0,listeInstance.get(0).getLabel(i).toString());
				
			
			
			listeInstance.add(CreerCopieIndices(listeInstance.get(0),indexes,i));
			listeInstance.add(0, effacerIndices(listeInstance.get(0),indexes));
			listeInstance.add(0,AjouterGroupe(listeInstance.get(0),listeInstance.get(listeInstance.size()-1),i));
			n = listeInstance.get(0).getNbCities();
				}
		}
		
		return listeInstance;
		
	}
public static Solution assemblage(Solution[] solutions, Instance inst) throws Exception {
	Solution sol = new Solution(inst);
	Solution mere = solutions[0];
	int k =1;
	for( int i =0; i< mere.getInstance().getNbCities(); i++) {
		if(mere.getCity(i)<inst.getNbCities()) {
			sol.setCityPosition(mere.getCity(i), i);
		}
		else {
			Solution groupe = solutions [k];			
			int	suivant = mere.getCity(i+1);	
			int [] ordonne = ordonner( inst,groupe, sol.getCity(i-1), suivant);
			k++;
		}
	}
	return sol;
	
	
}
public static int[] ordonner ( Instance inst,Solution solution, int precedent, int suivant) {
	int[] ordonne = new int[solution.getInstance().getNbCities()];
	
	return ordonne;
}
}


