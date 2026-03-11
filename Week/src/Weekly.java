import java.util.*;

class TokenBucket {

    int maxTokens;
    int tokens;
    long lastRefillTime;
    int refillRate; // tokens per hour

    public TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens based on elapsed time
    private void refill() {

        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;

        long tokensToAdd = (elapsed * refillRate) / (3600 * 1000);

        if (tokensToAdd > 0) {
            tokens = (int) Math.min(maxTokens, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }

    // Check if request is allowed
    public synchronized boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    public int getRemainingTokens() {
        refill();
        return tokens;
    }
}

class RateLimiter {

    // clientId -> token bucket
    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private int LIMIT = 1000;

    // Check rate limit
    public void checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(LIMIT, LIMIT));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {

            System.out.println("Allowed (" + bucket.getRemainingTokens() + " requests remaining)");

        } else {

            System.out.println("Denied (0 requests remaining, try again later)");
        }
    }

    // Display rate limit status
    public void getRateLimitStatus(String clientId) {

        if (!clients.containsKey(clientId)) {
            System.out.println("Client not found.");
            return;
        }

        TokenBucket bucket = clients.get(clientId);

        int remaining = bucket.getRemainingTokens();
        int used = LIMIT - remaining;

        System.out.println("Status → Used: " + used + ", Limit: " + LIMIT + ", Remaining: " + remaining);
    }
}

public class weekly {

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        limiter.checkRateLimit(client);
        limiter.checkRateLimit(client);
        limiter.checkRateLimit(client);

        limiter.getRateLimitStatus(client);
    }
}