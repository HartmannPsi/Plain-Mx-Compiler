package IR;

import defNodes.*;
import defNodes.exprNodes.*;
import defNodes.stmtNodes.*;
import util.error.*;
import util.*;

public class IRGenerator {
    ProgNode root;

    public IRGenerator(ProgNode root) {
        this.root = root;
    }

    public void generateIR() {
        visit(root);
    }

    public void throw_internal(String msg, position pos) {
        throw new internalError(msg, pos);
    }

    void visit(Node node) {
        if (node instanceof ProgNode) {
            visit((ProgNode) node);
        } else if (node instanceof BraceStmtNode) {
            visit((BraceStmtNode) node);
        } else if (node instanceof BreakNode) {
            visit((BreakNode) node);
        } else if (node instanceof ClsConsNode) {
            visit((ClsConsNode) node);
        } else if (node instanceof ContinueNode) {
            visit((ContinueNode) node);
        } else if (node instanceof DefClassNode) {
            visit((DefClassNode) node);
        } else if (node instanceof DefFuncNode) {
            visit((DefFuncNode) node);
        } else if (node instanceof DefVarNode) {
            visit((DefVarNode) node);
        } else if (node instanceof EmptyStmtNode) {
            visit((EmptyStmtNode) node);
        } else if (node instanceof ExprStmtNode) {
            visit((ExprStmtNode) node);
        } else if (node instanceof ForStmtNode) {
            visit((ForStmtNode) node);
        } else if (node instanceof IfStmtNode) {
            visit((IfStmtNode) node);
        } else if (node instanceof ReturnNode) {
            visit((ReturnNode) node);
        } else if (node instanceof WhileStmtNode) {
            visit((WhileStmtNode) node);
        } else if (node instanceof ArrayAccessNode) {
            visit((ArrayAccessNode) node);
        } else if (node instanceof ArrayNode) {
            visit((ArrayNode) node);
        } else if (node instanceof BinaryOpNode) {
            visit((BinaryOpNode) node);
        } else if (node instanceof BoolConstNode) {
            visit((BoolConstNode) node);
        } else if (node instanceof FStringNode) {
            visit((FStringNode) node);
        } else if (node instanceof FuncCallNode) {
            visit((FuncCallNode) node);
        } else if (node instanceof IdNode) {
            visit((IdNode) node);
        } else if (node instanceof MemAccNode) {
            visit((MemAccNode) node);
        } else if (node instanceof NewExprNode) {
            visit((NewExprNode) node);
        } else if (node instanceof NullNode) {
            visit((NullNode) node);
        } else if (node instanceof NumConstNode) {
            visit((NumConstNode) node);
        } else if (node instanceof StringConstNode) {
            visit((StringConstNode) node);
        } else if (node instanceof TernaryOpNode) {
            visit((TernaryOpNode) node);
        } else if (node instanceof ThisNode) {
            visit((ThisNode) node);
        } else if (node instanceof UnaryOpNode) {
            visit((UnaryOpNode) node);
        } else if (node instanceof TypeNode) {
            visit((TypeNode) node);
        } else if (node == null) {
            return;
        } else {
            throw_internal("Unknown Node Type", node.pos);
        }
    }

    void visit(ProgNode node) {
    }

    void visit(BraceStmtNode node) {
    }

    void visit(BreakNode node) {
    }

    void visit(ClsConsNode node) {
    }

    void visit(ContinueNode node) {
    }

    void visit(DefClassNode node) {
    }

    void visit(DefFuncNode node) {
    }

    void visit(DefVarNode node) {
    }

    void visit(EmptyStmtNode node) {
    }

    void visit(ExprStmtNode node) {
    }

    void visit(ForStmtNode node) {
    }

    void visit(IfStmtNode node) {
    }

    void visit(ReturnNode node) {
    }

    void visit(WhileStmtNode node) {
    }

    void visit(ArrayAccessNode node) {
    }

    void visit(ArrayNode node) {
    }

    void visit(BinaryOpNode node) {
    }

    void visit(BoolConstNode node) {
    }

    void visit(FStringNode node) {
    }

    void visit(FuncCallNode node) {
    }

    void visit(IdNode node) {
    }

    void visit(MemAccNode node) {
    }

    void visit(NewExprNode node) {
    }

    void visit(NullNode node) {
    }

    void visit(NumConstNode node) {
    }

    void visit(StringConstNode node) {
    }

    void visit(TernaryOpNode node) {
    }

    void visit(ThisNode node) {
    }

    void visit(UnaryOpNode node) {
    }

    void visit(TypeNode node) {
    }

}
