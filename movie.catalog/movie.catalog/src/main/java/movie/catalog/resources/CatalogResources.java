package movie.catalog.resources;

import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResources {

    // java -Dserver.port=8201 -jar jarName

  //  private final String getMovieById = "http://localhost:8082/movies/"; // service name should be std & fixed.
    private final String getMovieById = "http://movie-info-service/movies/";
 //   private final String getRatings = "http://localhost:8083/ratings/"; // after edureka has been added will use name deployed
    private final String getRatings = "http://rating-service/ratings/";

    @Autowired // let me check & fetch the instance
    private RestTemplate restTemplate;

    //@Autowired
    //private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient.Builder webClientBuilder;

  /*  @RequestMapping("/{userId}")
    public List<CatalogItem> getWebClientCatalog(@PathVariable("userId") String userId) {

        // RestTemplate restTemplate = new RestTemplate();


        List<Rating> ratings = Arrays.asList(
                new Rating("1", 2),
                new Rating("3", 4),
                new Rating("2", 3)
        );

        return ratings.stream().map(rating -> {
                    String url = getMovieById + rating.getMovieId();
                    //  Movie movie = restTemplate.getForObject(url, Movie.class);
                    Movie movie = webClientBuilder.build()
                            .get()
                            .uri(url)
                            .retrieve()
                            .bodyToMono(Movie.class) // you will get an anysc obj , u will get in future.
                            .block();
                    return new CatalogItem(movie.getName(), "test", rating.getRating());
                }
        ).collect(Collectors.toList());


        //  return Collections.singletonList(new CatalogItem("DDLJ", "test", 4));
    }*/


    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {


      /*  List<Rating> ratings = Arrays.asList(
                new Rating("1", 2),
                new Rating("3", 4),
                new Rating("2", 3)
        );*/

        String url = getRatings + "users/" + userId;
        // ParameterizedType<List<Rating>> --> used for any type. better to avoid it have simple object
        UserRating userRating = restTemplate.getForObject(url, UserRating.class);

        return userRating.getRatings().stream().map(rating -> {
                    String url1 = getMovieById + rating.getMovieId();
                    Movie movie = restTemplate.getForObject(url1, Movie.class);
                    return new CatalogItem(movie.getName(), "test", rating.getRating());
                }
        ).collect(Collectors.toList());


        //  return Collections.singletonList(new CatalogItem("DDLJ", "test", 4));
    }


}
