package com.yibuwulianwang.mysql.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alibaba.fastjson.JSONObject;
import com.yibuwulianwang.mysql.dao.TempHumDao;
import com.yibuwulianwang.mysql.entity.TempHum;

@Repository
public class TempHumDaoImpl implements TempHumDao{
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	//增
	final String sql_insert="insert into iot_temp_hum(date,temperature,humidity) values(?,?,?)";
	//删
	final String sql_delete= "delete from emp where id < ?";
	//改
	final String sql_update= "update emp set id = ? where temperature = ?";
	//查
	final String sql_select= "select * from iot_temp_hum order by id desc limit 1";
	
	public TempHum createTempHum(final TempHum tempHum) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql_insert, new String[]{"id"});
                int count = 1;
                psst.setString(count++, tempHum.getDate());
                psst.setString(count++, tempHum.getTemperature());
                psst.setString(count++, tempHum.getHumidity());
                return psst;
            }
        }, keyHolder);
        
        tempHum.setId(keyHolder.getKey().longValue());
        return tempHum;
	}

	public TempHum updateTempHum(TempHum tempHum) {
		return null;
	}

	public void deleteTempHum(Long id) {
		
	}

	public TempHum findOne(Long id) {
		return null;
	}

	public List<TempHum> findAll() {
		return null;
	}

	public TempHum findTemp(String id) {
		return null;
	}

	public TempHum findHum(String id) {
		return null;
	}

	public TempHum findLast() {
		List<TempHum> clientList = jdbcTemplate.query(sql_select, new BeanPropertyRowMapper(TempHum.class));
		if(clientList.size() == 0) {
            return null;
        }
        return clientList.get(0);
	}
	
}
