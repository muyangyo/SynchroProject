package BookManagementSystem.operations;

import BookManagementSystem.Books.BookList;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/23
 * Time: 23:47
 */
public class ExitOp implements OPeration {

    @Override
    public void work(BookList bookList) {
        System.out.println("退出系统!");
        System.exit(0);
    }
}
