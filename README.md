🧠 Emoji Memory

Jogo da memória com emojis para Android, desenvolvido em Kotlin + Jetpack Compose como entrega da A1 de Desenvolvimento Mobile.

O jogador cria uma conta, joga partidas de 16 cartas (4x4) com uma categoria de emojis sorteada, acumula pontos e acompanha o desempenho no perfil. Um administrador acompanha as últimas partidas e gerencia os usuários.

👥 Integrantes
Gustavo Silva (@gustavopurkoot)
Bruno Silva Caldato
Lucas Kirsten (@olucaskirsten)
🎮 Como funciona
Cada partida tem 16 cartas (8 pares) e uma categoria de emojis sorteada automaticamente.
Cada par encontrado vale +100 pontos; cada erro tira 10 pontos (a pontuação nunca fica negativa).
Ao encontrar todos os pares, a partida é salva no banco e entra na pontuação total do jogador.
Modo único: Rank (sem modo casual).
Categorias de emojis
6 locais (cadastradas no Room): Animais, Comidas, Esportes, Veículos, Natureza e Carinhas.
4 vindas da API (importadas via Retrofit, quando há internet): Animais, Comidas, Viagens e Atividades.
📱 Telas
Tela	Descrição
Login	Entrada com usuário e senha, com botão Criar Conta
Criar Conta	Cadastro com nome de usuário, e-mail, senha e confirmação (salvo no Room)
Início	Pontuação total, posição e botão Jogar
Jogo	Tabuleiro 4x4 com pontuação e tentativas em tempo real
Resultado	Tentativas e pontuação da partida, com opção de jogar novamente
Perfil	Dados do jogador, estatísticas, conquistas, últimas partidas, Editar Perfil e Sair da conta
Admin	Últimas partidas e Gestão de Usuários (só o administrador edita os dados dos usuários)

A barra de navegação inferior mostra Início e Perfil para todos, e Admin apenas para o administrador.

🛠️ Tecnologias
Kotlin 2.2.10
Jetpack Compose + Material 3
Navigation Compose
Room 3 (com KSP) para persistência local
Retrofit 2.11 + Gson para consumo da API de emojis
Arquitetura MVVM (ViewModel + StateFlow + UiState)
Android Gradle Plugin 9.3.2, minSdk 24
🧱 Arquitetura
app/src/main/java/com/example/emoji_memory_android/
├── data/
│   ├── local/        # Room: entidades, DAOs e AppDatabase
│   ├── remote/       # Retrofit: EmojiApi
│   └── repository/   # Repositories (acesso aos dados + tratamento de erro)
├── viewmodel/        # JogoViewModel, AdminViewModel e ViewModelFactory
├── ui/               # Telas, navegação, tema e componentes
└── MainActivity.kt
Banco de dados (Room)
Usuario: id, nome, e-mail, senha, admin
Partida: id, usuarioId, jogador, pontuação, tentativas, categoria
Categoria: id, nome, emojis
ViewModels
JogoViewModel: login, cadastro, edição de perfil, lógica do jogo e salvamento das partidas.
AdminViewModel: lista das últimas partidas e gestão dos usuários.
API

Os emojis das categorias extras são buscados em EmojiHub (GET api/all/category/{categoria}). Sem internet, o app continua funcionando com as categorias locais.

▶️ Como executar
Clone o repositório:
bash
git clone https://github.com/gustavopurkoot/Emoji-Memory-Android.git
Abra a pasta no Android Studio e aguarde o sync do Gradle.
Rode em um emulador ou celular com Android 7.0 (API 24) ou superior.
Para importar as categorias da API, o dispositivo precisa de internet na primeira abertura.
Acesso de administrador

Na primeira execução é criado automaticamente um administrador:

Usuário: admin
Senha: admin

⚠️ Observações
Por simplicidade acadêmica, as senhas são salvas sem criptografia e não há autenticação avançada.
O projeto usa apenas conceitos vistos em aula, com exceção do Retrofit para a API de emojis.
