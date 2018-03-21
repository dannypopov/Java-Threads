package commascounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Demo {

	public static void main(String[] args) {
		int numberOfThreads = 1;
		long minDuration = Long.MAX_VALUE;
		int minThread = 0;
		System.out.println("\n"+countWithThreads(getWholeText("src/warpeace.txt"), 7)+" commas counted!\n");
		System.out.println("=============(1s = 1000ms)=============\n");
		while (numberOfThreads < 10) {
			long start = System.currentTimeMillis();
			countWithThreads(getWholeText("src/warpeace.txt"), numberOfThreads);
			long end = System.currentTimeMillis();
			long duration = end - start;
			System.out.println(numberOfThreads + " Threads" + "\n" + duration + " miliseconds \n");
			if (duration < minDuration) {
				minDuration = duration;
				minThread = numberOfThreads;
			}
			++numberOfThreads;
		}
		System.out.println(minThread+" Threads count with minimal duration("+minDuration+")");
	}

	// Helper func which reads the whole file into a string
	public static String getWholeText(String pathToFile) {
		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(pathToFile)));
		} catch (IOException e) {
		}
		return content;
	}

	public static int countWithThreads(String input, int numberOfThreads) {
		if (numberOfThreads > 0 && !input.isEmpty()) {
			int number = 0;
			// Split string into (input.length/number of threads) parts
			String[] parts = input.split("(?s)(?<=\\G.{" + (input.length() / numberOfThreads) + "})");
			ArrayList<CommaCounter> threads = new ArrayList<>();
			for (int i = 0; i < numberOfThreads; ++i) {
				// Give every part to a thread
				threads.add(new CommaCounter(parts[i]));
			}
			// run every thread and then join because we want to execute the code after the
			// cycle last !
			for (CommaCounter cc : threads) {
				cc.start();
				try {
					cc.join();
				} catch (InterruptedException e) {
				}
			}
			// Return number of all commas
			for (CommaCounter cc : threads) {
				number += cc.getCount();
			}
			return number;
		}
		// Otherwise -1 (error code)
		return -1;
	}
}
