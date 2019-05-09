REM Por praticidade são usados o usuário e database postgres...
REM Mais pra frente usaremos um usuário e database específicos.

"C:\Program Files\PostgreSQL\10\bin\psql.exe" -h localhost -U postgres -d postgres -p 5432 -f cria_tabelas.sql