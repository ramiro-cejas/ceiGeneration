package SecondSemantic.Generation;

import SecondSemantic.Extras.CompiException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class CodeGenerator {

    private String path;
    private FileWriter myWriter;

    public CodeGenerator(String path, String name) throws CompiException {
        String fileName = "[" + name + "].out";

        this.path = fileName;
        try {
            myWriter = new FileWriter(fileName);
        } catch (IOException e) {
            throw new GenerationException("An error occurred while creating the file.");
        }
    }

    public void gen(String code) throws CompiException {
        //put the given string in a new line into the file
        try {
            myWriter.write(code + "\n");
        } catch (IOException e) {
            throw new GenerationException("Error while writing the code into the file");
        }
    }

    public void closeFile(){
        try {
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}