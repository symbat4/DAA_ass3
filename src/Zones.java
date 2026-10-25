/**
 * Missions 4 and 5. The slides: a directed graph. Cells are 0 .. n-1, slide i goes from s.from()[i] to s.to()[i]
 * and takes s.time()[i]. There are no slides from a cell to itself, and the exit can always be reached from the start.
 *
 * Ready for you: Graphs.slidesFrom(s)[u] is the list of the slides that leave cell u, and Graphs.slidesInto(s)[v] is the list
 * of the slides that enter cell v (reversed graph). The lists hold slide numbers i; the cell at the other end is s.to()[i]
 * or s.from()[i].
 */
public class Zones {

    /**
     * Mission 4. Kosaraju. Returns zoneOf: zoneOf[cell] = number of the zone, numbers 0 .. zones-1.
     * Number the zones in the order in which the second pass finds them. That order is a topological order
     * of the zone graph: every slide between two zones goes from a smaller number to a larger number.
     *
     * Plan: 1) DFS on the slides, write the cells in the order in which they are finished (use a stack, not recursion);
     * 2) go through the cells from the last finished to the first; if a cell has no zone yet, DFS from it on the reversed
     * slides (slidesInto) and give every cell it reaches the next zone number.
     * Is DAG in Main is 1 when the number of zones equals the number of cells (no cycle), so you do not need a separate check.
     */
    public static int[] kosaraju(Input.Slides s) {
        // TODO
        int n = s.n();
        int[][] out = Graphs.slidesFrom(s);
        int[][] in = Graphs.slidesInto(s);
        boolean[] seen = new boolean[n];
        int[] order = new int[n];
        int cnt = 0;
        int[] stack = new int[n];
        int[] next = new int[n];

        for (int r = 0; r < n; r++) {
            if (seen[r]) continue;
            int top = 0;
            stack[top++] = r;
            seen[r] = true;
            while (top > 0) {
                int u = stack[top - 1];
                if (next[u] < out[u].length) {
                    int v = s.to()[out[u][next[u]++]];
                    if (!seen[v]) { seen[v] = true; stack[top++] = v; }
                } else {
                    top--;
                    order[cnt++] = u;
                }
            }
        }

        int[] zone = new int[n];
        java.util.Arrays.fill(zone, -1);
        int z = 0;
        for (int i = n - 1; i >= 0; i--) {
            int r = order[i];
            if (zone[r] != -1) continue;
            int top = 0;
            stack[top++] = r;
            zone[r] = z;
            while (top > 0) {
                int u = stack[--top];
                for (int e : in[u]) {
                    int v = s.from()[e];
                    if (zone[v] == -1) { zone[v] = z; stack[top++] = v; }
                }
            }
            z++;
        }
        return zone;
    }

    /**
     * Mission 5. The graph of zones: the edge X to Y costs the cheapest slide from X to Y, walking inside a zone is free.
     * Go through the zones in the order of their numbers (no priority queue). Returns two numbers:
     * [0] flow stages: the number of zones on the longest chain of zones;
     * [1] the cost of the cheapest route from the zone of cell 0 to the zone of the last cell.
     */
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        // TODO
        int m = s.count();
        int[] start = new int[zoneCount + 1];
        for (int i = 0; i < m; i++) start[zoneOf[s.from()[i]] + 1]++;
        for (int z = 0; z < zoneCount; z++) start[z + 1] += start[z];
        int[] fill = java.util.Arrays.copyOf(start, zoneCount);
        int[] byZone = new int[m];
        for (int i = 0; i < m; i++) byZone[fill[zoneOf[s.from()[i]]]++] = i;

        long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[zoneCount];
        java.util.Arrays.fill(dist, INF);
        int[] stages = new int[zoneCount];
        java.util.Arrays.fill(stages, 1);
        dist[zoneOf[0]] = 0;

        for (int z = 0; z < zoneCount; z++) {
            for (int j = start[z]; j < start[z + 1]; j++) {
                int i = byZone[j];
                int y = zoneOf[s.to()[i]];
                if (y == z) continue;
                if (dist[z] < INF) dist[y] = Math.min(dist[y], dist[z] + s.time()[i]);
                stages[y] = Math.max(stages[y], stages[z] + 1);
            }
        }
        long longest = 0;
        for (int x : stages) longest = Math.max(longest, x);
        return new long[]{longest, dist[zoneOf[s.n() - 1]]};
    }
}
