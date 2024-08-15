package IR.IRNodes;

public class IRPhiNode extends IRNode {
    public String result = null, tp = null;
    public String[] vals = null, labels = null;

    public void printToString() {
        System.out.print(result + " = phi " + tp + " ");
        for (int i = 0; i != vals.length; ++i) {
            System.out.print("[ " + vals[i] + ", " + labels[i] + " ]");
            if (i != vals.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        printNext();
    }
}
