package IR.IRNodes;

public class IRPhiNode extends IRNode {
    public String result = null, tp = null;
    public String[] vals = null, labels = null;
    public boolean eliminated = false;

    public void printToString() {

        if (eliminated) {
            printNext();
            return;
        }

        // if (shadow) {
        // printNext();
        // return;
        // }

        if (tp != null && tp.equals("void")) {
            tp = "ptr";
        }

        if (result != null && tp != null)
            System.out.print(result + " = phi " + tp + " ");
        else
            System.out.print("undef = phi undef ");

        for (int i = 0; i != vals.length; ++i) {
            System.out.print("[ " + vals[i] + ", %" + labels[i] + " ]");
            if (i != vals.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        printNext();
    }

    public String toString() {

        if (eliminated) {
            return "";
        }

        String ret = result + " = phi " + tp + " ";
        for (int i = 0; i != vals.length; ++i) {
            ret += "[ " + vals[i] + ", %" + labels[i] + " ]";
            if (i != vals.length - 1) {
                ret += ", ";
            }
        }
        return ret;
    }
}
