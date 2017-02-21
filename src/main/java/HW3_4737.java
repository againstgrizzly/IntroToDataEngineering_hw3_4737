import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.store.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class HW3_4737 {

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {

        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:h2:" + // protocol
                        System.getProperty("user.dir") + "/movies", // db file path
                "sa", // user name
                ""); // password

        Statement statement = connection.createStatement();

        statement.execute("drop table movie");
        statement.execute("drop table actor");
        statement.execute("drop table character");


        statement.execute("create table movie( " +
                "id varchar(100)," +
                "title varchar(100)," +
                "year smallint unsigned," +
                "mpaa_rating varchar(10)," +
                "audience_score smallint unsigned," +
                "critics_score smallint unsigned," +
                "constraint pk_id primary key(id)" +
                ");"
        );

        statement.execute("create table actor (" +
                "id varchar(100)," +
                "name varchar(100)," +
                "constraint pk_actor_id primary key (id));");

        statement.execute("create table character(" +
                "actor_id varchar(100)," +
                "movie_id varchar(100)," +
                "character varchar(100)," +
                "constraint pk_character_id primary " +
                "key(movie_id, actor_id, character)," +
                "constraint fk_actor_id foreign key (actor_id) " +
                "references actor (id)," +
                "constraint fk_movie_id foreign key (movie_id) " +
                " references movie (id)" +
                ");"
        );

        ObjectMapper m = new ObjectMapper();
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MovieObject movieObjects[] = new MovieObject[25];

        //Create add movie objects to a list of movieobjects
        for (int i = 0; i < 25; i++) {
            int count = i + 1;
            MovieObject movieObject = m.readValue(new File("src/main/resources/movies/page" + count + ".json"), MovieObject.class);
            movieObjects[i] = movieObject;
        }

        //This is the loop for movie table
        for (int loopThroughMovieObjects = 0; loopThroughMovieObjects < movieObjects.length; loopThroughMovieObjects++) {

            Movie movies[] = movieObjects[loopThroughMovieObjects].getMovies();

            for (int i = 0; i < movies.length; i++) {

                int id = movies[i].getId();
                String title = movies[i].getTitle();
                int year = movies[i].getYear();
                String mpaa_rating = movies[i].getMpaa_rating();
                int audience_score = movies[i].getRatings().getAudience_score();
                int critics_score = movies[i].getRatings().getCritics_score();

                title = title.replace("'", "''");

                statement.executeUpdate(
                        "insert into movie(id,title, year, mpaa_rating, "
                                + "audience_score,critics_score) values('"
                                + id + "','" + title + "','"
                                + year + "','"
                                + mpaa_rating + "','"
                                + audience_score + "','"
                                + critics_score + "');");
            }

        }

        //this is the loop for the actor table
        Map<Integer, String> myHashMap = new HashMap<>();
        for (int i = 0; i < movieObjects.length; i++) {
            for (int j = 0; j < movieObjects[i].getMovies().length; j++) {
                for (int k = 0; k < movieObjects[i].getMovies()[j].getAbridged_cast().length; k++) {
                    int myId = movieObjects[i].getMovies()[j].getAbridged_cast()[k].getId();
                    String myName = movieObjects[i].getMovies()[j].getAbridged_cast()[k].getName();
                    myName = myName.replace("'", "''");
                    myHashMap.put(myId, myName);
                }
            }

        }

        for (int key : myHashMap.keySet()) {
            statement.executeUpdate(
                    "insert into actor(id, name"
                            + ") values('"
                            + key + "','" + myHashMap.get(key)
                            + "');");
        }

        //this is for the character table
        for (int i = 0; i < movieObjects.length; i++) {
            Movie movies[] = movieObjects[i].getMovies();
            for (int j = 0; j < movies.length; j++) {
                Abridged_Cast abridged_cast[] = movies[j].getAbridged_cast();
                for (int k = 0; k < abridged_cast.length; k++) {
                    String character[] = abridged_cast[k].getCharacters();

                    try {
                        for (int z = 0; z < character.length; z++) {
                            String name = character[z];
                            name = name.replace("'", "''");
                            statement.executeUpdate(
                                    "insert into character(actor_id, movie_id,"
                                            + "character) " + "values('"
                                            + abridged_cast[j].getId() + "','"
                                            + movies[i].getId() + "','" + name
                                            + "');");
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        connection.close();
    }


}



