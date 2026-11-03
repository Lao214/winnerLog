package com.example.winnerLogBackend.config;

import com.example.winnerLogBackend.entity.AiModel;
import com.example.winnerLogBackend.entity.League;
import com.example.winnerLogBackend.entity.Season;
import com.example.winnerLogBackend.enums.LeagueCategoryEnum;
import com.example.winnerLogBackend.repository.AiModelRepository;
import com.example.winnerLogBackend.repository.LeagueRepository;
import com.example.winnerLogBackend.repository.SeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;
    private final AiModelRepository aiModelRepository;

    public DataInitializer(LeagueRepository leagueRepository, SeasonRepository seasonRepository, AiModelRepository aiModelRepository) {
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
        this.aiModelRepository = aiModelRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initLeaguesAndSeasons();
        initAiModels();
    }

    private void initLeaguesAndSeasons() {
        if (leagueRepository.count() > 0) {
            return;
        }
        log.info("初始化联赛与杯赛基础数据（五大联赛、欧冠、世界杯、欧洲杯）...");

        List<League> defaultLeagues = List.of(
                League.builder().code("PL").name("英格兰足球超级联赛").shortName("英超").category(LeagueCategoryEnum.BIG_FIVE_LEAGUE).country("英格兰").sortOrder(1).status(1).build(),
                League.builder().code("LA_LIGA").name("西班牙足球甲级联赛").shortName("西甲").category(LeagueCategoryEnum.BIG_FIVE_LEAGUE).country("西班牙").sortOrder(2).status(1).build(),
                League.builder().code("SERIE_A").name("意大利足球甲级联赛").shortName("意甲").category(LeagueCategoryEnum.BIG_FIVE_LEAGUE).country("意大利").sortOrder(3).status(1).build(),
                League.builder().code("BUNDESLIGA").name("德国足球甲级联赛").shortName("德甲").category(LeagueCategoryEnum.BIG_FIVE_LEAGUE).country("德国").sortOrder(4).status(1).build(),
                League.builder().code("LIGUE_1").name("法国足球甲级联赛").shortName("法甲").category(LeagueCategoryEnum.BIG_FIVE_LEAGUE).country("法国").sortOrder(5).status(1).build(),
                League.builder().code("UCL").name("欧洲冠军联赛").shortName("欧冠").category(LeagueCategoryEnum.CONTINENTAL_CUP).country("欧洲").sortOrder(6).status(1).build(),
                League.builder().code("WORLD_CUP").name("国际足联世界杯").shortName("世界杯").category(LeagueCategoryEnum.INTERNATIONAL_CUP).country("全球").sortOrder(7).status(1).build(),
                League.builder().code("EURO").name("欧洲足球锦标赛").shortName("欧洲杯").category(LeagueCategoryEnum.INTERNATIONAL_CUP).country("欧洲").sortOrder(8).status(1).build()
        );

        List<League> savedLeagues = leagueRepository.saveAll(defaultLeagues);

        log.info("初始化赛事当前及代表性赛季数据...");
        for (League league : savedLeagues) {
            if ("WORLD_CUP".equalsIgnoreCase(league.getCode())) {
                seasonRepository.save(Season.builder()
                        .leagueId(league.getId())
                        .leagueCode(league.getCode())
                        .name("2026")
                        .startDate(LocalDate.of(2026, 6, 11))
                        .endDate(LocalDate.of(2026, 7, 19))
                        .isCurrent(1)
                        .status(1)
                        .build());
            } else if ("EURO".equalsIgnoreCase(league.getCode())) {
                seasonRepository.save(Season.builder()
                        .leagueId(league.getId())
                        .leagueCode(league.getCode())
                        .name("2024")
                        .startDate(LocalDate.of(2024, 6, 14))
                        .endDate(LocalDate.of(2024, 7, 14))
                        .isCurrent(0)
                        .status(0)
                        .build());
            } else {
                seasonRepository.save(Season.builder()
                        .leagueId(league.getId())
                        .leagueCode(league.getCode())
                        .name("2024-2025")
                        .startDate(LocalDate.of(2024, 8, 15))
                        .endDate(LocalDate.of(2025, 5, 30))
                        .isCurrent(1)
                        .status(1)
                        .build());

                seasonRepository.save(Season.builder()
                        .leagueId(league.getId())
                        .leagueCode(league.getCode())
                        .name("2025-2026")
                        .startDate(LocalDate.of(2025, 8, 15))
                        .endDate(LocalDate.of(2026, 5, 30))
                        .isCurrent(0)
                        .status(1)
                        .build());
            }
        }
    }

    private void initAiModels() {
        if (aiModelRepository.count() > 0) {
            return;
        }
        log.info("初始化AI模型基础数据 (GPT, 通义千问 Qwen, DeepSeek, Claude)...");

        List<AiModel> defaultModels = List.of(
                AiModel.builder()
                        .modelCode("GPT")
                        .modelName("OpenAI GPT-4o")
                        .provider("OpenAI")
                        .description("OpenAI 旗舰级多模态AI大模型 足球战术推演与高精度预测")
                        .status(1)
                        .build(),
                AiModel.builder()
                        .modelCode("QWEN")
                        .modelName("通义千问 Qwen-Max")
                        .provider("Alibaba Cloud")
                        .description("阿里云通义千问超大规模模型 足球比赛数据深度挖掘与赛果判定")
                        .status(1)
                        .build(),
                AiModel.builder()
                        .modelCode("DEEPSEEK")
                        .modelName("DeepSeek-V3")
                        .provider("DeepSeek")
                        .description("DeepSeek 深度思考与高性价比模型")
                        .status(1)
                        .build(),
                AiModel.builder()
                        .modelCode("CLAUDE")
                        .modelName("Claude 3.5 Sonnet")
                        .provider("Anthropic")
                        .description("Anthropic 复杂逻辑推理大模型")
                        .status(1)
                        .build()
        );

        aiModelRepository.saveAll(defaultModels);
    }
}
