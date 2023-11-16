package SecondSemantic.Semantic.Nodes.DefaultsBlocks;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.Nodes.NodeBlock;

public class BlockRead extends NodeBlock {

    public BlockRead(ConcreteClass currentClass, ConcreteMethod currentMethod) {
        super(new Token("punctuator_{","{",-1), currentClass, currentMethod, null);
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        String c1 = " # We read the next integer value on the input stream.";
        codeGenerator.gen("READ" + c1);

        String c2 = " # We push 48 because the integer 0 is represented by the char number 48 in the ASCII table";
        codeGenerator.gen("PUSH 48" + c2);

        String c3 = " # We subtract that 'ASCII Offset' to get the real number";
        codeGenerator.gen("SUB" + c3);

        String c4 = " # We save the return value in the space reserved for it (offset is hard-coded)";
        codeGenerator.gen("STORE 3" + c4);
    }
}
