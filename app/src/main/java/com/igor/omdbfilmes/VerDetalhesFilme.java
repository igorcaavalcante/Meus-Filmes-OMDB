package com.igor.omdbfilmes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerDetalhesFilme extends AppCompatActivity {

    private Filme filme;
    private TextView textViewDetalhes;
    private Button botaoExcluirFilme;
    private Button botaoVoltar;
    private String texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalhes_filme);

        textViewDetalhes = (TextView) findViewById(R.id.textViewDetalhes);
        botaoExcluirFilme = (Button) findViewById(R.id.botaoExcluirFilme);
        botaoVoltar = (Button) findViewById(R.id.botaoVoltar);

        Intent activityAnterior = getIntent();
        final String idFilme = activityAnterior.getExtras().getString("id"); //pegando id do filme clicado na home

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("filmes").child(idFilme);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Filme f = dataSnapshot.getValue(Filme.class);
                texto = "ID: " + f.getId() + " \nTítulo: " + f.getTitulo() + " \nAno: " + f.getAno() +
                        " \nDiretor: " + f.getDiretor() + " \nDuração: " + f.getDuracao() + " \nAtores: "  + f.getAtores() +
                        " \nClassificação OMDB: " + f.getClassificacao() + " \nGênero: " + f.getGenero() + " \nProdução: " + f.getProdutora();
                textViewDetalhes.setText(texto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarTela(); //volta pra home
            }
        });

        botaoExcluirFilme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(idFilme).removeValue(); //excluindo filme do firebase
                Toast.makeText(VerDetalhesFilme.this, "Filme removido com sucesso.", Toast.LENGTH_SHORT).show();
                voltarTela();
            }
        });

    }

    private void voltarTela(){
        Intent t = new Intent(VerDetalhesFilme.this, Home.class);
        startActivity(t);
        finish();
    }
}
