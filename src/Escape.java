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
        int cols = w.cols();
        int n = w.cells();
        int exit = n - 1;

        int[][] graph = Graphs.open(w,broken);

        int deadEnds = 0;

        for(int i=0;i<n;i++){
            if(graph[i].length == 1){
                deadEnds++;
            }
        }

        boolean[] visited = new boolean[n];

        int[] parent = new int[n];
        java.util.Arrays.fill(parent,-1);

        int[] stackCell = new int[n];
        int[] stackDirection = new int[n];

        visited[0] = true;
        int visitedCount = 1;

        int top = 0;
        stackCell[top] = 0;
        stackDirection[top] = 0;

        while(top >= 0 && !visited[exit]){

            int cell = stackCell[top];

            if(stackDirection[top] == 4){
                top--;
                continue;
            }

            int direction = stackDirection[top]++;
            int next = -1;


            if(direction == 0 && cell >= cols){
                next = cell - cols;
            }

            else if(direction == 1 && cell % cols < cols - 1){
                next = cell + 1;
            }

            else if(direction == 2 && cell < n - cols){
                next = cell + cols;
            }

            else if(direction == 3 && cell % cols > 0){
                next = cell - 1;
            }

            if(next == -1){
                continue;
            }

            boolean open = false;

            for(int neighbour : graph[cell]){
                if (neighbour == next) {
                    open = true;
                    break;
                }
            }

            if(!open || visited[next]){
                continue;
            }

            visited[next] = true;
            parent[next] = cell;
            visitedCount++;

            if (next == exit) {
                break;
            }

            top++;
            stackCell[top] = next;
            stackDirection[top] = 0;
        }

        if (!visited[exit]){
            return new Result(new int[0],visitedCount,deadEnds);
        }


        int pathLength = 0;

        for (int cell=exit;cell != -1;cell=parent[cell]){
            pathLength++;
        }

        int[] path = new int[pathLength];
        int cell = exit;

        for (int i=pathLength-1;i>=0;i--){
            path[i] = cell;
            cell = parent[cell];
        }
        return new Result(path,visitedCount,deadEnds);
    }
}
