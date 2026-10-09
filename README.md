# Adote um Gato

Aplicativo Android que apresenta gatos disponíveis para adoção e permite acompanhar o processo de adoção de cada um.

## Objetivo

Facilitar a divulgação de gatos resgatados: a pessoa interessada vê a lista de animais, abre os detalhes de um gato (história, características e temperamento) e acompanha em que etapa do processo de adoção ele está.

## Telas

1. **Lista de gatos** (`ListaGatosActivity`, `tela_lista_gatos.xml`): `RecyclerView` com foto, nome, etiqueta de faixa etária e idade.
2. **Detalhes do gato** (`FichaGatoActivity`, `tela_ficha_gato.xml`): foto, etiquetas, história, etapa do processo de adoção (com botões para avançar e voltar) e ficha de detalhes.

A navegação é feita por `Intent` explícita, passando o id do gato. Os dados são simulados (`gatosDisponiveis`, em `GatoAdocao.kt`).

## Como rodar

1. Instale o [Android Studio](https://developer.android.com/studio) em versão recente.
2. Clone o repositório e abra a pasta do projeto em **File > Open**.
3. Aguarde a sincronização do Gradle. Se o Android Studio pedir para instalar o SDK 37, aceite.
4. Escolha um emulador ou celular com Android 13 (API 33) ou superior e clique em **Run**.

## Bibliotecas externas

- **AndroidX AppCompat, Core KTX e Activity KTX**: base das Activities e compatibilidade entre versões do Android.
- **Material Components**: tema Material 3, `MaterialToolbar`, `LinearProgressIndicator`, `FloatingActionButton` e `MaterialDivider`.
- **RecyclerView** (incluído via Material Components): lista de gatos.
- **Jetpack Compose (BOM, UI, Material 3)**: já configurado para a etapa 2 do projeto; ainda não é usado nas telas.
