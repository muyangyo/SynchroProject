package com.muyang.booksystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/30
 * Time: 14:08
 */
@Data
public class Book {
    private Integer id;
    private String bookName;
    private String author;
    private Integer count;//数量

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Double price;


    private String publish;
    private Integer status;// 0-无效, 1-正常, 2-不允许借阅
    private String createTime;
    private String updateTime;
}
