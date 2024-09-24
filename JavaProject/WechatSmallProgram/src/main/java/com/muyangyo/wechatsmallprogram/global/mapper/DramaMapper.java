package com.muyangyo.wechatsmallprogram.global.mapper;

import com.muyangyo.wechatsmallprogram.global.model.Drama;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DramaMapper {
    void insertDrama(Drama drama);
    Drama findById(int id);
    List<Drama> findByUserId(int userId);
    void updateDrama(Drama drama);
    void deleteDrama(int id);
}
