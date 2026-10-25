/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        // TODO
        int n = w.cells(), cols = w.cols();
        int[][] g = Graphs.open(w, broken);
        int exit = n - 1;
        boolean[] seen = new boolean[n];
        int[] parent = new int[n];
        int[] dir = new int[n];
        int[] stack = new int[n];
        int top = 0, visited = 1;
        stack[top++] = 0;
        seen[0] = true;
        parent[0] = -1;
        while (top > 0 && !seen[exit]) {
            int c = stack[top - 1];
            if (dir[c] == 4) { top--; continue; }
            int d = dir[c]++;
            int nb;
            if (d == 0) nb = c >= cols ? c - cols : -1;
            else if (d == 1) nb = (c % cols != cols - 1) ? c + 1 : -1;
            else if (d == 2) nb = (c + cols < n) ? c + cols : -1;
            else nb = (c % cols != 0) ? c - 1 : -1;
            if (nb < 0 || seen[nb] || !isOpen(g[c], nb)) continue;
            seen[nb] = true;
            visited++;
            parent[nb] = c;
            stack[top++] = nb;
        }
        int len = 0;
        for (int c = exit; c != -1; c = parent[c]) len++;
        int[] path = new int[len];
        int i = len - 1;
        for (int c = exit; c != -1; c = parent[c]) path[i--] = c;
        int deadEnds = 0;
        for (int[] list : g) if (list.length == 1) deadEnds++;
        return new Result(path, visited, deadEnds);
    }

    private static boolean isOpen(int[] neighbours, int cell) {
        for (int x : neighbours) if (x == cell) return true;
        return false;
    }
}
