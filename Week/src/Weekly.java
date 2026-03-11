import java.util.*;

class AutocompleteSystem {

    // query -> frequency
    private HashMap<String, Integer> queryFrequency = new HashMap<>();

    // Add or update search query
    public void updateFrequency(String query) {

        int freq = queryFrequency.getOrDefault(query, 0) + 1;
        queryFrequency.put(query, freq);

        System.out.println(query + " → Frequency: " + freq);
    }

    // Return top 10 suggestions for a prefix
    public void search(String prefix) {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : queryFrequency.entrySet()) {

            if (entry.getKey().startsWith(prefix)) {

                pq.offer(entry);

                if (pq.size() > 10) {
                    pq.poll();
                }
            }
        }

        List<Map.Entry<String, Integer>> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        Collections.reverse(result);

        System.out.println("\nSuggestions for \"" + prefix + "\":");

        int rank = 1;
        for (Map.Entry<String, Integer> entry : result) {
            System.out.println(rank + ". " + entry.getKey() +
                    " (" + entry.getValue() + " searches)");
            rank++;
        }
    }
}

public class weekly {

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        // Existing queries
        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        // Search suggestions
        system.search("jav");

        // Update trending query
        system.updateFrequency("java 21 features");
    }
}