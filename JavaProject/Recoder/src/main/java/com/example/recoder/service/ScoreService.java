package com.example.recoder.service;

import com.example.recoder.constant.Score;
import com.example.recoder.mapper.ScoresMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/10
 * Time: 15:27
 */
@Service
public class ScoreService {
    @Autowired
    ScoresMapper scoresMapper;

    public Boolean addScore(Score score) {
        return scoresMapper.insertScore(score) > 0;
    }

    public List<Score> getAllScores() {
        return scoresMapper.selectAllScores();
    }
}
