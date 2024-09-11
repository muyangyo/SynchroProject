package com.example.recoder.mapper;

import com.example.recoder.constant.Score;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/10
 * Time: 15:22
 */
@Repository
@Mapper
public interface ScoresMapper {
    LinkedList<Score> selectAllScores();

    Integer insertScore(Score score);
}
