USE pet_service;

-- 模拟用户数据
INSERT INTO `user` (`id`, `openid`, `mobile`, `nickname`, `avatar_url`) VALUES
(1, 'mock_openid_001', '13800138001', '小明', 'https://via.placeholder.com/100'),
(2, 'mock_openid_002', '13800138002', '小红', 'https://via.placeholder.com/100');

-- 模拟商家数据（寄养）
INSERT INTO `merchant` (`id`, `name`, `type`, `address`, `lng`, `lat`, `phone`, `business_hours`, `description`, `cover_image`, `tags`, `avg_score`, `review_count`) VALUES
(1, '温馨宠物之家', 'family', '北京市朝阳区望京街道10号', 116.480053, 39.996062, '13900001111', '08:00-20:00', '家庭式寄养，24小时监控，每日遛狗3次，提供宠物直播', 'https://via.placeholder.com/400x300', '24h监控,家庭式寄养,每日直播', 4.8, 56),
(2, '爱宠寄养中心', 'shop', '北京市海淀区中关村大街15号', 116.326584, 39.991754, '13900002222', '07:00-22:00', '专业寄养连锁品牌，独立空间，专业护理团队', 'https://via.placeholder.com/400x300', '专业团队,独立空间,多年经验', 4.6, 128),
(3, '汪星人乐园', 'shop', '北京市西城区金融街8号', 116.366794, 39.912042, '13900003333', '09:00-21:00', '集寄养和美容于一体的综合宠物服务中心', 'https://via.placeholder.com/400x300', '综合服务,环境优雅,交通便利', 4.9, 89);

-- 模拟商家数据（美容）
INSERT INTO `merchant` (`id`, `name`, `type`, `address`, `lng`, `lat`, `phone`, `business_hours`, `description`, `cover_image`, `tags`, `avg_score`, `review_count`) VALUES
(4, '靓宠美容SPA', 'shop', '北京市朝阳区三里屯路5号', 116.454821, 39.937642, '13900004444', '10:00-20:00', '高端宠物美容SPA，采用进口洗护产品', 'https://via.placeholder.com/400x300', '高端SPA,进口产品,造型设计', 4.7, 203),
(5, '萌宠造型屋', 'family', '北京市朝阳区建外大街20号', 116.461234, 39.908765, '13900005555', '09:00-19:00', '专注宠物造型设计，网红宠物打卡地', 'https://via.placeholder.com/400x300', '网红造型,温柔手法,性价比高', 4.5, 67);

-- 寄养服务设置
INSERT INTO `boarding_setting` (`merchant_id`, `small_dog_price`, `medium_dog_price`, `big_dog_price`, `cat_price`, `max_capacity_per_day`) VALUES
(1, 80.00, 100.00, 130.00, 70.00, 5),
(2, 100.00, 130.00, 160.00, 90.00, 10),
(3, 90.00, 120.00, 150.00, 80.00, 8);

-- 美容服务项
INSERT INTO `grooming_service` (`merchant_id`, `service_name`, `duration`, `price`, `description`) VALUES
(3, '基础洗浴', 45, 68.00, '专业洗浴+吹干+梳毛'),
(3, '精致美容', 90, 158.00, '洗浴+造型修剪+耳朵清洁+指甲修剪'),
(4, '基础洗浴', 40, 88.00, '进口洗护产品，深层清洁'),
(4, '全身SPA', 120, 268.00, '精油SPA+深层护理+造型设计'),
(4, '造型设计', 60, 188.00, '日韩流行造型设计'),
(5, '基础洗浴', 45, 58.00, '温和洗护，适合敏感肌肤'),
(5, '网红造型', 90, 128.00, '潮流造型+拍照服务');

-- 商品数据
INSERT INTO `product` (`id`, `name`, `category`, `price`, `original_price`, `stock`, `main_image`, `description`, `sales_volume`) VALUES
(1, '皇家小型犬成犬粮2kg', 'dog_food', 138.00, 168.00, 200, 'https://via.placeholder.com/400x400', '皇家专为小型犬研发，易消化配方，美毛护肤', 1523),
(2, '渴望六种鱼犬粮2kg', 'dog_food', 258.00, 298.00, 150, 'https://via.placeholder.com/400x400', '加拿大进口，六种新鲜鱼肉，高蛋白低碳水', 892),
(3, '冻干鸡肉粒100g', 'snack', 35.00, 45.00, 500, 'https://via.placeholder.com/400x400', '100%纯鸡胸肉冻干，无添加，训练奖励首选', 3201),
(4, '磨牙洁齿棒(10支装)', 'snack', 28.00, 38.00, 300, 'https://via.placeholder.com/400x400', '天然牛皮制作，有效清洁牙齿，去除口臭', 2156),
(5, '发声毛绒玩具球', 'toy', 19.90, 29.90, 800, 'https://via.placeholder.com/400x400', '内置发声器，激发宠物玩耍兴趣，面料安全无毒', 4521),
(6, '智能漏食球', 'toy', 45.00, 65.00, 200, 'https://via.placeholder.com/400x400', '可调节难度，锻炼宠物智力，减少拆家行为', 1876),
(7, '宠物香波500ml(通用型)', 'wash', 49.00, 69.00, 400, 'https://via.placeholder.com/400x400', '温和无刺激，适合各种毛发，持久留香', 2789),
(8, '除臭喷雾350ml', 'wash', 39.00, 55.00, 350, 'https://via.placeholder.com/400x400', '植物配方，快速分解异味分子，安全无残留', 1654),
(9, '自动饮水机2L', 'other', 89.00, 129.00, 150, 'https://via.placeholder.com/400x400', '循环过滤，超静音水泵，大容量适合多宠家庭', 987),
(10, '宠物GPS定位器', 'other', 199.00, 299.00, 100, 'https://via.placeholder.com/400x400', '实时定位，电子围栏，超长续航30天', 562);

-- 可预约日历（未来7天数据）
INSERT INTO `service_calendar` (`merchant_id`, `service_date`, `total_capacity`, `used_capacity`, `is_closed`) VALUES
(1, CURDATE(), 5, 2, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 5, 1, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 5, 3, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 5, 0, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 5, 4, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 5, 0, 1),
(1, DATE_ADD(CURDATE(), INTERVAL 6 DAY), 5, 0, 0),
(2, CURDATE(), 10, 5, 0),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 10, 3, 0),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 10, 7, 0),
(2, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 10, 2, 0),
(3, CURDATE(), 8, 4, 0),
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 8, 6, 0),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 8, 2, 0);
