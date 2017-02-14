package comp.noname.samlibobserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import  android.content.*;


/**
 * Created by Rasul on 08.05.2016.
 */
public class LikedAutors {

    private static final Object MODE_PRIVATE = "";
    private ArrayList<Autor> Autors;
    public final String LikedAutorsFile = "LikedAutors";

    //Конструктор по умолчанию.
    public LikedAutors ()
    {
        Autors = new ArrayList<Autor>();
    }
    // Добавление нового автора в список избранных авторов
    public void AddAutor(Autor anAutor)
    {
        Autors.add(anAutor);
    }
// Удаление автора из списка избранное
    public void DeleteAutorByName(String Autor)
    {
      for(int i =0; i< Autors.size(); i++)
      {
          String AutorName = Autors.get(i).GetName().toString();
          if (AutorName.equalsIgnoreCase(Autor))
          {
              Autors.remove(i);
              break;
          }

      }
    }
// Полчить список Имен избранных авторов
    public ArrayList<String> GetLikedAutors()
    {
        ArrayList<String> Names = new ArrayList<String>();
        for(int i =0; i< Autors.size(); i++)
        {
            Names.add(Autors.get(i).GetName());
        }
        return  Names;
    }
// Поиск по имени
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

    public void DeleteByName(String Name)
    {

        for (int i=0; i<Autors.size(); i++)
        {
            String AutorName = Autors.get(i).GetName().toString();

            if (AutorName.equalsIgnoreCase(Name))
            {
                Autors.remove(i);
                return;
            }
        }
    }


// Сохранение списка избранных авторов
    public void SaveLikedAutors(Context aContext) {

        File file = new File(aContext.getFilesDir(), LikedAutorsFile);
        PrintWriter writer = null;

        try
        {
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));

            for (int i = 0; i < Autors.size(); i++) {
                writer.println(Autors.get(i).GetName());
                writer.println(Autors.get(i).GetUrl());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// Загрузка избранных авторов

    public void LoadLikedAutors(Context aContext) {
        File file = new File(aContext.getFilesDir(), LikedAutorsFile);
        Scanner aFileScanner = null;
        try {
            aFileScanner = new Scanner(file);
            while (aFileScanner.hasNext())
            {

                String AutorName = aFileScanner.nextLine();
                if (aFileScanner.hasNext()) {
                    String AutorUrl = aFileScanner.nextLine();
                    Autors.add(new Autor(AutorName, AutorUrl));

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
