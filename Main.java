import java.io.PrintStream;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import util.MxErrorListener;
import util.error.error;
import util.error.syntaxError;
import defNodes.*;
import IR.IRGenerator;
import Codegen.ASMTransformer;

public class Main {
    public static void main(String[] args) throws Exception {

        ArgumentParser arg_parser = new ArgumentParser(args);
        InputStream input = arg_parser.getInputStream();
        PrintStream output = arg_parser.getOutputStream();
        boolean DEBUG = arg_parser.isDebug();
        // System.out.println("; " + DEBUG);

        // InputStream input = System.in;
        if (DEBUG) {
            // String filename = "test.mx";
            // input = new FileInputStream(filename);
            // PrintStream out = new PrintStream(new FileOutputStream("test.ll"));
            // System.out.println("Debug Mode");
            System.setOut(output);
            System.setErr(arg_parser.getErrorStream());
        }

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
            System.out.println("; AST Construction Done.");

            SemanticChecker checker = new SemanticChecker((ProgNode) ast_root);
            checker.check();
            System.out.println("; Semantic Check Done.");

            IRGenerator generator = new IRGenerator((ProgNode) ast_root);
            generator.generateIR();
            System.out.println("; IR Generation Done.\n");
            generator.disposeIR();
            generator.printIR();

            // ASMTransformer transformer = new ASMTransformer(generator.beg);
            // transformer.generateASM();
            // transformer.printASM();

            System.exit(0);

        } catch (error err) {

            if (err instanceof syntaxError) {
                if (DEBUG)
                    System.out.println(err.toString());
                else
                    System.out.println("Invalid Identifier");
            } else {

                if (DEBUG)
                    System.out.println(err.toString());
                else
                    System.out.println(err.getMessage());
            }
            System.exit(1);
            throw new RuntimeException();
        }
    }
}
