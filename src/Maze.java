/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        // TODO
        DisjointSet d = new DisjointSet(w.cells());
        int[] tmp = new int[w.count()];
        int m = 0;
        for (int k = 0; k < w.count(); k++) {
            if (d.union(w.a()[k], w.b()[k])) tmp[m++] = k;
        }
        return java.util.Arrays.copyOf(tmp, m);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        // TODO
        Integer[] order = new Integer[w.count()];
        for (int i = 0; i < order.length; i++) order[i] = i;
        java.util.Arrays.sort(order, (x, y) -> Integer.compare(w.cost()[x], w.cost()[y]));
        DisjointSet d = new DisjointSet(w.cells());
        int[] tmp = new int[w.cells()];
        int m = 0;
        for (int k : order) {
            if (d.union(w.a()[k], w.b()[k])) tmp[m++] = k;
        }
        return java.util.Arrays.copyOf(tmp, m);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        // TODO
        int n = w.cells();
        java.util.ArrayList<java.util.ArrayList<int[]>> adj = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new java.util.ArrayList<>());
        for (int k = 0; k < w.count(); k++) {
            adj.get(w.a()[k]).add(new int[]{w.b()[k], w.cost()[k]});
            adj.get(w.b()[k]).add(new int[]{w.a()[k], w.cost()[k]});
        }
        boolean[] inTree = new boolean[n];
        java.util.PriorityQueue<int[]> pq = new java.util.PriorityQueue<>((x, y) -> Integer.compare(x[1], y[1]));
        pq.add(new int[]{0, 0});
        long total = 0;
        int taken = 0;
        while (!pq.isEmpty() && taken < n) {
            int[] cur = pq.poll();
            if (inTree[cur[0]]) continue;
            inTree[cur[0]] = true;
            taken++;
            total += cur[1];
            for (int[] e : adj.get(cur[0])) {
                if (!inTree[e[0]]) pq.add(e);
            }
        }
        return total;
    }
}
