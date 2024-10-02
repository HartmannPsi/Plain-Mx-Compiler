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
import java.io.FileInputStream;
import java.io.IOException;
import Mem2Reg.*;

public class Main {
    public static void main(String[] args) throws Exception {

        ArgumentParser arg_parser = new ArgumentParser(args);
        InputStream input = arg_parser.getInputStream();
        boolean DEBUG = arg_parser.isDebug();

        if (DEBUG) {
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

            if (DEBUG) {
                System.setOut(new PrintStream(arg_parser.getLLVMStream()));
            }

            // System.out.println("; AST Construction Done.");

            SemanticChecker checker = new SemanticChecker((ProgNode) ast_root);
            checker.check();
            // System.out.println("; Semantic Check Done.");

            IRGenerator generator = new IRGenerator((ProgNode) ast_root);
            generator.generateIR();
            // System.out.println("; IR Generation Done.\n");
            generator.disposeIR();
            // generator.printIR();

            if (DEBUG) {
                System.setOut(new PrintStream(arg_parser.getLLVMStream()));
                generator.printIR();
            }

            /*************/

            if (DEBUG) {
                System.setOut(new PrintStream(arg_parser.getOptiStream()));
            }

            IROptimizer optimizer = new IROptimizer(generator.beg);

            System.out.println("; OPTI");

            optimizer.calcCFG();

            System.out.println("; CCFG");

            optimizer.calcDominate();

            System.out.println("; CDOM");

            optimizer.placePhi();

            System.out.println("; PPHI");

            optimizer.printIR();

            optimizer.eliminatePhi();

            // System.setOut(System.out);

            // if (DEBUG) {
            // System.setOut(new PrintStream(arg_parser.getASMStream()));
            // }

            // // ASMTransformer transformer = new ASMTransformer(generator.beg);
            // ASMTransformer transformer = new ASMTransformer(optimizer.ir_beg);
            // transformer.generateASM();
            // System.out.println("# ASM Generation Done.\n");

            // try (FileInputStream fis = new FileInputStream("IR/builtin.s")) {
            // byte[] buffer = new byte[1024];
            // int length;
            // while ((length = fis.read(buffer)) != -1) {
            // System.out.write(buffer, 0, length);
            // }
            // } catch (IOException e) {
            // e.printStackTrace();
            // }

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

        } catch (Throwable e) {
            System.exit(0);
        }
    }
}
