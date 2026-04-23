#!/bin/bash
# Script para executar testes unitários
# Requer Maven instalado e configurado no PATH

echo "==================================="
echo "Executando testes unitários - Safety"
echo "==================================="
echo ""

# Executar todos os testes
mvn clean test

echo ""
echo "==================================="
echo "Testes concluídos!"
echo "==================================="
