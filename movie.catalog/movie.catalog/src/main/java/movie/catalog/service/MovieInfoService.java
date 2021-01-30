package movie.catalog.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import movie.catalog.resources.CatalogItem;
import movie.catalog.resources.Movie;
import movie.catalog.resources.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class MovieInfoService {


    @Autowired
    private RestTemplate restTemplate;

    private final String getMovieById = "http://movie-info-service/movies/";

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    public CatalogItem getCatalogItem(Rating rating) {
        String url1 = getMovieById + rating.getMovieId();
        Movie movie = restTemplate.getForObject(url1, Movie.class);
        return new CatalogItem(movie.getName(), "test", rating.getRating());
    }


    public CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Unavailable", "Unavailable", 0);
    }
}
