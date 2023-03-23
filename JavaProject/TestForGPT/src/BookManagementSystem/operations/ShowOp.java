package BookManagementSystem.operations;

import BookManagementSystem.Books.BookList;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/23
 * Time: 23:47
 */
public class ShowOp implements OPeration {

    @Override
    public void work(BookList bookList) {
        for (int i = 0; i < bookList.UsedSize; i++) {
            System.out.println(bookList.Books[i].toString());
        }
    }
}
