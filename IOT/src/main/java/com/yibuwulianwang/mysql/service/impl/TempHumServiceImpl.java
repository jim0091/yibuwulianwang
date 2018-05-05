package com.yibuwulianwang.mysql.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.yibuwulianwang.handle.Handle;
import com.yibuwulianwang.mysql.dao.TempHumDao;
import com.yibuwulianwang.mysql.entity.TempHum;
import com.yibuwulianwang.mysql.service.TempHumService;

@Transactional
@Service
public class TempHumServiceImpl implements TempHumService{
	
	@Autowired
	private TempHumDao tempHumDao;//导入dao层
	
	public TempHum createTempHum(TempHum tempHum) {
		return tempHumDao.createTempHum(tempHum);
	}

	public TempHum updateTempHum(TempHum tempHum) {
		return tempHumDao.updateTempHum(tempHum);
	}

	public void deleteTempHum(Long id) {
		tempHumDao.deleteTempHum(id);
	}

	public TempHum findOne(Long id) {
		return tempHumDao.findOne(id);
	}

	public List<TempHum> findAll() {
		return tempHumDao.findAll();
	}

	public TempHum findTemp(String id) {
		return tempHumDao.findTemp(id);
	}

	public TempHum findHum(String id) {
		return tempHumDao.findHum(id);
	}

	public TempHum findLast() {
		return tempHumDao.findLast();
	}
	
}
