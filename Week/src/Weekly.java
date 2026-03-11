import java.util.*;

// Entry class to store DNS information
class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

// DNS Cache Manager
class DNSCache {

    private int maxSize;

    // LinkedHashMap for LRU eviction
    private LinkedHashMap<String, DNSEntry> cache;

    private int hits = 0;
    private int misses = 0;

    public DNSCache(int maxSize) {
        this.maxSize = maxSize;

        cache = new LinkedHashMap<String, DNSEntry>(maxSize, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.maxSize;
            }
        };
    }

    // Resolve domain
    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null) {
            if (!entry.isExpired()) {
                hits++;
                System.out.println("Cache HIT → " + entry.ipAddress);
                return entry.ipAddress;
            } else {
                System.out.println("Cache EXPIRED → querying upstream...");
                cache.remove(domain);
            }
        }

        misses++;

        // Simulate upstream DNS lookup
        String ip = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(domain, ip, 5)); // TTL = 5 seconds for demo

        System.out.println("Cache MISS → Upstream returned: " + ip);

        return ip;
    }

    // Simulated DNS server
    private String queryUpstreamDNS(String domain) {
        Random r = new Random();
        return "172.217.14." + r.nextInt(255);
    }

    // Remove expired entries manually
    public void cleanup() {
        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> entry = it.next();
            if (entry.getValue().isExpired()) {
                it.remove();
            }
        }
    }

    // Cache statistics
    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }
}

public class weekly {

    public static void main(String[] args) throws InterruptedException {

        DNSCache dns = new DNSCache(5);

        dns.resolve("google.com");
        dns.resolve("google.com");

        Thread.sleep(6000); // wait for TTL expiration

        dns.resolve("google.com");

        dns.getCacheStats();
    }
}