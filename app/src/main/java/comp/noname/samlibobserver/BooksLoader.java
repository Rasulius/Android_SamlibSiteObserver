package comp.noname.samlibobserver;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Rasul on 09.05.2016.
 */
public class BooksLoader {

    private Autor aBookAutor; // Автор
    private ArrayList<Book> aBookList = new ArrayList<Book>();; // Коллекция книг автора
    protected final  String SamLibSite = "http://samlib.ru";

    public  BooksLoader(Autor anAutor)
    {
        aBookAutor = anAutor;// Копируем автора
    }

    public void LoadBooks()
    {
        //Ощищаем список
        if (aBookList.size() >0 ) {
            aBookList.clear();
        }
        // Теперь пойдут JSOUP для парсинга страницы
        try {
           // Ажрес странциы автора
            String aUrl = SamLibSite + aBookAutor.GetUrl();
            // Соедениямся с страницей автора на samlib
            Document doc = Jsoup.connect(aUrl).get();

            // Выделяем ноду <DL>
            Elements aElement = doc.select("DL");
            // Выделяем ноды <li>
            Elements anElement = aElement.select("li");
            // Создаем итератор ,или образна говоря переборщик коллекции
            Iterator<Element> iterator = anElement.iterator();
            int i = 0;

            while (iterator.hasNext())
            {
                // Выдернули аттрибует страницы
                Element aCurrentElement = iterator.next();

                // Вытаскиваем ссылку на страницу произведения
                String BookLinkReference = aCurrentElement.select("A").first().attr("HREF");
                // Вытаскиваем название книги
                String aBookName = aCurrentElement.select("b").text();
                // Вытаскиваем параметр новое или нет призведение, оно будет выделятся специальным цветом
                String aNew = aCurrentElement.select("font").attr("color");
                /*
                Здесь надо обработать новизну книги по умолчанию сделаем пока все книги старыми

                 */
                BookNew aNewBook = BookNew.bcOld;


                if (aNew.equalsIgnoreCase("brown") ||aNew.equalsIgnoreCase("gray") || aNew.equalsIgnoreCase("red") )
                {
                    aNewBook = BookNew.bcNew;

                }

                // Вытаскиваем короткое описание произведения
                String aText = aCurrentElement.select("DD").text();
                // Созадем экземпляр книги
                Book aBook = new Book(aBookName, aBookAutor, aNewBook, aText, BookLinkReference);

               // Добавляем в список
                aBookList.add(aBook);
            }


        } catch (IOException ex) {
            //Logger.getLogger(JavaApplication8.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    public Book FindByName(String aBookName)
    {
        for (int i=0; i<aBookList.size(); i++)
        {
            String aBook = aBookList.get(i).getBookName().toString();

            if (aBook.equalsIgnoreCase(aBookName))
            {
                return aBookList.get(i);
            }
        }
        return null;
    }

    // Возвращаем список книг автора
    public ArrayList<String> GetBooks()
    {

        ArrayList<String> stBooks = new ArrayList<String>();


        for(int i =0; i< aBookList.size(); i++)
        {

            Book aCurrentBook= aBookList.get(i);
            String aBookName = "";

            // Если книга новая добавляем впереди слово new

            if (aCurrentBook.isBookNew())
            {
                aBookName = "(Новое)";

            }
            //Складываем строки
            aBookName = aBookName + aCurrentBook.getBookName();

            stBooks.add(aBookName);
        }
        return  stBooks;
    }

    public Autor GetAutor()
    {

        return aBookAutor;
    }


}
