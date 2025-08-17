-- Financial Recommendations Engine Database Schema
-- PostgreSQL Database Setup

-- Create database (run this separately if needed)
-- CREATE DATABASE financial_recommendations;

-- Users table with risk profiles
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User financial profiles
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    risk_tolerance VARCHAR(20) CHECK (risk_tolerance IN ('CONSERVATIVE', 'MODERATE', 'AGGRESSIVE')) NOT NULL,
    investment_experience VARCHAR(20) CHECK (investment_experience IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')) NOT NULL,
    investment_horizon INTEGER NOT NULL, -- in years
    monthly_income DECIMAL(15,2),
    monthly_expenses DECIMAL(15,2),
    current_savings DECIMAL(15,2),
    investment_goals TEXT[],
    age INTEGER,
    employment_status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Financial instruments (stocks, bonds, funds, etc.)
CREATE TABLE IF NOT EXISTS financial_instruments (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    instrument_type VARCHAR(50) CHECK (instrument_type IN ('STOCK', 'BOND', 'ETF', 'MUTUAL_FUND', 'CRYPTO', 'COMMODITY')) NOT NULL,
    sector VARCHAR(100),
    market VARCHAR(50),
    currency VARCHAR(3) DEFAULT 'USD',
    risk_level VARCHAR(20) CHECK (risk_level IN ('LOW', 'MEDIUM', 'HIGH')) NOT NULL,
    expense_ratio DECIMAL(5,4), -- for funds
    dividend_yield DECIMAL(5,4),
    market_cap BIGINT,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Historical performance data
CREATE TABLE IF NOT EXISTS instrument_performance (
    id BIGSERIAL PRIMARY KEY,
    instrument_id BIGINT REFERENCES financial_instruments(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    open_price DECIMAL(15,4),
    close_price DECIMAL(15,4),
    high_price DECIMAL(15,4),
    low_price DECIMAL(15,4),
    volume BIGINT,
    adjusted_close DECIMAL(15,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(instrument_id, date)
);

-- User portfolios
CREATE TABLE IF NOT EXISTS portfolios (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    total_value DECIMAL(15,2) DEFAULT 0,
    cash_balance DECIMAL(15,2) DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Portfolio holdings
CREATE TABLE IF NOT EXISTS portfolio_holdings (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT REFERENCES portfolios(id) ON DELETE CASCADE,
    instrument_id BIGINT REFERENCES financial_instruments(id) ON DELETE CASCADE,
    quantity DECIMAL(15,6) NOT NULL,
    average_cost DECIMAL(15,4) NOT NULL,
    current_price DECIMAL(15,4),
    market_value DECIMAL(15,2),
    unrealized_gain_loss DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(portfolio_id, instrument_id)
);

-- Investment recommendations
CREATE TABLE IF NOT EXISTS recommendations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    recommendation_type VARCHAR(50) CHECK (recommendation_type IN ('BUY', 'SELL', 'HOLD', 'REBALANCE')) NOT NULL,
    status VARCHAR(20) CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'EXPIRED')) DEFAULT 'PENDING',
    confidence_score DECIMAL(3,2) CHECK (confidence_score >= 0 AND confidence_score <= 1),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    reasoning TEXT NOT NULL,
    expected_return DECIMAL(5,2),
    risk_assessment TEXT,
    time_horizon INTEGER, -- in days
    priority INTEGER DEFAULT 1,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Recommended instruments for each recommendation
CREATE TABLE IF NOT EXISTS recommendation_instruments (
    id BIGSERIAL PRIMARY KEY,
    recommendation_id BIGINT REFERENCES recommendations(id) ON DELETE CASCADE,
    instrument_id BIGINT REFERENCES financial_instruments(id) ON DELETE CASCADE,
    allocation_percentage DECIMAL(5,2) NOT NULL,
    target_amount DECIMAL(15,2),
    current_amount DECIMAL(15,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User transactions
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    portfolio_id BIGINT REFERENCES portfolios(id) ON DELETE CASCADE,
    instrument_id BIGINT REFERENCES financial_instruments(id) ON DELETE CASCADE,
    recommendation_id BIGINT REFERENCES recommendations(id) ON DELETE SET NULL,
    transaction_type VARCHAR(20) CHECK (transaction_type IN ('BUY', 'SELL', 'DIVIDEND', 'DEPOSIT', 'WITHDRAWAL')) NOT NULL,
    quantity DECIMAL(15,6),
    price DECIMAL(15,4),
    total_amount DECIMAL(15,2) NOT NULL,
    fees DECIMAL(15,2) DEFAULT 0,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('PENDING', 'COMPLETED', 'CANCELLED', 'FAILED')) DEFAULT 'PENDING',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Market indices for benchmarking
CREATE TABLE IF NOT EXISTS market_indices (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index performance data
CREATE TABLE IF NOT EXISTS index_performance (
    id BIGSERIAL PRIMARY KEY,
    index_id BIGINT REFERENCES market_indices(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    value DECIMAL(15,4) NOT NULL,
    change_percent DECIMAL(5,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(index_id, date)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_user_profiles_user_id ON user_profiles(user_id);
CREATE INDEX IF NOT EXISTS idx_financial_instruments_symbol ON financial_instruments(symbol);
CREATE INDEX IF NOT EXISTS idx_financial_instruments_type ON financial_instruments(instrument_type);
CREATE INDEX IF NOT EXISTS idx_instrument_performance_instrument_date ON instrument_performance(instrument_id, date);
CREATE INDEX IF NOT EXISTS idx_portfolios_user_id ON portfolios(user_id);
CREATE INDEX IF NOT EXISTS idx_portfolio_holdings_portfolio_id ON portfolio_holdings(portfolio_id);
CREATE INDEX IF NOT EXISTS idx_recommendations_user_id ON recommendations(user_id);
CREATE INDEX IF NOT EXISTS idx_recommendations_status ON recommendations(status);
CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_portfolio_id ON transactions(portfolio_id);
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(transaction_date);

-- Create updated_at trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply updated_at triggers
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_user_profiles_updated_at BEFORE UPDATE ON user_profiles FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_financial_instruments_updated_at BEFORE UPDATE ON financial_instruments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_portfolios_updated_at BEFORE UPDATE ON portfolios FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_portfolio_holdings_updated_at BEFORE UPDATE ON portfolio_holdings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_recommendations_updated_at BEFORE UPDATE ON recommendations FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
