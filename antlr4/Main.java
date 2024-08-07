import java.io.FileInputStream;
import java.io.InputStream;

import org.antlr.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import util.MxErrorListener;
import util.error.error;

import defNodes.*;

public class Main {
    public static void main(String[] args) throws Exception {
        InputStream input = new FileInputStream(args[0]);
        try {

            Node ast_root = null;

            MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
            lexer.removeErrorListeners();
            // lexer.addErrorListener();
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            // parser.addErrorListener(null);
            MxParser.ProgContext parsetree_root = parser.prog();
            ASTConstructor constructor = new ASTConstructor();
            ast_root = (ProgNode) constructor.visit(parsetree_root);

        } catch (error err) {
            System.out.println(err.toString());
            throw new RuntimeException();
        }
    }
}
