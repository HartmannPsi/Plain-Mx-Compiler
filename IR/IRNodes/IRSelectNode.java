package IR.IRNodes;

public class IRSelectNode extends IRNode {
    public String result = null, cond = null, tp = null, val1 = null, val2 = null;

    public String def() {
        return result;
    }

    public String[] use() {
        String[] res = new String[3];
        if (isActive(cond)) {
            res[0] = cond;
        }
        if (isActive(val1)) {
            res[1] = val1;
        }
        if (isActive(val2)) {
            res[2] = val2;
        }
        return res;
    }

    public void printToString() {

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.println(result + " = select i1 " + cond + ", " + tp + " " + val1 + ", " + tp + " " + val2);
        printNext();
    }

    public String toString() {
        return result + " = select i1 " + cond + ", " + tp + " " + val1 + ", " + tp + " " + val2;
    }
}
