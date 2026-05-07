# 🐾 宠物服务后端 API

基于 **Spring Boot 3.2.5** + **MyBatis-Plus** + **MySQL** 的宠物服务 RESTful API，为微信小程序提供全栈后端支持。

## 📦 技术栈

| 组件 | 版本 |
|------|------|
| Spring Boot | 3.2.5 |
| MyBatis-Plus | 3.5.6 |
| MySQL | 8.0+ |
| JWT | jjwt 0.12.5 |
| Java | 17 |

## 🚀 快速启动

### 1. 数据库初始化

```bash
mysql -u root -p < src/main/resources/db/schema.sql
mysql -u root -p < src/main/resources/db/data.sql
```

### 2. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pet_service?...
    username: root
    password: your_password
```

### 3. 启动服务

```bash
mvn spring-boot:run
```

服务默认端口：**9999**

## 🔌 API 概览

| 模块 | 路径前缀 | 说明 |
|------|---------|------|
| 👤 用户 | `/api/user` | 登录/注册(JWT) |
| 🐾 宠物 | `/api/pet` | 宠物档案CRUD |
| 🏪 商家 | `/api/merchant` | 附近查询(Haversine)、详情、日历 |
| 🛒 商城 | `/api` | 商品、购物车、订单 |
| 📝 预约 | `/api/booking` | 创建/取消预约、容量校验 |
| ⭐ 评价 | `/api/review` | 商家/商品评价 |
| 📍 地址 | `/api/address` | 收货地址管理 |
| 💰 支付 | `/api/pay` | 模拟微信支付回调 |
| 🔍 搜索 | `/api/search` | 统一搜索 |
| ⚙️ 管理 | `/api/merchant/admin` | 商家后台(11个端点) |

### JWT 白名单 (无需登录)

```
/api/user/login, /api/user/register,
/api/merchant/nearby, /api/merchant/*,
/api/products, /api/product/*,
/api/review/list, /api/review/merchant/*,
/api/search, /api/pay/wx/callback
```

## 📂 项目结构

```
c_W_h_d/
├── src/main/java/org/example/
│   ├── common/          # 通用类(Result/异常)
│   ├── config/          # 配置(JWT/Web/MyBatisPlus)
│   ├── controller/      # 11个控制器
│   ├── entity/          # 13个实体类
│   ├── mapper/          # 14个Mapper接口
│   └── service/         # 8个业务服务
└── src/main/resources/
    ├── application.yml  # 主配置
    └── db/              # 建表&测试数据SQL
```

## 🔗 关联项目

- 微信小程序前端：[pet-service-miniapp](https://github.com/gudu3280/c_w_x_c_x_q_d)
