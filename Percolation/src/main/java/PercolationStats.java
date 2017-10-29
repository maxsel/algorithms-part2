import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

    private int T;
    private double[] comps;
    
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0)
            throw new IllegalArgumentException("N <= 0");
        else if (T <= 0)
            throw new IllegalArgumentException("T <= 0");
        this.T = T;
        this.comps = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation p = new Percolation(N);
            int openSites = 0;
            while (!p.percolates()) {
                int row = StdRandom.uniform(N)+1;
                int col = StdRandom.uniform(N)+1;
                if (!(p.isOpen(row, col))) {
                    p.open(row, col);
                    openSites++;
                }
            }
            comps[i] = ((double) openSites)/(N*N);
        }
    }
    
    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(comps);
    }
    
    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(comps);
    }
    
    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        return (mean() - 1.96*stddev()/Math.sqrt(T));
    }
    
    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        return (mean() + 1.96*stddev()/Math.sqrt(T));
    }
    
    // test client, described below
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        StdOut.printf("mean                    = %f\n", ps.mean());
        StdOut.printf("stddev                  = %f\n", ps.stddev());
        String lo = Double.toString(ps.confidenceLo());
        String hi = Double.toString(ps.confidenceHi());
        StdOut.printf("95%% confidence interval = %s, %s\n", lo, hi);
    }
}