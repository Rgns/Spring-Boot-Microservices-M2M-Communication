package movie.catalog.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import movie.catalog.resources.Rating;
import movie.catalog.resources.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserRatingInfoService {


    @Autowired
    private RestTemplate restTemplate;

    private final String getRatings = "http://rating-service/ratings/";

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
    public List<Rating> getUserRating(String userId) {
        String url = getRatings + "users/" + userId;
        UserRating userRating = restTemplate.getForObject(url, UserRating.class);
        return userRating.getRatings();

    }

    public List<Rating> getFallbackUserRating(String userId) {
        return Arrays.asList(new Rating("Unavailable", 0));
    }

}
