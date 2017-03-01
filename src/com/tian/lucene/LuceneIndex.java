package com.tian.lucene;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;






/**
 *
 * Lucene与数据库结合使用
 *
 * @author 
 */
public class LuceneIndex {


    private static final String driverClassName="com.mysql.jdbc.Driver";
    private static final String url="jdbc:mysql://127.0.0.1:3306/patent?characterEncoding=utf-8";
    private static final String username="root";
    private static final String password="root";


    private static final Version version = Version.LUCENE_4_9;
    private Directory directory = new RAMDirectory();
    private DirectoryReader ireader = null;
    private IndexWriter iwriter = null;
    private IKAnalyzer analyzer;
    //存放索引文件的位置，即索引库
    private String searchDir = "d:\\luceneindex";
    private static File indexFile = null;   
    private Connection conn;
    /**
     * 读取索引文件
     * @return
     * @throws Exception
     */
    public IndexSearcher getSearcher() throws Exception{
        indexFile = new File(searchDir);
        ireader = DirectoryReader.open(FSDirectory.open(indexFile));
        return new IndexSearcher(ireader);
    }
    /**
     * 获取数据库连接
     * @return
     */
    public Connection getConnection(){
        if(this.conn == null){
            try {
                Class.forName(driverClassName);
                conn = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
        return conn;
    }
    /**
     * 中文分词工具
     * @return
     */
    private IKAnalyzer getAnalyzer(){
        if(analyzer == null){
            return new IKAnalyzer();
        }else{
            return analyzer;
        }
    }
    /**
     * 创建索引文件
     */
    public void createIndex(){
        Connection conn = getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        if(conn == null){
            System.out.println("get the connection error...");
            return ;
        }
        String sql = "select pid,zlName,zlAbstract from patent_information";
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            /**
             * 获取索引文件位置
             */
            indexFile = new File(searchDir);   
            if(!indexFile.exists()) {     
                indexFile.mkdirs();     
            }  
            /**
             * 设置索引参数
             */
            directory = FSDirectory.open(indexFile);  
            IndexWriterConfig iwConfig =  new IndexWriterConfig(version, getAnalyzer());
            iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory,iwConfig);
            /*lucene本身不支持更新 
             *  
             * 通过删除索引然后再建立索引来更新 
             */ 
            iwriter.deleteAll();//删除上次的索引文件，重新生成索引
            while(rs.next()){
            	long pid = rs.getLong(1);
                String zlName = rs.getString(2);
                String zlAbstract = rs.getString(3);
                Document doc = new Document();
                doc.add(new TextField("pid", pid+"",Field.Store.YES));
                doc.add(new TextField("nameAndabstract", zlName+""+zlAbstract,Field.Store.YES));
                iwriter.addDocument(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(iwriter != null)
                iwriter.close();
                rs.close();
                pstmt.close();
                if(!conn.isClosed()){
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    }
    /**
     * 查询方法
     * @param field 字段名称
     * @param keyword 字段值
     * @param num 
     * @throws Exception
     */
    public void searchByTerm(String field,String keyword,int num) throws Exception{
         IndexSearcher isearcher = getSearcher();
         Analyzer analyzer =  getAnalyzer();
        //使用QueryParser查询分析器构造Query对象
        QueryParser qp = new QueryParser(version,field,analyzer);
      
        qp.setDefaultOperator(QueryParser.OR_OPERATOR);
        try {
            Query query = qp.parse(keyword);
            ScoreDoc[] hits;


            //注意searcher的几个方法
            hits = isearcher.search(query,null,num).scoreDocs;

            System.out.println(hits.length);
            System.out.println("the ids is =");
            for (int i = 0; i < hits.length; i++) {
                Document doc = isearcher.doc(hits[i].doc);
                System.out.print(doc.get("pid")+" ");
                System.out.println(doc.get("nameAndabstract")+" ");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    /***如果想要获取为存储到索引中得值，可以根据ID去源文件中进行查找并返回**/
    public List<Long> searchPage(String field,String keyword,int pageIndex,int pageSize) {
    	List<Long> pids =null;
        try {
            IndexSearcher searcher = getSearcher();
            Analyzer analyzer =  getAnalyzer();
            QueryParser parser = new QueryParser(version,field,analyzer);
            Query q =null;
            try {
                q = parser.parse(keyword);
            } catch (org.apache.lucene.queryparser.classic.ParseException e) {
                e.printStackTrace();
            }
            TopDocs tds = searcher.search(q, 500);
            ScoreDoc[] sds = tds.scoreDocs;
            int start = (pageIndex-1)*pageSize;
            int end = pageIndex*pageSize;
            if(end>=sds.length) end = sds.length;
            pids = new ArrayList<Long>();
            for(int i=start;i<end;i++) {
                Document doc = searcher.doc(sds[i].doc);
                String pid = doc.get("pid");
                pids.add(Long.valueOf(pid));
               // System.out.println(pid);
            }
            
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return pids;
    }
    
    /**
     * 根据页码和分页大小获取上一次的最后一个ScoreDoc
     */
    private ScoreDoc getLastScoreDoc(int pageIndex,int pageSize,Query query,IndexSearcher searcher) throws IOException {
        if(pageIndex==1)return null;//如果是第一页就返回空
        int num = pageSize*(pageIndex-1);//获取上一页的数量
        TopDocs tds = searcher.search(query, num);
        return tds.scoreDocs[num-1];
    }
    /***
     * 在使用时，searchAfter查询的是指定页数后面的数据，效率更高，推荐使用
     * @param query
     * @param pageIndex
     * @param pageSize
     */
    public List<Long> searchPageByAfter(String field,String keyword,int pageIndex,int pageSize) {
    	List<Long> pids =null;
        try {
            IndexSearcher searcher = getSearcher();
            Analyzer analyzer =  getAnalyzer();
            QueryParser qp = new QueryParser(version,field,analyzer);
            Query q = null;
            try {
                q = qp.parse(keyword);
            } catch (org.apache.lucene.queryparser.classic.ParseException e) {
                e.printStackTrace();
            }
            //先获取上一页的最后一个元素
            ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, q, searcher);
            //通过最后一个元素搜索下页的pageSize个元素
            TopDocs tds = searcher.searchAfter(lastSd,q, pageSize);
            pids = new ArrayList<Long>();
            for(ScoreDoc sd:tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                String pid = doc.get("pid");
                pids.add(Long.valueOf(pid));
                System.out.println(pid);
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return pids;
		
    }
    /**   
     * 搜索索引   
     */    
    public static void main(String[] args) throws Exception {
        LuceneIndex ld = new LuceneIndex();
        long current = System.currentTimeMillis();
        //ld.createIndex();
        // ld.searchByTerm("nameAndabstract", "一种美容养颜的方法",100);
        //ld.searchPage("nameAndabstract", "一种美容养颜的方法",2,10);
        ld.searchPageByAfter("nameAndabstract", "一种美容养颜的方法",1,10);
        System.out.println("用时"+(System.currentTimeMillis()-current)+"ms");
        
    }
}
