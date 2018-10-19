package tsp.metaheuristic;

import java.io.File;
import java.io.IOException;

import tsp.Instance;


public class TEST extends ReductionInstance {

	public TEST(Instance instance, long timeLimit) {
		super(instance, timeLimit);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		
		Instance inst = new Instance("instances/kroA100.tsp", 2);
		 Instance instanceCopiee = CreerCopie(inst);
		int[] indices = new int[10];
		for (int i =0; i<10; i++) {
			indices [i]= 2*i+1;
		}
		
		CreerCopieIndices(instanceCopiee, indices,2);
		instanceCopiee = effacerIndices(instanceCopiee,indices);
		
		// TODO Auto-generated method stub

	}

}
