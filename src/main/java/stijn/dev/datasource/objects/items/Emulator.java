package stijn.dev.datasource.objects.items;

import javafx.beans.property.*;

import java.io.*;

public class Emulator {
    private StringProperty name;
    private StringProperty path;
    private StringProperty args;
    private StringProperty description;
    private Process process;

    public Emulator(String name, String path, String description, String args){
        this.name = new SimpleStringProperty(name);
        this.path = new SimpleStringProperty(path);
        this.description = new SimpleStringProperty(description);
        this.args = new SimpleStringProperty(args);
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


    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPath() {
        return path.get();
    }

    public StringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public String getArgs() {
        return args.get();
    }

    public StringProperty argsProperty() {
        return args;
    }

    public void setArgs(String args) {
        this.args.set(args);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }
}
