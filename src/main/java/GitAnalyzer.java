import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitAnalyzer {
    private Repository repository;

    public GitAnalyzer(String path){
        try{
            repository = new FileRepository(path);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getTags(){
        try{
            Git git = new Git(repository);
            List<Ref>tags = git.tagList().call();
            List<String>result = new ArrayList<String>();
            for(Ref tag:tags){
                result.add(tag.getName());
            }
            return result;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public RevCommit getCommitByTag(String tag){
        try{
            Git git = new Git(repository);
            List<Ref>tags = git.tagList().call();
            for(Ref s:tags){
                if(s.getName().equals(tag)){
                    RevWalk walk = new RevWalk(repository);
                    RevCommit commit = walk.parseCommit(s.getObjectId());
                    return commit;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RevCommit>getReleaseVersionCommit(){
        try{
            Git git = new Git(repository);
            List<Ref>refs = git.tagList().call();
            List<RevCommit>result = new ArrayList<RevCommit>();
            for(Ref ref:refs){
                String tag = ref.getName();
                if(tag.startsWith("refs/tags/REL") || tag.startsWith("refs/tags/releases")){
                    RevWalk walk = new RevWalk(repository);
                    RevCommit commit = walk.parseCommit(ref.getObjectId());
                    result.add(commit);
                }
            }
            return result;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Map<String,List<String>>getFileApiPair(RevCommit commit){
        try{
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(commit.getTree());
            treeWalk.setRecursive(true);
            Map<String,List<String>>FileApiPairs = new HashMap<String, List<String>>();
            while(treeWalk.next()){
                String filePath = treeWalk.getPathString();
                if(!filePath.endsWith(".java")) continue;
                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader loader = repository.open(objectId);
                String fileContent = new String(loader.getBytes());
                List<String>Apis =new CodeAnalyzer().getApiList(fileContent);
                FileApiPairs.put(filePath,Apis);
            }
            return FileApiPairs;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
