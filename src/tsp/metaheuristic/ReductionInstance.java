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
	
	public double d_moy(Instance inst) {
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

	public double[] barycentre(Instance inst) throws Exception {
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
	public static void CreerCopieIndices (Instance inst, int[] indices, int numeroGroupe) {
		String nom = inst.getFileName();
		nom += numeroGroupe;
		try {
			PrintWriter fille = new PrintWriter(new FileWriter(new File(nom)));
			try (BufferedReader mere = new BufferedReader(new FileReader(inst.getFileName())))
	        {
	            String line;
	            String aecrire = "DIMENSION : " + indices.length ;
	            fille.println(aecrire);
	            do
	    		{
	    			line = mere.readLine();
	    			System.err.println(line);
	    		} while (!line.startsWith("DIMENSION"));
	            
	    			line = mere.readLine();
	    			fille.println(line);
	    			line = mere.readLine();
	    			fille.println(line);
	    			// on recopie les deux lignes qui spécifient
	    			int i = 0;
	            while (!(line = mere.readLine()).startsWith("EOF") && i<indices.length) {
	            	// on parcourt toutes les lignes
	            	System.out.println(indices[i]);
	            	if(indices[i] ==Integer.parseInt(line.split(" ")[0])) {
	           // on ne recopie que les lignes qui nous interressent
	            		fille.println(line);
	            		System.out.println(indices[i]);
	            		System.out.println(line);
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
		
	}
	public static Instance  CreerCopie(Instance inst) throws IOException {
		
		String nom = inst.getFileName();
		nom += "_copie";
		try {
			PrintWriter fille = new PrintWriter(new FileWriter(new File(nom)));
			try (BufferedReader mere = new BufferedReader(new FileReader(inst.getFileName())))
	        {
				String line ;
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
	public void effacerIndices(Instance inst,int[] indices) {
		
		
	}

}
