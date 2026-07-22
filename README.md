# Sentinela - Triagem de Chamados com Inteligência Artificial

O **Sentinela** é uma solução inteligente e automatizada para recepção, triagem e classificação de chamados de suporte técnico. O objetivo principal do sistema é ler as reclamações textuais enviados por clientes e, utilizando Inteligência Artificial, extrair automaticamente o sentimento do usuário, a categoria do problema, o nível de urgência técnica e um resumo estruturado da queixa.

Para garantir a total privacidade e a soberania dos dados dos clientes, o projeto adota um modelo de linguagem (LLM) executado **100% localmente**, mitigando tráfego de dados sensíveis para APIs de nuvem externas e custos variáveis.

---

## 🏗️ Estrutura do Ecossistema (Monorepo)

O projeto é baseado em uma arquitetura modular dividida em três frentes principais:

* **`sentinela-core-api/`**: API principal desenvolvida em **Java e Spring Boot**, responsável pelas regras de negócio, segurança, persistência transacional de dados e controle de estados dos chamados.
* **`sentinela-ai-gateway/`**: Gateway cognitivo desenvolvido em **Python e FastAPI**, encarregado da engenharia de prompts, higienização de strings e validação rigorosa dos payloads trafegados com a IA.
* **`sentinela-frontend/`**: Interface visual do sistema para a interação dos analistas de suporte.

---

## 🛠️ Infraestrutura e Stack Homologada

Até o momento, a base estrutural do ambiente de desenvolvimento foi consolidada com as seguintes tecnologias:

### Camada de Dados (Persistência)

* **Banco de Dados:** PostgreSQL 16-Alpine orquestrado via Docker.


* **Mapeamento de Rede:** Porta física local redirecionada para **`5433`** para neutralizar conflitos nativos de portas em ambiente Windows.


* **Integridade:** Persistência de dados blindada através de volumes nomeados do Docker (`postgres_data`), garantindo resiliência contra desligamentos e manutenções do contêiner.



### Camada de Inteligência Artificial Local

* **Orquestrador:** Ollama (rodando como serviço nativo de background).
* **Modelo de Linguagem (LLM):** Mistral 7B (otimizado para inferências rápidas e geração de texto estruturado).

### Ambiente Python (Gateway de IA)

* **Framework:** FastAPI com suporte assíncrono nativo para alto volume de requisições.
* **Garantia de Tipagem/Contrato:** Pydantic v2 mapeando enums estritos e limites de criticidade numérica para barrar alucinações de dados da IA.
* **Isolamento de Ambiente:** Utilização de Ambiente Virtual (`.venv`) com fixação rigorosa de versões de dependências em `requirements.txt`.



---

## 🚀 Como Inicializar o Ambiente Atual

### 0. Configurar Variáveis de Ambiente
Antes de subir os serviços, você precisa criar o seu arquivo de credenciais local. Duplique o arquivo de exemplo e preencha com seus dados:

```powershell
# No PowerShell, duplique o arquivo de exemplo
copy .env.example .env
```
### 1. Inicializar o Banco de Dados

Certifique-se de ter as credenciais configuradas localmente no arquivo `.env` (que está blindado e oculto pelo `.gitignore`) e execute o comando a partir da pasta raiz:

```powershell
docker compose up -d

```

### 2. Ativar o Gateway de IA (Python)

Abra uma janela de terminal dedicada, navegue até a pasta do gateway e ative o ambiente virtual isolado:

```powershell
cd sentinela-ai-gateway
.\.venv\Scripts\Activate.ps1

```

