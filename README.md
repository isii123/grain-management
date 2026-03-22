# 智能粮食管理系统

这是一个完整的智能粮食管理系统，包含后端Spring Boot服务和前端Vue.js应用。

## 项目结构

```
├── backend/                 # Spring Boot后端
├── frontend/               # Vue.js前端
├── database-design.md      # 数据库设计文档
├── schema.sql             # 数据库建表脚本
└── README.md              # 项目说明
```

## 功能特性

### ✅ 已实现功能
1. **用户管理**
   - 用户登录/注销
   - JWT认证
   - 权限控制（admin, manager, operator）

2. **交易录入**
   - 多客户订单同时录入
   - 支持关闭客户订单
   - 每客户多车粮食管理
   - 自动计算净重、金额
   - 单价缓存功能
   - 支持玉米、小麦、油菜籽

3. **统计看板**
   - 日/月/年/自定义时间统计
   - 总单数、总重量、总金额统计
   - 平均单价计算
   - 每日收购趋势图表

4. **数据明细**
   - 订单分页展示
   - 按日期、客户名称、金额搜索
   - 订单详情查看
   - 订单修改/删除

## 技术栈

### 后端
- Spring Boot 2.7.14
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL 13+
- Lombok

### 前端
- Vue.js 2.6.14
- Vue Router 3.5.4
- Vuex 3.6.2
- Element UI 2.15.13
- ECharts 5.3.3
- Axios 0.27.2

## 快速开始

### 1. 数据库准备

```bash
# 创建数据库
createdb grain_management

# 执行建表脚本
psql -U postgres -d grain_management -f schema.sql
```

### 2. 后端启动

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 运行

### 3. 前端启动

```bash
cd frontend
npm install
npm run serve
```

前端应用将在 http://localhost:8081 运行

## 默认账号

- **用户名**: admin
- **密码**: admin123
- **角色**: admin

## 待完善功能

1. **客户档案管理** - 需要实现客户信息维护
2. **价格历史管理** - 需要完善价格管理界面
3. **打印功能** - 需要实现单据打印模板
4. **数据导出** - 支持Excel导出
5. **高级统计** - 客户排名、农作物占比等

## API文档

系统使用RESTful API设计，主要接口包括：

- `/api/auth/login` - 用户登录
- `/api/auth/register` - 用户注册
- `/api/orders` - 订单管理
- `/api/customers` - 客户管理
- `/api/statistics` - 统计查询

## 开发计划

### 第一阶段 ✅ 基础框架
- [x] 数据库设计
- [x] Spring Boot后端基础架构
- [x] Vue.js前端基础框架
- [x] 用户认证系统

### 第二阶段 ✅ 核心功能
- [x] 交易录入页面
- [x] 统计看板页面
- [x] 数据明细页面

### 第三阶段 ⏳ 功能完善
- [ ] 客户档案管理
- [ ] 价格历史管理
- [ ] 打印功能
- [ ] 数据导出
- [ ] 高级统计报表

### 第四阶段 ⏳ 优化和部署
- [ ] 性能优化
- [ ] 响应式适配
- [ ] 部署文档

## 联系方式

如有问题或建议，请提交Issue或联系开发团队。