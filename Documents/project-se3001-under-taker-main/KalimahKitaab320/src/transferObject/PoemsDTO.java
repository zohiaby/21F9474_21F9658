package transferObject;

public class PoemsDTO {

    private int poemId;
    private String title;

    public PoemsDTO(int poemId, String title) {
        this.poemId = poemId;
        this.title = title;
    }

    public int getPoemId() {
        return poemId;
    }

    public void setPoemId(int poemId) {
        this.poemId = poemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
