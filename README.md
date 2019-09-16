# ApiAnalysis

### 使用方法
将项目clone到本地，使用maven package打包，或者从[此处](https://github.com/pkupxl/ApiAnalysis/releases)下载已经打包好的jar包，之后调用其中的API.

目前只实现了非常naive的功能：
    给定项目的git数据(.git文件夹路径)，以及想要查看的api的名称，列出各个release的版本中和该api同名的那些api(参数可以不同)，
包括这些api的注释以及api的实现。

简单的示例代码
```java
    public static void main(String args[]){
        GitAnalyzer analyzer = new GitAnalyzer("D:\\项目源代码\\poi\\.git");  //.git文件夹的路径
        Map<String, List<String>> pair =  analyzer.getApiHistory("createHyperlink"); //相要查看的api名

        for(String tag:pair.keySet()){
            System.out.println(tag);
            for(String s:pair.get(tag)){
                System.out.println(s);
            }
        }
    }
```

to be continue......
