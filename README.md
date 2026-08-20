# 🛡️ Sentinela IA — Triagem Inteligente de Suporte

O **Sentinela IA** é uma plataforma distribuída de triagem e priorização automática de chamados de suporte técnico. Utilizando Inteligência Artificial local (LLM via Ollama), o sistema analisa o teor emocional, a urgência técnica e a categoria de cada solicitação, calculando dinamicamente a prioridade de atendimento na fila.

---

## 💻 Recomendação de Hardware

Para o processamento das inferências da IA (Modelo Mistral):

* **Com GPU (Recomendado):** Placa NVIDIA com suporte a CUDA e no mínimo 6 GB a 8 GB de VRAM (respostas entre 1 a 3 segundos).
* **Apenas CPU:** Mínimo de 16 GB de RAM e processador quad-core moderno (respostas entre 10 a 30 segundos).

---

## 🛠️ Arquitetura e Tecnologias

| Serviço | Tecnologia | Descrição |
| --- | --- | --- |
| **`sentinela-core-api`** | Java 22, Spring Boot 3, JPA, HikariCP | API principal responsável pela gestão dos chamados, persistência e regras de negócio. |
| **`sentinela-ai-gateway`** | Python 3.11, FastAPI, Pydantic, httpx | Microserviço responsável pela engenharia de prompt, validação de contrato e comunicação com o Ollama. |
| **`postgres-db`** | PostgreSQL 16 (Alpine) | Banco de dados relacional para persistência de chamados e histórico de triagens. |
| **`Ollama`** | LLM Mistral (Local no Host) | Engine de IA para processamento de linguagem natural e extração de JSON estruturado. |
| **`sentinela-frontend`** | HTML5, CSS3, JavaScript (Fetch API) | Interfaces visuais do cliente (`cliente.html`) e do atendente (`dashboard.html`). |

---

## 🔄 Fluxo de Funcionamento e Regra de Negócio

1. **Abertura do Chamado:** O cliente submete a solicitação via `cliente.html`.
2. **Processamento Assíncrono:** A `core-api` salva o registro inicial e aciona assincronamente (`@Async`) o `sentinela-ai-gateway`.
3. **Triagem Cognitiva (LLM):** O gateway FastAPI envia a requisição ao Ollama via `host.docker.internal:11434`, exigindo validação Pydantic dos dados:
* **Sentimento:** `IRRITADO`, `NEUTRO`, `SATISFEITO`, `NAO_ANALISADO`
* **Categoria:** `FINANCEIRO`, `BUG`, `DUVIDA`, `OUTROS`, `INVALIDO`
* **Urgência:** Escala de 1 a 5
* **Resumo:** Frase síntese com no máximo 15 palavras


4. **Cálculo da Prioridade:** A `core-api` calcula o score final utilizando a fórmula:

$$\text{Score} = (\text{Urgência} \times 15) + \text{Peso do Sentimento}$$



*(Pesos de Sentimento: `IRRITADO` = 20 pts, `NEUTRO` = 10 pts, `SATISFEITO`/`NAO_ANALISADO` = 0 pts)*
5. **Resiliência (Fallback):** Em caso de falha de conexão com a IA ou estouro de timeout, o chamado assume o status `PENDENTE_FILA_COMUM` com score fixo `50`, garantindo tolerância a falhas sem perda de dados.

---

## ⚙️ Pré-requisitos e Configuração

### 1. Requisitos do Sistema

* **Docker Desktop** instalado e em execução.
* **Ollama** instalado no sistema operacional host (Windows/Linux/macOS).

### 2. Configurar o Ollama e Modelo

No terminal do seu sistema operacional, faça o download do modelo e garanta a variável para aceitar conexões vindas do Docker:

```powershell
# Baixar o modelo
ollama pull mistral

# Adicionar a variável de ambiente OLLAMA_HOST=0.0.0.0 no sistema operacional host
# para permitir que o contêiner Docker acesse o Ollama

```

### 3. Variáveis de Ambiente (`.env`)

Copie o arquivo de exemplo `.env.example` para `.env` na raiz do projeto:

```powershell
cp .env.example .env

```

*(As dependências Python e a compilação do Spring Boot são tratadas automaticamente dentro das imagens Docker).*

---

## 🚀 Como Executar o Projeto

1. Abra o terminal na raiz do projeto (`sentinela-suporte-ia`) e execute:
```powershell
docker compose up -d --build

```


2. **Acessar as Aplicações:**
* **Portal do Cliente (Abertura):** Abra o arquivo `sentinela-frontend/cliente.html` no navegador.
* **Painel do Atendente (Dashboard):** Abra o arquivo `sentinela-frontend/dashboard.html` no navegador.
* **Documentação OpenAPI (FastAPI):** `http://localhost:8000/docs`



---

## 🔍 Diagnóstico e Logs

Para acompanhar a execução dos microserviços e a triagem da IA em tempo real:

```powershell
# Logs da API Java Spring Boot
docker logs -f sentinela-core-api

# Logs do Gateway de IA Python FastAPI
docker logs -f sentinela-ai-gateway

# Parar os serviços e limpar os volumes
docker compose down -v

```
