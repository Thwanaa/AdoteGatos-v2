package com.tauana.adotegatos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tauana.adotegatos.databinding.ItemGatoBinding
import com.tauana.adotegatos.databinding.TelaListaGatosBinding

/** Tela 1: lista de gatos disponíveis para adoção. */
class ListaGatosActivity : AppCompatActivity() {
    private lateinit var tela: TelaListaGatosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        tela = TelaListaGatosBinding.inflate(layoutInflater)
        setContentView(tela.root)

        tela.listaGatos.layoutManager = LinearLayoutManager(this)
        tela.listaGatos.adapter = GatosAdapter(gatosDisponiveis) { gato -> abrirFicha(gato) }

        ajustarMargensDoSistema()
    }

    /** Abre a tela de ficha com uma Intent explícita, enviando o id do gato escolhido. */
    private fun abrirFicha(gato: GatoAdocao) {
        val intent = Intent(this, FichaGatoActivity::class.java)
        intent.putExtra(FichaGatoActivity.CHAVE_ID_GATO, gato.id)
        startActivity(intent)
    }

    /** Evita que o conteúdo fique embaixo da barra de status e da barra de navegação. */
    private fun ajustarMargensDoSistema() {
        ViewCompat.setOnApplyWindowInsetsListener(tela.raizLista) { view, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }
    }
}

class GatosAdapter(
    private val gatos: List<GatoAdocao>,
    private val aoEscolher: (GatoAdocao) -> Unit,
) : RecyclerView.Adapter<GatosAdapter.GatoViewHolder>() {

    class GatoViewHolder(val item: ItemGatoBinding) : RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GatoViewHolder {
        val item = ItemGatoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GatoViewHolder(item)
    }

    override fun onBindViewHolder(holder: GatoViewHolder, position: Int) {
        val gato = gatos[position]
        val item = holder.item
        val contexto = item.root.context

        item.itemNome.text = gato.nome
        item.itemIdade.text = contexto.idadePorExtenso(gato.idadeMeses)
        item.itemEtiqueta.textoEtiqueta.text =
            contexto.getString(if (gato.ehFilhote) R.string.filhote else R.string.adulto)

        // foto é opcional: sem foto, mostra a ilustração padrão
        item.itemFoto.setImageResource(gato.foto ?: R.drawable.gato_sem_foto)
        item.itemFoto.contentDescription = contexto.getString(R.string.foto_do_gato, gato.nome)

        item.root.setOnClickListener { aoEscolher(gato) }
    }

    override fun getItemCount(): Int = gatos.size
}
