package com.tian.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.tian.dao.PatentDao;
import com.tian.entity.Patent;
import com.tian.test.HiveTest;

public class HivePatentDaoImpl implements PatentDao {
    private HiveTest hiveTest = null;
	@Override
	public Patent getPatentByZlNumber(String zlNumber){
		// TODO Auto-generated method stub
		if(hiveTest==null){
			hiveTest = new HiveTest();
		}
		Patent patent = null;
		try {
			patent = hiveTest.findByZlNumber(zlNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return patent;
	}

	@Override
	public List<Patent> getPatentsByPids(List<Long> pids) {
		// TODO Auto-generated method stub
		if(hiveTest==null){
			hiveTest = new HiveTest();
		}
		List<Patent> patents = null;
		try {
			patents = hiveTest.findByPids(pids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return patents;
	}

}
