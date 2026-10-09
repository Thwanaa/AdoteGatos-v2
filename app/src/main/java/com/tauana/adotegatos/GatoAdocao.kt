package com.tauana.adotegatos

import android.content.Context
import androidx.annotation.DrawableRes

/**
 * Modelo imutável de um gato disponível para adoção.
 * Para "alterar" a etapa do processo, cria-se uma cópia com copy().
 */
data class GatoAdocao(
    val id: String,
    val nome: String,
    val historia: String,
    val sexo: String,
    val porte: String,
    val idadeMeses: Int,
    val castrado: Boolean,
    val temperamento: List<String>,
    val resgatadoEm: String? = null,      // opcional: nem todo gato tem a data registrada
    @DrawableRes val foto: Int? = null,   // opcional: nem todo gato tem foto
    val etapaDoProcesso: Int = 0,              // 0 = processo não iniciado
) {
    init {
        require(idadeMeses >= 0) { "idadeMeses não pode ser negativa" }
        require(etapaDoProcesso in 0..QTD_ETAPAS) { "etapaDoProcesso deve estar entre 0 e $QTD_ETAPAS" }
    }

    val ehFilhote: Boolean
        get() = idadeMeses < 12

    val percentualConcluido: Int
        get() = etapaDoProcesso * 100 / QTD_ETAPAS

    val podeVoltar: Boolean
        get() = etapaDoProcesso > 0

    val podeAvancar: Boolean
        get() = etapaDoProcesso < QTD_ETAPAS

    /** Devolve uma cópia na etapa anterior, ou null se já está no início. */
    fun voltarEtapa(): GatoAdocao? = if (podeVoltar) copy(etapaDoProcesso = etapaDoProcesso - 1) else null

    /** Devolve uma cópia na próxima etapa, ou null se a adoção já foi concluída. */
    fun avancarEtapa(): GatoAdocao? = if (podeAvancar) copy(etapaDoProcesso = etapaDoProcesso + 1) else null

    companion object {
        const val QTD_ETAPAS = 4
    }
}

/** "4 meses", "1 ano", "3 anos"... */
fun Context.idadePorExtenso(idadeMeses: Int): String =
    if (idadeMeses < 12) {
        resources.getQuantityString(R.plurals.idade_meses, idadeMeses, idadeMeses)
    } else {
        val anos = idadeMeses / 12
        resources.getQuantityString(R.plurals.idade_anos, anos, anos)
    }

// Dados simulados (mocks): nesta etapa não há API nem banco de dados.
val gatosDisponiveis: List<GatoAdocao> = listOf(
    GatoAdocao(
        id = "mel",
        nome = "Mel",
        historia = "Mel foi encontrada com os irmãos em uma caixa de papelão, na porta de um mercado. " +
                "É curiosa, adora brincar com bolinhas de papel e dorme no colo de quem deixar.",
        sexo = "Fêmea",
        porte = "Pequeno",
        idadeMeses = 4,
        castrado = false,
        temperamento = listOf("Brincalhona", "Carinhosa"),
        resgatadoEm = "Junho de 2026",
        foto = R.drawable.gato_mel,
    ),
    GatoAdocao(
        id = "luna",
        nome = "Luna",
        historia = "Luna vivia em um estacionamento e demorou a confiar em pessoas. Hoje pede carinho " +
                "esfregando a cabeça na mão. Prefere casas tranquilas, sem muito barulho.",
        sexo = "Fêmea",
        porte = "Médio",
        idadeMeses = 24,
        castrado = true,
        temperamento = listOf("Tímida", "Tranquila"),
        resgatadoEm = "Março de 2025",
        foto = R.drawable.gato_luna,
        etapaDoProcesso = 1,
    ),
    GatoAdocao(
        id = "nino",
        nome = "Nino",
        historia = "Nino apareceu no quintal de uma voluntária e nunca mais foi embora. Convive bem " +
                "com outros gatos e com cachorros de pequeno porte.",
        sexo = "Macho",
        porte = "Médio",
        idadeMeses = 36,
        castrado = true,
        temperamento = listOf("Sociável", "Calmo"),
        resgatadoEm = "Novembro de 2024",
        foto = R.drawable.gato_nino,
    ),
    GatoAdocao(
        id = "mia",
        nome = "Mia",
        historia = "Mia foi resgatada de um telhado durante uma chuva forte. É independente, gosta de " +
                "observar a rua pela janela e de lugares altos.",
        sexo = "Fêmea",
        porte = "Pequeno",
        idadeMeses = 9,
        castrado = true,
        temperamento = listOf("Independente", "Observadora"),
        resgatadoEm = "Janeiro de 2026",
        foto = R.drawable.gato_mia,
        etapaDoProcesso = 2,
    ),
    GatoAdocao(
        id = "fred",
        nome = "Fred",
        historia = "Fred é o veterano do abrigo. Foi deixado por uma família que se mudou e desde " +
                "então espera um novo lar. É companheiro e segue as pessoas pela casa.",
        sexo = "Macho",
        porte = "Grande",
        idadeMeses = 72,
        castrado = true,
        temperamento = listOf("Companheiro", "Dócil", "Guloso"),
        foto = R.drawable.gato_fred,
        // resgatadoEm não informado
    ),
    GatoAdocao(
        id = "amora",
        nome = "Amora",
        historia = "Amora é falante e mia para pedir atenção. Foi encontrada perto de uma escola e " +
                "se dá muito bem com crianças.",
        sexo = "Fêmea",
        porte = "Médio",
        idadeMeses = 18,
        castrado = true,
        temperamento = listOf("Falante", "Carinhosa"),
        resgatadoEm = "Agosto de 2025",
        foto = R.drawable.gato_amora,
    ),
    GatoAdocao(
        id = "pipoca",
        nome = "Pipoca",
        historia = "Pipoca acabou de chegar ao abrigo e ainda está em observação. Em breve terá foto " +
                "e mais informações.",
        sexo = "Macho",
        porte = "Pequeno",
        idadeMeses = 1,
        castrado = false,
        temperamento = emptyList(),
        // sem foto e sem data de resgate
    ),
)

/** Busca um gato pelo id. Devolve null se o id não existir. */
fun encontrarGato(id: String): GatoAdocao? = gatosDisponiveis.find { it.id == id }
