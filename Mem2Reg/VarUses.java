package Mem2Reg;

import IR.IRNodes.*;
import java.util.Set;
import java.util.HashSet;

public class VarUses {
    public IRNode def = null;
    public Set<IRNode> uses = new HashSet<>();
}
