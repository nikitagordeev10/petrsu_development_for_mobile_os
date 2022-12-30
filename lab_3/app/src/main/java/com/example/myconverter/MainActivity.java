package com.example.myconverter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

// объявление класса MainActivity. AppCompatActivity – класс, написанный разработчиками Android, именно он позволяет поместить в Android наш Java-код
public class MainActivity extends AppCompatActivity {
    // ======== СОЗДАНИЕ ПЕРЕМЕННЫХ ========
    Map<String, String> rezult_xml = new HashMap<>();
    boolean data_load = false;
    Button button;
    TextView result;
    // ======================================

    // (1) ======== ОСНОВНАЯ ПРОГРАММА ========
    // задаёт начальную установку параметров при инициализации активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button); // кнопка "Конвертировать"
        result = findViewById(R.id.result);// поле "Получу"

        checkConnection();

        // Анонимный класс параметров - это анонимная функция. new Thread(() ->{})
        new Thread(() -> {
            TextView text = findViewById(R.id.editTextNumber);
            try {
                String str_xml = downloadXml();
                text.post(() -> parser_xml(str_xml));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Конвертация, по нажатию на кнопку
        button.setOnClickListener(view -> {
            Spinner cur1=  findViewById(R.id.spinner2); // из валюты
            Spinner cur2 = findViewById(R.id.spinner3); // в валюту
            String[] list = getResources().getStringArray(R.array.Values); // поддерживаемые валюты
            if (data_load) perevod(list[(int) cur1.getSelectedItemId()], list[(int) cur2.getSelectedItemId()]);
        });
    }
    // ======================================

    // ======== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ========
    // (2) Проверка соединения с интрнетом
    private void checkConnection() {
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = false;
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo networkInfo : info)
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        connected = true;
                    }
        }
        if (!connected) {
            // Если подключение отсутсует, то выводим соответвующий dialog с предложением закрытия приложения
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("ВНИМАНИЕ")
                    .setMessage("Отсутсвует подключение к Интернету. Вы уверены, что хотите продолжить?")
                    .setPositiveButton(android.R.string.no, (dialog, which) -> MainActivity.this.finish())
                    .setNegativeButton(android.R.string.yes, null)
                    .show();
        }
    }

    // (2) Загружаем xml по ссылке
    private String downloadXml() throws IOException {

        // Подготавливаем переменные
        StringBuilder xmlResult = new StringBuilder();
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;

        try {
            URL url = new URL("https://www.cbr.ru/scripts/XML_daily.asp");
            connection = (HttpsURLConnection)url.openConnection();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line=reader.readLine()) != null) {
                xmlResult.append(line);
            }
            return xmlResult.toString(); // Все данные с сайта ЦБ РФ
        } finally {
            // Правильное закрытие потоков ввода/вывода
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    // (4) Парскер xml
    @SuppressLint("RestrictedApi")
    private void parser_xml(String xml) {
        String tag = ""; // Парсим xml валют по тегам
        String key = ""; // Возвращаем словарь, ключ - абревиатура валюты, значение - отношение к рублю

        try {
            XmlPullParser xpp = prepareXpp(xml);
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT: // начало документа
                        break;
                    case XmlPullParser.START_TAG: // начало тэга
                        tag = xpp.getName();
                    case XmlPullParser.END_TAG: // конец тэга
                        break;
                    case XmlPullParser.TEXT: // содержимое тэга
                        if (Objects.equals(tag, "CharCode"))
                            key = xpp.getText();
                        if (Objects.equals(tag, "Value"))
                            rezult_xml.put(key, xpp.getText());
                        break;
                    default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        data_load = true;
    }

    // (5) XML-парсер для разбора XML документа в Android
    XmlPullParser prepareXpp(String xml) throws XmlPullParserException {
        // получаем фабрику
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); // получаем фабрику
        // включаем поддержку namespace (по умолчанию выключена)
        factory.setNamespaceAware(true);
        // создаем парсер
        XmlPullParser xpp = factory.newPullParser();
        // даем парсеру на вход Reader
        xpp.setInput(new StringReader(xml));
        return xpp;
    }

    // (6) отношение двух валют, через рубль
    @SuppressLint("SetTextI18n")
    private void perevod(String cur1, String cur2){

        // инициализация переменных
        TextView money_will = findViewById(R.id.result);
        EditText money_original = findViewById(R.id.editTextNumber);
        double v1, result;

        // количество денег у пользователя
        if (!money_original.getText().toString().matches("")) {
            v1 = Double.parseDouble(money_original.getText().toString());
        } else {
            v1 = 0;
        }

        if (!cur1.equals("RUB")){
            // коэффициент к рублю валюта 1
            double k1 = Double.parseDouble(rezult_xml.get(cur1).replaceAll(",", "."));
            if (!cur2.equals("RUB")) {
                // коэффициент к рублю валюта 2
                double k2 = Double.parseDouble(rezult_xml.get(cur2).replaceAll(",", "."));
                result = v1 * k1 / k2;
            }
            else result=v1*k1;
        } else if (!cur2.equals("RUB")){
            // коэффициент к рублю валюта 2
            double k2 = Double.parseDouble(rezult_xml.get(cur2).replaceAll(",","."));
            result=v1*k2;
        } else
            result=v1;

        // Выводим результат, округлённый
        String out = String.valueOf(result).length() > 6 ? String.valueOf(result).substring(0, 6) : String.valueOf(result);
        money_will.setText(out);
    }
    // ======================================
}