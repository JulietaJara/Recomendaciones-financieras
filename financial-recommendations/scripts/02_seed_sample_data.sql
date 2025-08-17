-- Sample data for Financial Recommendations Engine
-- This script populates the database with sample data for testing

-- Insert sample market indices
INSERT INTO market_indices (symbol, name, description) VALUES
('SPY', 'S&P 500 ETF', 'SPDR S&P 500 ETF Trust tracking the S&P 500 index'),
('QQQ', 'NASDAQ-100 ETF', 'Invesco QQQ Trust tracking the NASDAQ-100 index'),
('VTI', 'Total Stock Market ETF', 'Vanguard Total Stock Market ETF'),
('BND', 'Total Bond Market ETF', 'Vanguard Total Bond Market ETF');

-- Insert sample financial instruments
INSERT INTO financial_instruments (symbol, name, instrument_type, sector, market, risk_level, expense_ratio, dividend_yield, market_cap, description) VALUES
-- Stocks
('AAPL', 'Apple Inc.', 'STOCK', 'Technology', 'NASDAQ', 'MEDIUM', NULL, 0.0044, 3000000000000, 'Technology company specializing in consumer electronics'),
('MSFT', 'Microsoft Corporation', 'STOCK', 'Technology', 'NASDAQ', 'MEDIUM', NULL, 0.0068, 2800000000000, 'Technology corporation developing software and cloud services'),
('GOOGL', 'Alphabet Inc.', 'STOCK', 'Technology', 'NASDAQ', 'MEDIUM', NULL, 0.0000, 1700000000000, 'Technology conglomerate specializing in internet services'),
('AMZN', 'Amazon.com Inc.', 'STOCK', 'Consumer Discretionary', 'NASDAQ', 'HIGH', NULL, 0.0000, 1500000000000, 'E-commerce and cloud computing company'),
('TSLA', 'Tesla Inc.', 'STOCK', 'Consumer Discretionary', 'NASDAQ', 'HIGH', NULL, 0.0000, 800000000000, 'Electric vehicle and clean energy company'),
('JNJ', 'Johnson & Johnson', 'STOCK', 'Healthcare', 'NYSE', 'LOW', NULL, 0.0290, 450000000000, 'Pharmaceutical and consumer goods company'),
('JPM', 'JPMorgan Chase & Co.', 'STOCK', 'Financial Services', 'NYSE', 'MEDIUM', NULL, 0.0250, 500000000000, 'Investment banking and financial services'),
('V', 'Visa Inc.', 'STOCK', 'Financial Services', 'NYSE', 'MEDIUM', NULL, 0.0070, 450000000000, 'Payment technology company'),

-- ETFs
('SPY', 'SPDR S&P 500 ETF', 'ETF', 'Diversified', 'NYSE', 'MEDIUM', 0.0945, 0.0130, NULL, 'ETF tracking the S&P 500 index'),
('QQQ', 'Invesco QQQ ETF', 'ETF', 'Technology', 'NASDAQ', 'MEDIUM', 0.0020, 0.0050, NULL, 'ETF tracking the NASDAQ-100 index'),
('VTI', 'Vanguard Total Stock Market ETF', 'ETF', 'Diversified', 'NYSE', 'MEDIUM', 0.0003, 0.0140, NULL, 'ETF tracking the entire US stock market'),
('BND', 'Vanguard Total Bond Market ETF', 'ETF', 'Fixed Income', 'NYSE', 'LOW', 0.0003, 0.0210, NULL, 'ETF tracking the US bond market'),
('VEA', 'Vanguard FTSE Developed Markets ETF', 'ETF', 'International', 'NYSE', 'MEDIUM', 0.0005, 0.0280, NULL, 'ETF tracking developed international markets'),
('VWO', 'Vanguard FTSE Emerging Markets ETF', 'ETF', 'Emerging Markets', 'NYSE', 'HIGH', 0.0008, 0.0320, NULL, 'ETF tracking emerging markets'),

-- Bonds
('TLT', 'iShares 20+ Year Treasury Bond ETF', 'ETF', 'Fixed Income', 'NYSE', 'LOW', 0.0015, 0.0180, NULL, 'ETF tracking long-term US Treasury bonds'),
('IEF', 'iShares 7-10 Year Treasury Bond ETF', 'ETF', 'Fixed Income', 'NYSE', 'LOW', 0.0015, 0.0190, NULL, 'ETF tracking intermediate-term US Treasury bonds'),

-- Commodities
('GLD', 'SPDR Gold Shares', 'ETF', 'Commodities', 'NYSE', 'MEDIUM', 0.0040, 0.0000, NULL, 'ETF tracking gold prices'),
('SLV', 'iShares Silver Trust', 'ETF', 'Commodities', 'NYSE', 'HIGH', 0.0050, 0.0000, NULL, 'ETF tracking silver prices');

-- Insert sample historical performance data (last 30 days for SPY)
INSERT INTO instrument_performance (instrument_id, date, open_price, close_price, high_price, low_price, volume, adjusted_close)
SELECT 
    (SELECT id FROM financial_instruments WHERE symbol = 'SPY'),
    CURRENT_DATE - INTERVAL '1 day' * generate_series(0, 29),
    450 + (random() * 20 - 10), -- open_price
    450 + (random() * 20 - 10), -- close_price  
    460 + (random() * 15 - 7.5), -- high_price
    440 + (random() * 15 - 7.5), -- low_price
    50000000 + (random() * 20000000)::BIGINT, -- volume
    450 + (random() * 20 - 10) -- adjusted_close
FROM generate_series(0, 29);

-- Insert sample users
INSERT INTO users (email, password_hash, first_name, last_name, date_of_birth, phone) VALUES
('juan.perez@email.com', '$2a$10$example.hash.here', 'Juan', 'Pérez', '1985-03-15', '+1234567890'),
('maria.garcia@email.com', '$2a$10$example.hash.here', 'María', 'García', '1990-07-22', '+1234567891'),
('carlos.rodriguez@email.com', '$2a$10$example.hash.here', 'Carlos', 'Rodríguez', '1978-11-08', '+1234567892'),
('ana.martinez@email.com', '$2a$10$example.hash.here', 'Ana', 'Martínez', '1992-05-30', '+1234567893');

-- Insert sample user profiles
INSERT INTO user_profiles (user_id, risk_tolerance, investment_experience, investment_horizon, monthly_income, monthly_expenses, current_savings, investment_goals, age, employment_status) VALUES
(1, 'MODERATE', 'INTERMEDIATE', 10, 5000.00, 3500.00, 25000.00, ARRAY['RETIREMENT', 'WEALTH_BUILDING'], 39, 'EMPLOYED'),
(2, 'CONSERVATIVE', 'BEGINNER', 15, 4000.00, 2800.00, 15000.00, ARRAY['RETIREMENT', 'EMERGENCY_FUND'], 34, 'EMPLOYED'),
(3, 'AGGRESSIVE', 'ADVANCED', 20, 8000.00, 4500.00, 75000.00, ARRAY['WEALTH_BUILDING', 'EARLY_RETIREMENT'], 46, 'EMPLOYED'),
(4, 'MODERATE', 'INTERMEDIATE', 8, 3500.00, 2200.00, 12000.00, ARRAY['HOME_PURCHASE', 'EDUCATION'], 32, 'EMPLOYED');

-- Insert sample portfolios
INSERT INTO portfolios (user_id, name, description, total_value, cash_balance) VALUES
(1, 'Portfolio Principal', 'Cartera de inversión principal de Juan', 25000.00, 2500.00),
(2, 'Cartera Conservadora', 'Inversiones conservadoras para María', 15000.00, 1500.00),
(3, 'Portfolio Agresivo', 'Cartera de alto crecimiento de Carlos', 75000.00, 5000.00),
(4, 'Inversiones Ana', 'Portfolio diversificado de Ana', 12000.00, 1000.00);

-- Insert sample portfolio holdings
INSERT INTO portfolio_holdings (portfolio_id, instrument_id, quantity, average_cost, current_price, market_value, unrealized_gain_loss) VALUES
-- Juan's portfolio
(1, (SELECT id FROM financial_instruments WHERE symbol = 'SPY'), 25, 440.00, 450.00, 11250.00, 250.00),
(1, (SELECT id FROM financial_instruments WHERE symbol = 'BND'), 50, 75.00, 76.00, 3800.00, 50.00),
(1, (SELECT id FROM financial_instruments WHERE symbol = 'AAPL'), 30, 180.00, 185.00, 5550.00, 150.00),
(1, (SELECT id FROM financial_instruments WHERE symbol = 'VTI'), 15, 220.00, 225.00, 3375.00, 75.00),

-- María's portfolio  
(2, (SELECT id FROM financial_instruments WHERE symbol = 'BND'), 80, 75.00, 76.00, 6080.00, 80.00),
(2, (SELECT id FROM financial_instruments WHERE symbol = 'SPY'), 15, 440.00, 450.00, 6750.00, 150.00),
(2, (SELECT id FROM financial_instruments WHERE symbol = 'JNJ'), 20, 160.00, 165.00, 3300.00, 100.00),

-- Carlos's portfolio
(3, (SELECT id FROM financial_instruments WHERE symbol = 'QQQ'), 50, 350.00, 360.00, 18000.00, 500.00),
(3, (SELECT id FROM financial_instruments WHERE symbol = 'TSLA'), 40, 250.00, 260.00, 10400.00, 400.00),
(3, (SELECT id FROM financial_instruments WHERE symbol = 'GOOGL'), 25, 140.00, 145.00, 3625.00, 125.00),
(3, (SELECT id FROM financial_instruments WHERE symbol = 'AMZN'), 30, 150.00, 155.00, 4650.00, 150.00),
(3, (SELECT id FROM financial_instruments WHERE symbol = 'VEA'), 100, 45.00, 47.00, 4700.00, 200.00),

-- Ana's portfolio
(4, (SELECT id FROM financial_instruments WHERE symbol = 'VTI'), 25, 220.00, 225.00, 5625.00, 125.00),
(4, (SELECT id FROM financial_instruments WHERE symbol = 'BND'), 30, 75.00, 76.00, 2280.00, 30.00),
(4, (SELECT id FROM financial_instruments WHERE symbol = 'VEA'), 40, 45.00, 47.00, 1880.00, 80.00),
(4, (SELECT id FROM financial_instruments WHERE symbol = 'MSFT'), 10, 320.00, 330.00, 3300.00, 100.00);

-- Insert sample recommendations
INSERT INTO recommendations (user_id, recommendation_type, title, description, reasoning, expected_return, risk_assessment, time_horizon, confidence_score, priority) VALUES
(1, 'BUY', 'Incrementar posición en tecnología', 
 'Recomendamos aumentar la exposición al sector tecnológico mediante QQQ', 
 'Basado en tu perfil de riesgo moderado y horizonte de 10 años, el sector tecnológico ofrece buen potencial de crecimiento. Tu cartera actual tiene baja exposición a tech.',
 8.5, 'Riesgo medio-alto debido a la volatilidad del sector tecnológico', 365, 0.78, 1),

(2, 'REBALANCE', 'Rebalancear hacia más diversificación',
 'Tu cartera está muy concentrada en bonos. Sugerimos diversificar con acciones internacionales',
 'Con 15 años de horizonte de inversión, puedes permitirte más riesgo. VEA ofrece diversificación internacional con riesgo controlado.',
 6.2, 'Riesgo bajo-medio con diversificación internacional', 180, 0.85, 2),

(3, 'SELL', 'Reducir concentración en Tesla',
 'Tu posición en TSLA representa un riesgo de concentración alto',
 'TSLA representa más del 15% de tu cartera. Para un perfil agresivo, recomendamos no superar el 10% en una sola acción.',
 NULL, 'Reducción de riesgo de concentración', 90, 0.92, 1),

(4, 'BUY', 'Agregar exposición a mercados emergentes',
 'Considera agregar VWO para diversificación en mercados emergentes',
 'Tu cartera está bien diversificada pero carece de exposición a mercados emergentes. Con 8 años de horizonte, puedes beneficiarte del crecimiento de estos mercados.',
 9.2, 'Riesgo alto pero con potencial de retorno superior', 270, 0.72, 3);

-- Insert recommendation instruments
INSERT INTO recommendation_instruments (recommendation_id, instrument_id, allocation_percentage, target_amount) VALUES
(1, (SELECT id FROM financial_instruments WHERE symbol = 'QQQ'), 100.00, 2500.00),
(2, (SELECT id FROM financial_instruments WHERE symbol = 'VEA'), 60.00, 1800.00),
(2, (SELECT id FROM financial_instruments WHERE symbol = 'SPY'), 40.00, 1200.00),
(3, (SELECT id FROM financial_instruments WHERE symbol = 'TSLA'), -50.00, -5200.00), -- Sell half position
(4, (SELECT id FROM financial_instruments WHERE symbol = 'VWO'), 100.00, 1500.00);

-- Insert sample transactions
INSERT INTO transactions (user_id, portfolio_id, instrument_id, transaction_type, quantity, price, total_amount, fees, status) VALUES
(1, 1, (SELECT id FROM financial_instruments WHERE symbol = 'SPY'), 'BUY', 25, 440.00, 11000.00, 9.99, 'COMPLETED'),
(1, 1, (SELECT id FROM financial_instruments WHERE symbol = 'AAPL'), 'BUY', 30, 180.00, 5400.00, 9.99, 'COMPLETED'),
(2, 2, (SELECT id FROM financial_instruments WHERE symbol = 'BND'), 'BUY', 80, 75.00, 6000.00, 9.99, 'COMPLETED'),
(3, 3, (SELECT id FROM financial_instruments WHERE symbol = 'TSLA'), 'BUY', 40, 250.00, 10000.00, 9.99, 'COMPLETED'),
(4, 4, (SELECT id FROM financial_instruments WHERE symbol = 'VTI'), 'BUY', 25, 220.00, 5500.00, 9.99, 'COMPLETED');
