package stijn.dev.datasource.objects.items;

import java.io.*;

public class Emulator {
    private String name;
    private String path;

    private String args;
    private Process process;

    public Emulator(String name, String path){
        this.name = name;
        this.path = path;
    }

    public Process launchGame(GameImportItem gameImportItem){

        String gamePath = "\"" +path+ "\"";

        if(args != null){
            gamePath +=  " " +args;
        }

        gamePath +=  " \"" + gameImportItem.getPath()+ "\"";

        ProcessBuilder builder = new ProcessBuilder(gamePath);
        builder.redirectErrorStream(true);
        try {
            process = builder.start();
            System.out.println();
            return process;
        } catch (IOException e){
            //TODO file not found message
            return null;
        }
    }

    public void closeGame(){
        if(process.isAlive()) {
            process.destroy();
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }
}
