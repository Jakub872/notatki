package com.example.notatki;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    EditText pole;
    ListView lista;
    Button dodaj, zapisz, wyczysc, wczytaj;

    ArrayList<String> listaNotatek;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] notatki = {"Zakupy: chleb, masło, ser", "Do zrobienia: obiad, umyć podłogi", "weekend: kino, spacer z psem"};
        listaNotatek = new ArrayList<>(Arrays.asList(notatki));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaNotatek);

        pole = findViewById(R.id.pole);
        lista = findViewById(R.id.lista);
        dodaj = findViewById(R.id.dodaj);
        zapisz = findViewById(R.id.zapisz);
        wyczysc = findViewById(R.id.wyczysc);
        wczytaj = findViewById(R.id.wczytaj);

        lista.setAdapter(adapter);

        dodaj.setOnClickListener(v -> {
            String tekst = pole.getText().toString().trim();
            listaNotatek.add(tekst);
            adapter.notifyDataSetChanged();
            pole.setText("");
        });

        zapisz.setOnClickListener(v -> {
            try{
                FileOutputStream plik = openFileOutput("plik.txt", MODE_PRIVATE);
                OutputStreamWriter streamWriter = new OutputStreamWriter(plik);
                BufferedWriter writer = new BufferedWriter(streamWriter);

                for(String notatka : listaNotatek){
                    writer.write(notatka);
                    writer.newLine();
                }
                writer.close();
                Toast.makeText(this, "Zapisano notatki do pliku", Toast.LENGTH_SHORT).show();
            } catch(IOException e){
                Toast.makeText(this, "Błąd podczas zapisu pliku: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        wyczysc.setOnClickListener(v -> {
            listaNotatek.clear();
            adapter.notifyDataSetChanged();
        });

        wczytaj.setOnClickListener(v -> {
            try{
                listaNotatek.clear();
                FileInputStream plik = openFileInput("plik.txt");
                InputStreamReader streamReader = new InputStreamReader(plik);
                BufferedReader reader = new BufferedReader(streamReader);

                String linia;
                while ((linia = reader.readLine()) != null){
                    listaNotatek.add(linia);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Wczytano notatki z pliku", Toast.LENGTH_SHORT).show();
            } catch(IOException e){
                Toast.makeText(this, "Błąd podczas wczytywania pliku: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}