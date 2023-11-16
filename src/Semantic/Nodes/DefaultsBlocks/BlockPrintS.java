package SecondSemantic.Semantic.Nodes.DefaultsBlocks;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.Nodes.NodeBlock;

public class BlockPrintS extends NodeBlock {

    public BlockPrintS(ConcreteClass currentClass, ConcreteMethod currentMethod) {
        super(new Token("punctuator_{","{",-1), currentClass, currentMethod, null);
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        String c1 = " # We put the parameter i at the top of the stack (offset is hard-coded)";
        codeGenerator.gen("LOAD 3" + c1);

        String c2 = " # We load the 'fake attribute' through the this reference and the offset (hardcoded)";
        codeGenerator.gen("LOADREF 1"+c2);

        String c3 = " # We print the string we just loaded";
        codeGenerator.gen("SPRINT" + c3);
    }
}
