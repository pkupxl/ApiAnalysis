import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class CodeAnalyzer {
    public CodeAnalyzer(){ }

    public List<String>getApiList(String Content){
        ASTParser parser = ASTParser.newParser(AST.JLS10);
        parser.setSource(Content.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        MethodVisitor methodVisitor = new MethodVisitor(Content);
        CompilationUnit unit = (CompilationUnit)parser.createAST(null);
        unit.accept(methodVisitor);
        return methodVisitor.getApis();
    }
}

class MethodVisitor extends ASTVisitor {
    private String Content;
    private List<String>APIS = new ArrayList<String>();
    private List<String>APIContents = new ArrayList<String>();
    public MethodVisitor(String Content){
        this.Content=Content;
    }

    public List<String>getApis(){
        return this.APIS;
    }

    public boolean visit(MethodDeclaration node){
        this.APIS.add(node.getName().toString());
        this.APIContents.add(Content.substring(node.getStartPosition(),node.getStartPosition()+node.getLength()));
        return true;
    }
}
