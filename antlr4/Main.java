
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import util.MxErrorListener;
import util.error.error;
import defNodes.*;

public class Main {
    public static void main(String[] args) throws Exception {

        InputStream input = System.in;

        String filename = "antlr4/testcases/simple.txt";
        input = new FileInputStream(filename);
        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        System.setOut(out);

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

            // System.out.println("AST Done.");
            // ast_root.printToString();

            SemanticChecker checker = new SemanticChecker((ProgNode) ast_root);
            checker.check();

            System.out.println("Semantic Check Done: No Error.");
            System.exit(0);

        } catch (error err) {
            System.out.println(err.toString());
            System.exit(1);
            throw new RuntimeException();
        }
    }
}
