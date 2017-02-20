/**
 * Created by Brannon on 2/19/2017.
 */
public class MovieObject {

    private Movies movies[];
    private Links links;
    private String link_template;
    private int total;

    public int getTotal(){
        return total;
    }

    public void setTotal(int total){
        this.total = total;
    }

    public Movies[] getMovies() {
        return movies;
    }

    public void setMovies(Movies[] movies) {
        this.movies = movies;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getLink_template() {
        return link_template;
    }

    public void setLink_template(String link_template) {
        this.link_template = link_template;
    }
}
