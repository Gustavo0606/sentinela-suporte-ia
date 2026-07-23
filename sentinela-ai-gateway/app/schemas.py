from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
from pydantic import BaseModel, ValidationError
from enum import Enum
import httpx

app = FastAPI()
class Sentimento(str, Enum):
    IRRITADO = "IRRITADO"
    SATISFEITO = "SATISFEITO"
    NEUTRO = "NEUTRO"
    NAO_ANALISADO = "NAO_ANALISADO"
class Categoria(str, Enum):
    FINANCEIRO = "FINANCEIRO"
    BUG = "BUG"
    DUVIDA = "DUVIDA"
    OUTROS = "OUTROS"
    INVALIDO = "INVALIDO"
class review(BaseModel):
    reclamacao: str

class outputOllama(BaseModel):
    sentimento: Sentimento = Field(description="Humor predominante identificado no texto do cliente")
    categoria: Categoria = Field(description="Domínio de classificação do problema")
    urgencia: int = Field(..., ge=1, le=5, description="Nível de criticidade técnica calculado de 1 a 5")
    resumo: str = Field(description="Uma frase curta resumindo a queixa com no máximo 15 palavras")

@app.post("/analyze", response_model=outputOllama)
async def fazerTriagem(entrada : review):
    promptTriagem = f"""
Você é o Sentinela, um agente especialista em triagem cognitiva de chamados de suporte técnico. Sua única tarefa é analisar o texto do cliente e extrair informações estruturadas.

Instruções Cruciais:
1. Analise o teor emocional, o domínio do problema e a criticidade técnica.
2. Sua resposta deve ser EXCLUSIVAMENTE um objeto JSON válido, sem qualquer texto de introdução, conclusão ou formatação de Markdown (não use blocos de código com ```json).

O JSON gerado deve conter exatamente as seguintes chaves e restrições de valores:
{{
  "sentimento": "Escolha obrigatoriamente um destes quatro valores: IRRITADO, NEUTRO, SATISFEITO ou NAO_ANALISADO",
  "categoria": "Escolha obrigatoriamente um destes cinco valores: FINANCEIRO, BUG, DUVIDA, OUTROS ou INVALIDO",
  "urgencia": "Um número inteiro de 1 a 5, onde 1 é baixíssima criticidade e 5 é parada total de sistema/crítico",
  "resumo": "Uma frase curta resumindo a queixa principal em no máximo 15 palavras"
}}

Texto do chamado enviado pelo cliente para análise:
---
{entrada.reclamacao}
---

Saída JSON estrita:

"""

    ollama_url = "http://localhost:11434/api/generate"

    payload_ollama = {
        "model": "mistral",
        "prompt": promptTriagem,
        "stream": False,
        "format": "json"
    }
    async with httpx.AsyncClient(timeout=9.0) as client:
        try:
            resposta_rede = await client.post(ollama_url, json=payload_ollama)
            
            dados_resposta = resposta_rede.json()
            
            texto_gerado_pela_ia = dados_resposta.get("response")
            
            objeto_final_validado = outputOllama.model_validate_json(texto_gerado_pela_ia)
            
            return objeto_final_validado
        except httpx.HTTPError as erroRede:
            raise HTTPException(status_code=502, detail="erro de rede")
        except ValidationError as erroValidacao:
            raise HTTPException(status_code=422, detail="falha estrutural dos dados")

