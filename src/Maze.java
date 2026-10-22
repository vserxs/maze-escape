import java.util.Arrays;
import java.util.PriorityQueue;
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
        DisjointSet dsu = new DisjointSet(w.cells());

        int[] selected = new int[w.count()];
        int selectedCount = 0;

        for(int i=0;i<w.count();i++){
            if(dsu.union(w.a()[i],w.b()[i])){
                selected[selectedCount] = i;
                selectedCount++;
            }
        }

        int[] result = new int[selectedCount];

        for(int i=0;i<selectedCount;i++){
            result[i] = selected[i];
        }
        return result;
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        // TODO
        int n = w.cells();
        int m = w.count();

        int[] a = w.a();
        int[] b = w.b();
        int[] cost = w.cost();

        Integer[] order = new Integer[m];

        for(int i=0;i<m;i++){
            order[i] = i;
        }

        Arrays.sort(order,(left,right) -> Integer.compare(cost[left],cost[right]));

        DisjointSet dsu = new DisjointSet(n);

        int[] selected = new int[Math.max(0,n-1)];
        int selectedCount = 0;

        for(int k : order){
            if(dsu.union(a[k],b[k])){
                selected[selectedCount++] = k;

                if(selectedCount == n-1){
                    break;
                }
            }
        }
        return Arrays.copyOf(selected,selectedCount);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        // TODO
        int n = w.cells();
        int m = w.count();

        if(n<=1){
            return 0;
        }

        int[] a = w.a();
        int[] b = w.b();
        int[] cost = w.cost();

        int[] degree = new int[n];
        int[] incident = new int[4*n];

        for(int k=0;k<m;k++){
            int u = a[k];
            int v = b[k];

            incident[4*u+degree[u]++] = k;
            incident[4*v+degree[v]++] = k;
        }

        boolean[] inTree = new boolean[n];

        PriorityQueue<Integer> pq = new PriorityQueue<>((left,right) -> Integer.compare(cost[left],cost[right]));

        inTree[0] = true;
        int visitedCount = 1;
        long totalCost = 0;

        for (int j=0;j<degree[0];j++){
            pq.offer(incident[j]);
        }

        while (visitedCount<n && !pq.isEmpty()){
            int k = pq.poll();

            int u = a[k];
            int v = b[k];

            if (inTree[u] && inTree[v]){
                continue;
            }

            int next = inTree[u] ? v : u;

            inTree[next] = true;
            visitedCount++;
            totalCost += cost[k];


            for (int j=0;j<degree[next];j++){
                int edge = incident[4 * next + j];

                int other = a[edge] == next ? b[edge] : a[edge];

                if (!inTree[other]){
                    pq.offer(edge);
                }
            }
        }

        return totalCost;
    }
}
