import java.io.FileInputStream;
import java.io.InputStream;

import org.antlr.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.semantics.SymbolCollector;

import util.MxErrorListener;
import util.error.error;

import defNodes.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String filename = "antlr4/testcases/testAST.txt";
        InputStream input = new FileInputStream(filename);
        try {

            Node ast_root = null;

            MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new MxErrorListener());
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new MxErrorListener());
            MxParser.ProgContext parsetree_root = parser.prog();
            ASTConstructor constructor = new ASTConstructor();
            ast_root = (ProgNode) constructor.visit(parsetree_root);
            System.out.println("AST Done");

            SemanticChecker checker = new SemanticChecker((ProgNode) ast_root);
            checker.check();

        } catch (error err) {
            System.out.println(err.toString());
            throw new RuntimeException();
        }
    }
}
