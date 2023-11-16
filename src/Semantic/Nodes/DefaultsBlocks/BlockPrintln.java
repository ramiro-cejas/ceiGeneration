package SecondSemantic.Semantic.Nodes.DefaultsBlocks;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.Nodes.NodeBlock;

public class BlockPrintln extends NodeBlock {

    public BlockPrintln(ConcreteClass currentClass, ConcreteMethod currentMethod) {
        super(new Token("punctuator_{","{",-1), currentClass, currentMethod, null);
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        String c = " # We print the carriage return";
        codeGenerator.gen("PRNLN" + c);
    }
}
