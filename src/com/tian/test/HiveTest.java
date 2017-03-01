package com.tian.test;
 
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jruby.compiler.ir.operands.Array;
import org.junit.Before;
import org.junit.Test;

import com.tian.entity.Patent;
 
public class HiveTest {
    private Connection connection =null;
    private PreparedStatement ps =null;
    private ResultSet rs = null;
    
    public void getConnection() {
        try {
 
            Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
            connection = DriverManager.getConnection("jdbc:hive://192.168.33.132:10000/default", "hive", "hive");
            System.out.println(connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //关闭连接
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
   
    public void createTable() {
        String sql = "create table goods2(id int,name string) row format delimited fields terminated by '\t' ";
        try {
            ps = connection.prepareStatement(sql);
            ps.execute(sql);
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable() {
        String sql = "drop table goods";
        try {
            ps = connection.prepareStatement(sql);
            ps.execute();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert() throws SQLException{
        String sql = "load data inpath '/goods.txt' into table goods";
        //记得先在文件系统中上传goods.txt
        ps = connection.prepareStatement(sql);
        ps.execute();
        close();
    }
  
    public void find() throws SQLException {
        String sql = "SELECT * FROM hive_patent where pid = 803810";
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getObject(1) + "---" + rs.getObject(2));
        }
        close();
    }
    public Patent findByZlNumber(String zlNumber)throws SQLException, UnsupportedEncodingException{
    	Patent patent =null;
    	if(connection==null){
    		getConnection();
    	}
    	String sql = "SELECT * FROM hive_patent where zlNumber = ?";
        ps = connection.prepareStatement(sql);
        ps.setString(1, zlNumber);
	    ResultSet rs =ps.executeQuery();
        if(rs.next()){
        	patent = new Patent();
        	patent.setPid(rs.getLong(1));
        	patent.setZlName(new String(rs.getString(2).getBytes(),"UTF-8"));
			patent.setZlNumber(new String(rs.getString(3).getBytes(),"UTF-8"));
			patent.setZlDate(new String(rs.getString(4).getBytes(),"UTF-8"));
			patent.setZlFenlei(new String(rs.getString(5).getBytes(),"UTF-8"));
			
			patent.setZlAbstract(new String(rs.getString(6).getBytes(),"UTF-8"));
           
			patent.setZlUrl(new String(rs.getString(7).getBytes(),"UTF-8"));
			patent.setZlApplicant(new String(rs.getString(8).getBytes(),"UTF-8"));
        	
         }
        return patent;
    }
    
    public List<Patent> findByPids(List<Long> pids)throws SQLException, UnsupportedEncodingException{
    	List<Patent> patents = new ArrayList<Patent>();
    	if(connection==null){
    		getConnection();
    	}
    	Patent patent = null;
    	String sql = "select * from hive_patent where pid = ?";
		for(long pid:pids){
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1,pid);
            ResultSet rs =pstmt.executeQuery();
            if(rs.next()){
            	patent = new Patent();
            	patent.setPid(pid);
				patent.setZlName(new String(rs.getString(2).getBytes(),"UTF-8"));
				patent.setZlNumber(new String(rs.getString(3).getBytes(),"UTF-8"));
				patent.setZlDate(new String(rs.getString(4).getBytes(),"UTF-8"));
				patent.setZlFenlei(new String(rs.getString(5).getBytes(),"UTF-8"));
				
				patent.setZlAbstract(new String(rs.getString(6).getBytes(),"UTF-8"));
               
				patent.setZlUrl(new String(rs.getString(7).getBytes(),"UTF-8"));
				patent.setZlApplicant(new String(rs.getString(8).getBytes(),"UTF-8"));
            }
            patents.add(patent);
		}
		return patents;
    }
 
     
}