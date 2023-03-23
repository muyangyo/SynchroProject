package BookManagementSystem;

import BookManagementSystem.Books.BookList;
import BookManagementSystem.Users.Admin;
import BookManagementSystem.Users.NormalUser;
import BookManagementSystem.Users.User;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BookList bookList = new BookList();
        Login(bookList);
    }

    public static void Login(BookList bookList) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入你的名字:");
        String string = scanner.nextLine();
        System.out.print("请输入身份：1.管理员 2.普通用户 :>");
        int choice = scanner.nextInt();
        User user = null;
        switch (choice) {
            case 1:
                user = new Admin(string);
                user.showRole();
                user.showMenu(bookList);
                break;
            case 2:
                user = new NormalUser(string);
                user.showRole();
                user.showMenu(bookList);
                break;
            default:
                System.out.println("输入有误");
                break;
        }
    }
}

