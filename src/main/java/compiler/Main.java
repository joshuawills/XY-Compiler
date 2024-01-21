package compiler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import compiler.nodes.NodeFunction;
import compiler.nodes.NodeProgram;

public class Main {

    private String source;
    private final HashMap<String, String> commandArgs = new HashMap<>();
    private final HashMap<String, String> configSettings = new HashMap<>();
    
    public Main() {}
   
    private void setSource(String file_path) {
        try {
            this.source = Files.readString(Paths.get(file_path));
        } catch (Exception e) {
            Error.handleError("KEY", "Unable to open specified file, check it exists");
        }
    }

    private String getFileSource() {
        return this.source;
    }

    private void help() {
        System.out.println("XY Compiler Options:");
        System.out.println("\t-h | --help => Provides summary of CL arguments and use of program");
        System.out.println("\t-r | --run => Will run the program after compilation");
        System.out.println("\t-o | --out => Specify the name of the executable (default to a.out)");
        System.out.println("\t-t | --tokens => Logs to stdout a summary of all the tokens");
        System.out.println("\t-p | --parser => Logs to stdout a summary of the parse tree");
        System.out.println("\t-a | --assembly => Generates a .c file instead of an executable");
        System.out.println("\t-q | --quiet  => Silence any non-crucial warnings");
        System.out.println("\nDeveloped by Joshua Wills 2024");
        System.out.println("See https://github.com/joshuawills/XY-Compiler for documentation and source code");
        System.exit(0);
    }

    private void handleCLArgs(ArrayList<String> args) {

        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            switch (arg) {
                case "-h":
                case "--help":
                    this.commandArgs.put("help", "true");
                case "-r":
                case "--run":
                    this.commandArgs.put("run", "true");
                    break;
                case "-o":
                case "--out":
                    String fileName = args.get(i + 1);
                    if (fileName == null) break;
                    this.commandArgs.put("executableName", fileName);
                    i++;
                    break;
                case "-t":
                case "--tokens":
                    this.commandArgs.put("tokensLog", "true");
                    break;
                case "-p":
                case "--parser":
                    this.commandArgs.put("parserLog", "true");
                    break;
                case "-a":
                case "--assembly":
                    this.commandArgs.put("assembly", "true");
                    break;
                case "-q":
                case "--quiet":
                    this.commandArgs.put("quiet", "true");
                    break;
                default:
                    // Assume you've provided the filename then
                    this.commandArgs.put("sourceName", arg);
            }
        }

    }

    private void handleShellCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode != 0)
                Error.handleError("KEY", "Failed to execute shell command: " + command);

        } catch (Exception e) {
            Error.handleError("KEY", "Failed to execute shell command: " + command);
        }
    }

    public static void main(String[] args) {
        
        Main myCompiler = new Main();
        myCompiler.handleCLArgs(new ArrayList<>(Arrays.asList(args)));

        if (myCompiler.commandArgs.containsKey("help"))
            myCompiler.help();
        
        // if (!myCompiler.commandArgs.containsKey("sourceName"))
        //     Error.handleError("KEY", "No source filename provided");

        // String filePath = myCompiler.commandArgs.get("sourceName");
        String filePath = "test.xy";
        myCompiler.setSource(filePath);

        Lexer myLexer = new Lexer(myCompiler.getFileSource());

        // Read in config settings
        File possibleConfig = new File("xy.config");
        if (possibleConfig.exists() && !possibleConfig.isDirectory()) {
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("xy.config"))) {
                String line;
                while ((line = reader.readLine()) != null)
                    lines.add(line);
            } catch (Exception e) {
                Error.handleError("CONFIG HANDLER", "Unexpected error in reading config file 'xy.config'");
            }

            for (String line: lines) {
                String command = line.split("=")[0];
                String directive = line.split("=")[1].toLowerCase().strip();
                if (command.strip().startsWith("#"))
                    continue;
                if (!directive.equals("true") && !directive.equals("false")) {
                    Error.minorError("CONFIG", "Directive '" + directive + "' not recognized");
                    continue;
                }
                myCompiler.configSettings.put(command, directive);
            }
        }

        ArrayList<Token> tokens = myLexer.tokenize();
        if (myCompiler.commandArgs.containsKey("tokensLog")) {
            System.out.println("TOKENS: ");
            for (Token x: tokens) 
                System.out.println("\t" + x.toString());
        }
            
        Parser myParser = new Parser(tokens, myCompiler.configSettings);
        NodeProgram myNode = myParser.parseProgram();
        // if (myCompiler.commandArgs.containsKey("parserLog")) {
            System.out.println("PARSER: \n");
            for (NodeFunction function: myNode.getNodeFunctions())
                System.out.println(function.toString());
        // }

        Generator myGenerator = new Generator(myNode);

        Verifier myVerifier = new Verifier(myNode, myCompiler.configSettings);
        myVerifier.verify();

        String contents = myGenerator.generateProgram();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("out.c"));
            writer.write(contents);
            writer.close();

            if (myCompiler.commandArgs.containsKey("assembly"))
                System.exit(0);

            String executableName = "a.out";
            if (myCompiler.commandArgs.containsKey("executableName"))
                executableName = myCompiler.commandArgs.get("executableName");

            myCompiler.handleShellCommand(String.format("gcc out.c -o %s", executableName));
            myCompiler.handleShellCommand("rm out.c");
            if (myCompiler.commandArgs.containsKey("run"))  {
                ProcessBuilder runProcessBuilder = new ProcessBuilder("./" + executableName);
                runProcessBuilder.redirectErrorStream(true);
                Process runProcess = runProcessBuilder.start();
                BufferedReader runReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                String runOutput;
                while ((runOutput = runReader.readLine()) != null)
                    System.out.println(runOutput);
                int runExitCode = runProcess.waitFor();
                if (runExitCode != 0)
                    Error.handleError("KEY", "error with running executable");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}