# Recomendaciones-financieras

# Crear la base de datos y: 
\c financial_recommendations
\i 'C:/Users/jaraj/Documents/Recomendaciones financieras/Recomendaciones-financieras/financial-recommendations/scripts/01_create_database_schema.sql'
\i 'C:/Users/jaraj/Documents/Recomendaciones financieras/Recomendaciones-financieras/financial-recommendations/scripts/02_seed_sample_data.sql'

# Correr el backend: 
financial-recommendations\backend =  mvn spring-boot:run

# Correr el frontend: 
financial-recommendations = 
npm install 
npm run dev

