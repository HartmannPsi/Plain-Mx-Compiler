package defNodes.exprNodes;

import defNodes.*;

public class FStringNode extends ExprNode {
    public String[] literals = null;
    public Node[] exprs = null;

    public void printToString() {
        System.out.print("f\"");
        if (exprs != null) {
            for (int i = 0; i != exprs.length; ++i) {
                System.out.print(literals[i] + "$ ");
                exprs[i].printToString();
                System.out.print(" $");
            }
        }
        System.out.print(literals[literals.length - 1] + "\"");
    }

    public String pracVal(int n) {
        String ret = "";
        for (int i = 0; i != literals[n].length(); ++i) {
            if (literals[n].charAt(i) == '$') {
                ret += "$";
                ++i;
            } else {
                ret += literals[n].charAt(i);
            }
        }
        return ret;
    }

    public String toString(int n) {
        String ret = "";

        for (int i = 0; i != literals[n].length(); ++i) {
            if (literals[n].charAt(i) == '\\') {
                ++i;
                switch (literals[n].charAt(i)) {
                    case 'n':
                        ret += "\\0A";
                        break;
                    case '\"':
                        ret += "\\22";
                        break;
                    case '\\':
                        ret += "\\\\";
                        break;
                    default:
                        ret += literals[n].charAt(i);
                        break;
                }
            } else if (literals[n].charAt(i) == '$') {
                ret += "$";
                ++i;
            } else {
                ret += literals[n].charAt(i);
            }
        }

        return ret;
    }

    public int getLength(int n) {
        int ret = 0;
        for (int i = 0; i != literals[n].length(); ++i) {
            if (literals[n].charAt(i) == '\\' || literals[n].charAt(i) == '$') {
                ++i;

            }
            ++ret;
        }

        return ret;
    }
}
