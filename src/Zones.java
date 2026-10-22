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
     * <p>
     * Plan: 1) DFS on the slides, write the cells in the order in which they are finished (use a stack, not recursion);
     * 2) go through the cells from the last finished to the first; if a cell has no zone yet, DFS from it on the reversed
     * slides (slidesInto) and give every cell it reaches the next zone number.
     * Is DAG in Main is 1 when the number of zones equals the number of cells (no cycle), so you do not need a separate check.
     */
    public static int[] kosaraju(Input.Slides s) {
        // TODO
        int n = s.n();

        int[] from = s.from();
        int[] to = s.to();

        int[][] outgoing = Graphs.slidesFrom(s);
        int[][] incoming = Graphs.slidesInto(s);

        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int orderSize = 0;

        int[] stackCell = new int[n];
        int[] stackNext = new int[n];

        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            int top = 0;
            stackCell[top] = start;
            stackNext[top] = 0;
            visited[start] = true;

            while (top >= 0) {
                int cell = stackCell[top];

                if (stackNext[top] < outgoing[cell].length) {
                    int slide = outgoing[cell][stackNext[top]++];
                    int next = to[slide];

                    if (!visited[next]) {
                        visited[next] = true;

                        top++;
                        stackCell[top] = next;
                        stackNext[top] = 0;
                    }
                } else {
                    order[orderSize++] = cell;
                    top--;
                }
            }
        }
        int[] zoneOf = new int[n];
        java.util.Arrays.fill(zoneOf, -1);

        int zoneCount = 0;

        for (int i = orderSize - 1; i >= 0; i--) {
            int start = order[i];

            if (zoneOf[start] != -1) {
                continue;
            }

            int top = 0;
            stackCell[top++] = start;
            zoneOf[start] = zoneCount;

            while (top > 0) {
                int cell = stackCell[--top];

                for (int slide : incoming[cell]) {
                    int next = from[slide];

                    if (zoneOf[next] == -1) {
                        zoneOf[next] = zoneCount;
                        stackCell[top++] = next;
                    }
                }
            }

            zoneCount++;
        }

        return zoneOf;
    }

    /**
     * Mission 5. The graph of zones: the edge X to Y costs the cheapest slide from X to Y, walking inside a zone is free.
     * Go through the zones in the order of their numbers (no priority queue). Returns two numbers:
     * [0] flow stages: the number of zones on the longest chain of zones;
     * [1] the cost of the cheapest route from the zone of cell 0 to the zone of the last cell.
     */
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        // TODO
        int[] from = s.from();
        int[] to = s.to();
        int[] time = s.time();
        int m = s.count();

        int[] outgoingCount = new int[zoneCount];

        for (int i=0;i<m; i++){
            int fromZone = zoneOf[from[i]];
            int toZone = zoneOf[to[i]];

            if(fromZone != toZone){
                outgoingCount[fromZone]++;
                }
            }

            int[][] outgoingSlides = new int[zoneCount][];

            for (int z=0;z<zoneCount;z++){
                outgoingSlides[z] = new int[outgoingCount[z]];
            }

            int[] position = new int[zoneCount];

            for (int i=0;i<m;i++){
                int fromZone = zoneOf[from[i]];
                int toZone = zoneOf[to[i]];

                if(fromZone != toZone){
                    outgoingSlides[fromZone][position[fromZone]++] = i;
                }
            }

            int[] stages = new int[zoneCount];

            for (int z=0;z<zoneCount;z++){
                stages[z] = 1;
            }

            long[] dist = new long[zoneCount];
            java.util.Arrays.fill(dist,Long.MAX_VALUE);

            int startZone = zoneOf[0];
            int exitZone = zoneOf[s.n() - 1];

            dist[startZone] = 0;

            for(int z=0;z<zoneCount;z++){
                for(int slide : outgoingSlides[z]){
                    int nextZone = zoneOf[to[slide]];

                    stages[nextZone] = Math.max(
                            stages[nextZone],
                            stages[z] + 1
                    );

                    if(dist[z] != Long.MAX_VALUE){
                        long candidate = dist[z] + time[slide];

                        if(candidate<dist[nextZone]){
                            dist[nextZone] = candidate;
                        }
                    }
                }
            }

            int flowStages = 0;

            for (int z=0;z<zoneCount;z++){
                flowStages = Math.max(flowStages,stages[z]);
            }

            return new long[]{flowStages, dist[exitZone]};
        }

    }


