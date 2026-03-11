import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    int time; // minutes for simplicity

    Transaction(int id, int amount, String merchant, String account, int time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        System.out.println("Two-Sum Results:");

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);

                System.out.println("(" + other.id + ", " + t.id + ") → "
                        + other.amount + " + " + t.amount);
            }

            map.put(t.amount, t);
        }
    }

    // Two-Sum within 60-minute window
    public void findTwoSumTimeWindow(int target) {

        System.out.println("\nTwo-Sum within 1 hour:");

        for (int i = 0; i < transactions.size(); i++) {

            for (int j = i + 1; j < transactions.size(); j++) {

                Transaction a = transactions.get(i);
                Transaction b = transactions.get(j);

                if (Math.abs(a.time - b.time) <= 60 &&
                        a.amount + b.amount == target) {

                    System.out.println("(" + a.id + ", " + b.id + ")");
                }
            }
        }
    }

    // 3-Sum example for K-Sum
    public void findThreeSum(int target) {

        System.out.println("\n3-Sum Results:");

        for (int i = 0; i < transactions.size(); i++) {

            HashMap<Integer, Transaction> map = new HashMap<>();
            int newTarget = target - transactions.get(i).amount;

            for (int j = i + 1; j < transactions.size(); j++) {

                int complement = newTarget - transactions.get(j).amount;

                if (map.containsKey(complement)) {

                    Transaction t1 = transactions.get(i);
                    Transaction t2 = map.get(complement);
                    Transaction t3 = transactions.get(j);

                    System.out.println("(" + t1.id + ", " + t2.id + ", " + t3.id + ")");
                }

                map.put(transactions.get(j).amount, transactions.get(j));
            }
        }
    }

    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<String>> map = new HashMap<>();

        System.out.println("\nDuplicate Transactions:");

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t.account);
        }

        for (String key : map.keySet()) {

            List<String> accounts = map.get(key);

            if (accounts.size() > 1) {
                System.out.println("Duplicate → " + key + " Accounts: " + accounts);
            }
        }
    }
}

public class weekly {

    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        analyzer.addTransaction(new Transaction(1, 500, "Store A", "acc1", 600));
        analyzer.addTransaction(new Transaction(2, 300, "Store B", "acc2", 615));
        analyzer.addTransaction(new Transaction(3, 200, "Store C", "acc3", 630));
        analyzer.addTransaction(new Transaction(4, 500, "Store A", "acc4", 640));

        analyzer.findTwoSum(500);

        analyzer.findTwoSumTimeWindow(500);

        analyzer.findThreeSum(1000);

        analyzer.detectDuplicates();
    }
}