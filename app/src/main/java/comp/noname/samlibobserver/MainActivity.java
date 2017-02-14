package comp.noname.samlibobserver;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;



enum OperationState {osSelectAutor, osSelectLikedAutor, osViewBooks, osOpenBrouwser};

public class MainActivity extends AppCompatActivity {
    // Загрузчик авторов
    private AutorsLoader Loader;
    // Компонент вывода поиска текста
    private EditText anTextSearcher;
    // Компонент вывода списка
    private ListView aListView;
    // адптер нужегн для смены вида списка
    private ArrayAdapter adapter;
    // Список избранных авторов
    private LikedAutors aLikedAutors;
    // Состояние операции
    private OperationState aState;
// Загрузичик книг
    private BooksLoader BookLoader;



    final String LOG_TAG = "myLogs";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Полный список авторов
        Loader = new AutorsLoader(getAssets());
        // Список избранных авторов
        aLikedAutors = new LikedAutors();
        aLikedAutors.LoadLikedAutors(getBaseContext());
        //Кнопка нажатия авторы
        Button AutorButton = (Button) findViewById(R.id.button);
        // Подписываемся на событие нажатие кнопки, создаем свой обработчик события нажатия клавиши
        AutorButton.setOnClickListener(FindAutorListener);
        // Создаем обработчик нажатия "списка избранных авторов"
        Button LikedButton = (Button) findViewById(R.id.button2);
        // Добавляем обработчик списка изюранных авторов
        LikedButton.setOnClickListener(LikedAutorListener);
        // Состояние выбор автора из списка
        aState = OperationState.osSelectAutor;

        // Получаем поле ввода для "живого поиска", когда при наборе изменяется набор данных
        anTextSearcher = (EditText) findViewById(R.id.editText);
        anTextSearcher.setClickable(false);
        // Делаем свой адаптер поиска по авторам
        anTextSearcher.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged (CharSequence s,int start, int before, int count){
              // Во время изменения такста
            }

            @Override
            public void beforeTextChanged (CharSequence s,int start, int count,
            int after){
                // До изменеия тектса
            }

            @Override
            public void afterTextChanged (Editable s){
                // После изменения текста
                adapter.getFilter().filter(s.toString());
            }

        });

        // Обработчик нажатия на список
        aListView = (ListView) findViewById(R.id.listView);


        aListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Обработчик нажатия на списке ListView
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                if (aState == OperationState.osSelectAutor) {
                    String title = "Выберите действие";
                    String message = "Добавить в избранное";

                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this);
                    dlgAlert.setTitle(title);  // заголовок
                    dlgAlert.setMessage(message);

                    // Кнопка добавить
                    dlgAlert.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            // *    Добавляем автора в список избранных         *//

                            // Находим выбранного автора
                            String aSelectedAutor = adapter.getItem(position).toString();
                            // производим поиск по имени
                            Autor aFindAutor = Loader.findAutorByName(aSelectedAutor);
                            // Добавляем в список избранных
                            aLikedAutors.AddAutor(aFindAutor);

                            aLikedAutors.SaveLikedAutors(getBaseContext());
                            // Сообщеам адаптеру, что список авторов обновился
                            adapter.notifyDataSetInvalidated();

                            // Выводим сообщенеи что добавился новый автор
                            Toast.makeText(MainActivity.this, "в избранное добавлен новый автор" + adapter.getItem(position).toString(), Toast.LENGTH_LONG).show();

                        }
                    });
                    // Кнопка отмены
                    dlgAlert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Toast.makeText(MainActivity.this, "автор не добавлен", Toast.LENGTH_LONG).show();
                        }
                    });


                    dlgAlert.setCancelable(true);

                    dlgAlert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(MainActivity.this, "Вы ничего не выбрали",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                     // Состояние переходит в состояние выбора избранных
                    dlgAlert.create().show();


                } //Конец Условия if (aState == OperationState.osSelectAutor)

                if (aState == OperationState.osSelectLikedAutor) {
                    String title = "Выберите действие";
                    String message = "Действия с текущим автором";
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this);

                    // Запрашиваем диалог что мы хотим выполнить (просмотреть книги автора, открыть в браузере страницу, или удалить из списка )
                    dlgAlert.setTitle(title);  // заголовок
                    dlgAlert.setMessage(message);


                    dlgAlert.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            // *    Добавляем автора в список избранных         *//

                            // Находим выбранного автора
                            String aSelectedAutor = adapter.getItem(position).toString();
                            // Удаляемм из списка автора
                            aLikedAutors.DeleteAutorByName(aSelectedAutor);
                            // Сообщеам адаптеру, что список авторов обновился
                            adapter.notifyDataSetChanged();
                            // Выводим сообщенеи что добавился новый автор
                            Toast.makeText(MainActivity.this, "Удаляется из списка избранных" + adapter.getItem(position).toString(), Toast.LENGTH_LONG).show();

                        }
                    });

                    dlgAlert.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Toast.makeText(MainActivity.this, "ничего не выбрано", Toast.LENGTH_LONG).show();
                        }
                    });

                    dlgAlert.setPositiveButton("Книги", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            // *    Просмотреть книгу       *//
                            // Если нажали на книжки то переводим состояние на просмотр книг автора
                            aState = OperationState.osViewBooks;
                            //Показываем диалог ожидания
                            pd = ProgressDialog.show(MainActivity.this, "Идет загрузка книг", "ожидаем запрос от сервера", true, false);

                            // Находим выбранного автора
                            String aSelectedAutor = adapter.getItem(position).toString();
                            // подгружаем авторов если не загружены
                            if (!Loader.isLoaded) {
                                Loader.LoadAutors();
                            }


                            // производим поиск по имени
                            Autor aFindAutor = Loader.findAutorByName(aSelectedAutor);

                            LoadBooks loadBooks = new LoadBooks(aFindAutor);
                            //Запускаем загрузчик
                            loadBooks.execute();

                            Toast.makeText(MainActivity.this, "Открытие книг автора" + adapter.getItem(position).toString(), Toast.LENGTH_LONG).show();

                        }
                    });



                    dlgAlert.setCancelable(true);

                    dlgAlert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(MainActivity.this, "Вы ничего не выбрали",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    dlgAlert.create().show();

                }//Конец Условия if (aState == OperationState.osSelectLikedAutor)


                if (aState == OperationState.osViewBooks) {

                    //Создаем диалог
                    String title = "Выберите действие";
                    String message = "Открыть в браузере";

                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this);
                    dlgAlert.setTitle(title);  // заголовок
                    dlgAlert.setMessage(message);

                    // Кнопка добавить
                    dlgAlert.setPositiveButton("Открыть", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            // Открываем в браузере*//

                         String SelectedBookname = adapter.getItem(position).toString();
                            // Получить книгу по спику загруженных книг
                         Book aBook = BookLoader.FindByName(SelectedBookname);
                            //Полчить автора
                         Autor anCurrentAutor = BookLoader.GetAutor();
                            // Получить отнасительный URL книги.
                         String aBookURL = aBook.getBookURL();
                         String SamLibSite = "http://samlib.ru";
                         String AutorURL = anCurrentAutor.GetUrl();

                            if (AutorURL.charAt(AutorURL.length()-1)!='\\')
                            {
                                AutorURL = AutorURL + "\\";

                            }

                         String aBookFullURL = SamLibSite + AutorURL + aBookURL;
                         Uri address = Uri.parse(aBookFullURL);
                         Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
                         startActivity(openlinkIntent);

                        }
                    });
                    // Кнопка отмены
                    dlgAlert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Toast.makeText(MainActivity.this, "отмена действия", Toast.LENGTH_LONG).show();
                        }
                    });
                    dlgAlert.setCancelable(true);

                    dlgAlert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(MainActivity.this, "Вы ничего не выбрали",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    // Состояние переходит в состояние выбора избранных
                    dlgAlert.create().show();


                }// Конец условия просотра Книг

            } // Конец процедуры
        });

        aListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {


            }

            public void onNothingSelected(AdapterView<?> parent) {
                //Log.d(LOG_TAG, "itemSelect: nothing");
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public String insert_slesh_in_lastsymbol(String aURLString){
        String new_path = aURLString;

            if (aURLString.charAt(aURLString.length())!='/') {
                new_path=new StringBuffer(aURLString).insert(aURLString.length()+1, '/').toString();

            }
        return new_path;
    }





    // Диалог ожидания прогресса
    private ProgressDialog pd;
    // Загрузик авторов

    // При нажатии списка авторов
    private View.OnClickListener FindAutorListener = new View.OnClickListener() {
        public void onClick(View v) {
           // Состояние выбора списка
            aState = OperationState.osSelectAutor;
            //Показываем диалог ожидания
            pd = ProgressDialog.show(MainActivity.this, "Идет загрузка авторов", "ожидаем запрос от сервера", true, false);
            //Запускаем загрузчик
            new LoadAutors().execute();
        }
    };

    // При нажатии на список избранных авторов
    private View.OnClickListener LikedAutorListener = new View.OnClickListener() {
        public void onClick(View v) {
           // Состояние переходит в состояние выбора Избранных авторов
            aState = OperationState.osSelectLikedAutor;
            //Показываем диалог ожидания
            pd = ProgressDialog.show(MainActivity.this, "Идет загрузка избранных авторов", "ожидаем запрос от сервера", true, false);
            //Запускаем загрузчик
            new LoadLikedAutors().execute();
        }
    };

    // При выборе книг автора
    private View.OnClickListener  LoadBooksListener = new View.OnClickListener() {
        public void onClick(View v) {
            // Состояние переходит в состояние выбора книг
            aState = OperationState.osViewBooks;
            //Показываем диалог ожидания
            pd = ProgressDialog.show(MainActivity.this, "Идет загрузка книг автора", "ожидаем запрос от сервера", true, false);
            //Запускаем загрузчик
           // new LoadLikedAutors().execute();

        }
    };

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://comp.noname.samlibobserver/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://comp.noname.samlibobserver/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    // Ассинхронная загрузка элементов списка

    private class LoadAutors extends AsyncTask<String, Void, ArrayList<String>> {

        //Фоновая операция
        protected ArrayList<String> doInBackground(String... arg) {
            ArrayList<String> output = new ArrayList<String>();
            output = Loader.LoadAutorNames();
            return output;
        }

        //Событие по окончанию парсинга
        protected void onPostExecute(ArrayList<String> output) {
            //Убираем диалог загрузки
            pd.dismiss();
            //Находим ListView
            ListView listview = (ListView) findViewById(R.id.listView);
            //Загружаем в него результат работы doInBackground
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, output);

            listview.setAdapter(adapter);
            // Делаем поиск активным
            anTextSearcher.setClickable(true);
        }

    }
// Асинхронная загрузка избранных авторов, которых добавляем в список

    private class LoadLikedAutors extends AsyncTask<String, Void, ArrayList<String>> {



        //Фоновая операция
        protected ArrayList<String> doInBackground(String... arg) {
            ArrayList<String> output = new ArrayList<String>();
            output = aLikedAutors.GetLikedAutors();
            return output;
        }

        //Событие по окончанию парсинга
        protected void onPostExecute(ArrayList<String> output) {
            //Убираем диалог загрузки
            pd.dismiss();
            //Находим ListView
            ListView listview = (ListView) findViewById(R.id.listView);
            //Загружаем в него результат работы doInBackground
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, output);

            listview.setAdapter(adapter);
            // Делаем поиск активным
            anTextSearcher.setClickable(true);
        }

    }

    private class LoadBooks extends AsyncTask<String, Void, ArrayList<String>> {

        private Autor autor;

        //Фоновая операция
        public LoadBooks(Autor anAutor){
            autor = anAutor;
        }


        protected ArrayList<String> doInBackground(String... arg) {

            ArrayList<String> output = new ArrayList<String>();
            BookLoader = new BooksLoader(autor);
            BookLoader.LoadBooks();
            output = BookLoader.GetBooks();
            return output;
        }

        //Событие по окончанию парсинга
        protected void onPostExecute(ArrayList<String> output) {
            //Убираем диалог загрузки
            pd.dismiss();
            //Находим ListView
            ListView listview = (ListView) findViewById(R.id.listView);
            //Загружаем в него результат работы doInBackground
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, output);

            listview.setAdapter(adapter);
            // Делаем поиск активным
            anTextSearcher.setClickable(true);
        }

    }




}

