import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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
        }
    }


    public static void main(String[] args) {
        
        Main myCompiler = new Main("test.xy");
        Lexer myLexer = new Lexer(myCompiler.getFileSource());
        ArrayList<Token> tokens = myLexer.tokenize();

        for (Token x: tokens) 
            System.out.println(x.toString());   
            
        String contents = Generator.tokensToASM(tokens);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("out.asm"));
            writer.write(contents);
            writer.close();
            Runtime.getRuntime().exec("nasm -felf64 out.asm");
            Runtime.getRuntime().exec("ld out.o -o test");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}