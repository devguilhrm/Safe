@echo off
REM Script para executar testes unitários
REM Requer Maven instalado e configurado no PATH

echo ===================================
echo Executando testes unitários - Safety
echo ===================================
echo.

REM Executar todos os testes
mvn clean test

echo.
echo ===================================
echo Testes concluídos!
echo ===================================
