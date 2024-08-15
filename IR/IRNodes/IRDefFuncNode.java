package IR.IRNodes;

public class IRDefFuncNode extends IRNode {
    public String result_tp = null, func_name = null;
    public String[] tps = null, ids = null;
    public IRNode stmt = null;

    public void printToString() {
        System.out.print("define " + result_tp + " " + func_name + "(");

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

        printNext();
    }
}
