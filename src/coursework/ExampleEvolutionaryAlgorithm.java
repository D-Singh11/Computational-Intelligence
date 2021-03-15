package coursework;

import java.util.ArrayList;
import java.util.Iterator;

import model.Fitness;
import model.Individual;
import model.LunarParameters.DataSet;
import model.NeuralNetwork;

/**
 * Implements a basic Evolutionary Algorithm to train a Neural Network
 * 
 * You Can Use This Class to implement your EA or implement your own class that extends {@link NeuralNetwork} 
 * 
 */
public class ExampleEvolutionaryAlgorithm extends NeuralNetwork {
	

	/**
	 * The Main Evolutionary Loop
	 */
	@Override
	public void run() {		
		//Initialise a population of Individuals with random weights
		population = initialise();

		//Record a copy of the best Individual in the population
		best = getBest();
		System.out.println("Best From Initialisation " + best);

		/**
		 * main EA processing loop
		 */		
		
		while (evaluations < Parameters.maxEvaluations) {

			/**
			 * this is a skeleton EA - you need to add the methods.
			 * You can also change the EA if you want 
			 * You must set the best Individual at the end of a run
			 * 
			 */

			
	// SELECTION
			// Select 2 Individuals from the current population.
			ArrayList<Individual> parents = new ArrayList<Individual>();
//			parents = selectRandom();
//			parents.add(selectRoullete());	parents.add(selectRoullete());		// roulette selection

			parents = selectTournament();  // tournament selection
			
			

			
			
	// CROSSOVER
			// Generate a child by crossover.		
//			ArrayList<Individual> children = crossover1Point(parents.get(0), parents.get(1));
//			ArrayList<Individual> children = crossover2Point(parents.get(0), parents.get(1));
//			ArrayList<Individual> children = crossoverTwins(parents.get(0), parents.get(1));
			ArrayList<Individual> children = crossoverUniform(parents.get(0), parents.get(1));			
			
			
			
			
	// MUTATAION
			//mutate the offspring
			mutateStandard(children);
//			mutateRandomSwap(children);
//			mutataionInverse(children);
			
	// EVALUATION		
			// Evaluate the children
			
			evaluateIndividuals(children);			

	// REPLACEMENT
			// Replace children in population
//			replaceRandom(children);  
//			replaceWorst(children);
			replaceTournament(children);

			// check to see if the best has improved
			best = getBest();
			
			// Implemented in NN class. 
			outputStats();
			
			//Increment number of completed generations			
		}

		//save the trained network to disk
		saveNeuralNetwork();
	}

	

	/**
	 * Sets the fitness of the individuals passed as parameters (whole population)
	 * 
	 */
	private void evaluateIndividuals(ArrayList<Individual> individuals) {
		for (Individual individual : individuals) {
			individual.fitness = Fitness.evaluate(individual, this);
		}
	}


	/**
	 * Returns a copy of the best individual in the population
	 * 
	 */
	private Individual getBest() {
		best = null;;
		for (Individual individual : population) {
			if (best == null) {
				best = individual.copy();
			} else if (individual.fitness < best.fitness) {
				best = individual.copy();
			}
		}
		return best;
	}

	/**
	 * Generates a randomly initialised population
	 * 
	 */
	private ArrayList<Individual> initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.popSize; ++i) {
			//chromosome weights are initialised randomly in the constructor
			Individual individual = new Individual();
			population.add(individual);
		}
		evaluateIndividuals(population);
		return population;
	}
	
	
	
	
	

	/**
	 * Selection --
	 * 
	 * NEEDS REPLACED with proper selection this just returns a copy of a random
	 * member of the population
	 */
	
	
	
	private ArrayList<Individual> selectRandom() {		
//		Individual parent = population.get(Parameters.random.nextInt(Parameters.popSize));
		//******************************
		Individual parent = population.get(0);
		ArrayList<Individual> parents = new ArrayList<>();
		int firstParetIndex = Parameters.random.nextInt(Parameters.popSize);
		parent = population.get(firstParetIndex);
		parents.add(parent.copy());
		// second parent
		int exitloop = Parameters.popSize;
		for (int i = 0; i <= exitloop ; i++) {
			if (i != firstParetIndex) {
				parent = population.get(i);
				exitloop = i;
			}
		}
		parents.add(parent.copy());
		return parents;
		//******************************
	}

	
	/*************************  start- tournament selection, tournament size less than population ***********************/
	private ArrayList<Individual> selectTournament() {	
		ArrayList<Individual> parents = new ArrayList<>();
		Individual potentialParent = null;
		int parentIndex = 0;
		
		// randomly select k individuals from population to be candidates for tournament selection for first parent
		for (int i = 0; i < Parameters.selectionTournamentSize; i++) {		
			int random = Parameters.random.nextInt(Parameters.popSize -1);		// randomly choose individuals
			Individual indv = population.get(random);
			if (potentialParent == null) {
				potentialParent = indv;
			}
			else if(potentialParent.fitness > indv.fitness) {
				potentialParent = indv;						// assign best to variable
				parentIndex = random;
			} 
		}
		parents.add(potentialParent.copy());
		potentialParent = null;
		
		// perform tournament selection for second parent excluding parent 1 from population
		for (int i = 0; i <  Parameters.selectionTournamentSize; i++) {
			int random = Parameters.random.nextInt(Parameters.popSize -1);		// randomly choose individuals
			Individual indv = population.get(random);
			if ( random != parentIndex) {						// ensure parent 1 is excluded from second tournament
				if (potentialParent == null) {
					potentialParent = indv;
				}
				else if(potentialParent.fitness > indv.fitness) {
					potentialParent = indv;						// assign best to variable
				}
			} 
		}
		parents.add(potentialParent.copy());
		return parents;
	}
	
	/*************************  end- tournament selection ***********************/
	
private Individual selectRoullete() {	
		double totalFitness = 0;
		for (int i = 0; i < Parameters.popSize; i++) {
			totalFitness += population.get(i).fitness;
		}
		
		double selectionPoint = Parameters.random.nextDouble() * totalFitness;

		Individual parent = new Individual();
		double abc = 0;
		for (int i = 0; i < Parameters.popSize; i++) {
			double flipFitness = 1/population.get(i).fitness;  // doing this because best fitness is individual with fitness value close to 0;
			abc += flipFitness;
			if (abc < selectionPoint) {
				parent = population.get(i);
				return parent;
			}
		}
		return parent;
	}
	
	
	
	



	
	//***************************************
	/**
	 * Crossover / Reproduction
	 * 
	 * NEEDS REPLACED with proper method this code just returns exact copies of the
	 * parents. 
	 */


	private ArrayList<Individual> reproduce(Individual parent1, Individual parent2) {
	
		//***************** 1-point start**************	
		return crossoverUniform(parent1, parent2);
		//***************** 1-point end**************
	} 
	
	
	private ArrayList<Individual> crossover1Point(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
		
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		int chromosomeLength = parent1.chromosome.length;
		//***************** 1-point start**************
		
		int cutPoint = Parameters.random.nextInt(chromosomeLength);
		
		for (int i = 0; i < cutPoint; i++) {
			child1.chromosome[i] = parent1.chromosome[i];		// child1 first half from parent 1
			child2.chromosome[i] = parent2.chromosome[i];		// child2 first half from parent 2
		}
		for (int i = cutPoint; i < chromosomeLength; i++) {
			child1.chromosome[i] = parent2.chromosome[i];		// child 1 second half of genes from parent 2
			child2.chromosome[i] = parent1.chromosome[i];		// child 2 second half of genes from parent 1
		}
		children.add(child1.copy());
		children.add(child2.copy());		
		return children;
		//***************** 1-point end**************
	}
	
	private ArrayList<Individual> crossoverTwins(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
		
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		int chromosomeLength = parent1.chromosome.length;
		//***************** Twin-Crossover start**************
		//*** It generates better results because increases the overall fitness of population by introducing twins in each cycle.
		
		int cutPoint = Parameters.random.nextInt(chromosomeLength);
		
		for (int i = 0; i < cutPoint; i++) {
			child1.chromosome[i] = parent1.chromosome[i];		// child1 first half from parent 1
			child2.chromosome[i] = parent2.chromosome[i];		// child2 first half from parent 2
		}
		for (int i = cutPoint; i < chromosomeLength; i++) {
			child1.chromosome[i] = parent2.chromosome[i];		// child 1 second half of genes from parent 2
			child2.chromosome[i] = parent1.chromosome[i];		// child 2 second half of genes from parent 1
		}
		
		// deciding which child to use genearte twins
		if (Parameters.random.nextBoolean()) {
			children.add(child1.copy());
			children.add(child1.copy());	// twins with child1
		} else {
			children.add(child2.copy());
			children.add(child2.copy());	// twins with child2
		}
			
		return children;
		//***************** Twin-Crossover end**************
	}
	
	private ArrayList<Individual> crossover2Point(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
		
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		child1.chromosome = parent1.chromosome;			// copied genes of parent 1 to child1
		child1.chromosome = parent2.chromosome;			// copied genes of parent 2 to child2
		
		int chromosomeLength = parent1.chromosome.length;
		//***************** 2-point start**************
		
		int cutPoint1 = Parameters.random.nextInt(chromosomeLength/2)+5;
		int cutPoint2 = cutPoint1 + (Parameters.random.nextInt(10) + 2);
		
		for (int i = cutPoint1; i <= cutPoint2; i++) {
			child1.chromosome[i] = parent2.chromosome[i];		// child1 2 point middle from from parent 2
			child2.chromosome[i] = parent1.chromosome[i];		// child2 2 point middle from from parent 1
		}
		children.add(child1.copy());
		children.add(child2.copy());		
		return children;
		//***************** 2-point end**************
	}
	
	private ArrayList<Individual> crossoverUniform(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
		
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		int chromosomeLength = parent1.chromosome.length;
		//***************** uniform Crossover start**************
		
		int tossResult = 0;  // uniformly choose which parent to choose gene from
		
		for (int i = 0; i < chromosomeLength; i++) {
			tossResult = Parameters.random.nextInt(2);				// randomly choose 1 or 0;
			if (tossResult == 0) {
				child1.chromosome[i] = parent1.chromosome[i];		// child1 first half from parent 1
				child2.chromosome[i] = parent2.chromosome[i];		// child2 first half from parent 2	
			}
			else if (tossResult == 1) {								// if toss result 1 then swap parent gene to assign to children
				child1.chromosome[i] = parent2.chromosome[i];		// child1 first half from parent 2
				child2.chromosome[i] = parent1.chromosome[i];		// child2 first half from parent 1	
			}
		}
		children.add(child1.copy());
		children.add(child2.copy());		
		return children;
		//***************** uniform Crossover end**************
	}



	
	/**
	 * Mutation
	 * 
	 * 
	 */
	
	
	private void mutateStandard(ArrayList<Individual> individuals) {		
		for(Individual individual : individuals) {
			for (int i = 0; i < individual.chromosome.length; i++) {
				if (Parameters.random.nextDouble() < Parameters.mutateRate) {
					if (Parameters.random.nextBoolean()) {
						individual.chromosome[i] += (Parameters.mutateChange);
					} else {
						individual.chromosome[i] -= (Parameters.mutateChange);
					}
				}
			}
		}		
	}
	
	
	
	// select 2 random genes and swap their values
	private void mutateRandomSwap(ArrayList<Individual> individuals) {
		
		int chromosomeLength = individuals.get(0).chromosome.length;
		for(Individual individual : individuals) {
			if (Parameters.random.nextDouble() < Parameters.mutateRate) {
				// randomly choose two genes
				int gene1 = Parameters.random.nextInt(chromosomeLength - 1);
				int gene2 = Parameters.random.nextInt(chromosomeLength - 1);
				double gene1Value  = individual.chromosome[gene1];					// save gene1 value before change
				individual.chromosome[gene1] = individual.chromosome[gene2];		//		swap values
				individual.chromosome[gene2] = gene1Value;
				}
		}		
	}
	
	
	private void mutataionInverse(ArrayList<Individual> individuals) {
		// choose couple of genes from each individual and then inverse the position
		// example choose from index 3 - 10. replace 10 with 3rd, 9th with 4th and so on.
		int chromosomeLength = individuals.get(0).chromosome.length;
		for(Individual individual : individuals) {
			if (Parameters.random.nextDouble() < Parameters.mutateRate) {
				// randomly choose two genes
				int rangeStart = Parameters.random.nextInt(chromosomeLength/2);
				int rangeEnd = rangeStart + Parameters.random.nextInt(chromosomeLength/2);
				double [] inverseList = new double[rangeEnd-rangeStart];
				int index = 0;
				for (int i = rangeStart; i < rangeEnd; i++) {
					inverseList[index] = individual.chromosome[i];
					index++;	
				}
				index = index - 1;
				for (int i = rangeStart; i < rangeEnd; i++) {
					individual.chromosome[i] = inverseList[index];
					index--;
				}
			}	
		}
	}		
	
	
	

	/**
	 * 
	 * Replaces the random member of the population 
	 * (fitness based)
	 * 
	 */
	private void replaceRandom(ArrayList<Individual> individuals) {
		int firstChildIdx = 0;
		for(Individual individual : individuals) {
			int idx = Parameters.random.nextInt(Parameters.popSize);
			if (firstChildIdx != idx) {
				if (population.get(idx).fitness > individual.fitness) {				// only replace if new child dont have worst fitness than worst in population
					population.set(idx, individual);
					firstChildIdx = idx;
				}
			}
			
		}		
	}
	
	
	/**
	 * 
	 * Replaces the worst member of the population 
	 * (fitness based)
	 * 
	 */
	private void replaceWorst(ArrayList<Individual> individuals) {
		for(Individual individual : individuals) {
			int idx = getWorstIndex();
			if (population.get(idx).fitness > individual.fitness) {				// only replace if new child dont have worst fitness than worst in population
				population.set(idx, individual);
			}
		}		
	}
	
	
	/**
	 * 
	 * Replaces the worst member of the tournament
	 * (fitness based)
	 * 
	 */
	private void replaceTournament(ArrayList<Individual> individuals) {
		
		// randomly select  individuals from population to be candidates for tournament selection
		for (Individual individual : individuals) {
			int index = 0;
			Individual worstCandidate = null;
			for (int i = 0; i <  Parameters.replaceTournamentSize; i++) {
				int random = Parameters.random.nextInt(Parameters.popSize -1);		// stores index of member in population arraylist
				Individual indv = population.get(random);
				if (worstCandidate == null) {
					worstCandidate = indv;
					index = random;
				}
				else if (worstCandidate.fitness < indv.fitness) {			// if new candidate has larger fitness value than it becomes  worstCandidate
					worstCandidate = indv;
					index = random;				// assign worst to variable
				}
			}
			// perform tournament replacement - fitness bases
			if (worstCandidate.fitness > individual.fitness ) {
				population.set(index, individual);	// replace tournament winner with child only if child has better fitness will keep diversity	
			}
		}
	}

	

	/**
	 * Returns the index of the worst member of the population
	 * @return
	 */
	private int getWorstIndex() {
		Individual worst = null;
		int idx = -1;
		for (int i = 0; i < population.size(); i++) {
			Individual individual = population.get(i);
			if (worst == null) {
				worst = individual;
				idx = i;
			} else if (individual.fitness > worst.fitness) {
				worst = individual;
				idx = i; 
			}
		}
		return idx;
	}	

	@Override
	public double activationFunction(double x) {
//		return TanhFunction(x);			// Tanh 
		return ReLUFunction(x);			// RELU	
	}
	
	//Tanh
	private double TanhFunction(double x) {
		if (x < -20.0) {
			return -1.0;
		} else if (x > 20.0) {
			return 1.0;
		}
		return Math.tanh(x);
	}
	
	//// RELU
	private double ReLUFunction(double x) {
		if (x > 0) {
			return x;
		} else {
			return -1;
		}
	}
	
}
