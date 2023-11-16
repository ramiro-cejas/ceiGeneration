package SecondSemantic.Semantic.Nodes.DefaultsBlocks;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.Nodes.NodeBlock;

public class BlockSystemConstructor extends NodeBlock {

    public BlockSystemConstructor(ConcreteClass currentClass, ConcreteMethod currentMethod) {
        super(new Token("punctuator_{","{",-1), currentClass, currentMethod, null);
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        //TODO see if this need to has something
    }
}
