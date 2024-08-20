package IR.IRNodes;

public class IRDclFuncNode extends IRNode {
    public String result_tp = null, func_name = null;
    public String[] tps = null, ids = null;

    public void printToString() {

        shadow = false;

        if (shadow) {
            printNext();
            return;
        }

        System.out.print("declare " + result_tp + " " + func_name + "(");
        if (tps != null) {
            for (int i = 0; i != tps.length; ++i) {
                System.out.print(tps[i] + " " + ids[i]);
                if (i != tps.length - 1) {
                    System.out.print(", ");
                }
            }
        }
        System.out.println(")");

        printNext();
    }
}
