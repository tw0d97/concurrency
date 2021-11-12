package concurrency;
import java.util.Random;

class Sum extends Thread {
	
	int[] numArray;
	int min, max, partial;
	
	public Sum(int[] numArray, int min, int max) { // fields and variables
		this.numArray = numArray;
		this.min = min;
		this.max = Math.min(max, numArray.length);
	} 
	
	public int getPartial() {
		return partial;
	}
	
	public void run() {
		partial = sum(numArray, min, max);
	}
	
	public static int sum(int[] arr) {
		return sum(arr, 0, arr.length);
	}
	
	public static int sum(int[] numArray, int min, int max) { // calculates sum of each element in given array
		int total = 0;
		
		for(int i = min; i < max; i++) {
			total += numArray[i];
		}
		return total;
	}
	
	public static int parallelSum(int[] numArray) { // runs parallel sum with a number of threads based on available processing power
		return parallelSum(numArray, Runtime.getRuntime().availableProcessors());
	}
	
	public static int parallelSum(int[] numArray, int threads) { // runs threads in parallel to calculate sum
		int size = (int)Math.ceil(numArray.length * 1.0 / threads);
		Sum[] sums = new Sum[threads];
		int total = 0;
		
		for(int i = 0; i < threads; i++) { // dynamically creates and runs threads based on computational resources available
			sums[i] = new Sum(numArray, i * size, (i + 1) * size);
			sums[i].start();
		}
		
		try { // tells threads to stop running after desired sum is calculated
			for(Sum sum : sums) {
				sum.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(Sum sum : sums ) { // adds each sum from each thread to a total sum 
			total += sum.getPartial();
		}
		return total;
	}
}

public class TestClass {

	public static void main(String[] args) {
		
		Random randNum = new Random();
		int[] numArray = new int[200000000];
		
		for(int i = 0; i < numArray.length; i++) { // populate array with 200,000,000 random numbers between 1 & 10
			numArray[i] = randNum.nextInt(10) + 1;
		}
		
		long start = System.currentTimeMillis(); // start timer
		System.out.println("The single thread array found the sum: " + Sum.sum(numArray) + " in " + (System.currentTimeMillis() - start) + " ms."); //single thread
		start = System.currentTimeMillis(); // restart timer
		System.out.println("\nThe parallel thread array found the sum: " + Sum.parallelSum(numArray)+ " in " + (System.currentTimeMillis() - start) + " ms."); //parallel
		

	}

}
