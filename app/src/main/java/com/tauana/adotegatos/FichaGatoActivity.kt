package com.tauana.adotegatos

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.tauana.adotegatos.databinding.EtiquetaBinding
import com.tauana.adotegatos.databinding.TelaFichaGatoBinding

/** Tela 2: ficha de um gato e acompanhamento do processo de adoção. */
class FichaGatoActivity : AppCompatActivity() {
    companion object {
        const val CHAVE_ID_GATO = "id_gato"
        private const val CHAVE_ETAPA_SALVA = "etapa_salva"
    }

    private lateinit var tela: TelaFichaGatoBinding
    private lateinit var gato: GatoAdocao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // O id vem da Intent. Tanto o extra quanto o gato podem não existir (valores opcionais).
        val gatoRecebido = intent.getStringExtra(CHAVE_ID_GATO)?.let { encontrarGato(it) }
        if (gatoRecebido == null) {
            Toast.makeText(this, R.string.gato_nao_encontrado, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        gato = restaurarEtapa(gatoRecebido, savedInstanceState)

        tela = TelaFichaGatoBinding.inflate(layoutInflater)
        setContentView(tela.root)

        tela.fichaToolbar.setNavigationOnClickListener { finish() }

        preencherFicha()
        atualizarProgresso()
        configurarBotoesDeEtapa()
        ajustarMargensDoSistema()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::gato.isInitialized) {
            outState.putInt(CHAVE_ETAPA_SALVA, gato.etapaDoProcesso)
        }
    }

    /** Se a tela foi recriada (rotação, por exemplo), volta para a etapa em que o usuário estava. */
    private fun restaurarEtapa(original: GatoAdocao, estadoSalvo: Bundle?): GatoAdocao {
        if (estadoSalvo == null || !estadoSalvo.containsKey(CHAVE_ETAPA_SALVA)) return original
        val etapa = estadoSalvo.getInt(CHAVE_ETAPA_SALVA).coerceIn(0, GatoAdocao.QTD_ETAPAS)
        return original.copy(etapaDoProcesso = etapa)
    }

    private fun preencherFicha() {
        tela.fichaNome.text = gato.nome
        tela.textoHistoria.text = gato.historia
        tela.textoSexo.text = gato.sexo
        tela.textoPorte.text = gato.porte

        // Valores opcionais: quando não há dado, mostra "Não informado"
        val naoInformado = getString(R.string.nao_informado)
        tela.textoResgate.text = gato.resgatadoEm ?: naoInformado
        tela.textoTemperamento.text = gato.temperamento.joinToString(", ").ifEmpty { naoInformado }

        tela.fichaFoto.setImageResource(gato.foto ?: R.drawable.gato_sem_foto)
        tela.fichaFoto.contentDescription = getString(R.string.foto_do_gato, gato.nome)

        // Etiquetas: o mesmo componente XML é inflado uma vez para cada texto
        val etiquetas = listOfNotNull(
            getString(if (gato.ehFilhote) R.string.filhote else R.string.adulto),
            gato.sexo,
            idadePorExtenso(gato.idadeMeses),
            gato.porte,
            if (gato.castrado) getString(R.string.castrado) else null,
        )
        etiquetas.take(3).forEach { criarEtiqueta(tela.etiquetasLinha1, it) }
        etiquetas.drop(3).forEach { criarEtiqueta(tela.etiquetasLinha2, it) }
    }

    private fun criarEtiqueta(linha: LinearLayout, texto: String) {
        val etiqueta = EtiquetaBinding.inflate(layoutInflater, linha, false)
        etiqueta.textoEtiqueta.text = texto
        linha.addView(etiqueta.root)
    }

    /** Interações que atualizam a interface: cada clique troca o gato por uma cópia em outra etapa. */
    private fun configurarBotoesDeEtapa() {
        tela.botaoVoltarEtapa.setOnClickListener {
            gato.voltarEtapa()?.let { atualizado ->
                gato = atualizado
                atualizarProgresso()
            }
        }

        tela.botaoAvancarEtapa.setOnClickListener {
            gato.avancarEtapa()?.let { atualizado ->
                gato = atualizado
                atualizarProgresso()
            }
        }
    }

    private fun atualizarProgresso() {
        val nomesDasEtapas = resources.getStringArray(R.array.etapas_adocao)

        tela.textoNomeEtapa.text = nomesDasEtapas[gato.etapaDoProcesso]
        tela.textoEtapa.text = getString(R.string.etapa, gato.etapaDoProcesso, GatoAdocao.QTD_ETAPAS)
        tela.textoPercentual.text = getString(R.string.porcentagem, gato.percentualConcluido)
        tela.barraProgresso.setProgressCompat(gato.percentualConcluido, true)

        tela.botaoVoltarEtapa.isEnabled = gato.podeVoltar
        tela.botaoAvancarEtapa.isEnabled = gato.podeAvancar
    }

    /** Afasta a toolbar da barra de status e o fim do conteúdo da barra de navegação. */
    private fun ajustarMargensDoSistema() {
        ViewCompat.setOnApplyWindowInsetsListener(tela.raizFicha) { _, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            tela.fichaToolbar.updatePadding(top = barras.top)
            tela.fichaConteudo.updatePadding(bottom = barras.bottom)
            insets
        }
    }
}
