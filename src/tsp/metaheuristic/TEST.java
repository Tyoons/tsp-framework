package tsp.metaheuristic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import tsp.Instance;


public class TEST extends ReductionInstance {

	public TEST(Instance instance, long timeLimit) {
		super(instance, timeLimit);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		
		Instance inst = new Instance("instances/kroA100.tsp", 2);
		 ArrayList<Instance> liste = new ArrayList<Instance>();
		 liste = reduction(inst);
		
		// TODO Auto-generated method stub

	}

}
