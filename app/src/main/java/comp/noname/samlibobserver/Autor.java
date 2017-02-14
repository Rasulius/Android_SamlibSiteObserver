package comp.noname.samlibobserver;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/*
* Класс Автор, у него есть имя, уникальный номер и коллекция книг
*
* */

public class Autor {
    private String AutorName;
    private String AutorUrl;

    private List<Book> Books;

    public Autor(String anAutorName, String anAutorURL)
    {
        AutorName = anAutorName;
        AutorUrl = anAutorURL;
    }

    // Полчаем имя автора
    public String GetName()    {

        return  AutorName;
    }
//Возвращаем адрес страницы автора
    public String GetUrl(){


        return AutorUrl;
    }



}
