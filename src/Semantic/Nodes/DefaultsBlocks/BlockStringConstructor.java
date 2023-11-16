package SecondSemantic.Semantic.Nodes.DefaultsBlocks;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Generation.TagHandler;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.Nodes.NodeBlock;

public class BlockStringConstructor extends NodeBlock {

    public BlockStringConstructor(ConcreteClass currentClass, ConcreteMethod currentMethod) {
        super(new Token("punctuator_{","{",-1), currentClass, currentMethod, null);
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        codeGenerator.gen("LOAD 3");

        String c1 = " # We put the empty String tag at the top of the stack";
        codeGenerator.gen("PUSH " + "str_empty" + c1);

        String cStore = " # We link the 'fake attribute' to the empty string tag (attribute is hardcoded)";
        codeGenerator.gen("STOREREF 1" + cStore);
    }
}
