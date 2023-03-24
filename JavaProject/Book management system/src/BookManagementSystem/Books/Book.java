package BookManagementSystem.Books;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/23
 * Time: 23:30
 */
public class Book {
    private String SN;
    private String BookName;
    private String author;
    private double price;
    private boolean isBorrowed; //true为借出,false为未借出

    public Book(String SN, String BookName, String author, double price) {
        this.SN = SN;
        this.BookName = BookName;
        this.author = author;
        this.price = price;
    }


    public void setSN(String SN) {
        this.SN = SN;
    }

    public void setBookName(String BookName) {
        this.BookName = BookName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public boolean getBorrowed() {
        return isBorrowed;
    }

    public String getBookName() {
        return BookName;
    }

    @Override
    public String toString() {
        return "Book: " + "SN: " + SN + ", BookName: " + BookName + ", author: " + author + ", price: " + price + ", isBorrowed: " + isBorrowed;

    }
}
