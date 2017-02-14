package comp.noname.samlibobserver;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Сканер авторов из статичного списка, авторов, нужен для выбора автора из списка.
 */
public class AutorsLoader {


    // Список авторов
    private ArrayList<Autor> Autors;
  // Менеджер ассетов для доступа к ресурсам
    private AssetManager Manager;

    public Boolean isLoaded;

    // Конструктор по умолчанию
    public AutorsLoader (AssetManager aManager)
    {
        Autors = new ArrayList<Autor>();
        Manager = aManager;
        isLoaded= false;

    }

   // Имя файла со статичным списоком
    public final String ResourceName = "StaticAutorList";

    // Загрузить авторов
    public ArrayList<Autor> LoadAutors()
    {
        // Создаем сканер файлов
        Scanner aFileScanner = null;
        // Создаем экземпляр сканера
        try {
            String[] Files = Manager.list("");

        // Получаем ресурсы
            InputStream inputStream = Manager.open(ResourceName);
            // Создаем сканер потока
            aFileScanner  = new Scanner(inputStream);
            while (aFileScanner.hasNext())
            {
                String AutorUrl = aFileScanner.next();
                if (aFileScanner.hasNext()) {
                    String AutorName = aFileScanner.nextLine();
                    Autors.add(new Autor(AutorName, AutorUrl));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Проверяем не достигли ли конца списка

        return Autors;
    }

// Поиск автора по имени перебериаем все элементы если находим с таким именем то переключаем
     public int findByName(String Name)
     {

        for (int i=0; i<Autors.size(); i++)
        {
            if (Autors.get(i).GetName().equals(Name))
            {
                return i;

            }

        }
         return -1;

     }
// Вернуть объект автор по имени
    public Autor findAutorByName(String Name)
    {

        for (int i=0; i<Autors.size(); i++)
        {
            String AutorName = Autors.get(i).GetName().toString();

            if (AutorName.equalsIgnoreCase(Name))
            {
                return Autors.get(i);
            }

        }
        return null;
    }


    // Получить спиоок имен авторов

    public ArrayList<String> LoadAutorNames(){

        Scanner aFileScanner = null;
        ArrayList<String> Names = new ArrayList<String>();

        //  Если длина списка больше нуля то стираем его, и строим заново, в цикле
        if (Autors.size()>0) {
            Autors.clear();
        }
        // Создаем экземпляр сканера
        try {
            String[] Files = Manager.list("");

            // Получаем ресурсы
            InputStream inputStream = Manager.open(ResourceName);
            // Создаем сканер потока
            aFileScanner  = new Scanner(inputStream);
            while (aFileScanner.hasNext())
            {

                String AutorUrl = aFileScanner.next();
                if (aFileScanner.hasNext()) {
                    String AutorName = aFileScanner.nextLine();
                    Names.add(AutorName);
                    Autors.add(new Autor(AutorName, AutorUrl));

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ставим флаг что данные загружены
        isLoaded = true;
        return Names;
    }




}
