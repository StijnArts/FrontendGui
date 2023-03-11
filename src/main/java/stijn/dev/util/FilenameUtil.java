package stijn.dev.util;

import java.io.*;
import java.util.*;

public class FilenameUtil {


    public static ArrayList<String> extractFileExtensions(List<File> files) {
        ArrayList<String> fileExtensions = new ArrayList<>();
        for (File file : files) {
            fileExtensions.add(extractFileExtension(file).toLowerCase());
            System.out.println(extractFileExtension(file).toLowerCase());
        }
        return fileExtensions;
    }

    public static String extractFileExtension(File file) {
        String[] segments = file.getName().split("\\.");
        String fileExtension= "."+segments[segments.length-1];
        boolean didNotContainFileExtension = false;
        int iterator = segments.length-2;
        while(!didNotContainFileExtension){
            if(FileExtensionUtil.getFileExtensions().contains(segments[iterator])){
                fileExtension = "."+segments[iterator]+fileExtension;
                iterator--;
            } else{
                didNotContainFileExtension=true;
            }
        }
        //System.out.println("NEW WAY OF SPLITTING: "+fileExtension.substring(1));
        return fileExtension.substring(1).toLowerCase();
    }

    public static String cleanFilename(String rawFilename) {
        String cleanFilename = rawFilename;
        String delimiters = "\\(|\\[";
        int pos = rawFilename.lastIndexOf('.');
        if (pos > -1) cleanFilename = rawFilename.substring(0,pos);
        cleanFilename = cleanFilename.split(delimiters)[0];
        cleanFilename = cleanFilename.trim();
        return cleanFilename.replace('_',' ');
    }
}
