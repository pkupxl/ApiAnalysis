import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;
import java.util.List;
import java.util.Map;

public class GitAnalyzerTest {
    @Test
    public void TestGetTags(){
        String path="E:\\luceneGIT\\.git";
        GitAnalyzer gitAnalyzer = new GitAnalyzer(path);
        List<String> tags = gitAnalyzer.getTags();
        for(String tag:tags){
            System.out.println(tag);
        }
    }

    @Test
    public void TestGetCommitByTag(){
        String path="E:\\poi\\.git";
        GitAnalyzer gitAnalyzer = new GitAnalyzer(path);
        String tag1= "refs/tags/REL_3_7_BETA1";
        String tag2= "refs/tags/REL_3_7_BETA2";
        System.out.println(gitAnalyzer.getCommitByTag(tag1).getFullMessage());
        System.out.println(gitAnalyzer.getCommitByTag(tag2).getFullMessage());
    }

    @Test
    public void TestGetFileApiPair(){
        String path="E:\\poi\\.git";
        GitAnalyzer gitAnalyzer = new GitAnalyzer(path);
        String tag= "refs/tags/REL_3_7_BETA1";
        RevCommit commit = gitAnalyzer.getCommitByTag(tag);
        Map<String,List<String>>fileApiPairs = gitAnalyzer.getFileApiPair(commit);

        for(String s:fileApiPairs.keySet()){
            System.out.print(s+"  ");
            List<String>apis = fileApiPairs.get(s);
            System.out.println(apis.size());
            for(String api:apis){
                System.out.println(api);
            }
            System.out.println("-------------------------------");
        }
    }
}
