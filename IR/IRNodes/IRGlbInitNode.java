package IR.IRNodes;

public class IRGlbInitNode extends IRNode {
    public String result = null, tp = null, val = null;

    public void printToString() {
        System.out.print(result + " = global " + tp);
        if (val != null) {
            System.out.println(" " + val);
        } else {
            System.out.println(" 0");
        }
        printNext();
    }

}
