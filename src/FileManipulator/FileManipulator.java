package SecondSemantic.FileManipulator;

import java.io.*;

public class FileManipulator {
    private final BufferedReader reader;
    private final String filePath;
    private int currentLine, currentColumn;
    private char lastReadedChar;
    private String currentOS;

    public FileManipulator(String filePath) throws IOException {
        this.currentLine = 1;
        this.currentColumn = -1;
        this.filePath = filePath;
        reader = new BufferedReader(new FileReader(this.filePath));
        this.lastReadedChar = ' ';
    }

    public char getNextChar() throws IOException {
        if (lastReadedChar == '\n'){
            currentLine ++;
            currentColumn = -1;
        }

        int charCode = reader.read();
        lastReadedChar = (char) charCode;
        currentColumn++;

        return (char) charCode;
    }

    public String getContentLine(int lineNumber) throws IOException {
        BufferedReader tempReader = new BufferedReader(new FileReader(this.filePath));
        String line;
        int currentLine = 1;

        while ((line = tempReader.readLine()) != null) {
            if (currentLine == lineNumber) {
                tempReader.close();
                return line;
            }
            currentLine++;
        }

        tempReader.close();
        return null; // Si el número de línea está fuera del rango del archivo.
    }

    public void close() throws IOException {
        reader.close();
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    public boolean isEOF(char c) {
        return (c == (char) -1);
    }
}
