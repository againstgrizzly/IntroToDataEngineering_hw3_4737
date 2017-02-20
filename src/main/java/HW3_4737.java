import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.store.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

        List<MovieObject> movieObjects = new ArrayList<MovieObject>();

        for(int i = 0; i < 25; i++ ){

            int count = i + 1;

            MovieObject movieObject = m.readValue(new File ("src/main/resources/movies/page" + count + ".json"), MovieObject.class);
            movieObjects.add(movieObject);

        }


        connection.close();

    }

}
