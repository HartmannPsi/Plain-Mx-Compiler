package IR.IRNodes;

import java.util.Set;

public class IRRetNode extends IRNode {
    public String tp = null, val = null;
    public Set<String> used_regs = null;

    public String[] use() {
        if (val != null && isActive(val)) {
            return new String[] { val };
        } else {
            return null;
        }
    }

    public void printToString() {

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.print("ret " + tp);
        if (val != null) {
            System.out.println(" " + val);
        } else {
            System.out.println();
        }
        // shadow = true;

        printNext();
    }

    public String toString() {
        if (val == null) {
            return "ret " + tp;
        } else {
            return "ret " + tp + " " + val;
        }
    }
}
