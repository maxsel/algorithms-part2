import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    
    // grid size, virtual top and bottom
    private int N;
    private int vtop;
    private int vbot;
    
    // which open are opened
    private boolean[][] open;
    
    // which open are connected to which other open
    // uf1 - two virtual sites; uf2 - only virtual top
    private WeightedQuickUnionUF uf1;
    private WeightedQuickUnionUF uf2;
    
    // create N-by-N grid, with all open blocked
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("N <= 0");
        this.N = N;
        this.vtop = 0;
        this.vbot = N*N+1;
        open = new boolean[N+1][N+1];
        for (int i = 0; i < N+1; i++)
            for (int j = 0; j < N+1; j++)
                open[i][j] = false;
        uf1 = new WeightedQuickUnionUF(N*N+2);
        uf2 = new WeightedQuickUnionUF(N*N+1);
    }
    
    // mapping 2D coordinates to 1D coordinates
    private int xyTo1D(int row, int col) {
        return (row-1)*N + col;
    }
    
    // validating indices
    private void validate(int i, int j) {
        if (i <= 0 || i > N)
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j <= 0 || j > N)
            throw new IndexOutOfBoundsException("column index j out of bounds");
    }
    
    // double union
    private void union(int i, int j) {
        uf1.union(i, j);
        uf2.union(i, j);
    }
    
    // open site (row i, column j) if it is not already
    public void open(int i, int j) {
        validate(i, j);
        if (open[i][j]) return;
        open[i][j] = true;
        
        if (N == 1) {
            union(1, vtop);
            uf1.union(1, vbot);
            return;
        }
        
        int site = xyTo1D(i, j);
        int up = xyTo1D(i-1, j);
        int down = xyTo1D(i+1, j);
        int right = xyTo1D(i, j+1);
        int left = xyTo1D(i, j-1);
        
        if (i == 1) {
            union(site, vtop);
            if (isOpen(i+1, j))
                union(site, down);
        }
        else {
            if (i == N) {
                if (isOpen(i-1, j))
                    union(site, up);
                uf1.union(site, vbot);
            }
            else {
                if (isOpen(i-1, j))
                    union(site, up);
                if (isOpen(i+1, j))
                    union(site, down);
            }
        }

        if (j == 1) {
            if (isOpen(i, j+1)) {
                union(site, right);
            }
        }
        else {
            if (j == N) {
                if (isOpen(i, j-1)) {
                    union(site, left);
                }
            }
            else {
                if (isOpen(i, j+1)) {
                    union(site, right);
                }
                if (isOpen(i, j-1)) {
                    union(site, left);
                }
            }
        }
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return open[i][j];
    }
    
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        validate(i, j);
        return uf2.connected(vtop, xyTo1D(i, j));
    }
    
    // number of open sites
    public int numberOfOpenSites() {
        int numOpen = 0;
        for (int i = 0; i < N+1; i++)
            for (int j = 0; j < N+1; j++)
                if (open[i][j])
                    numOpen++;
        return numOpen;
    }
    
    // does the system percolate?
    public boolean percolates() {
        return uf1.connected(vtop, vbot);
    }
    
    // test method
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Percolation p = new Percolation(n);
        int i = StdIn.readInt();
        int j = StdIn.readInt();
        p.open(i, j);
        StdOut.println(p.percolates());
    }
}