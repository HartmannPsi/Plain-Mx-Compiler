import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.InputStream;

public class ArgumentParser {
    private String inputFile = null;
    private String outputFile = null;
    private String errFile = null;
    private boolean debug = false;

    public ArgumentParser(String[] args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                case "--input":
                    if (i + 1 < args.length) {
                        inputFile = args[++i];
                    } else {
                        throw new IllegalArgumentException("Missing input file argument");
                    }
                    break;
                case "-o":
                case "--output":
                    if (i + 1 < args.length) {
                        outputFile = args[++i];
                    } else {
                        throw new IllegalArgumentException("Missing output file argument");
                    }
                    break;
                case "-e":
                case "--error":
                    if (i + 1 < args.length) {
                        errFile = args[++i];
                    } else {
                        throw new IllegalArgumentException("Missing error file argument");
                    }
                    break;
                case "-d":
                case "--debug":
                    debug = true;
                    inputFile = "test.mx";
                    outputFile = "test.ll";
                    errFile = "debug.out";
                    break;
                default:
                    throw new IllegalArgumentException("Unknown argument: " + args[i]);
            }
        }
    }

    public InputStream getInputStream() throws Exception {
        if (inputFile != null) {
            return new FileInputStream(inputFile);
        } else {
            return System.in;
        }
    }

    public PrintStream getOutputStream() throws Exception {
        if (outputFile != null) {
            return new PrintStream(new FileOutputStream(outputFile));
        } else {
            return System.out;
        }
    }

    public PrintStream getErrorStream() throws Exception {
        if (errFile != null) {
            return new PrintStream(new FileOutputStream(errFile));
        } else {
            return System.err;
        }
    }

    public boolean isDebug() {
        return debug;
    }
}