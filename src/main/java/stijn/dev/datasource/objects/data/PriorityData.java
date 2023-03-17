package stijn.dev.datasource.objects.data;

public class PriorityData {
    private String name;
    private int priority;

    public PriorityData(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return name+", "+priority;
    }
}
