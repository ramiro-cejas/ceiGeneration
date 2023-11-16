package SecondSemantic.Generation;

import SecondSemantic.Extras.CompiException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class CodeGenerator {

    private String path;

    public CodeGenerator(String path) throws CompiException {
        String fileName = "a.out";

        // Creating the folder
        File folder = new File(path);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                //do nothing
            } else {
                throw new GenerationException("Failed to create the folder.");
            }
        }

        // Creating the file inside the folder
        String filePath = path + File.separator + fileName;
        this.path = filePath;
        try {
            File file = new File(filePath);
            file.createNewFile();
        } catch (IOException e) {
            throw new GenerationException("An error occurred while creating the file.");
        }
    }

    public void gen(String code) throws CompiException {
        //put the given string in a new line into the file
        try {
            java.nio.file.Files.writeString(Path.of(path), code + "\n", java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GenerationException("Error while writing the code into the file");
        }
    }

}