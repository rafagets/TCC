package es.esy.rafaelsilva.tcc.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.adapters.PesquisaProduto;
import es.esy.rafaelsilva.tcc.controle.CtrlProduto;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.modelo.Produto;

public class ListarProdutoActivity extends AppCompatActivity {
    private ListView listView;
    private List<Produto> listaProduto;
    private PesquisaProduto adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_produto);
        listView = (ListView) findViewById(R.id.listViewProdutos);
        new CtrlProduto(this).listar("", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                listaProduto = new ArrayList<>();

                for (Object obj : lista) {
                    listaProduto.add((Produto) obj);
                }

                adapter = new PesquisaProduto(ListarProdutoActivity.this, listaProduto);

                listView.setAdapter(adapter);
            }
            @Override
            public void falha() {

            }
        });

    }

    /*Aqui é onde a grande magica da pesquisa acontece!*/
    private void monitorarPesquisa(MenuItem item){
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(item);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*Atravez do adapter criado especialmente para esse fim
                * o componente pega os cliques do teclado e manda para
                * o adapter filtara as opçoes dinamicamente */
                adapter.getFilter().filter(newText);
                return false;
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pesquisar_produto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search_produtos){
            monitorarPesquisa(item);
        }
        return super.onOptionsItemSelected(item);
    }

}
