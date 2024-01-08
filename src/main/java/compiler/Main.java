package compiler;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import compiler.nodes.NodeProgram;
import compiler.nodes.statement_nodes.NodeStatement;

public class Main {

    private String source;

    public String getFileSource() {
        return this.source;
    }

    public Main(String file_path) {
        try {
            this.source = Files.readString(Paths.get(file_path));
        } catch (Exception e) {
            this.source = "";
            System.exit(1);
        }
    }


    public static void main(String[] args) {
        
        String filename;
        if (args.length == 0)
            filename = "test.xy";
        else
            filename = args[0];


        Main myCompiler = new Main(filename);
        Lexer myLexer = new Lexer(myCompiler.getFileSource());
        ArrayList<Token> tokens = myLexer.tokenize();
        // for (Token x: tokens) 
        //     System.out.println(x.toString());
            
        Parser myParser = new Parser(tokens);
        NodeProgram myNode = myParser.parseProgram();

        for (NodeStatement statement: myNode.getStatements())
            System.out.println(statement.toString());

        Generator myGenerator = new Generator(myNode);
        String contents = myGenerator.generateProgram();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("out.asm"));
            writer.write(contents);
            String macros = Files.readString(Paths.get("src/main/java/compiler/macros.txt"));
            writer.write(macros);
            writer.close();
            Runtime.getRuntime().exec("nasm -felf64 out.asm");
            // Runtime.getRuntime().exec("ld -o test out.o");
            Runtime.getRuntime().exec("gcc -no-pie -o test out.o");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}