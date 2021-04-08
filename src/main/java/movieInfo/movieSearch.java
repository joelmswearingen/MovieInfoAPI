package movieInfo;

import com.google.gson.Gson;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;

import static input.InputUtils.stringInput;

public class movieSearch {

    public static void main(String[] args) {

        // Configure Unirest to use Gson to convert JSON to Java object
        Unirest.config().setObjectMapper(new ObjectMapper() {
            private Gson gson = new Gson();
            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                return gson.fromJson(s, aClass);
            }

            @Override
            public String writeValue(Object o) {
                return gson.toJson(o);
            }
        });

        // set OMDb api key as a system environment variable
        String OMDB_API_KEY = System.getenv("OMDB_API_KEY");

        // set OMBd url to include personalized API key
        String OMDB_URL = "http://www.omdbapi.com/?apikey=" + OMDB_API_KEY + "&";

        // user enters movie title or keyword in movie title
        String movieKeyword = stringInput("Please enter a movie title or movie title keyword " +
                "for which you would like to know more information: ");

        // via Unirest...
        // set openMovieDatabaseResponse object with gson converted JSON response based on user entered movieKeyword
        openMovieDatabaseResponse response = Unirest.get(OMDB_URL)
                .queryString("t", movieKeyword)
                .asObject(openMovieDatabaseResponse.class)
                .getBody();

        // set selected variable with object fields
        String movieTitle = response.Title;
        String movieRating = response.Rated;
        String starring = response.Actors;
        String movieDescription = response.Plot;
        String releaseDate = response.Released;

        // print movie info if a movie title is returned
        if (movieTitle != null) {
            System.out.println("The following information was found:");
            System.out.println("Title: " + movieTitle);
            System.out.println("Rated: " +movieRating);
            System.out.println("Starring: " + starring);
            System.out.println("The plot of \"" + movieTitle + "\" is as follows: \n" + movieDescription);
            System.out.println("Release Date: " + releaseDate);
        } else {  // otherwise print "Not found" message
            System.out.println("Sorry, we could not find a movie that matched your search criteria.");
        }


    }
}
