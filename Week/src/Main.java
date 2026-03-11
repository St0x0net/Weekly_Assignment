import java.util.*;

class UsernameChecker {

    // Stores registered usernames
    private HashMap<String, Integer> usernameMap = new HashMap<>();

    // Stores attempt frequency
    private HashMap<String, Integer> attemptCount = new HashMap<>();

    // Constructor with sample users
    public UsernameChecker() {
        usernameMap.put("john_doe", 101);
        usernameMap.put("admin", 1);
        usernameMap.put("user123", 202);
    }

    // Check username availability
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);

        return !usernameMap.containsKey(username);
    }

    // Register username
    public void registerUsername(String username, int userId) {
        usernameMap.put(username, userId);
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // Replace underscore with dot suggestion
        String dotSuggestion = username.replace("_", ".");
        if (!usernameMap.containsKey(dotSuggestion)) {
            suggestions.add(dotSuggestion);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String popular = null;
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : attemptCount.entrySet()) {
            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                popular = entry.getKey();
            }
        }

        return popular + " (" + maxAttempts + " attempts)";
    }

    // Display registered usernames
    public void showUsers() {
        System.out.println("Registered Users: " + usernameMap);
    }
}

public class Main {
    public static void main(String[] args) {

        UsernameChecker checker = new UsernameChecker();

        System.out.println("john_doe available? " + checker.checkAvailability("john_doe"));
        System.out.println("jane_smith available? " + checker.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe:");
        System.out.println(checker.suggestAlternatives("john_doe"));

        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");

        System.out.println("Most Attempted Username: " + checker.getMostAttempted());
    }
}