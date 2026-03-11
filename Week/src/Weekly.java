=import java.util.*;

class FlashSaleInventoryManager {

    // HashMap to store product stock
    private HashMap<String, Integer> stockMap = new HashMap<>();

    // Waiting list for each product
    private HashMap<String, Queue<Integer>> waitingList = new HashMap<>();

    // Add product with stock
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check available stock
    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    // Purchase item (thread-safe)
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        }
        else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }

    // Restock product and process waiting list
    public synchronized void restock(String productId, int quantity) {

        int stock = stockMap.getOrDefault(productId, 0);
        stockMap.put(productId, stock + quantity);

        Queue<Integer> queue = waitingList.get(productId);

        while (!queue.isEmpty() && stockMap.get(productId) > 0) {
            int userId = queue.poll();
            stockMap.put(productId, stockMap.get(productId) - 1);

            System.out.println("Waiting user " + userId + " purchase confirmed.");
        }
    }
}

public class weekly {

    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        manager.addProduct("IPHONE15_256GB", 100);

        System.out.println("Stock Available: " + manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate stock selling out
        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}