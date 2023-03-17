package stijn.dev.datasource.objects.data;

public class AlternateName {
    private String alternateName;
    private String alternateNameID;

    public AlternateName(String alternateNameID,String alternateName){
        this.alternateNameID = alternateNameID;
        this.alternateName = alternateName;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }
}
