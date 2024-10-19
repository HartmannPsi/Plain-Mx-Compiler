package IR.IRNodes;

public class IRMvNode extends IRBinaryNode {
    // public String result = null, src = null;

    public String def() {
        if (IRNode.var_def.containsKey(result) && IRNode.var_def.get(result) == order) {
            return result;
        } else {
            return null;
        }
    }

    public String[] use() {
        String[] res = new String[3];
        if (isActive(op1)) {
            res[0] = op1;
        }
        if (isActive(op2)) {
            res[1] = op2;
        }
        if (IRNode.var_def.containsKey(result) && IRNode.var_def.get(result) < order) {
            res[2] = result;
        }
        return res;
    }

    public void printToString() {

        // shadow = false;

        // if (shadow) {
        // printNext();
        // return;
        // }

        if (tp.equals("ptr")) {
            tp = "i32";
        }

        System.out.println(result + " = add " + tp + " 0, " + op2 + "; this is a MV instruction to eliminate PHI");

        printNext();
    }

    public String toString() {
        if (tp.equals("ptr")) {
            tp = "i32";
        }

        return result + " = add " + tp + " 0, " + op2 + "; this is a MV instruction to eliminate PHI";
    }

}
