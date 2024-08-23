package IR.IRNodes;

public class IRSelectNode extends IRNode {
    public String result = null, cond = null, tp = null, val1 = null, val2 = null;

    public void printToString() {

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.println(result + " = select i1 " + cond + ", " + tp + " " + val1 + ", " + tp + " " + val2);
        printNext();
    }
}
