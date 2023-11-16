package SecondSemantic.Semantic.Nodes.DefaultsBlocks;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.Nodes.NodeBlock;

public class BlockPrintB extends NodeBlock {
    public BlockPrintB(ConcreteClass currentClass, ConcreteMethod currentMethod) {

        super(new Token("punctuator_{","{",-1), currentClass, currentMethod, null);
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        String c1 = " # We put the parameter i at the top of the stack (offset is hard-coded)";
        codeGenerator.gen("LOAD 3" + c1);

        String c2 = " # We print the boolean we just loaded";
        codeGenerator.gen("BPRINT" + c2);
    }
}
