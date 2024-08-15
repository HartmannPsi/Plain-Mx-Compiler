package IR.IRNodes;

public class IRIcmpNode extends IRNode {
    public String result = null, cond = null, tp = null, op1 = null, op2 = null;

    public void printToString() {
        System.out.println(result + " = icmp " + cond + " " + tp + " " + op1 + ", " + op2);
        printNext();
    }
}
