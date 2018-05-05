package com.yibuwulianwang.mysql.dao;

import java.util.List;

import com.yibuwulianwang.mysql.entity.TempHum;

public interface TempHumDao {
	public TempHum createTempHum(TempHum tempHum);
    public TempHum updateTempHum(TempHum tempHum);
    public void deleteTempHum(Long id);
    TempHum findOne(Long id);
    List<TempHum> findAll();
    TempHum findTemp(String id);
    TempHum findHum(String id);
    TempHum findLast();
}
