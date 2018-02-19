package com.igor.omdbfilmes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class AddFilme extends AppCompatActivity {

    private EditText etNomeFilme;
    private TextView textViewRespostaAPI;
    private RequestQueue requestQueque;
    private Button botaoAdicionarFilme;
    private DatabaseReference mDatabase;
    private Filme filmeASerAdicionado;
    private Button botaoCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_filme);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        etNomeFilme = (EditText) findViewById(R.id.etNomeFilme);
        textViewRespostaAPI = (TextView) findViewById(R.id.textViewRespostaAPI);
        botaoAdicionarFilme = (Button) findViewById(R.id.botaoAdicionarFilme);
        botaoCancelar = (Button) findViewById(R.id.botaoCancelar);

        requestQueque = Volley.newRequestQueue(this);

        etNomeFilme.addTextChangedListener(new TextWatcher() { //faz uma requisicao a api toda vez que é adicionado um novo caractere
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarFilme(etNomeFilme.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        botaoAdicionarFilme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarFilme();
            }
        });

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarTela(); //volta para a home
            }
        });

    }

    private void adicionarFilme(){
        mDatabase.child("filmes").child(filmeASerAdicionado.getId()).setValue(filmeASerAdicionado);
        Toast.makeText(this, "Filme adicionado com sucesso.", Toast.LENGTH_SHORT).show();
        voltarTela();
    }

    private void voltarTela(){
        Intent t = new Intent(AddFilme.this, Home.class);
        startActivity(t);
        finish();
    }

    private void buscarFilme(String filme) {

        String url = "http://www.omdbapi.com/?t=" + filme + "&apikey=2b8b96c3"; //link de consulta a api do omdb

        Response.Listener responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String statusResposta = response.getString("Response");
                    String texto;
                    if(statusResposta.equals("True")){

                        String id = (String) response.getString("imdbID");
                        String titulo = (String) response.getString("Title");
                        String diretor = (String) response.getString("Director");
                        String atores = (String) response.getString("Actors");
                        String genero = (String) response.getString("Genre");
                        String capa = (String) response.getString("Poster");
                        String produtora = (String) response.getString("Production");
                        String duracao = (String) response.getString("Runtime");
                        String autor = (String) response.getString("Writer");
                        String ano = (String) response.getString("Year");
                        String classificacao = (String) response.getString("imdbRating");

                        filmeASerAdicionado = new Filme(id, titulo, diretor, atores, genero, capa, produtora, duracao, autor, ano, classificacao);

                        texto = "ID: " + id + " \nTítulo: " + titulo + " \nAno: " + ano + " \nDiretor: " + diretor + " \nDuração: " + duracao;

                        botaoAdicionarFilme.setEnabled(true); //caso exista um filme que bata com aquela string, habilita o botao de add filme
                    }else{
                        texto = "Nenhum título correspondente!";
                        botaoAdicionarFilme.setEnabled(false); //desabilita o botao caso nao encontre um filme que bata com aquela string
                    }
                    textViewRespostaAPI.setText(texto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener responseError = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, (String) null, responseListener, responseError);

        requestQueque.add(request);

    }
}
