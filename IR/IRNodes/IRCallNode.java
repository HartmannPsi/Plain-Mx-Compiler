package IR.IRNodes;

public class IRCallNode extends IRNode {
    public String result = null, res_tp = null, func_name = null;
    public String[] args = null, tps = null;

    public void printToString() {

        // if (shadow) {
        // printNext();
        // return;
        // }

        if (res_tp.equals("void")) {
            System.out.print("call void " + func_name + "(");
        } else {
            System.out.print(result + " = call " + res_tp + " " + func_name + "(");
        }

        if (args != null) {
            for (int i = 0; i != args.length; ++i) {
                System.out.print(tps[i] + " " + args[i]);
                if (i != args.length - 1) {
                    System.out.print(", ");
                }
            }
        }

        System.out.println(")");
        printNext();
    }

    public String toString() {

        String ret;

        if (res_tp.equals("void")) {
            ret = "call void " + func_name + "(";
        } else {
            ret = result + " = call " + res_tp + " " + func_name + "(";
        }

        if (args != null) {
            for (int i = 0; i != args.length; ++i) {
                ret += tps[i] + " " + args[i];
                if (i != args.length - 1) {
                    ret += ", ";
                }
            }
        }

        return ret + ")";
    }
}
