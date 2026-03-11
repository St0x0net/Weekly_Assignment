import java.util.*;

class AnalyticsSystem {

    // Page view counts
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // Unique visitors per page
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // Traffic source counts
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process incoming event
    public void processEvent(String url, String userId, String source) {

        // Count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Track traffic source
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("\nTop Pages:");

        // Convert to list for sorting
        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        int count = 0;

        for (Map.Entry<String, Integer> entry : list) {

            if (count == 10) break;

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println((count + 1) + ". " + page + " - " + views +
                    " views (" + unique + " unique)");

            count++;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;
        for (int v : trafficSources.values()) total += v;

        for (String source : trafficSources.keySet()) {

            int countSource = trafficSources.get(source);
            double percent = (countSource * 100.0) / total;

            System.out.printf("%s: %.2f%%\n", source, percent);
        }
    }
}

public class weekly {

    public static void main(String[] args) {

        AnalyticsSystem system = new AnalyticsSystem();

        // Simulated events
        system.processEvent("/article/breaking-news", "user_123", "Google");
        system.processEvent("/article/breaking-news", "user_456", "Facebook");
        system.processEvent("/sports/championship", "user_789", "Direct");
        system.processEvent("/sports/championship", "user_123", "Google");
        system.processEvent("/article/breaking-news", "user_999", "Direct");

        // Display dashboard
        system.getDashboard();
    }
}