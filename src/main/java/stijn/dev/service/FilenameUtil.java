package stijn.dev.service;

import java.io.*;
import java.util.*;

public class FilenameUtil {


    public static ArrayList<String> extractFileExtensions(List<File> files) {
        fillExtensionList();
        ArrayList<String> fileExtensions = new ArrayList<>();
        for (File file : files) {
            fileExtensions.add(extractFileExtension(file));
        }
        return fileExtensions;
    }

    public static String extractDirectoryName(File file){
        File parent = file.getParentFile();
        String filePath = parent.getAbsolutePath();
                //.replaceAll(parent.getParentFile().getParent(),"");
        System.out.println(filePath);
        return filePath;
    }

    public static String extractFileExtension(File file) {
        fillExtensionList();
        String[] segments = file.getName().split("\\.");
        String fileExtension= "";
        boolean didNotContainFileExtension = false;
        int iterator = segments.length-1;
        while(!didNotContainFileExtension){
            if(extensions.contains(segments[iterator])){
                fileExtension = "."+segments[iterator]+fileExtension;
                iterator--;
            } else{
                didNotContainFileExtension=true;
            }
        }
        System.out.println("NEW WAY OF SPLITTING: "+fileExtension.substring(1));
        return fileExtension;
    }

    public static String cleanFilename(String rawFilename) {
        fillExtensionList();
        String cleanFilename = rawFilename;
        String delimiters = "\\(|\\[";
        int pos = rawFilename.lastIndexOf('.');
        if (pos > -1) cleanFilename = rawFilename.substring(0,pos);
        cleanFilename = cleanFilename.split(delimiters)[0];
        cleanFilename = cleanFilename.trim();
        return cleanFilename;
    }
    private static boolean hasBeenFilled = false;
    private static ArrayList<String> extensions = new ArrayList<>();
    private static void fillExtensionList(){
        if(!hasBeenFilled) {
            extensions.add("rar");
            extensions.add("7z");
            extensions.add("7zip");
            extensions.add("gz");
            extensions.add("tar");
            extensions.add("001");
            extensions.add("part1");
            extensions.add("r01");
            extensions.add("gzip");
            extensions.add("iso");
            extensions.add("img");
            extensions.add("ccd");
            extensions.add("bin");
            extensions.add("cue");
            extensions.add("chd");
            extensions.add("mdf");
            extensions.add("mds");
            extensions.add("ecm");
            extensions.add("cso");
            extensions.add("gcz");
            extensions.add("rvz");
            extensions.add("cdi");
            extensions.add("gdi");
            extensions.add("sbl");
            extensions.add("fds");
            extensions.add("ndd");
            extensions.add("wav");
            extensions.add("tap");
            extensions.add("tzx");
            extensions.add("cdc");
            extensions.add("cas");
            extensions.add("nes");
            extensions.add("nez");
            extensions.add("unf");
            extensions.add("unif");
            extensions.add("smc");
            extensions.add("sfc");
            extensions.add("md");
            extensions.add("smd");
            extensions.add("gen");
            extensions.add("gg");
            extensions.add("z64");
            extensions.add("v64");
            extensions.add("n64");
            extensions.add("gb");
            extensions.add("gbc");
            extensions.add("gba");
            extensions.add("gcm");
            extensions.add("nkit");
            extensions.add("nds");
            extensions.add("srl");
            extensions.add("dsi");
            extensions.add("app");
            extensions.add("ids");
            extensions.add("wbfs");
            extensions.add("wad");
            extensions.add("cia");
            extensions.add("3ds");
            extensions.add("nsp");
            extensions.add("xci");
            extensions.add("ngp");
            extensions.add("ngc");
            extensions.add("pce");
            extensions.add("vpk");
            extensions.add("vb");
            extensions.add("ws");
            extensions.add("wsc");
            extensions.add("ipa");
            extensions.add("apk");
            extensions.add("obb");
            extensions.add("elf");
            extensions.add("pbp");
            extensions.add("dol");
            extensions.add("xbe");
            extensions.add("xex");
            extensions.add("xml");
            extensions.add("hsi");
            extensions.add("lay");
            extensions.add("nv");
            extensions.add("m3u");
            extensions.add("exe");
            extensions.add("lnk");
            extensions.add("url");
            extensions.add("cda");
            extensions.add("rpx");
            hasBeenFilled =true;
        }
    }
    
    
}
