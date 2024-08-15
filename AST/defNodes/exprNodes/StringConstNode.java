package defNodes.exprNodes;

public class StringConstNode extends ExprNode {
    public String val = null;

    public void printToString() {
        System.out.print("\"" + val + "\"");
    }

    public String toString() {
        String ret = "";

        for (int i = 0; i != val.length(); ++i) {
            if (val.charAt(i) == '\\') {
                ++i;
                switch (val.charAt(i)) {
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
                        ret += val.charAt(i);
                        break;
                }
            } else {
                ret += val.charAt(i);
            }
        }

        return ret;
    }

    public int getLength() {
        int ret = 0;
        for (int i = 0; i != val.length(); ++i) {
            if (val.charAt(i) == '\\') {
                ++i;

            }
            ++ret;
        }

        return ret;
    }
}
