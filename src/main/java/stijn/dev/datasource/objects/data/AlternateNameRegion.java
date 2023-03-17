package stijn.dev.datasource.objects.data;

public class AlternateNameRegion extends AlternateName{
    private String region;
    public AlternateNameRegion(String alternateNameId, String alternateName, String region) {
        super(alternateNameId,alternateName);
        this.region = region;
    }
}
