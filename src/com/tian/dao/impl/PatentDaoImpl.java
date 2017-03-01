package com.tian.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import com.tian.dao.PatentDao;
import com.tian.entity.Patent;
import com.tian.util.DBUtil;

public class PatentDaoImpl implements PatentDao{
	
    private DBUtil util = new DBUtil();
    private Connection conn =  null;
	@Override
	public List<Patent> getPatentsByPids(List<Long> pids) {
		// TODO Auto-generated method stub
		List<Patent> patents = new ArrayList<Patent>();
		String sql = "select * from patent_information where pid = ?";
		if(conn==null){
			conn =  util.openConnection();
		}
		try {
			Patent patent = null;
			
			for(long pid:pids){
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1,pid);
                ResultSet rs =pstmt.executeQuery();
                if(rs.next()){
                	patent = new Patent();
                	patent.setPid(pid);
    				patent.setZlName(rs.getString(2));
    				patent.setZlNumber(rs.getString(3));
    				patent.setZlDate(rs.getString(4));
    				patent.setZlFenlei(rs.getString(5));
    				
    				patent.setZlAbstract(rs.getString(6));
                   
    				patent.setZlUrl(rs.getString(7));
    				patent.setZlApplicant(rs.getString(8));
                }
                patents.add(patent);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			util.closeConn(conn);
		}
		return patents;
	}

	@Override
	public Patent getPatentByZlNumber(String zlNumber) {
		// TODO Auto-generated method stub
		Patent patent =null;
		if(conn==null){
			conn =  util.openConnection();
		}
		String sql = "select * from patent_information where zlNumber = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
		    pstmt.setString(1, zlNumber);
		    ResultSet rs =pstmt.executeQuery();
	        if(rs.next()){
	        	patent = new Patent();
            	patent.setPid(rs.getLong(1));
				patent.setZlName(rs.getString(2));
				patent.setZlNumber(rs.getString(3));
				patent.setZlDate(rs.getString(4));
				patent.setZlFenlei(rs.getString(5));
				
				patent.setZlAbstract(rs.getString(6));
               
				patent.setZlUrl(rs.getString(7));
				patent.setZlApplicant(rs.getString(8));
	        	
	         }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patent;
	}

}
