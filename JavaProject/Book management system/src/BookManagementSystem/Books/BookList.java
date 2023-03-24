package BookManagementSystem.Books;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/23
 * Time: 23:30
 */
public class BookList {
    public Book[] Books = new Book[10];//初始化为10
    public int UsedSize = 0;//数组中实际存储的元素个数

    public BookList() {
        Books[0] = new Book("01", "西游记", "xx", 20);
        Books[1] = new Book("02", "大话西游", "xx", 20);
        Books[2] = new Book("03", "红楼梦", "xx", 20);
        UsedSize = 3;
    }
}
