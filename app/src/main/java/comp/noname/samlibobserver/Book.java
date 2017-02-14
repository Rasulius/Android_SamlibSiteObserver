package comp.noname.samlibobserver;

/**
 * Класс книга, у книги есть Имя, Автор и размер
 */
enum BookNew {bcOld, bcNew, bcRed, bcBlue, bcDark }; //Книга по новизне будет отличаться цветом. bcOld -сьтарое

public class Book {
    private String bookName; // Имя книги
    private Autor autor;    // Автор
    private BookNew newBook; // Новая или нет книга
    private String bookDescription; // Описание книги
    private String bookUrl;

    public Book(String aBookName, Autor anAutor, BookNew aNewBook, String Description, String aBookUrl)
    {
        bookName = aBookName;
        autor = anAutor;
        newBook = aNewBook;
        bookDescription = Description;
        bookUrl = aBookUrl;
    }
    // Возвращеаем имя книги
    public String getBookName()
    {
        return  bookName;
    }
    public String getBookURL() {return bookUrl;}
    public  boolean isBookNew() {
        if (newBook== BookNew.bcOld) {
        return false;
        }
        else {
            return true;
        }


    }


}
