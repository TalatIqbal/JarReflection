package ca.talat.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarReflection {
	
	private static String fileExtension = ".jar" ;

	/**
	 * Finds the list of all class files in a jar file and returns their fully qualified names
	 * @param jarFilePath
	 * @return List<String>
	 */
	private List<String> processClassFiles(String jarFilePath) {
		List<String> classNames = new ArrayList<String>();
		try {
			ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFilePath));
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
			    if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
			        String className = entry.getName().replace('/', '.'); // including ".class"
			        classNames.add(className.substring(0, className.length() - ".class".length()));
			    }
			}
			return classNames;
		} catch (Exception ex) {
			return null;
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
					classNames.addAll(processClassFiles(listOfFiles[index].getPath()));
				}
			} else if (listOfFiles[index].isDirectory()) {
				classNames.addAll(getAllContents(listOfFiles[index].getPath()));
			}
		}
		
		return classNames;
	}
}
