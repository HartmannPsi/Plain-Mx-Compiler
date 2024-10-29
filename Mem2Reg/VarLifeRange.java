package Mem2Reg;

public class VarLifeRange implements Comparable<VarLifeRange> {
    public String name = null;
    public int beg = 0, end = 0;

    public VarLifeRange(String name, int beg, int end) {
        this.name = name;
        this.beg = beg;
        this.end = end;
    }

    @Override
    public int compareTo(VarLifeRange other) {
        int res1 = Integer.compare(this.end, other.end);
        if (res1 != 0) {
            return res1;
        } else {
            int res2 = Integer.compare(this.beg, other.beg);
            if (res2 != 0) {
                return res2;
            } else {
                return this.name.compareTo(other.name);
            }
        }
    }

    public String toString() {
        return name + " " + beg + " " + end;
    }
}
