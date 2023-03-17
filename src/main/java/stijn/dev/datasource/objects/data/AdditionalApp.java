package stijn.dev.datasource.objects.data;

public class AdditionalApp {
    private String path;
    private String name;
    private String arguments;

    public AdditionalApp(String path, String name, String arguments) {
        this.path = path;
        this.name = name;
        this.arguments = arguments;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
}
