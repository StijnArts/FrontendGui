package stijn.dev.datasource.objects.data.enums;

public enum MediaTypes {
    IMAGE("image"),
    MOVIE("movie"),
    PDF("pdf"),
    AUDIO("audio")
            ;
    MediaTypes(String type){
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
