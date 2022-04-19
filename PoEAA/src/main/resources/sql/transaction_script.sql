CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键，产品的ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品名称',
  `type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品类型',
  `add_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品列表';

CREATE TABLE `contracts` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键，合同ID',
  `product_id` int(11) NOT NULL COMMENT '产品ID',
  `revenue` decimal(64,2) NOT NULL COMMENT '产品收入, CNY',
  `signed_date` date NOT NULL COMMENT '合同签订日期',
  `add_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合同信息'

CREATE TABLE `revenue_recognitions` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键，无意义',
  `contract_id` int(11) NOT NULL COMMENT '合同ID',
  `amount` double NOT NULL COMMENT '收入金额',
  `recognized_date` date NOT NULL COMMENT '收入确认日期',
  `add_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收入确认';

-- 初始化产品列表
INSERT INTO products(name, type)
VALUES
('Word Process A', 'WP'), ('Word Process B', 'WP'),
('Mysql', 'DB'), ('Oracle', 'DB'),
('Excel A', 'XLS'), ('Excel B', 'XLS');

-- 添加合同
INSERT INTO contracts(product_id, revenue, signed_date)
VALUES
(1, 1999.99, '2022-01-10'),
(2, 2888.00, '2022-01-20'),
(3, 19999.66, '2022-02-14'),
(4, 1888.00, '2022-02-24'),
(5, 999.08, '2021-12-25'),
(6, 288888.34, '2022-03-01')