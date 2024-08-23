package IR.IRNodes;

public class IRGlbInitNode extends IRNode {
    public String result = null, tp = null, val = null;

    public void printToString() {

        // shadow = false;

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.print(result + " = global " + tp);
        if (val != null) {
            System.out.println(" " + val);
        } else {
            switch (tp) {
                case "i32":
                    System.out.println(" 0");
                    break;

                case "i1":
                    System.out.println(" true");
                    break;

                default:
                    System.out.println(" null");
                    break;
            }
        }
        System.out.println();

        printNext();
    }

    public String toString() {
        String tmp;
        if (val == null) {
            switch (tp) {
                case "i32":
                    tmp = "0";
                    break;

                case "i1":
                    tmp = "true";
                    break;

                default:
                    tmp = "null";
                    break;
            }
        } else {
            tmp = val;
        }
        return result + " = global " + tp + " " + tmp;
    }
}
