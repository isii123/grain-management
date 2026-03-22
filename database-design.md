# 智能粮食管理系统 - 数据库设计文档

## 数据库概述
系统采用PostgreSQL数据库，包含用户管理、客户管理、订单管理、价格历史等核心功能模块。

## 数据表设计

### 1. 用户表 (users)
存储系统用户信息，支持登录和权限管理。

| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | SERIAL | PRIMARY KEY | 用户ID，自增 |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 用户名 |
| password | VARCHAR(255) | NOT NULL | 密码（加密存储） |
| full_name | VARCHAR(100) | NOT NULL | 用户全名 |
| role | VARCHAR(20) | NOT NULL | 角色：admin, manager, operator |
| is_active | BOOLEAN | DEFAULT TRUE | 是否激活 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 2. 客户表 (customers)
存储客户基本信息。

| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | SERIAL | PRIMARY KEY | 客户ID，自增 |
| customer_name | VARCHAR(100) | UNIQUE, NOT NULL | 客户名称 |
| contact_person | VARCHAR(50) | | 联系人 |
| phone | VARCHAR(20) | | 联系电话 |
| address | VARCHAR(255) | | 地址 |
| remark | TEXT | | 备注 |
| is_active | BOOLEAN | DEFAULT TRUE | 是否激活 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 3. 农作物类型表 (crop_types)
存储农作物类型信息。

| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | SERIAL | PRIMARY KEY | 作物类型ID |
| crop_name | VARCHAR(50) | UNIQUE, NOT NULL | 作物名称（玉米、小麦、油菜籽） |
| is_active | BOOLEAN | DEFAULT TRUE | 是否激活 |

### 4. 价格历史表 (price_history)
存储不同农作物的价格历史记录。

| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | SERIAL | PRIMARY KEY | 价格记录ID |
| crop_type_id | INTEGER | FOREIGN KEY, NOT NULL | 作物类型ID |
| price_per_kg | DECIMAL(10,2) | NOT NULL | 单价（元/kg） |
| effective_date | DATE | NOT NULL | 生效日期 |
| expiry_date | DATE | | 失效日期 |
| is_current | BOOLEAN | DEFAULT TRUE | 是否当前有效价格 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 5. 订单表 (orders)
存储客户订单主信息。

| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | SERIAL | PRIMARY KEY | 订单ID，自增 |
| order_no | VARCHAR(50) | UNIQUE, NOT NULL | 订单编号 |
| customer_id | INTEGER | FOREIGN KEY, NOT NULL | 客户ID |
| order_date | DATE | NOT NULL | 订单日期 |
| total_vehicles | INTEGER | DEFAULT 0 | 总车数 |
| total_net_weight | DECIMAL(12,2) | DEFAULT 0 | 总净重（kg） |
| total_amount | DECIMAL(12,2) | DEFAULT 0 | 总金额（元） |
| avg_price | DECIMAL(10,2) | DEFAULT 0 | 平均单价 |
| remark | TEXT | | 备注 |
| status | VARCHAR(20) | DEFAULT 'active' | 状态：active, closed |
| created_by | INTEGER | FOREIGN KEY, NOT NULL | 创建用户ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 6. 订单明细表 (order_details)
存储订单的每车粮食详细信息。

| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | SERIAL | PRIMARY KEY | 明细ID，自增 |
| order_id | INTEGER | FOREIGN KEY, NOT NULL | 订单ID |
| vehicle_no | VARCHAR(20) | NOT NULL | 运输工具/车牌号 |
| gross_weight | DECIMAL(10,2) | NOT NULL | 毛重（kg） |
| tare_weight | DECIMAL(10,2) | NOT NULL | 皮重（kg） |
| net_weight | DECIMAL(10,2) | NOT NULL | 净重（kg） = 毛重 - 皮重 |
| crop_type_id | INTEGER | FOREIGN KEY, NOT NULL | 作物类型ID |
| price_per_kg | DECIMAL(10,2) | NOT NULL | 单价（元/kg） |
| amount | DECIMAL(12,2) | NOT NULL | 金额 = 净重 × 单价 |
| remark | TEXT | | 备注 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

## 索引设计

1. users表：username索引
2. customers表：customer_name索引
3. orders表：order_date索引、customer_id索引
4. order_details表：order_id索引
5. price_history表：crop_type_id索引、effective_date索引

## 初始数据

### 农作物类型初始数据
```sql
INSERT INTO crop_types (crop_name) VALUES
('玉米'), ('小麦'), ('油菜籽');
```

### 管理员用户初始数据
```sql
INSERT INTO users (username, password, full_name, role) VALUES
('admin', '$2a$10$YourEncryptedPassword', '系统管理员', 'admin');
```