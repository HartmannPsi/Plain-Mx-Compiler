package IR.IRNodes;

public class IRBinaryNode extends IRNode {
    public String result = null, tp = null, op1 = null, op2 = null, operator = null;

    public void printToString() {
        System.out.println(result + " = " + operator + " " + tp + " " + op1 + ", " + op2);
        printNext();
    }
}
