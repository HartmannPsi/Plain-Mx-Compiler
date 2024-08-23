package IR.IRNodes;

public class IRDefFuncNode extends IRNode {
    public String result_tp = null, func_name = null;
    public String[] tps = null, ids = null;
    public IRNode stmt = null;

    public void printToString() {

        // shadow = false;

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.print("define dso_local " + result_tp + " " + func_name + "(");

        if (tps != null) {
            for (int i = 0; i != tps.length; ++i) {
                System.out.print(tps[i] + " " + ids[i]);
                if (i != tps.length - 1) {
                    System.out.print(", ");
                }
            }
        }

        System.out.println(") {");
        if (stmt != null)
            stmt.printToString();
        System.out.println("}");
        System.out.println();

        printNext();
    }

    public String toString() {
        String res = "define dso_local " + result_tp + " " + func_name + "(";

        if (tps != null) {
            for (int i = 0; i != tps.length; ++i) {
                res += tps[i] + " " + ids[i];
                if (i != tps.length - 1) {
                    res += ", ";
                }
            }
        }
        res += ") { ... }";
        return res;
    }
}
