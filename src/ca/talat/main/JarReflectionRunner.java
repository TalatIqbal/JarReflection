package ca.talat.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
		Scanner scanner = null;
		
		try {			
			while(true) {
				System.out.println("\n\nSelect Option\n\t1.List All Classes\n\t2. Find a class\n\tAny other input to exit");
				scanner = new Scanner(System.in);
				int input = scanner.nextInt();
				
				JarReflection jarReflection = new JarReflection();
				
				switch(input) {
					case 1:						
						List<String> allClassNames = new ArrayList<>();
						allClassNames.addAll(jarReflection.getAllContents(directoryPath));
	
						// Output
						allClassNames.forEach((className) -> {
							System.out.println(className);
						});
						
						break;
					case 2:
						
						System.out.print("Enter the class name: ");
						scanner = new Scanner(System.in);
						String inputClassName = scanner.nextLine();
						
						Map<String, String> allJars = new HashMap<>();
						allJars.putAll(jarReflection.findClassesInJars(directoryPath, inputClassName));
	
						// Output
						allJars.forEach((className,jarFile) -> {
							System.out.println(className+"\t"+jarFile);
						});
						
						break ;
					default:
						return;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out.println("Bye Bye");
			scanner.close();
		}	
	}
}
