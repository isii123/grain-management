-- 智能粮食管理系统 - 数据库建表脚本

-- 创建用户表
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建客户表
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100) UNIQUE NOT NULL,
    contact_person VARCHAR(50),
    phone VARCHAR(20),
    address VARCHAR(255),
    remark TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建农作物类型表
CREATE TABLE crop_types (
    id SERIAL PRIMARY KEY,
    crop_name VARCHAR(50) UNIQUE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

-- 创建价格历史表
CREATE TABLE price_history (
    id SERIAL PRIMARY KEY,
    crop_type_id INTEGER NOT NULL,
    price_per_kg DECIMAL(10,2) NOT NULL,
    effective_date DATE NOT NULL,
    expiry_date DATE,
    is_current BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (crop_type_id) REFERENCES crop_types(id)
);

-- 创建订单表
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    customer_id INTEGER NOT NULL,
    order_date DATE NOT NULL,
    total_vehicles INTEGER DEFAULT 0,
    total_net_weight DECIMAL(12,2) DEFAULT 0,
    total_amount DECIMAL(12,2) DEFAULT 0,
    avg_price DECIMAL(10,2) DEFAULT 0,
    remark TEXT,
    status VARCHAR(20) DEFAULT 'active',
    created_by INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- 创建订单明细表
CREATE TABLE order_details (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    vehicle_no VARCHAR(20) NOT NULL,
    gross_weight DECIMAL(10,2) NOT NULL,
    tare_weight DECIMAL(10,2) NOT NULL,
    net_weight DECIMAL(10,2) NOT NULL,
    crop_type_id INTEGER NOT NULL,
    price_per_kg DECIMAL(10,2) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    remark TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (crop_type_id) REFERENCES crop_types(id)
);

-- 创建索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_customers_name ON customers(customer_name);
CREATE INDEX idx_orders_date ON orders(order_date);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_order_details_order ON order_details(order_id);
CREATE INDEX idx_price_history_crop ON price_history(crop_type_id);
CREATE INDEX idx_price_history_date ON price_history(effective_date);

-- 插入初始数据：农作物类型
INSERT INTO crop_types (crop_name) VALUES
('玉米'), ('小麦'), ('油菜籽');

-- 插入初始管理员用户（密码需要使用BCrypt加密）
-- 默认密码：admin123
INSERT INTO users (username, password, full_name, role) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBa.1fCj1Uhn8G', '系统管理员', 'admin');