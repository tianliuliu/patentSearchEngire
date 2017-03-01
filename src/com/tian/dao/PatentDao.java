package com.tian.dao;

import java.util.List;

import com.tian.entity.Patent;

public interface PatentDao {
	
	public List<Patent> getPatentsByPids(List<Long> pids);
	public Patent getPatentByZlNumber(String zlNumber);

}
