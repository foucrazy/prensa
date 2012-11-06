package com.foucrazy;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import static com.foucrazy.Main.log;

public class Compressor {
	
	private final String ZIP_EXTENSSION=".zip";
	private final String XML_EXTENSSION=".xml";
	
	public Compressor() {				
	}

	public void compress(String password, File dirToCompress, File outputDir, boolean compressSubDirectories, boolean isParentDir) throws IOException {
		try {
			log("Comprimiendo:"+dirToCompress.getAbsolutePath()+" en "+outputDir.getAbsolutePath());
			
			if (outputDir.exists() && outputDir.listFiles().length>0)
				throw new IOException("El directorio de salida existe y no esta vacio.");
			else
				outputDir.mkdir();
			
			FileFilter onlyFilesFilter = new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory()?false:true;
				}
			};
			ArrayList<File> filesToAdd = new ArrayList<File>(Arrays.asList(dirToCompress.listFiles(onlyFilesFilter)));
			
			if (filesToAdd.size()>0){
				ZipFile zipFile = new ZipFile(outputDir+File.separator+normalizeName(dirToCompress.getName())+ZIP_EXTENSSION);			
				zipFile.setRunInThread(true);
				ZipParameters parameters = new ZipParameters();
				parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_MAXIMUM); 
				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
				parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
				parameters.setPassword(password);
	
				zipFile.addFiles(filesToAdd, parameters);
			}					
			
			//Informe del directorio actual
			Reporter reporter = new Reporter();
			reporter.generateXml(dirToCompress.listFiles(),outputDir+File.separator+normalizeName(dirToCompress.getName())+XML_EXTENSSION);
						
			//Compresion de los sub directorios
			if (compressSubDirectories){
				FileFilter onlyDirectoriesFilter = new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						return pathname.isDirectory()?true:false;
					}
				};			
				ArrayList<File> subDirectories = new ArrayList<File>(Arrays.asList(dirToCompress.listFiles(onlyDirectoriesFilter)));
				for (File dir : subDirectories){
					File outputDirCompresed=new File(outputDir+File.separator+dir.getName());
					compress(password, dir, outputDirCompresed, compressSubDirectories,false);
				}
			}			
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}	
	
	private String normalizeName(String name){
		return name.replace(" ", "_");	
	}
}
