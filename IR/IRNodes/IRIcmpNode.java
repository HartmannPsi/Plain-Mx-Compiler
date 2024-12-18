package IR.IRNodes;

public class IRIcmpNode extends IRNode {
    public String result = null, cond = null, tp = null, op1 = null, op2 = null;

    public void printToString() {

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.println(result + " = icmp " + cond + " " + tp + " " + op1 + ", " + op2);
        printNext();
    }

    public String def() {
        return result;
    }

    public String[] use() {
        String[] res = new String[2];
        if (isActive(op1)) {
            res[0] = op1;
        }
        if (isActive(op2)) {
            res[1] = op2;
        }
        return res;
    }

    public String toString() {
        return result + " = icmp " + cond + " " + tp + " " + op1 + ", " + op2;
    }
}
