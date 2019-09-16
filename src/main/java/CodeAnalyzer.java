import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class CodeAnalyzer {
    private ASTParser parser;
    public CodeAnalyzer(){
        parser = ASTParser.newParser(AST.JLS10);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
    }

    public List<String>getApiList(String Content){
        parser.setSource(Content.toCharArray());
        MethodVisitor methodVisitor = new MethodVisitor(Content);
        CompilationUnit unit = (CompilationUnit)parser.createAST(null);
        unit.accept(methodVisitor);
        return methodVisitor.getApis();
    }

    public List<String>getApiContents(String Content,String apiName){
        parser.setSource(Content.toCharArray());
        SimilarMethodVisitor visitor = new SimilarMethodVisitor(Content,apiName);
        CompilationUnit unit = (CompilationUnit)parser.createAST(null);
        unit.accept(visitor);
        return visitor.getApiContents();
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

class SimilarMethodVisitor extends ASTVisitor{
    private String Content;
    private String apiName;
    private List<String>APIS = new ArrayList<String>();
    private List<String>APIContents = new ArrayList<String>();
    public SimilarMethodVisitor(String Content,String apiName){
        this.Content = Content;
        this.apiName = apiName;
    }

    public List<String> getApiContents(){
        return APIContents;
    }

    public boolean visit(MethodDeclaration node){
        if(node.getName().toString().equals(apiName)){
            APIS.add(node.getName().toString());
            APIContents.add(Content.substring(node.getStartPosition(),node.getStartPosition()+node.getLength()));
        }
        return true;
    }
}
