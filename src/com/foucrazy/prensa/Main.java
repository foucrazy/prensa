package com.foucrazy.prensa;

import java.io.File;
import java.io.IOException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3){
			log("Faltan argumentos");
			log("java -jar prensa.jar password dirToCompress compressSubDirectories");
			log("java -jar prensa.jar pruebas123 e:\\\\FG\\\\imagenes true");
			System.exit(-1);
		}
		
		//Compresion
		File dirToCompress=null;
		File outputDir=null;
		boolean compressSubDirectories=false;
		String password="";		
		try{
			password=args[0];
			dirToCompress=new File(args[1]);
			outputDir=new File(args[1]+"["+System.currentTimeMillis()+"]");
			compressSubDirectories=Boolean.parseBoolean(args[2]);
		}catch(IllegalArgumentException iaex){
			log("Argumentos inválidos.");
		}
		
		if (password!=null && !password.equals("") && dirToCompress!=null && dirToCompress.isDirectory()){
			Compressor compressor = new Compressor();
			try {
				compressor.compress(password, dirToCompress, outputDir, compressSubDirectories,true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		log("Fin.");
	}

	
	public static void log(String msg){		
		System.out.println(msg);
	}
}
