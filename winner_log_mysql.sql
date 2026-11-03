-- ==============================================================================
-- WinnerLog 足球比赛AI预测与胜率排行榜系统 - 数据库初始化脚本 (MySQL 5.7 / 8.0+)
-- ==============================================================================

-- 1. 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `winner_log` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `winner_log`;

-- ------------------------------------------------------------------------------
-- 2. 联赛/赛事字典表 (league)
-- 包含五大联赛（英超、西甲、意甲、德甲、法甲）、欧冠、世界杯、欧洲杯等
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS `ai_leaderboard_stat`;
DROP TABLE IF EXISTS `match_prediction`;
DROP TABLE IF EXISTS `football_match`;
DROP TABLE IF EXISTS `season`;
DROP TABLE IF EXISTS `ai_model`;
DROP TABLE IF EXISTS `league`;

CREATE TABLE `league` (
    `id` BIGINT AUTO_INCREMENT COMMENT '联赛主键ID',
    `code` VARCHAR(50) NOT NULL COMMENT '联赛唯一编码 (如 PL, LA_LIGA, SERIE_A, BUNDESLIGA, LIGUE_1, UCL, WORLD_CUP, EURO)',
    `name` VARCHAR(100) NOT NULL COMMENT '联赛完整名称 (如 英格兰足球超级联赛)',
    `short_name` VARCHAR(50) NOT NULL COMMENT '联赛简称 (如 英超, 西甲, 意甲, 德甲, 法甲, 欧冠, 世界杯, 欧洲杯)',
    `category` VARCHAR(50) NOT NULL COMMENT '赛事分类 (BIG_FIVE_LEAGUE:五大联赛, CONTINENTAL_CUP:洲际杯赛, INTERNATIONAL_CUP:国际大赛, OTHER:其他)',
    `country` VARCHAR(50) DEFAULT NULL COMMENT '所属国家/地区 (如 英格兰, 西班牙, 欧洲, 全球)',
    `logo_url` VARCHAR(255) DEFAULT NULL COMMENT '联赛Logo图URL',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序权重 (数字越小越靠前)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 (1:启用, 0:停用)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_league_code` (`code`),
    KEY `idx_league_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='联赛与杯赛字典表';

-- ------------------------------------------------------------------------------
-- 3. 赛季表 (season)
-- 区分各联赛的不同赛季（如 2024-2025, 2025-2026, 2026）
-- ------------------------------------------------------------------------------
CREATE TABLE `season` (
    `id` BIGINT AUTO_INCREMENT COMMENT '赛季主键ID',
    `league_id` BIGINT NOT NULL COMMENT '关联联赛ID',
    `league_code` VARCHAR(50) NOT NULL COMMENT '关联联赛编码',
    `name` VARCHAR(50) NOT NULL COMMENT '赛季名称 (如 2024-2025, 2025-2026, 2026)',
    `start_date` DATE DEFAULT NULL COMMENT '赛季开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '赛季结束日期',
    `is_current` TINYINT NOT NULL DEFAULT 1 COMMENT '是否当前活跃赛季 (1:是, 0:否)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '赛季状态 (1:进行中, 0:已结束)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_league_season` (`league_id`, `name`),
    KEY `idx_season_league_code` (`league_code`),
    KEY `idx_season_name` (`name`),
    CONSTRAINT `fk_season_league` FOREIGN KEY (`league_id`) REFERENCES `league` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='赛事赛季表';

-- ------------------------------------------------------------------------------
-- 4. AI模型表 (ai_model)
-- 记录 GPT、通义千问 (Qwen) 及未来可扩展的 AI 模型 (Claude, DeepSeek等)
-- ------------------------------------------------------------------------------
CREATE TABLE `ai_model` (
    `id` BIGINT AUTO_INCREMENT COMMENT 'AI模型主键ID',
    `model_code` VARCHAR(50) NOT NULL COMMENT 'AI唯一标识编码 (如 GPT, QWEN, CLAUDE, DEEPSEEK)',
    `model_name` VARCHAR(100) NOT NULL COMMENT 'AI模型显示名称 (如 GPT-4o, 通义千问 Qwen-Max)',
    `provider` VARCHAR(50) DEFAULT NULL COMMENT '所属厂商/提供方 (如 OpenAI, 阿里云 Alibaba)',
    `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '模型头像/图标URL',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '模型简介',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 (1:启用, 0:停用)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ai_model_code` (`model_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI预测模型表';

-- ------------------------------------------------------------------------------
-- 5. 足球比赛表 (football_match)
-- 记录比赛双方、开赛时间、联赛、赛季、赛果及比分
-- ------------------------------------------------------------------------------
CREATE TABLE `football_match` (
    `id` BIGINT AUTO_INCREMENT COMMENT '比赛主键ID',
    `match_code` VARCHAR(64) DEFAULT NULL COMMENT '比赛外部/业务唯一编号',
    `league_id` BIGINT NOT NULL COMMENT '关联联赛ID',
    `league_code` VARCHAR(50) NOT NULL COMMENT '联赛编码',
    `league_name` VARCHAR(100) NOT NULL COMMENT '联赛名称快照',
    `season_id` BIGINT NOT NULL COMMENT '关联赛季ID',
    `season_name` VARCHAR(50) NOT NULL COMMENT '赛季名称快照 (如 2024-2025)',
    `round_name` VARCHAR(50) DEFAULT NULL COMMENT '轮次/阶段 (如 第28轮, 小组赛A组第1轮, 1/8决赛)',
    `home_team` VARCHAR(100) NOT NULL COMMENT '主队名称',
    `away_team` VARCHAR(100) NOT NULL COMMENT '客队名称',
    `match_time` DATETIME NOT NULL COMMENT '比赛开球时间 (北京时间)',
    `venue` VARCHAR(100) DEFAULT NULL COMMENT '比赛球场',
    `home_score` INT DEFAULT NULL COMMENT '完场主队得分',
    `away_score` INT DEFAULT NULL COMMENT '完场客队得分',
    `half_home_score` INT DEFAULT NULL COMMENT '半场主队得分',
    `half_away_score` INT DEFAULT NULL COMMENT '半场客队得分',
    `result` VARCHAR(20) DEFAULT NULL COMMENT '赛果 (HOME_WIN:主胜, DRAW:平局, AWAY_WIN:客胜)',
    `status` VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' COMMENT '比赛状态 (SCHEDULED:未开赛, IN_PROGRESS:进行中, FINISHED:已完赛, POSTPONED:延期, CANCELLED:取消)',
    `is_settled` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已结算预测结果 (1:已结算, 0:未结算)',
    `settled_at` DATETIME DEFAULT NULL COMMENT '结算时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_match_code` (`match_code`),
    KEY `idx_match_league_season` (`league_code`, `season_name`),
    KEY `idx_match_time` (`match_time`),
    KEY `idx_match_status` (`status`),
    KEY `idx_match_settled` (`is_settled`),
    KEY `idx_match_teams` (`home_team`, `away_team`, `match_time`),
    CONSTRAINT `fk_match_league` FOREIGN KEY (`league_id`) REFERENCES `league` (`id`),
    CONSTRAINT `fk_match_season` FOREIGN KEY (`season_id`) REFERENCES `season` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='足球比赛表';

-- ------------------------------------------------------------------------------
-- 6. 比赛预测记录表 (match_prediction)
-- 记录 GPT、千问等 AI 对单场比赛的胜负、最多3个比分、最多3个进球数预测及各自赔率和盈利计算
-- ------------------------------------------------------------------------------
CREATE TABLE `match_prediction` (
    `id` BIGINT AUTO_INCREMENT COMMENT '预测记录主键ID',
    `match_id` BIGINT NOT NULL COMMENT '关联足球比赛ID',
    `ai_model_id` BIGINT NOT NULL COMMENT '关联AI模型ID',
    `ai_model_code` VARCHAR(50) NOT NULL COMMENT 'AI模型编码 (如 GPT, QWEN)',
    `predicted_result` VARCHAR(20) DEFAULT NULL COMMENT '预测胜负赛果 (HOME_WIN:主胜, DRAW:平局, AWAY_WIN:客胜)',
    `result_odds` DECIMAL(6,2) DEFAULT 1.85 COMMENT '胜负预测对应赔率',
    `predicted_results_json` TEXT DEFAULT NULL COMMENT '胜负多选项JSON(如不败 [{"result":"HOME_WIN","odds":1.90},{"result":"DRAW","odds":3.40}])',
    `predicted_scores_json` TEXT DEFAULT NULL COMMENT '预测比分列表JSON(最多3个含各自赔率，如 [{"score":"2-1","odds":7.50}])',
    `predicted_goals_json` TEXT DEFAULT NULL COMMENT '预测总进球数JSON(最多3个含各自赔率，如 [{"goals":3,"odds":3.80}])',
    `predicted_scores` VARCHAR(100) DEFAULT NULL COMMENT '兼容最多3个预测比分字符串',
    `predicted_goals` VARCHAR(50) DEFAULT NULL COMMENT '兼容最多3个预测总进球数字符串',
    `predicted_home_score` INT DEFAULT NULL COMMENT '兼容单个预测主队得分',
    `predicted_away_score` INT DEFAULT NULL COMMENT '兼容单个预测客队得分',
    `confidence` DECIMAL(5,2) DEFAULT NULL COMMENT 'AI预测置信度 (0.00 ~ 100.00%)',
    `analysis_summary` TEXT DEFAULT NULL COMMENT 'AI预测核心理由/摘要',
    `detailed_analysis` MEDIUMTEXT DEFAULT NULL COMMENT 'AI完整推理与技战术分析报告',
    `predicted_odds` DECIMAL(6,2) DEFAULT NULL COMMENT '全局参考赔率',
    `settle_status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '结算状态 (PENDING:未结算, WON:红单/命中, LOST:黑单/未命中, VOID:走水/无效)',
    `is_result_correct` TINYINT DEFAULT NULL COMMENT '胜负方向是否命中 (1:命中, 0:未命中)',
    `is_score_correct` TINYINT DEFAULT NULL COMMENT '比分是否命中(3个比分命中其一即为1) (1:命中, 0:未命中)',
    `hit_score` VARCHAR(20) DEFAULT NULL COMMENT '命中的具体比分 (如 2-1)',
    `is_goals_correct` TINYINT DEFAULT NULL COMMENT '进球数是否命中(3个进球数命中其一即为1) (1:命中, 0:未命中)',
    `hit_goals` INT DEFAULT NULL COMMENT '命中的具体进球数 (如 3)',
    `hit_importance` INT NOT NULL DEFAULT 0 COMMENT '命中重要性等级 (1:比分最高, 2:进球数次之, 3:胜负基础, 0:未命中)',
    `hit_tier` VARCHAR(30) DEFAULT NULL COMMENT '命中级别标识 (PERFECT_HIT, SCORE_HIT, GOALS_HIT, RESULT_HIT, MISSED)',
    `hit_tier_desc` VARCHAR(50) DEFAULT NULL COMMENT '命中级别描述 (如 🎯精准比分, ⚽进球数命中, 🌟全中大满贯)',
    `options_count` INT NOT NULL DEFAULT 7 COMMENT '本场预测选项总数 (初始利润负数源, 如 胜负2+比分3+进球3=8项)',
    `initial_profit` DECIMAL(8,2) NOT NULL DEFAULT -7.00 COMMENT '初始利润 (如 -8.00)',
    `stake_amount` DECIMAL(8,2) NOT NULL DEFAULT 7.00 COMMENT '本场投入总注数 (如 7.00 或 8.00)',
    `return_amount` DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '本场中奖总返还金额',
    `net_profit` DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '本场净赚倍数 (return_amount - stake_amount)',
    `result_return` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '胜负玩法中奖返还',
    `score_return` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '比分玩法中奖返还',
    `goals_return` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '进球数玩法中奖返还',
    `settled_at` DATETIME DEFAULT NULL COMMENT '结算时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_match_ai_model` (`match_id`, `ai_model_code`),
    KEY `idx_pred_model_settle` (`ai_model_code`, `settle_status`),
    KEY `idx_pred_match` (`match_id`),
    CONSTRAINT `fk_pred_match` FOREIGN KEY (`match_id`) REFERENCES `football_match` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_pred_ai_model` FOREIGN KEY (`ai_model_id`) REFERENCES `ai_model` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI比赛预测明细表';

-- ------------------------------------------------------------------------------
-- 7. AI排行榜与统计快照表 (ai_leaderboard_stat)
-- 按赛季、联赛、全局多维度汇总各 AI 模型的预测战绩、胜率、净赚倍数、盈利率与连红记录
-- ------------------------------------------------------------------------------
CREATE TABLE `ai_leaderboard_stat` (
    `id` BIGINT AUTO_INCREMENT COMMENT '统计快照主键ID',
    `ai_model_code` VARCHAR(50) NOT NULL COMMENT 'AI模型编码 (如 GPT, QWEN)',
    `scope_type` VARCHAR(30) NOT NULL COMMENT '统计维度 (OVERALL:全局总榜, SEASON:赛季总榜, LEAGUE_SEASON:联赛赛季榜, LEAGUE:联赛历史总榜)',
    `league_id` BIGINT DEFAULT NULL COMMENT '联赛ID',
    `league_code` VARCHAR(50) DEFAULT NULL COMMENT '联赛编码',
    `season_id` BIGINT DEFAULT NULL COMMENT '赛季ID',
    `season_name` VARCHAR(50) DEFAULT NULL COMMENT '赛季名称',
    `total_matches` INT NOT NULL DEFAULT 0 COMMENT '总预测场次',
    `settled_matches` INT NOT NULL DEFAULT 0 COMMENT '已结算场次',
    `won_matches` INT NOT NULL DEFAULT 0 COMMENT '红单/胜负命中场次',
    `lost_matches` INT NOT NULL DEFAULT 0 COMMENT '黑单/未命中场次',
    `void_matches` INT NOT NULL DEFAULT 0 COMMENT '走水/无效场次',
    `exact_score_matches` INT NOT NULL DEFAULT 0 COMMENT '精确比分命中场次',
    `goals_matches` INT NOT NULL DEFAULT 0 COMMENT '总进球数命中场次',
    `win_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '胜负胜率百分比 (如 68.50%)',
    `score_accuracy_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '比分命中率 (如 35.20%)',
    `goals_accuracy_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '进球数命中率 (如 62.50%)',
    `total_stake` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '累计总投入本金 (注数)',
    `total_return` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '累计总中奖返还金额',
    `net_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '累计净赚倍数 (正数为赚, 负数为亏, 如 +18.50倍)',
    `roi_rate` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '投资回报率 / 盈利率 % (如 +42.50%)',
    `result_net_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '胜负玩法净赚倍数',
    `score_net_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '比分玩法净赚倍数',
    `goals_net_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '进球数玩法净赚倍数',
    `current_streak` INT NOT NULL DEFAULT 0 COMMENT '当前胜负连红/连黑 (正数为连红, 负数为连黑)',
    `max_win_streak` INT NOT NULL DEFAULT 0 COMMENT '历史最高连红数',
    `last_settled_at` DATETIME DEFAULT NULL COMMENT '最近一次结算时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ai_scope_stat` (`ai_model_code`, `scope_type`, `league_code`, `season_name`),
    KEY `idx_leaderboard_rank` (`scope_type`, `season_name`, `league_code`, `net_profit` DESC, `win_rate` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI排行榜与胜率收益统计表';

-- ------------------------------------------------------------------------------
-- 8. 每日比赛成本利润与串关策略大数据战绩表 (daily_profit_record)
-- 记录每日比赛日、穿盘数、未穿盘数、主三副二与主二副三成本/利润，以及大数据分析衍生指标
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS `daily_profit_record`;

CREATE TABLE `daily_profit_record` (
    `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
    `match_date` DATE NOT NULL COMMENT '比赛日 (YYYY-MM-DD)',
    
    -- 穿盘相关核心属性
    `break_count` INT NOT NULL DEFAULT 0 COMMENT '穿盘比赛数量 (让球打穿/大胜场数)',
    `unbreak_count` INT NOT NULL DEFAULT 0 COMMENT '未穿盘比赛数量 (未穿盘/受让下盘/走水卡盘场数)',
    `total_matches` INT NOT NULL DEFAULT 0 COMMENT '当日总比赛场数 (穿盘数 + 未穿盘数)',
    `break_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '当日穿盘率百分比 (%)',

    -- 策略一：主三串副二串 (3M2S)
    `strategy_3m2s_cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '主三串副二串成本 (元)',
    `strategy_3m2s_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '主三串副二串净利润 (元，正为盈利，负为亏损)',
    `strategy_3m2s_roi` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '主三串副二串投资回报率 ROI (%)',

    -- 策略二：主二串副三串 (2M3S)
    `strategy_2m3s_cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '主二串副三串成本 (元)',
    `strategy_2m3s_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '主二串副三串净利润 (元，正为盈利，负为亏损)',
    `strategy_2m3s_roi` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '主二串副三串投资回报率 ROI (%)',

    -- 大数据分析综合聚合指标
    `total_cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '当日总投入成本 (主3副2成本 + 主2副3成本)',
    `total_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '当日总净利润 (主3副2利润 + 主2副3利润)',
    `total_return` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '当日总回款/返奖 (总成本 + 总利润)',
    `roi_rate` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '当日综合投资回报率 ROI (%)',
    `dominant_strategy` VARCHAR(20) DEFAULT 'NONE' COMMENT '当日优势领跑策略 (3M2S / 2M3S / DRAW / NONE)',
    `market_sentiment` VARCHAR(30) DEFAULT 'BALANCED' COMMENT '盘口冷热态势 (DEEP_BREAK:深盘大穿, BALANCED:中庸均衡, UNDERDOG_COLD:下盘冷门日)',
    `performance_level` VARCHAR(20) DEFAULT 'PROFIT' COMMENT '当日收益评级 (BIG_WIN:大红, PROFIT:小盈, BREAK_EVEN:走水, LOSS:微亏, BIG_LOSS:深黑)',
    `remarks` TEXT DEFAULT NULL COMMENT '当日复盘笔记与策略心得',

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_match_date` (`match_date`),
    KEY `idx_profit_date` (`match_date`),
    KEY `idx_total_profit` (`total_profit`),
    KEY `idx_roi_rate` (`roi_rate`),
    KEY `idx_dominant_strategy` (`dominant_strategy`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日比赛成本利润与串关策略大数据战绩表';

-- ------------------------------------------------------------------------------
-- 9. 初始预置基础数据
-- ------------------------------------------------------------------------------

-- 插入八大主流赛事（五大联赛 + 欧冠 + 世界杯 + 欧洲杯）
INSERT INTO `league` (`code`, `name`, `short_name`, `category`, `country`, `sort_order`, `status`) VALUES
('PL', '英格兰足球超级联赛', '英超', 'BIG_FIVE_LEAGUE', '英格兰', 1, 1),
('LA_LIGA', '西班牙足球甲级联赛', '西甲', 'BIG_FIVE_LEAGUE', '西班牙', 2, 1),
('SERIE_A', '意大利足球甲级联赛', '意甲', 'BIG_FIVE_LEAGUE', '意大利', 3, 1),
('BUNDESLIGA', '德国足球甲级联赛', '德甲', 'BIG_FIVE_LEAGUE', '德国', 4, 1),
('LIGUE_1', '法国足球甲级联赛', '法甲', 'BIG_FIVE_LEAGUE', '法国', 5, 1),
('UCL', '欧洲冠军联赛', '欧冠', 'CONTINENTAL_CUP', '欧洲', 6, 1),
('WORLD_CUP', '国际足联世界杯', '世界杯', 'INTERNATIONAL_CUP', '全球', 7, 1),
('EURO', '欧洲足球锦标赛', '欧洲杯', 'INTERNATIONAL_CUP', '欧洲', 8, 1)
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`), `short_name`=VALUES(`short_name`);

-- 插入主流 AI 模型（GPT 与 通义千问 Qwen，并可扩展 DeepSeek, Claude）
INSERT INTO `ai_model` (`model_code`, `model_name`, `provider`, `description`, `status`) VALUES
('GPT', 'OpenAI GPT-4o', 'OpenAI', 'OpenAI Flagship Multimodal AI Model for Sports Analytics', 1),
('QWEN', '通义千问 Qwen-Max', 'Alibaba Cloud', '阿里云通义千问超大规模语言模型 足球赛事专项分析', 1),
('DEEPSEEK', 'DeepSeek-V3', 'DeepSeek', 'DeepSeek 高性价比前沿大语言模型 战术与数据推理', 1),
('CLAUDE', 'Claude 3.5 Sonnet', 'Anthropic', 'Anthropic 高智能长文本逻辑推理模型', 1)
ON DUPLICATE KEY UPDATE `model_name`=VALUES(`model_name`), `provider`=VALUES(`provider`);

-- 插入各赛事当前及代表性赛季
INSERT INTO `season` (`league_id`, `league_code`, `name`, `start_date`, `end_date`, `is_current`, `status`)
SELECT l.id, l.code, '2024-2025', '2024-08-15', '2025-05-30', 1, 1 FROM `league` l WHERE l.code IN ('PL', 'LA_LIGA', 'SERIE_A', 'BUNDESLIGA', 'LIGUE_1', 'UCL')
ON DUPLICATE KEY UPDATE `is_current`=VALUES(`is_current`);

INSERT INTO `season` (`league_id`, `league_code`, `name`, `start_date`, `end_date`, `is_current`, `status`)
SELECT l.id, l.code, '2025-2026', '2025-08-15', '2026-05-30', 0, 1 FROM `league` l WHERE l.code IN ('PL', 'LA_LIGA', 'SERIE_A', 'BUNDESLIGA', 'LIGUE_1', 'UCL')
ON DUPLICATE KEY UPDATE `is_current`=VALUES(`is_current`);

INSERT INTO `season` (`league_id`, `league_code`, `name`, `start_date`, `end_date`, `is_current`, `status`)
SELECT l.id, l.code, '2026', '2026-06-11', '2026-07-19', 1, 1 FROM `league` l WHERE l.code = 'WORLD_CUP'
ON DUPLICATE KEY UPDATE `is_current`=VALUES(`is_current`);

INSERT INTO `season` (`league_id`, `league_code`, `name`, `start_date`, `end_date`, `is_current`, `status`)
SELECT l.id, l.code, '2024', '2024-06-14', '2024-07-14', 0, 0 FROM `league` l WHERE l.code = 'EURO'
ON DUPLICATE KEY UPDATE `is_current`=VALUES(`is_current`);

-- 插入每日成本与利润串关策略示例数据
INSERT INTO `daily_profit_record` 
(`match_date`, `break_count`, `unbreak_count`, `total_matches`, `break_rate`, `strategy_3m2s_cost`, `strategy_3m2s_profit`, `strategy_3m2s_roi`, `strategy_2m3s_cost`, `strategy_2m3s_profit`, `strategy_2m3s_roi`, `total_cost`, `total_profit`, `total_return`, `roi_rate`, `dominant_strategy`, `market_sentiment`, `performance_level`, `remarks`) 
VALUES
('2026-10-21', 6, 2, 8, 75.00, 300.00, 480.00, 160.00, 200.00, 150.00, 75.00, 500.00, 630.00, 1130.00, 126.00, '3M2S', 'DEEP_BREAK', 'BIG_WIN', '欧冠小组赛正路大穿盘，主3副2策略爆发拿下超高赔率'),
('2026-10-22', 4, 4, 8, 50.00, 260.00, -80.00, -30.77, 240.00, 180.00, 75.00, 500.00, 100.00, 600.00, 20.00, '2M3S', 'BALANCED', 'PROFIT', '欧冠次日强强对话半数卡盘，主2副3稳健对冲兜底盈利'),
('2026-10-23', 2, 5, 7, 28.57, 250.00, -150.00, -60.00, 250.00, 60.00, 24.00, 500.00, -90.00, 410.00, -18.00, '2M3S', 'UNDERDOG_COLD', 'LOSS', '欧联杯冷门频出穿盘率低，主3副2受挫，主2副3减亏止损'),
('2026-10-24', 7, 3, 10, 70.00, 320.00, 560.00, 175.00, 280.00, 220.00, 78.57, 600.00, 780.00, 1380.00, 130.00, '3M2S', 'DEEP_BREAK', 'BIG_WIN', '德甲西甲周六深盘豪门打穿，主3副2斩获大红单'),
('2026-10-25', 5, 4, 9, 55.56, 300.00, 120.00, 40.00, 300.00, 210.00, 70.00, 600.00, 330.00, 930.00, 55.00, '2M3S', 'BALANCED', 'PROFIT', '英超强强对话平局打平，双策略双红');

-- ------------------------------------------------------------------------------
-- 10. 每日总进球数成本利润与规律大数据分析表 (daily_goals_profit_record)
-- 记录每日总进球数维度的规律吻合分布、双串关对冲策略投入产出及进球模型大数据指标
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS `daily_goals_profit_record`;

CREATE TABLE `daily_goals_profit_record` (
    `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
    `match_date` DATE NOT NULL COMMENT '比赛日 (YYYY-MM-DD)',
    
    -- 1. 总进球数规律核心分布属性
    `pattern_fit_count` INT NOT NULL DEFAULT 0 COMMENT '符合规律比赛数量 (完全命中总进球预期模型)',
    `pattern_slight_count` INT NOT NULL DEFAULT 0 COMMENT '稍微符合规律比赛数量 (差1球/次选进球区间)',
    `pattern_mismatch_count` INT NOT NULL DEFAULT 0 COMMENT '完全不符合规律比赛数量 (异常进球/极端反规律)',
    `total_matches` INT NOT NULL DEFAULT 0 COMMENT '当日总比赛场数 (符合 + 稍微符合 + 完全不符合)',

    -- 2. 总进球数规律大数据衍生指标
    `fit_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '完全符合规律占比 (%)',
    `slight_fit_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '稍微符合规律占比 (%)',
    `mismatch_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '完全不符合规律占比 (%)',
    `generalized_fit_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '广义吻合率/泛规律覆盖率 (%) ((符合+微符合)/总场数)',
    `pattern_score` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '进球规律综合指数 (符合*1.0 + 稍微符合*0.5 加权得分)',

    -- 3. 策略一：主三串副二串 (3M2S 进攻型策略)
    `strategy_3m2s_cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '主三串副二串成本 (元)',
    `strategy_3m2s_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '主三串副二串净利润 (元，正为盈利，负为亏损)',
    `strategy_3m2s_roi` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '主三串副二串投资回报率 ROI (%)',

    -- 4. 策略二：主二串副三串 (2M3S 防守型策略)
    `strategy_2m3s_cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '主二串副三串成本 (元)',
    `strategy_2m3s_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '主二串副三串净利润 (元，正为盈利，负为亏损)',
    `strategy_2m3s_roi` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '主二串副三串投资回报率 ROI (%)',

    -- 5. 大数据财务与综合表现指标
    `total_cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '当日总投入成本 (主3副2成本 + 主2副3成本)',
    `total_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '当日总净利润 (主3副2利润 + 主2副3利润)',
    `total_return` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '当日总回款/派彩 (总成本 + 总利润)',
    `roi_rate` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '当日综合投资回报率 ROI (%)',
    `dominant_strategy` VARCHAR(20) DEFAULT 'NONE' COMMENT '当日优势领跑策略 (STRATEGY_3M2S / STRATEGY_2M3S / DRAW / NONE)',
    `goals_trend` VARCHAR(30) DEFAULT 'BALANCED' COMMENT '进球数规律走势 (STABLE_PATTERN:规律稳定, SLIGHT_FLUCTUATION:微幅偏离, HIGH_CHAOS:异常极端球)',
    `performance_level` VARCHAR(20) DEFAULT 'PROFIT' COMMENT '当日收益评级 (GREAT_PROFIT:大红大赚, PROFIT:小稳盈, BREAKEVEN:保本, LOSS:微亏, HEAVY_LOSS:深黑)',
    `remarks` TEXT DEFAULT NULL COMMENT '当日总进球数战报复盘与模式总结',

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_goals_match_date` (`match_date`),
    KEY `idx_goals_profit_date` (`match_date`),
    KEY `idx_goals_total_profit` (`total_profit`),
    KEY `idx_goals_roi_rate` (`roi_rate`),
    KEY `idx_goals_dominant_strategy` (`dominant_strategy`),
    KEY `idx_goals_fit_rate` (`fit_rate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日总进球数成本利润与规律大数据分析表';

-- 预置总进球数示例测试数据
INSERT INTO `daily_goals_profit_record` 
(`match_date`, `pattern_fit_count`, `pattern_slight_count`, `pattern_mismatch_count`, `total_matches`, `fit_rate`, `slight_fit_rate`, `mismatch_rate`, `generalized_fit_rate`, `pattern_score`, `strategy_3m2s_cost`, `strategy_3m2s_profit`, `strategy_3m2s_roi`, `strategy_2m3s_cost`, `strategy_2m3s_profit`, `strategy_2m3s_roi`, `total_cost`, `total_profit`, `total_return`, `roi_rate`, `dominant_strategy`, `goals_trend`, `performance_level`, `remarks`) 
VALUES
('2026-10-21', 5, 2, 1, 8, 62.50, 25.00, 12.50, 87.50, 6.00, 300.00, 520.00, 173.33, 200.00, 180.00, 90.00, 500.00, 700.00, 1200.00, 140.00, 'STRATEGY_3M2S', 'STABLE_PATTERN', 'GREAT_PROFIT', '欧冠各组进球数高度契合预期，2-3球与大球精准命中，3M2S实现爆发式盈利'),
('2026-10-22', 4, 3, 2, 9, 44.44, 33.33, 22.22, 77.78, 5.50, 280.00, 90.00, 32.14, 220.00, 160.00, 72.73, 500.00, 250.00, 750.00, 50.00, 'STRATEGY_2M3S', 'SLIGHT_FLUCTUATION', 'PROFIT', '欧冠次轮部分场次略有微调，次选进球数兜底，主2副3稳健守住胜果'),
('2026-10-23', 2, 2, 4, 8, 25.00, 25.00, 50.00, 50.00, 3.00, 250.00, -180.00, -72.00, 250.00, 40.00, 16.00, 500.00, -140.00, 360.00, -28.00, 'STRATEGY_2M3S', 'HIGH_CHAOS', 'LOSS', '欧联多场次出现红牌扰动与补时进球，产生异常比分，主2副3对冲止损'),
('2026-10-24', 6, 3, 1, 10, 60.00, 30.00, 10.00, 90.00, 7.50, 320.00, 680.00, 212.50, 280.00, 260.00, 92.86, 600.00, 940.00, 1540.00, 156.67, 'STRATEGY_3M2S', 'STABLE_PATTERN', 'GREAT_PROFIT', '周末五大联赛进球模型完全吻合，德甲进球大爆发，3M2S狂揽大红'),
('2026-10-25', 4, 4, 1, 9, 44.44, 44.44, 11.11, 88.89, 6.00, 300.00, 150.00, 50.00, 300.00, 230.00, 76.67, 600.00, 380.00, 980.00, 63.33, 'STRATEGY_2M3S', 'STABLE_PATTERN', 'PROFIT', '英超意甲进球数稳步落入中段区间，广义吻合率近90%，双策略双红');

-- ------------------------------------------------------------------------------
-- 11. 盘口水位冷门翻车与大数据分析战绩表 (odds_water_upset_record)
-- 记录历史总场数、高中低水场数及各自主客场翻车场数，实时洞悉翻车率与冷门规律
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS `odds_water_upset_record`;

CREATE TABLE `odds_water_upset_record` (
    `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
    `record_date` DATE NOT NULL COMMENT '记录日期/比赛日 (YYYY-MM-DD)',
    `batch_title` VARCHAR(100) DEFAULT NULL COMMENT '批次/记录名称 (如 2026历史盘口大数据总览)',
    
    -- 1. 基础比赛场数
    `total_matches` INT NOT NULL DEFAULT 0 COMMENT '历史总场数',

    -- 2. 高水盘口统计
    `high_water_matches` INT NOT NULL DEFAULT 0 COMMENT '高水场数',
    `high_water_home_upset_count` INT NOT NULL DEFAULT 0 COMMENT '高水主场翻车场数',
    `high_water_away_upset_count` INT NOT NULL DEFAULT 0 COMMENT '高水客场翻车场数',
    `high_water_upset_count` INT NOT NULL DEFAULT 0 COMMENT '高水翻车总场数 (主翻 + 客翻)',
    `high_water_normal_count` INT NOT NULL DEFAULT 0 COMMENT '高水正路场数 (高水场数 - 高水翻车)',
    `high_water_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '高水综合翻车率 (%)',
    `high_water_home_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '高水主场翻车率 (%)',
    `high_water_away_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '高水客场翻车率 (%)',

    -- 3. 中水盘口统计
    `mid_water_matches` INT NOT NULL DEFAULT 0 COMMENT '中水场数',
    `mid_water_home_upset_count` INT NOT NULL DEFAULT 0 COMMENT '中水主场翻车场数',
    `mid_water_away_upset_count` INT NOT NULL DEFAULT 0 COMMENT '中水客场翻车场数',
    `mid_water_upset_count` INT NOT NULL DEFAULT 0 COMMENT '中水翻车总场数 (主翻 + 客翻)',
    `mid_water_normal_count` INT NOT NULL DEFAULT 0 COMMENT '中水正路场数 (中水场数 - 中水翻车)',
    `mid_water_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '中水综合翻车率 (%)',
    `mid_water_home_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '中水主场翻车率 (%)',
    `mid_water_away_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '中水客场翻车率 (%)',

    -- 4. 低水盘口统计
    `low_water_matches` INT NOT NULL DEFAULT 0 COMMENT '低水场数',
    `low_water_home_upset_count` INT NOT NULL DEFAULT 0 COMMENT '低水主场翻车场数',
    `low_water_away_upset_count` INT NOT NULL DEFAULT 0 COMMENT '低水客场翻车场数',
    `low_water_upset_count` INT NOT NULL DEFAULT 0 COMMENT '低水翻车总场数 (主翻 + 客翻)',
    `low_water_normal_count` INT NOT NULL DEFAULT 0 COMMENT '低水正路场数 (低水场数 - 低水翻车)',
    `low_water_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '低水综合翻车率 (%)',
    `low_water_home_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '低水主场翻车率 (%)',
    `low_water_away_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '低水客场翻车率 (%)',

    -- 5. 大数据综合衍生指标
    `total_upset_count` INT NOT NULL DEFAULT 0 COMMENT '历史总翻车场数 (高中低水翻车之和)',
    `total_normal_count` INT NOT NULL DEFAULT 0 COMMENT '历史总正路打出场数 (总场数 - 总翻车场数)',
    `total_home_upset_count` INT NOT NULL DEFAULT 0 COMMENT '历史主场总翻车场数 (各水位主场翻车之和)',
    `total_away_upset_count` INT NOT NULL DEFAULT 0 COMMENT '历史客场总翻车场数 (各水位客场翻车之和)',
    `overall_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '历史综合总翻车率 (%)',
    `overall_home_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '历史综合主场翻车率 (%)',
    `overall_away_upset_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '历史综合客场翻车率 (%)',
    `highest_upset_water_level` VARCHAR(30) DEFAULT 'BALANCED' COMMENT '最高翻车水位 (HIGH_WATER / MID_WATER / LOW_WATER / BALANCED)',
    `upset_tendency` VARCHAR(30) DEFAULT 'BALANCED' COMMENT '冷门主客场倾向 (HOME_BIAS:主冷偏多, AWAY_BIAS:客冷偏多, BALANCED:均衡)',
    `risk_level` VARCHAR(30) DEFAULT 'SAFE_NORMAL' COMMENT '综合冷门风险评级 (HIGH_RISK:高危频发, MODERATE:中度震荡, SAFE_NORMAL:正路稳健)',
    `remarks` TEXT DEFAULT NULL COMMENT '复盘笔记与操盘备忘',

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_water_record_date` (`record_date`),
    KEY `idx_water_record_date` (`record_date`),
    KEY `idx_water_overall_rate` (`overall_upset_rate`),
    KEY `idx_water_risk_level` (`risk_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='盘口水位冷门翻车与大数据分析战绩表';

-- 预置盘口水位翻车示例测试数据
INSERT INTO `odds_water_upset_record`
(`record_date`, `batch_title`, `total_matches`, `high_water_matches`, `high_water_home_upset_count`, `high_water_away_upset_count`, `high_water_upset_count`, `high_water_normal_count`, `high_water_upset_rate`, `high_water_home_upset_rate`, `high_water_away_upset_rate`, `mid_water_matches`, `mid_water_home_upset_count`, `mid_water_away_upset_count`, `mid_water_upset_count`, `mid_water_normal_count`, `mid_water_upset_rate`, `mid_water_home_upset_rate`, `mid_water_away_upset_rate`, `low_water_matches`, `low_water_home_upset_count`, `low_water_away_upset_count`, `low_water_upset_count`, `low_water_normal_count`, `low_water_upset_rate`, `low_water_home_upset_rate`, `low_water_away_upset_rate`, `total_upset_count`, `total_normal_count`, `total_home_upset_count`, `total_away_upset_count`, `overall_upset_rate`, `overall_home_upset_rate`, `overall_away_upset_rate`, `highest_upset_water_level`, `upset_tendency`, `risk_level`, `remarks`)
VALUES
('2026-10-21', '欧冠小组赛第一轮水位统计', 16, 5, 2, 1, 3, 2, 60.00, 40.00, 20.00, 6, 1, 1, 2, 4, 33.33, 16.67, 16.67, 5, 1, 0, 1, 4, 20.00, 20.00, 0.00, 6, 10, 4, 2, 37.50, 25.00, 12.50, 'HIGH_WATER', 'HOME_BIAS', 'MODERATE', '高水主场诱导严重，主场翻车达2场，低水场次基本正路打出'),
('2026-10-22', '欧冠小组赛第二轮水位统计', 16, 6, 2, 2, 4, 2, 66.67, 33.33, 33.33, 5, 1, 1, 2, 3, 40.00, 20.00, 20.00, 5, 1, 1, 2, 3, 40.00, 20.00, 20.00, 8, 8, 4, 4, 50.00, 25.00, 25.00, 'HIGH_WATER', 'BALANCED', 'HIGH_RISK', '高水盘口翻车率超66%，主客冷门均衡，爆冷密度极高'),
('2026-10-23', '欧联与欧协联淘汰赛水位盘口', 18, 7, 3, 2, 5, 2, 71.43, 42.86, 28.57, 6, 2, 1, 3, 3, 50.00, 33.33, 16.67, 5, 0, 1, 1, 4, 20.00, 0.00, 20.00, 9, 9, 5, 4, 50.00, 27.78, 22.22, 'HIGH_WATER', 'HOME_BIAS', 'HIGH_RISK', '弱旅主场受让高水频频爆冷，低水仅1场翻车，正路防冷策略奏效'),
('2026-10-24', '五大联赛周六超级比赛日盘口大数据', 28, 9, 3, 2, 5, 4, 55.56, 33.33, 22.22, 11, 2, 2, 4, 7, 36.36, 18.18, 18.18, 8, 1, 1, 2, 6, 25.00, 12.50, 12.50, 11, 17, 6, 5, 39.29, 21.43, 17.86, 'HIGH_WATER', 'HOME_BIAS', 'MODERATE', '英超与德甲正路打出居多，高水中主场冷门较多，中水与低水相对稳固'),
('2026-10-25', '五大联赛周日决战水位盘口', 25, 8, 2, 3, 5, 3, 62.50, 25.00, 37.50, 10, 2, 1, 3, 7, 30.00, 20.00, 10.00, 7, 1, 0, 1, 6, 14.29, 14.29, 0.00, 9, 16, 5, 4, 36.00, 20.00, 16.00, 'HIGH_WATER', 'HOME_BIAS', 'MODERATE', '意甲与西甲低水正路打出率超85%，客场翻车集中在高水区间');

-- ------------------------------------------------------------------------------
-- 12. 每日水位规律串子战绩与大数据收益分析表 (daily_water_combo_record)
-- 手动记录比赛日期、比赛场数、打票成本、最终利润、长串成本与利润，自动分析ROI、总回款与走势
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS `daily_water_combo_record`;

CREATE TABLE `daily_water_combo_record` (
    `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
    `record_date` DATE NOT NULL COMMENT '比赛日 (YYYY-MM-DD)',
    `match_count` INT NOT NULL DEFAULT 0 COMMENT '下注分析比赛场数',
    `ticket_cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '常规打票成本 (元)',
    `final_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '常规最终利润 (元，正为盈利，负为亏损)',
    `long_combo_cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '长串打票成本 (元)',
    `long_combo_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '长串最终利润 (元，正为盈利，负为亏损)',
    `long_combo_roi` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '长串投资回报率 ROI (%)',
    `total_cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '综合总打票成本 (常规成本 + 长串成本)',
    `total_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '综合总最终利润 (常规利润 + 长串利润)',
    `combo_type` VARCHAR(50) DEFAULT '水位对冲串关' COMMENT '串关类型/策略名称 (如 2串1, 3串1, 水位高低配串)',
    `total_return` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '打票回款/总派彩 (总成本 + 总利润)',
    `roi_rate` DECIMAL(6,2) NOT NULL DEFAULT 0.00 COMMENT '综合投资回报率 ROI (%)',
    `win_loss_status` VARCHAR(20) DEFAULT 'PROFIT' COMMENT '单日胜负状态 (PROFIT:红单盈利, BREAKEVEN:保本, LOSS:黑单亏损)',
    `profit_level` VARCHAR(20) DEFAULT 'PROFIT' COMMENT '收益等级 (GREAT_PROFIT:翻倍大红, PROFIT:稳盈, BREAKEVEN:保本, LOSS:微亏, HEAVY_LOSS:深黑)',
    `avg_match_cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '场均综合成本 (元)',
    `avg_match_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '场均综合利润 (元)',
    `remarks` TEXT DEFAULT NULL COMMENT '复盘心得与打票备忘',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_combo_record_date` (`record_date`),
    KEY `idx_combo_record_date` (`record_date`),
    KEY `idx_combo_final_profit` (`final_profit`),
    KEY `idx_combo_total_profit` (`total_profit`),
    KEY `idx_combo_roi_rate` (`roi_rate`),
    KEY `idx_combo_win_loss` (`win_loss_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日水位规律串子战绩与大数据收益表';

-- 预置每日水位串子战绩示例测试数据
INSERT INTO `daily_water_combo_record`
(`record_date`, `match_count`, `ticket_cost`, `final_profit`, `long_combo_cost`, `long_combo_profit`, `long_combo_roi`, `total_cost`, `total_profit`, `combo_type`, `total_return`, `roi_rate`, `win_loss_status`, `profit_level`, `avg_match_cost`, `avg_match_profit`, `remarks`)
VALUES
('2026-10-21', 6, 400.00, 680.00, 100.00, 350.00, 350.00, 500.00, 1030.00, '水位高低配2串1+梦想长串', 1530.00, 206.00, 'PROFIT', 'GREAT_PROFIT', 83.33, 171.67, '欧冠高水主场冷门精准抓中，低水二串稳健收米，长串斩获3.5倍收益'),
('2026-10-22', 8, 500.00, 260.00, 100.00, -100.00, -100.00, 600.00, 160.00, '中低水防冷3串4+娱乐长串', 760.00, 26.67, 'PROFIT', 'PROFIT', 75.00, 20.00, '欧冠次轮防守常规串关打出，长串断1场未能打出，综合净利160元'),
('2026-10-23', 6, 450.00, -180.00, 50.00, -50.00, -100.00, 500.00, -230.00, '高水中水攻守对冲串+小额长串', 270.00, -46.00, 'LOSS', 'LOSS', 83.33, -38.33, '欧联强队客场爆冷导致常规串与长串双双受挫，次日控制注数'),
('2026-10-24', 10, 600.00, 1120.00, 150.00, 850.00, 566.67, 750.00, 1970.00, '五大联赛超级串4串11+高赔长串', 2720.00, 262.67, 'PROFIT', 'GREAT_PROFIT', 75.00, 197.00, '德甲英超高水诱盘被识破，逆向博冷大丰收，高赔长串暴击红单'),
('2026-10-25', 9, 500.00, 430.00, 100.00, 180.00, 180.00, 600.00, 610.00, '意甲西甲低水稳健2串1+3串1长串', 1210.00, 101.67, 'PROFIT', 'GREAT_PROFIT', 66.67, 67.78, '周日正路打出概率高，低水盘口稳如磐石，长串亦小红打出');



