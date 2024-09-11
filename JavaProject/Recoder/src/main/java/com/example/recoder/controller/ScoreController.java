package com.example.recoder.controller;

import com.example.recoder.constant.Score;
import com.example.recoder.service.ScoreService;
import com.example.recoder.starter.GetStartPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/10
 * Time: 15:31
 */
@RestController
@RequestMapping("/api")
public class ScoreController {
    @Autowired
    ScoreService scoreService;
    @Autowired
    GetStartPort getStartPort;

    @RequestMapping(value = "/addScore", method = RequestMethod.POST)
    Boolean addScore(Score score) {
        if (score != null) return scoreService.addScore(score);
        return false;
    }

    @RequestMapping(value = "/getAllScores", method = RequestMethod.GET)
    List<Score> getAllScores() {
        return scoreService.getAllScores();
    }

    @RequestMapping(value = "/deleteAllScores")
    String deleteAllScores(String token) {
        if (token.equals(getStartPort.getToken()) && scoreService.deleteAllScores()) {
            return "删除数据成功!";
        } else {
            return "token错误!删除数据失败!";
        }
    }
}
