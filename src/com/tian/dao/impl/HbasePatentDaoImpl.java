package com.tian.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.tian.dao.PatentDao;
import com.tian.entity.Patent;
import com.tian.test.HbaseTest;

public class HbasePatentDaoImpl implements PatentDao {

	@Override
	public Patent getPatentByZlNumber(String zlNumber) {
		// TODO Auto-generated method stub
		System.out.println("实现不了");
		return null;
	}

	@Override
	public List<Patent> getPatentsByPids(List<Long> pids){
		// TODO Auto-generated method stub
		List<Patent> patents = null;
		String tableName ="hpatents";
		
		try {
			  patents = HbaseTest.getByRows(tableName, pids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return patents;
	}

}
