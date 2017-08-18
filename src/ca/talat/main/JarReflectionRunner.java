package ca.talat.main;

import java.util.ArrayList;
import java.util.List;

public class JarReflectionRunner {
	
	/**
	 * Main function to find the classes from a jar file
	 * @param args - first argument is the directory
	 */
	public static void main (String[] args) {
		
		if (args.length < 1) {
			System.out.println("Need at least one argument - directory name");
		}
		String directoryPath = args[0];
		JarReflection jarReflection = new JarReflection();
		List<String> allClassNames = new ArrayList<>();
		allClassNames.addAll(jarReflection.getAllContents(directoryPath));
		
		// Output
		allClassNames.forEach((className) -> {
			System.out.println(className);
		});
	}
}
