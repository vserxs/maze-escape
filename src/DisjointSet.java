/**
 * Mission 0. Disjoint set union (DSU).
 * Cells are numbered 0 .. n-1. Write the whole class here.
 * Needed: union by size (the smaller tree goes under the larger one) and path compression in find.
 */
public class DisjointSet {
        // TODO
        private final int[] parent;
        private final int[] size;
        private int setCOunt;

        public DisjointSet(int n){
            parent = new int[n];
            size = new int[n];
            setCOunt = n;

            for(int i=0;i<n;i++){
                parent[i] = i;
                size[i] = 1;
            }
        }


    /** The root of the set that contains x. */
    public int find(int x) {
        // TODO
        if(parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /** Joins the sets of a and b. Returns true if they were different sets, false if they were already one set. */
    public boolean union(int a, int b) {
        // TODO
        int rootA = find(a);
        int rootB = find(b);

        if(rootA == rootB){
            return false;
        }
        if(size[rootA] < size[rootB]){
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }
        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        setCOunt--;

        return true;
    }

    public boolean connected(int a, int b) {
        // TODO
        return find(a) == find(b);
    }

    /** Number of sets now. */
    public int count() {
        // TODO
        return setCOunt;
    }
}
