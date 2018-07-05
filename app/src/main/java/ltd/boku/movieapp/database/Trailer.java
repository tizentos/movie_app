package ltd.boku.movieapp.database;

public class Trailer {

    private String trailerId;
    private String trailerTitle;

    public Trailer(String id, String title){
        trailerId=id;
        trailerTitle=title;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getTrailerTitle() {
        return trailerTitle;
    }

    public void setTrailerTitle(String trailerTitle) {
        this.trailerTitle = trailerTitle;
    }
}
