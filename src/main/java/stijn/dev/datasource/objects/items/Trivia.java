package stijn.dev.datasource.objects.items;

import javafx.beans.property.*;

public class Trivia {
    private StringProperty triviaID;
    private StringProperty fact;
    public Trivia(String triviaID, String fact){
        this.triviaID = new SimpleStringProperty(triviaID);
        this.fact = new SimpleStringProperty(fact);
    }

    public StringProperty triviaIDProperty() {
        return triviaID;
    }

    public StringProperty factProperty() {
        return fact;
    }

    public StringProperty getTriviaID() {
        return triviaID;
    }

    public void setTriviaID(String triviaID) {
        this.triviaID.set(triviaID);
    }

    public StringProperty getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact.set(fact);
    }
}
