package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.*;

import java.util.ArrayList;

public class NodeBlock implements Node{

    public Token initialToken;
    private ArrayList<Node> sentences = new ArrayList<>();
    ArrayList<ConcreteAttribute> classAttributes = new ArrayList<>();
    ArrayList<ConcreteAttribute> methodParameters = new ArrayList<>();
    ArrayList<ConcreteAttribute> localVariables = new ArrayList<>();

    public NodeBlock parentBlock;
    public ConcreteClass currentClass;
    public ConcreteMethod currentMethod;

    private SymbolTable symbolTable;

    public NodeBlock(Token initialToken, ConcreteClass currentClass, ConcreteMethod currentMethod, NodeBlock parentBlock) {
        this.initialToken = initialToken;
        this.currentClass = currentClass;
        this.currentMethod = currentMethod;
        this.parentBlock = parentBlock;
        this.symbolTable = currentClass.symbolTable;
    }

    public void addSentence(Node sentence){
        sentence.setParentBlock(this);
        sentences.add(sentence);
    }

    public void check(SymbolTable symbolTable) throws SemanticException {
        if (parentBlock != null){
            classAttributes.addAll(parentBlock.classAttributes);
            methodParameters.addAll(parentBlock.methodParameters);
            localVariables.addAll(parentBlock.localVariables);
        }else{
            methodParameters.addAll(currentMethod.parameters.values());
            classAttributes.addAll(currentClass.attributes.values());
        }
        for (Node sentence : sentences){
            sentence.check(symbolTable);
            if (sentence instanceof NodeExpression || sentence instanceof NodeBinaryOp || sentence instanceof NodeUnaryOp || sentence instanceof NodeLiteral){
                symbolTable.semExceptionHandler.show(new SemanticException(sentence.getToken(),"Not a valid sentence"));
            } else if (sentence instanceof NodeAssignment){
                if (((NodeAssignment) sentence).leftExpression instanceof NodeVariable){
                    if (((NodeVariable)((NodeAssignment) sentence).leftExpression).inTheLastIsMethod()){
                        symbolTable.semExceptionHandler.show(new SemanticException(sentence.getToken(),"Not a valid sentence. Cannot assign to a method"));
                    }
                }
            } else if(sentence instanceof NodeVariableThis){
                NodeVariableThis senThis = (NodeVariableThis) sentence;
                if (senThis.childChain == null || !senThis.childChain.inTheLastIsMethod())
                    symbolTable.semExceptionHandler.show(new SemanticException(sentence.getToken(),"Not a valid sentence. Cannot call only this"));
            } else if(sentence instanceof NodeParenthesis){
                NodeParenthesis senParen = (NodeParenthesis) sentence;
                if (senParen.childChain == null || !senParen.childChain.inTheLastIsMethod()){
                    symbolTable.semExceptionHandler.show(new SemanticException(sentence.getToken(),"Not a valid sentence."));
                }
            }
        }
    }

    public ConcreteAttribute getVisible(String toSearch){
        for (int i = methodParameters.size() - 1; i >= 0; i--){
            if (methodParameters.get(i).getName().getLexeme().equals(toSearch)){
                return methodParameters.get(i);
            }
        }
        for (int i = localVariables.size() - 1; i >= 0; i--){
            if (localVariables.get(i).getName().getLexeme().equals(toSearch)) {
                return localVariables.get(i);
            }
        }
        for (int i = classAttributes.size() - 1; i >= 0; i--){
            if (classAttributes.get(i).getName().getLexeme().equals(toSearch)) {
                return classAttributes.get(i);
            }
        }
        return null;
    }

    @Override
    public Token getType() {
        return new Token("keyword_null", "null", -1);
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }

    @Override
    public Token getToken() {
        return null;
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        int localVarCount = localVariables.size();

        if(localVarCount > 0) {
            String cReserve = " # Reserving space for block local vars";
            codeGenerator.gen("RMEM " + localVarCount + cReserve);
        }

        for(Node s : sentences)
            s.generate(codeGenerator);

        if(localVarCount > 0) {
            String cFree = " # Freeing space reserved for block local vars";
            codeGenerator.gen("FMEM " + localVarCount + cFree);
        }
    }

}
