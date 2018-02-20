package com.igor.omdbfilmes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private FloatingActionButton fab;
    private ArrayList<String> nomesDosFilmes;
    private ArrayList<Filme> objetosFilmes;
    private ArrayAdapter<String> adapter;
    private ValueEventListener valueEventListener;
    private ListView listViewFilmes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        listViewFilmes = (ListView) findViewById(R.id.listViewFilmes);

        //configuracoes do firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("filmes");

        //nomes pro listview
        nomesDosFilmes = new ArrayList<>();
        objetosFilmes = new ArrayList<>();

        //adapter pro listview de filmes
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomesDosFilmes);
        listViewFilmes.setAdapter(adapter);
        listViewFilmes.setOnItemClickListener(verDetalhesFilme(Home.this)); //"link" pra abrir detalhes do app


        ////////////////////////////////////////////////////Listeners
        fab.setOnClickListener(new View.OnClickListener() { //novo filme
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Home.this, AddFilme.class);
                startActivity(it);
                finish();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() { //carregando os filmes
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //limpando os arrays pra não duplicar quando atualizar
                nomesDosFilmes.clear();
                objetosFilmes.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Filme f = data.getValue(Filme.class);
                    nomesDosFilmes.add(f.getTitulo()); //adicionando os nomes dos objetosFilmes pra serem exibidos no listview
                    objetosFilmes.add(f); //adicionando o objeto filme pra quando quiser ver detalhes a partir do listview
                    adapter.notifyDataSetChanged(); //atuzlizando o adapter
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public AdapterView.OnItemClickListener verDetalhesFilme(final Context context){
        return(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long id) {
                //esta funcao abre os detalhes de um filme passando o id como parametro pelo intent
                String idFilme = objetosFilmes.get(position).getId();
                Intent intentVerDetalhes = new Intent(context, VerDetalhesFilme.class);
                intentVerDetalhes.putExtra("id", idFilme);
                startActivity(intentVerDetalhes);
                finish();
            }
        });
    }
}
