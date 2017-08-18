package ca.talat.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarReflection {
	
	private static String fileExtension = ".jar" ; 

	/**
	 * Finds the list of all class files in a jar file and returns their fully qualified names
	 * @param jarFilePath
	 * @return List<String>
	 */
	private List<String> getClassesFromJar(String jarFilePath) {
		List<String> classNames = new ArrayList<String>();
		ZipInputStream zip = null;
		try {
			zip = new ZipInputStream(new FileInputStream(jarFilePath));
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
			    if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
			        String className = entry.getName().replace('/', '.'); // including ".class"
			        classNames.add(className.substring(0, className.length() - ".class".length()));
			    }
			}
			return classNames;
		} catch (Exception ex) {
			return null;
		} finally {
			try {
				zip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Finds all the class names within all the jar files across multiple levels of directory
	 * @param directoryPath
	 * @return List<String>
	 */
	public List<String> getAllContents(String directoryPath){
		List<String> classNames = new ArrayList<>();
		File folder = new File(directoryPath);
		File[] listOfFiles = folder.listFiles();
		
		for (int index=0; index<listOfFiles.length; index++) {
			if (listOfFiles[index].isFile()) {
				if(listOfFiles[index].getPath().endsWith(fileExtension)) {
					classNames.addAll(getClassesFromJar(listOfFiles[index].getPath()));
				}
			} else if (listOfFiles[index].isDirectory()) {
				classNames.addAll(getAllContents(listOfFiles[index].getPath()));
			}
		}		
		return classNames;
	}
	
	/**
	 * Finds all the jars that contains the class name sent as a parameter
	 * @param directoryPath
	 * @param searchClassName
	 * @return A map of Fully qualified class name and the jar file which contains the class name
	 */
	public Map<String, String> findClassesInJars(String directoryPath, String searchClassName){
		Map<String, String> classNames = new HashMap<>();
		File folder = new File(directoryPath);
		File[] listOfFiles = folder.listFiles();
		
		for (int index=0; index<listOfFiles.length; index++) {
			
			File currentFileOrFolder = listOfFiles[index];
			
			if (currentFileOrFolder.isFile()) {
				if(currentFileOrFolder.getPath().endsWith(fileExtension)) {
					classNames.putAll(isClassInJar(listOfFiles[index].getPath(), searchClassName));					
				}
			} else if (listOfFiles[index].isDirectory()) {
				classNames.putAll(findClassesInJars(listOfFiles[index].getPath(), searchClassName));
			}
		}		
		return classNames;
	}
	
	/**
	 * Finds if the jar file contains the class name and returns the fully qualified class names
	 * @param jarFilePath
	 * @param className
	 * @return The fully qualified class name
	 */
	private Map<String, String> isClassInJar(String jarFilePath, String className) {
	
		List<String> fullyQualifiedClassNames = getClassesFromJar(jarFilePath);
		Map<String, String> result = new HashMap<String, String>();
		
		for (int i=0; i<fullyQualifiedClassNames.size(); i++) {
			String fqClassName = fullyQualifiedClassNames.get(i);
			String shortClassName = extractShortClassName(fqClassName);
			if (shortClassName.toLowerCase().contains(className.toLowerCase())) {
				result.put(fqClassName, jarFilePath);
			}
		}
		
		return result;
	}
	
	/**
	 * Extract the short name (final class name) from the fully qualified class name
	 * @param fullClassName
	 * @return String - Class Name
	 */
	private String extractShortClassName(String fullClassName) {
		return fullClassName.substring(fullClassName.lastIndexOf('.'),fullClassName.length());
	}
}
