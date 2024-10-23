package Mem2Reg;

import IR.IRNodes.*;
import java.util.Map;
import java.util.ArrayList;
//import java.util.HashMap;

public class InlineRetType {
    public IRNode beg = null, end = null;
    public String ret_val = null;
    public Map<IRDefFuncNode, ArrayList<IRCallNode>> callee_update = null;

    public InlineRetType(IRNode beg, IRNode end, String ret_val,
            Map<IRDefFuncNode, ArrayList<IRCallNode>> callee_update) {
        this.beg = beg;
        this.end = end;
        this.ret_val = ret_val;
        this.callee_update = callee_update;
    }
}
