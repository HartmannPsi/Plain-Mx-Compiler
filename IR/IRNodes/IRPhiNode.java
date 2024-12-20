package IR.IRNodes;

public class IRPhiNode extends IRNode {
    public String result = null, tp = null;
    public String[] vals = null, labels = null;
    // public boolean eliminated = false;

    public String[] use() {
        String[] res = new String[vals.length];
        for (int i = 0; i != vals.length; ++i) {
            if (isActive(vals[i])) {
                res[i] = vals[i];
            }
        }
        return res;
    }

    public String def() {
        return result;
    }

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

        if (result != null)
            System.out.print(result + " ");
        else
            System.out.print("undef ");

        System.out.print("= phi ");

        if (tp != null)
            System.out.print(tp + " ");
        else
            System.out.print("undef ");

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
