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
        ersteAnwort = (TextView) findViewById(R.id.textView1);
        zweiteAnwort = (TextView) findViewById(R.id.textView2);
        }

    public void run(View v) {
        TCPVerbindung process = new TCPVerbindung();
        process.start();
    }
//String zu CharArray
    public void numbersToChar(View v) {
        String matrNr = matrikelNummer.getText().toString();
        char[] neueMatrikelNummer = new char[matrNr.length()];
        char[] buchstabenliste = new char[10];
        char buchstabeA = 97;
        for(int i = 0; i < buchstabenliste.length; i++,buchstabeA++ ){ //Buchstaben 1-9
            buchstabenliste[i] = buchstabeA;
        }
        for (int i = 0; i < neueMatrikelNummer.length; i++) { //Array durchgehen
            if ((i % 2) == 0) {
                neueMatrikelNummer[i] = matrNr.charAt(i);
            } else {
                int help = Character.getNumericValue(matrNr.charAt(i));
                if(help ==0){
                    neueMatrikelNummer[i] = 'j';
                }else {
                    neueMatrikelNummer[i] = buchstabenliste[help-1];
                }
            }
        }
        String neueMatrNr = new String(neueMatrikelNummer);
        zweiteAnwort.setText(neueMatrNr);
    }

    class TCPVerbindung extends Thread {
        @Override
        public void run() {
            try {
                String matrNr = matrikelNummer.getText().toString();
                Socket clientSocket = new Socket("se2-isys.aau.at", 53212); //create client socket, connect to server
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); //create output stream attached to socket, MatNr zum Server schicken
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); ////create input stream attached to socket, gibt mir was zurück
                outToServer.writeBytes(matrNr + '\n'); //als Bytestream schicken
                ersteAnwort.setText(inFromServer.readLine());
                clientSocket.close(); //schließen
            } catch (Exception  e) {
               e.printStackTrace();
            }
        }
    }
}