# Shared Counters
## Contadores Compartilhados

Considere resolver os seguintes problemas de forma escalavel:

1. Contar quantos vezes um URL é acessada
1. Contar quantas pessoas passam por uma porta
1. Contar ...

Esse problema pode ser escrito da seguinte maneira:

    UPDATE Visits SET counter = counter + 1 WHERE id = 'main' 


