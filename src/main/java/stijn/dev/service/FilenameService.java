package stijn.dev.service;

import java.io.*;
import java.util.*;

public class FilenameService {


    public static ArrayList<String> extractFileExtensions(List<File> files) {
        ArrayList<String> fileExtensions = new ArrayList<>();
        for (File file : files) {
            int pos = file.getName().lastIndexOf('.');
            fileExtensions.add(file.getName().substring(pos+1));
            System.out.println(file.getName().substring(pos+1));
        }
        return fileExtensions;
    }

    public static String cleanFilename(String rawFilename) {
        System.out.println(rawFilename);
        String cleanFilename = rawFilename;
        String delimiters = "\\(";
        int pos = rawFilename.lastIndexOf('.');
        if (pos > -1) cleanFilename = rawFilename.substring(0,pos);
        cleanFilename = cleanFilename.split(delimiters)[0];
        cleanFilename = cleanFilename.trim();
        System.out.println(cleanFilename);
        return cleanFilename;
    }
}
