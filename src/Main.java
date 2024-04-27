import java.util.*;

class Main {
    public static void main(String[] args) {
        Graph g = new Graph();

        // Add edges (exchange rates)
        g.addEdge("USD", "JPY", 0.5);  // 1 USD = 0.5 JPY
        g.addEdge("USD", "EUR", 0.25); // 1 USD = 0.25 EUR
        g.addEdge("JPY", "EUR", 0.6);  // 1 JPY = 0.6 EUR
        g.addEdge("EUR", "GBP", 0.8);  // 1 EUR = 0.8 GBP
        g.addEdge("GBP", "CHF", 1.2);  // 1 GBP = 1.2 CHF

        String from = "USD", to = "GBP";
        double maxRate = maxConversionRate(g, from, to);
        System.out.println("Maximum conversion rate from " + from + " to " + to + " is " + maxRate);
    }
    public static class Edge {
        String from, to;
        double weight;

        Edge(String source, String dest, double weight) {
            this.from = source;
            this.to = dest;
            this.weight = weight;
        }
    }

    public static class Graph {
        Map<String, List<Edge>> adj;

        Graph() {
            adj = new HashMap<>();
        }

        public void addEdge(String u, String v, double weight) {
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(u, v, weight));
        }
    }

    public static double maxConversionRate(Graph g, String from, String to) {
        Map<String, Double> dist = new HashMap<>();
        dist.put(from, 1.0);

        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> Double.compare(b.weight, a.weight));
        pq.offer(new Edge(from, from, 1.0));

        while (!pq.isEmpty()) {
            Edge u = pq.poll();
            if (dist.containsKey(u.to) && u.weight < dist.get(u.to))
                continue;

            for (Edge v : g.adj.getOrDefault(u.to, new ArrayList<>())) {
                double newRate = dist.get(u.to) * v.weight;
                if (!dist.containsKey(v.to) || newRate > dist.getOrDefault(v.to, 0.0)) {
                    dist.put(v.to, newRate);
                    pq.offer(new Edge(u.from, v.to, newRate));
                }
            }
        }

        return dist.getOrDefault(to, 0.0);
    }
}
