package SecondSemantic.Semantic.Nodes.DefaultsBlocks;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.Nodes.NodeBlock;

public class BlockPrintSln extends NodeBlock {

    public BlockPrintSln(ConcreteClass currentClass, ConcreteMethod currentMethod) {
        super(new Token("punctuator_{","{",-1), currentClass, currentMethod, null);
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        String c1 = " # We put the parameter i at the top of the stack (offset is hard-coded)";
        codeGenerator.gen("LOAD 3" + c1);

        String c1dot5 = " # We load the 'fake attribute' of string that has the pointer to data";
        codeGenerator.gen("LOADREF 1"+c1dot5);

        String c2 = " # We print the String we just loaded";
        codeGenerator.gen("SPRINT" + c2);

        String c3 = " # We print the carriage return";
        codeGenerator.gen("PRNLN" + c3);
    }
}
