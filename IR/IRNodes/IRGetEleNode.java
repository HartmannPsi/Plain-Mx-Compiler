package IR.IRNodes;

public class IRGetEleNode extends IRNode {
    public String result = null, tp = null, ptr = null;
    public String[] tps = null, idxs = null;

    public void printToString() {

        if (shadow) {
            printNext();
            return;
        }

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
}
