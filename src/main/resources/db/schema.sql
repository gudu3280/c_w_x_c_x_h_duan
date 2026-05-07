-- 宠物服务小程序数据库
CREATE DATABASE IF NOT EXISTS pet_service DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pet_service;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `openid` VARCHAR(64) DEFAULT NULL COMMENT '微信openid',
    `mobile` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar_url` VARCHAR(500) DEFAULT NULL COMMENT '头像',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 0未知 1男 2女',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`),
    UNIQUE KEY `uk_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户地址表
CREATE TABLE IF NOT EXISTS `user_address` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `name` VARCHAR(50) NOT NULL COMMENT '收货人',
    `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `province` VARCHAR(50) DEFAULT NULL COMMENT '省',
    `city` VARCHAR(50) DEFAULT NULL COMMENT '市',
    `district` VARCHAR(50) DEFAULT NULL COMMENT '区',
    `detail` VARCHAR(200) NOT NULL COMMENT '详细地址',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认 0否 1是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址表';

-- 宠物档案表
CREATE TABLE IF NOT EXISTS `pet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `name` VARCHAR(50) NOT NULL COMMENT '宠物昵称',
    `breed` VARCHAR(50) DEFAULT NULL COMMENT '品种',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 0未知 1公 2母',
    `birthday` DATE DEFAULT NULL COMMENT '出生日期',
    `weight` DECIMAL(5,2) DEFAULT NULL COMMENT '体重(kg)',
    `is_neutered` TINYINT DEFAULT 0 COMMENT '是否绝育 0否 1是',
    `vaccine_record` JSON DEFAULT NULL COMMENT '疫苗记录JSON',
    `medical_history` TEXT DEFAULT NULL COMMENT '病史/过敏史',
    `habit` TEXT DEFAULT NULL COMMENT '生活习惯',
    `photo_url` VARCHAR(500) DEFAULT NULL COMMENT '宠物照片',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物档案表';

-- 商家表
CREATE TABLE IF NOT EXISTS `merchant` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '商家名称',
    `type` VARCHAR(20) NOT NULL COMMENT '类型: shop/family',
    `address` VARCHAR(200) NOT NULL COMMENT '详细地址',
    `lng` DECIMAL(10,6) DEFAULT NULL COMMENT '经度',
    `lat` DECIMAL(10,6) DEFAULT NULL COMMENT '纬度',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `business_hours` VARCHAR(100) DEFAULT NULL COMMENT '营业时间',
    `description` TEXT DEFAULT NULL COMMENT '描述',
    `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图',
    `images` JSON DEFAULT NULL COMMENT '环境照片JSON数组',
    `tags` VARCHAR(200) DEFAULT NULL COMMENT '标签,逗号分隔',
    `avg_score` DECIMAL(2,1) DEFAULT 5.0 COMMENT '平均评分',
    `review_count` INT DEFAULT 0 COMMENT '评价数量',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_lng_lat` (`lng`, `lat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家表';

-- 寄养服务设置
CREATE TABLE IF NOT EXISTS `boarding_setting` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `merchant_id` BIGINT NOT NULL COMMENT '商家ID',
    `small_dog_price` DECIMAL(10,2) DEFAULT NULL COMMENT '小型犬价格/天',
    `medium_dog_price` DECIMAL(10,2) DEFAULT NULL COMMENT '中型犬价格/天',
    `big_dog_price` DECIMAL(10,2) DEFAULT NULL COMMENT '大型犬价格/天',
    `cat_price` DECIMAL(10,2) DEFAULT NULL COMMENT '猫咪价格/天',
    `max_capacity_per_day` INT DEFAULT 5 COMMENT '每日最大接养数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_merchant_id` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='寄养服务设置';

-- 美容服务项
CREATE TABLE IF NOT EXISTS `grooming_service` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `merchant_id` BIGINT NOT NULL COMMENT '商家ID',
    `service_name` VARCHAR(100) NOT NULL COMMENT '服务名称',
    `duration` INT DEFAULT 60 COMMENT '时长(分钟)',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_merchant_id` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美容服务项';

-- 可预约日历
CREATE TABLE IF NOT EXISTS `service_calendar` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `merchant_id` BIGINT NOT NULL COMMENT '商家ID',
    `service_date` DATE NOT NULL COMMENT '服务日期',
    `total_capacity` INT DEFAULT 5 COMMENT '总容量',
    `used_capacity` INT DEFAULT 0 COMMENT '已用容量',
    `is_closed` TINYINT DEFAULT 0 COMMENT '是否休息 0否 1是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_merchant_date` (`merchant_id`, `service_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='可预约日历';

-- 预约订单表
CREATE TABLE IF NOT EXISTS `order_booking` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `merchant_id` BIGINT NOT NULL COMMENT '商家ID',
    `pet_id` BIGINT NOT NULL COMMENT '宠物ID',
    `order_type` VARCHAR(20) NOT NULL COMMENT '类型: boarding/grooming',
    `start_date` DATE NOT NULL COMMENT '开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '结束日期(寄养)',
    `grooming_service_id` BIGINT DEFAULT NULL COMMENT '美容服务ID',
    `time_slot` VARCHAR(20) DEFAULT NULL COMMENT '时间段(美容)',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
    `status` VARCHAR(20) DEFAULT 'pending_pay' COMMENT '状态: pending_pay/paid/in_service/completed/cancelled',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '附加需求备注',
    `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_merchant_id` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约订单表';

-- 商品表
CREATE TABLE IF NOT EXISTS `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    `category` VARCHAR(50) NOT NULL COMMENT '分类: dog_food/snack/toy/wash/other',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
    `stock` INT DEFAULT 0 COMMENT '库存',
    `main_image` VARCHAR(500) DEFAULT NULL COMMENT '主图',
    `images` JSON DEFAULT NULL COMMENT '轮播图JSON数组',
    `description` TEXT DEFAULT NULL COMMENT '详细描述',
    `sales_volume` INT DEFAULT 0 COMMENT '销量',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 购物车表
CREATE TABLE IF NOT EXISTS `cart` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `quantity` INT DEFAULT 1 COMMENT '数量',
    `selected` TINYINT DEFAULT 1 COMMENT '是否选中',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 商城订单表
CREATE TABLE IF NOT EXISTS `order_mall` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
    `status` VARCHAR(20) DEFAULT 'pending_pay' COMMENT '状态: pending_pay/paid/shipped/completed/cancelled',
    `address_json` JSON DEFAULT NULL COMMENT '收货地址快照',
    `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `ship_time` DATETIME DEFAULT NULL COMMENT '发货时间',
    `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商城订单表';

-- 订单商品关联表
CREATE TABLE IF NOT EXISTS `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(100) DEFAULT NULL COMMENT '商品名称快照',
    `product_image` VARCHAR(500) DEFAULT NULL COMMENT '商品图片快照',
    `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `quantity` INT NOT NULL COMMENT '数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品关联表';

-- 评价表
CREATE TABLE IF NOT EXISTS `review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `target_type` VARCHAR(20) NOT NULL COMMENT '评价类型: booking/product',
    `target_id` BIGINT NOT NULL COMMENT '目标ID(订单ID或商品ID)',
    `merchant_id` BIGINT DEFAULT NULL COMMENT '商家ID(预约评价时)',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `score` TINYINT NOT NULL COMMENT '评分 1-5',
    `content` TEXT DEFAULT NULL COMMENT '评价内容',
    `images` JSON DEFAULT NULL COMMENT '评价图片JSON数组',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_target` (`target_type`, `target_id`),
    KEY `idx_merchant_id` (`merchant_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';
