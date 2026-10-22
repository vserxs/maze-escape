/**
 * Checks of Mission 0. Run:  java -cp out DisjointSetTest
 * Add your own checks below: a large chain, a union of a set with itself, count after many unions.
 */
public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String what) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what);
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        DisjointSet d = new DisjointSet(6);
        check(d.count() == 6, "6 sets at the start");
        check(!d.connected(0, 1), "0 and 1 are apart at the start");
        check(d.union(0, 1), "union(0, 1) joins two sets");
        check(d.connected(0, 1), "0 and 1 are together");
        check(!d.union(1, 0), "union(1, 0) again returns false");
        check(d.count() == 5, "5 sets after one union");
        d.union(2, 3); d.union(1, 3);
        check(d.connected(0, 2), "0 and 2 are together through 1 and 3");
        check(d.count() == 3, "3 sets: {0,1,2,3} {4} {5}");
        check(d.find(4) == 4 && d.find(5) == 5, "a single cell is its own root");

        // TODO: add your own checks here
        DisjointSet self = new DisjointSet(3);

        check(!self.union(1, 1),
                "union of an element with itself returns false");

        check(self.count() == 3,
                "self-union does not change count");

        int n = 10_000;
        DisjointSet large = new DisjointSet(n);

        for (int i = 1; i < n; i++) {
            large.union(i - 1, i);
        }

        check(large.connected(0, n - 1),
                "large chain connects first and last elements");

        check(large.count() == 1,
                "count after joining 10000 elements");

        check(!large.union(0, n - 1),
                "union of already connected elements returns false");

        check(large.count() == 1,
                "repeated union does not change count");

        System.out.println(failed == 0 ? "All checks passed." : failed + " checks failed.");
    }
}
