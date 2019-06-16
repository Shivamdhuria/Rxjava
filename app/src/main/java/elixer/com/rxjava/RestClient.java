package elixer.com.rxjava;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a mock REST Client. It simulates making blocking calls to an REST endpoint.
 */
public class RestClient {
    private Context mContext;
    String[] cities;

    public RestClient(Context context) {
        mContext = context;
        cities = mContext.getResources().getStringArray(R.array.city_list);
    }

    public List<String> getFavoriteTvShows() {
        try {
            // "Simulate" the delay of network.
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return createTvShowList();
    }

    public List<String> getFavoriteTvShowsWithException() {
        try {
            // "Simulate" the delay of network.
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to load");
    }

    private List<String> createTvShowList() {
        List<String> tvShows = new ArrayList<>();
        tvShows.add("The Joy of Painting");
        tvShows.add("The Simpsons");
        tvShows.add("Futurama");
        tvShows.add("Rick & Morty");
        tvShows.add("The X-Files");
        tvShows.add("Star Trek: The Next Generation");
        tvShows.add("Archer");
        tvShows.add("30 Rock");
        tvShows.add("Bob's Burgers");
        tvShows.add("Breaking Bad");
        tvShows.add("Parks and Recreation");
        tvShows.add("House of Cards");
        tvShows.add("Game of Thrones");
        tvShows.add("Law And Order");
        return tvShows;
    }

    public List<String> searchForCity(String searchString) {
        try {
            // "Simulate" the delay of network.
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getMatchingCities(searchString);
    }

    private List<String> getMatchingCities(String searchString) {
        if (searchString.isEmpty()) {
            return new ArrayList<>();
        }

        String[] cities = mContext.getResources().getStringArray(R.array.city_list);
        List<String> toReturn = new ArrayList<>();
        for (String city : cities) {
            if (city.toLowerCase().startsWith(searchString.toLowerCase())) {
                toReturn.add(city);
            }
        }
        return toReturn;
    }

    List<String> getCitiesByPage(int page) {

        try {
            // "Simulate" the delay of network.
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> returnCities = new ArrayList<>();
        int index = page*10;
        for (int i = index + 0; i < index + 10; i++) {
            returnCities.add(cities[i]);
        }

        return returnCities;
    }
}
