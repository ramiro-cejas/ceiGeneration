package SecondSemantic.Lexical;

import java.io.IOException;

import SecondSemantic.Extras.Keywords;
import SecondSemantic.FileManipulator.*;

public class LexicalAnalyzer {
    private Keywords keywords;
    private String lexeme;
    private FileManipulator fileManipulator;
    private char currentChar;

    public LexicalAnalyzer(FileManipulator fileManipulator){
        this.fileManipulator = fileManipulator;
        lexeme = "";
        initializeKeywords();
        currentChar = ' ';
    }

    public int getLine(){
        return fileManipulator.getCurrentLine();
    }
    private void initializeKeywords() {
       keywords = new Keywords();
    }

    private Token returnResult(Token tokenToReturn) throws LexicalException, IOException {
        String tokenName = tokenToReturn.getName();
        String tokenLexeme = tokenToReturn.getLexeme();

        if (tokenName == "intLiteral" && tokenLexeme.length() > 9){
            throw new LexicalException(tokenLexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"No se permiten literales enteros de mas de 9 dígitos.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));
        }
        if (tokenName == "floatLiteral"){
            checkFloat(tokenToReturn);
        }
        if (keywords.isKeyword(tokenLexeme)) {
            return new Token("keyword_"+tokenLexeme, tokenLexeme, fileManipulator.getCurrentLine());
        }else if(tokenName == "idClass" || tokenName == "idMetVar" || tokenName == "strLiteral"|| tokenName == "intLiteral" || tokenName == "floatLiteral" || tokenName == "charLiteral" || tokenName == "EOF"){
            return tokenToReturn;
        }else return new Token(tokenName+"_"+tokenLexeme,tokenLexeme, fileManipulator.getCurrentLine());
    }

    private void checkFloat(Token tokenToReturn) throws IOException, LexicalException {
        String tokenLexeme = tokenToReturn.getLexeme();
        boolean isZero = false;

        if (tokenLexeme.contains("e")){
            String[] splitted = tokenLexeme.split("e");
            if (Double.parseDouble(splitted[0]) == 0){
                isZero = true;
            }
        }
        if (tokenLexeme.contains("E")){
            String[] splitted = tokenLexeme.split("E");
            if (Double.parseDouble(splitted[0]) == 0){
                isZero = true;
            }
        }
        if (tokenLexeme.contains(".")){
            String[] splitted = tokenLexeme.split("\\.");
            if (Double.parseDouble(splitted[0]) == 0){
                isZero = true;
            }
            if (Double.parseDouble(splitted[1]) == 0){
                isZero = true;
            }
        }

        try {
            double num = Double.parseDouble(tokenLexeme);
            if (!isZero && num < Float.MIN_VALUE){
                throw new LexicalException(tokenLexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"El literal es mas pequeño que el menor float permitido.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));
            }else if (num > Float.MAX_VALUE){
                throw new LexicalException(tokenLexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"El literal es mas grande que el mayor float permitido.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));
            }
        }catch (NumberFormatException exception){
            throw new LexicalException(tokenLexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"El literal esta fuera de los rangos permitidos por los floats.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));
        }

    }

    public Token getNextToken() throws LexicalException, IOException {
        return returnResult(e0());
    }

    private void updateLexeme(){
        lexeme += currentChar;
    }

    private void updateChar(){
        try {
            currentChar = fileManipulator.getNextChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Token e0() throws LexicalException, IOException {
        lexeme = "";

        while (currentChar == ' ' || currentChar == '\n' || currentChar == '\r' || currentChar == '\t'){
            updateChar();

        }
        if(Character.isDigit(currentChar)) {
            updateLexeme();
            updateChar();
            return eLitInt();

        }else if(Character.isLowerCase(currentChar)) {
            updateLexeme();
            updateChar();
            return eIDMetVar();

        }else if(Character.isUpperCase(currentChar)) {
            updateLexeme();
            updateChar();
            return eIDClass();

        }else if(currentChar == '>' || currentChar == '<' || currentChar == '!') {
            updateLexeme();
            updateChar();
            return eOperatorsWithPossibleEquals();

        }else if(currentChar == '=') {
            updateLexeme();
            updateChar();
            return eOnlyEquals();

        }else if(currentChar == '*' || currentChar == '%') {
            updateLexeme();
            updateChar();
            return eOperators();

        }else if(currentChar == '/') {
            updateLexeme();
            updateChar();
            return eCommentsOrOperator();

        }else if(currentChar == '+' || currentChar == '-') {
            updateLexeme();
            updateChar();
            return eOperatorsOrAssignment();

        }else if(currentChar == '(' || currentChar == ')' || currentChar == '{' || currentChar == '}' || currentChar == ';' || currentChar == ',') {
            updateLexeme();
            updateChar();
            return ePunctuator();

        }else if(currentChar == '.') {
            updateLexeme();
            updateChar();
            return ePoint();

        }else if(currentChar == '&') {
            updateLexeme();
            updateChar();
            return eOperatorAmpersand();

        }else if(currentChar == '|') {
            updateLexeme();
            updateChar();
            return eOperatorVerticalBar();

        }else if(currentChar == '"') {
            updateLexeme();
            updateChar();
            return eString1();

        }else if(currentChar == '\'') {
            updateLexeme();
            updateChar();
            return eChar1();

        }else if(fileManipulator.isEOF(currentChar)) {
            return eFinal();

        }else {
            updateLexeme();
            updateChar();
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Caractér inválido.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));
        }
    }

    private Token ePoint() throws LexicalException, IOException {
        if (Character.isDigit(currentChar)){
            updateLexeme();
            updateChar();
            return eFloatNumber1();
        }else {
            return ePunctuator();
        }
    }

    private Token eFinal() {
        updateLexeme();
        return new Token("EOF","", fileManipulator.getCurrentLine());
    }

    private Token eString1() throws LexicalException, IOException {
        if(fileManipulator.isEOF(currentChar)) {
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba \" pero se encontro EOF.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else if(currentChar == '\"'){
            updateLexeme();
            updateChar();
            return new Token("strLiteral",lexeme, fileManipulator.getCurrentLine());

        }else if(currentChar == '\\'){
            updateLexeme();
            updateChar();
            return eString2();

        }else if(currentChar == '\n' || currentChar == '\r'){
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(), "Se esperaba \" pero se encontro un salto de linea.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else {
            updateLexeme();
            updateChar();
            return eString1();
        }
    }

    private Token eString2() throws LexicalException, IOException {
        if(fileManipulator.isEOF(currentChar)) {
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba \" o un caracter pero se encontro EOF.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else if(currentChar == '\n' || currentChar == '\r'){
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(), "Se esperaba \" o un caracter pero se encontro un salto de linea.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else {
            updateLexeme();
            updateChar();
            return eString1();
        }
    }

    private Token eChar1() throws LexicalException, IOException {
        if(fileManipulator.isEOF(currentChar)) {
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba un caractér pero se encontro EOF.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else if(currentChar == '\n' || currentChar == '\r'){
            updateChar();
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba un caractér pero se encontro un salto de línea o un retorno de carro.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else if(currentChar == '\''){
            updateChar();
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"No se permiten caracteres vacíos.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else if(currentChar == '\\'){
            updateLexeme();
            updateChar();
            return eChar2();

        }else {
            updateLexeme();
            updateChar();
            return eChar3();
        }
    }

    private Token eChar2() throws LexicalException, IOException {
        if(fileManipulator.isEOF(currentChar)) {
            updateChar();
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba un caractér pero se encontro EOF.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else {
            updateLexeme();
            updateChar();
            return eChar3();
        }
    }

    private Token eChar3() throws LexicalException, IOException {
        if(fileManipulator.isEOF(currentChar)) {
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba \' pero se encontro EOF.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else if(currentChar == '\''){
            updateLexeme();
            updateChar();
            return new Token("charLiteral",lexeme, fileManipulator.getCurrentLine());

        }else {
            updateChar();
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba \'.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));
        }
    }

    private Token eOnlyEquals() {
        if(currentChar == '=') {
            updateLexeme();
            updateChar();
            return eOperators();

        }else {
            return new Token("assignment",lexeme, fileManipulator.getCurrentLine());

        }
    }

    //eLitInt literales enteros
    private Token eLitInt() throws LexicalException, IOException {
        if(Character.isDigit(currentChar)) {
            updateLexeme();
            updateChar();
            return eLitInt();

        }else if(currentChar == '.') {
            updateLexeme();
            updateChar();
            return eFloatNumber1();

        }else if(currentChar == 'e' || currentChar == 'E') {
            updateLexeme();
            updateChar();
            return eFloatNumber2();

        }else {
            return new Token("intLiteral",lexeme, fileManipulator.getCurrentLine());
        }
    }

    //punto del float
    private Token eFloatNumber1() throws LexicalException, IOException {
        if(Character.isDigit(currentChar)) {
            updateLexeme();
            updateChar();
            return eFloatNumber1();

        }else if(currentChar == 'e' || currentChar == 'E') {
            updateLexeme();
            updateChar();
            return eFloatNumber2();

        }else {
            return new Token("floatLiteral",lexeme, fileManipulator.getCurrentLine());
        }
    }

    private Token eFloatNumber2() throws IOException, LexicalException {
        if(currentChar == '+' || currentChar == '-') {
            updateLexeme();
            updateChar();
            return eFloatNumber3();

        }else if(Character.isDigit(currentChar)) {
            updateLexeme();
            updateChar();
            return eFloatNumber4();

        }else {
            throw new LexicalException(lexeme,fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba un digito o un signo.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));
        }
    }

    private Token eFloatNumber3() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)) {
            updateLexeme();
            updateChar();
            return eFloatNumber4();

        }else {
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba un dígito.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));
        }
    }

    private Token eFloatNumber4() {
        if(Character.isDigit(currentChar)) {
            updateLexeme();
            updateChar();
            return eFloatNumber4();

        }else {
            return new Token("floatLiteral",lexeme, fileManipulator.getCurrentLine());
        }
    }

    //eIDMetVar identificadores de metodos o variables
    private Token eIDMetVar() {
        if(Character.isDigit(currentChar) || Character.isLowerCase(currentChar) || Character.isUpperCase(currentChar) || currentChar == '_') {
            updateLexeme();
            updateChar();
            return eIDMetVar();

        }else {
            return new Token("idMetVar", lexeme, fileManipulator.getCurrentLine());

        }
    }

    //eIDClass identificadores de clases
    private Token eIDClass() {
        if(Character.isDigit(currentChar) || Character.isLowerCase(currentChar) || Character.isUpperCase(currentChar) || currentChar == '_') {
            updateLexeme();
            updateChar();
            return eIDClass();

        }else {
            return new Token("idClass", lexeme, fileManipulator.getCurrentLine());

        }
    }

    //ePunctuator puntuadores, (){};,.
    private Token ePunctuator(){
        return new Token("punctuator",lexeme, fileManipulator.getCurrentLine());
    }
    private Token eOperatorAmpersand() throws LexicalException, IOException {
        if(currentChar == '&') {
            updateLexeme();
            updateChar();
            return eOperators();

        }else {
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba un andpersand doble.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));
        }
    }

    private Token eOperatorVerticalBar() throws LexicalException, IOException {
        if(currentChar == '|') {
            updateLexeme();
            updateChar();
            return eOperators();

        }else {
            throw new LexicalException(lexeme, fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Se esperaba una linea vertical doble.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));
        }
    }

    private Token eOperators(){
        return new Token("operator",lexeme, fileManipulator.getCurrentLine());
    }

    private Token eOperatorsWithPossibleEquals(){
        if(currentChar == '=') {
            updateLexeme();
            updateChar();
            return eOperators();

        }else {
            return eOperators();

        }
    }

    private Token eOperatorsOrAssignment(){
        if(currentChar == '=') {
            updateLexeme();
            updateChar();
            return new Token("assignment",lexeme, fileManipulator.getCurrentLine());

        }else {
            return eOperators();

        }
    }

    //eComments inicio de comentarios y operadores unicamente /, /
    private Token eCommentsOrOperator() throws LexicalException, IOException {
        if(currentChar == '*') {
            lexeme = "";
            updateLexeme();
            updateChar();
            return eMultilnComments1();

        }else if(currentChar == '/') {
            updateLexeme();
            updateChar();
            return eUnilnComments1();

        }else {
            return eOperators();

        }
    }

    private Token eUnilnComments1() throws LexicalException, IOException {
        if(currentChar == '\n') {
            updateLexeme();
            updateChar();
            return e0();

        }else if(fileManipulator.isEOF(currentChar)) {
            updateLexeme();
            return eFinal();

        }else {
            updateLexeme();
            updateChar();
            return eUnilnComments1();

        }
    }

    private Token eMultilnComments1() throws LexicalException, IOException {
        if(currentChar == '*') {
            lexeme = "";
            updateLexeme();
            updateChar();
            return eMultilnComments2();

        }else if(fileManipulator.isEOF(currentChar)) {
            throw new LexicalException(lexeme,fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Comentario multilinea no cerrado.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else {
            lexeme = "";
            updateLexeme();
            updateChar();
            return eMultilnComments1();

        }
    }

    private Token eMultilnComments2() throws LexicalException, IOException {
        if(currentChar == '/') {
            updateChar();
            return e0();

        }else if(fileManipulator.isEOF(currentChar)) {
            throw new LexicalException(lexeme,fileManipulator.getCurrentLine(), fileManipulator.getCurrentColumn(),"Comentario multilinea no cerrado.", fileManipulator.getContentLine(fileManipulator.getCurrentLine()));

        }else if(currentChar == '*') {
            lexeme = "";
            updateLexeme();
            updateChar();
            return eMultilnComments2();

        }else {
            updateLexeme();
            updateChar();
            return eMultilnComments1();

        }
    }
}
