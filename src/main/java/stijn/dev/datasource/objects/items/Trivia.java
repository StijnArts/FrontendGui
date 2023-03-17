package stijn.dev.datasource.objects.items;

public class Trivia {
    private String triviaID;
    private String fact;
    public Trivia(String triviaID, String fact){
        this.triviaID = triviaID;
        this.fact = fact;
    }

    public String getTriviaID() {
        return triviaID;
    }

    public void setTriviaID(String triviaID) {
        this.triviaID = triviaID;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }
}
