package com.example.einzelbeispiel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;

import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText matrikelNummer;
    private TextView ersteAnwort;
    private TextView zweiteAnwort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        matrikelNummer = (EditText) findViewById(R.id.feld);
        ersteAnwort = (TextView) findViewById(R.id.textView2);
        zweiteAnwort = (TextView) findViewById(R.id.textView3Server);
        }

    public void run(View v) {
        TCPVerbindung process = new TCPVerbindung();
        process.start();
    }

    class TCPVerbindung extends Thread {
        @Override
        public void run() {
            try {
                String matrNr = matrikelNummer.getText().toString();
                Socket clientSocket = new Socket("se2-isys.aau.at", 53212); //create client socket, connect to server
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); //create output stream attached to socket, MatNr zum Server schicken
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); ////create input stream attached to socket, gibt mir was zurück
                outToServer.writeBytes(matrNr + '\n');
                ersteAnwort.setText(inFromServer.readLine());
                clientSocket.close(); //schließen
            } catch (Exception  e) {
               e.printStackTrace();
            }
        }
    }
}