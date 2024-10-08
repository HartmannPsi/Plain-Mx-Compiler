package IR.IRNodes;

public class IRGetEleNode extends IRNode {
    public String result = null, tp = null, ptr = null;
    public String[] tps = null, idxs = null;

    public String def() {
        return result;
    }

    public String[] use() {
        String[] res = new String[idxs.length + 1];
        if (isActive(ptr)) {
            res[0] = ptr;
        }
        for (int i = 0; i != idxs.length; ++i) {
            if (isActive(idxs[i])) {
                res[i + 1] = idxs[i];
            }
        }
        return res;
    }

    public void printToString() {

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.print(result + " = getelementptr " + tp + ", ptr " + ptr + ", ");
        for (int i = 0; i != tps.length; ++i) {
            System.out.print(tps[i] + " " + idxs[i]);
            if (i != tps.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        printNext();
    }

    public String toString() {

        String ret = result + " = getelementptr " + tp + ", ptr " + ptr + ", ";
        for (int i = 0; i != tps.length; ++i) {
            ret += tps[i] + " " + idxs[i];
            if (i != tps.length - 1) {
                ret += ", ";
            }
        }
        return ret;
    }
}
