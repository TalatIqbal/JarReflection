package ca.talat.main;

import java.util.ArrayList;
import java.util.List;

public class JarReflectionRunner {
	
	public static void main (String[] args) {		
		String directoryPath = "jars/";
		JarReflection jarReflection = new JarReflection();
		List<String> allClassNames = new ArrayList<>();
		allClassNames.addAll(jarReflection.getAllContents(directoryPath));
		
		// Output
		allClassNames.forEach((className) -> {
			System.out.println(className);
		});
	}
}
