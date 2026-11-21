package com.example.winnerLogBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Schema(description = "0球与7球两极进球赔率大数据分析概况与多维规律看板")
public class ExtremeGoalsStatsSummaryVO {

    @Schema(description = "样本总场次", example = "58")
    private Integer totalMatches;

    @Schema(description = "0球平均赔率", example = "10.45")
    private BigDecimal avgZeroGoalsOdds;

    @Schema(description = "7球+平均赔率", example = "31.20")
    private BigDecimal avgSevenGoalsOdds;

    @Schema(description = "实际平均全场总进球数", example = "2.68")
    private BigDecimal avgTotalGoals;

    @Schema(description = "平均两极赔率比率 (7+赔率 / 0球赔率)", example = "3.02")
    private BigDecimal avgOddsRatio;

    // 四大进球区间结构
    @Schema(description = "0-1球 (极小球) 场次", example = "12")
    private Integer zeroOneCount;
    @Schema(description = "0-1球 打出概率(%)", example = "20.69")
    private BigDecimal zeroOneRate;

    @Schema(description = "2-3球 (黄金中球) 场次", example = "31")
    private Integer twoThreeCount;
    @Schema(description = "2-3球 打出概率(%)", example = "53.45")
    private BigDecimal twoThreeRate;

    @Schema(description = "4-6球 (进攻大球) 场次", example = "13")
    private Integer fourSixCount;
    @Schema(description = "4-6球 打出概率(%)", example = "22.41")
    private BigDecimal fourSixRate;

    @Schema(description = "7+球 (超大惨案) 场次", example = "2")
    private Integer sevenPlusCount;
    @Schema(description = "7+球 打出概率(%)", example = "3.45")
    private BigDecimal sevenPlusRate;

    // 各具体进球数 (0~7+球) 分布映射
    @Schema(description = "0~7+球 打出场次映射")
    private Map<String, Integer> goalsDistribution;

    // 0球赔率分档分析列表
    @Schema(description = "0球赔率区间规律分析分档列表")
    private List<TierAnalysisItemVO> zeroOddsTiers;

    // 7球+赔率分档分析列表
    @Schema(description = "7球赔率区间规律分析分档列表")
    private List<TierAnalysisItemVO> sevenOddsTiers;

    // 赔率比值 (7+/0) 分档列表
    @Schema(description = "两极赔率比值规律分档列表")
    private List<TierAnalysisItemVO> ratioTiers;

    // 各主流联赛进球风格排行榜
    @Schema(description = "主流联赛进球特征排行榜")
    private List<LeagueGoalFeatureVO> leagueRankings;

    // 内部类：赔率区间分档统计
    public static class TierAnalysisItemVO {
        private String tierName;        // 如 "< 9.00 (极致防守)"
        private String tierRange;       // 如 "0.00-8.99"
        private Integer matchCount;     // 该分档样本场次
        private BigDecimal countRate;   // 样本占比 %
        private BigDecimal avgGoals;    // 实际平均总进球数
        private BigDecimal zeroOneRate; // 0-1球占比 %
        private BigDecimal twoThreeRate;// 2-3球占比 %
        private BigDecimal fourPlusRate;// 4球+占比 %
        private String dominantGoals;   // 众数进球数 (最常出的进球数)

        public String getTierName() {
            return tierName;
        }

        public void setTierName(String tierName) {
            this.tierName = tierName;
        }

        public String getTierRange() {
            return tierRange;
        }

        public void setTierRange(String tierRange) {
            this.tierRange = tierRange;
        }

        public Integer getMatchCount() {
            return matchCount;
        }

        public void setMatchCount(Integer matchCount) {
            this.matchCount = matchCount;
        }

        public BigDecimal getCountRate() {
            return countRate;
        }

        public void setCountRate(BigDecimal countRate) {
            this.countRate = countRate;
        }

        public BigDecimal getAvgGoals() {
            return avgGoals;
        }

        public void setAvgGoals(BigDecimal avgGoals) {
            this.avgGoals = avgGoals;
        }

        public BigDecimal getZeroOneRate() {
            return zeroOneRate;
        }

        public void setZeroOneRate(BigDecimal zeroOneRate) {
            this.zeroOneRate = zeroOneRate;
        }

        public BigDecimal getTwoThreeRate() {
            return twoThreeRate;
        }

        public void setTwoThreeRate(BigDecimal twoThreeRate) {
            this.twoThreeRate = twoThreeRate;
        }

        public BigDecimal getFourPlusRate() {
            return fourPlusRate;
        }

        public void setFourPlusRate(BigDecimal fourPlusRate) {
            this.fourPlusRate = fourPlusRate;
        }

        public String getDominantGoals() {
            return dominantGoals;
        }

        public void setDominantGoals(String dominantGoals) {
            this.dominantGoals = dominantGoals;
        }
    }

    // 内部类：联赛进球特征排行榜
    public static class LeagueGoalFeatureVO {
        private String leagueName;
        private Integer matchCount;
        private BigDecimal avgGoals;
        private BigDecimal avgZeroOdds;
        private BigDecimal avgSevenOdds;
        private BigDecimal zeroOneRate;
        private BigDecimal twoThreeRate;
        private BigDecimal fourPlusRate;
        private String styleTag; // "大球天堂", "防守胶着", "中规中矩", "小球温床"

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public Integer getMatchCount() {
            return matchCount;
        }

        public void setMatchCount(Integer matchCount) {
            this.matchCount = matchCount;
        }

        public BigDecimal getAvgGoals() {
            return avgGoals;
        }

        public void setAvgGoals(BigDecimal avgGoals) {
            this.avgGoals = avgGoals;
        }

        public BigDecimal getAvgZeroOdds() {
            return avgZeroOdds;
        }

        public void setAvgZeroOdds(BigDecimal avgZeroOdds) {
            this.avgZeroOdds = avgZeroOdds;
        }

        public BigDecimal getAvgSevenOdds() {
            return avgSevenOdds;
        }

        public void setAvgSevenOdds(BigDecimal avgSevenOdds) {
            this.avgSevenOdds = avgSevenOdds;
        }

        public BigDecimal getZeroOneRate() {
            return zeroOneRate;
        }

        public void setZeroOneRate(BigDecimal zeroOneRate) {
            this.zeroOneRate = zeroOneRate;
        }

        public BigDecimal getTwoThreeRate() {
            return twoThreeRate;
        }

        public void setTwoThreeRate(BigDecimal twoThreeRate) {
            this.twoThreeRate = twoThreeRate;
        }

        public BigDecimal getFourPlusRate() {
            return fourPlusRate;
        }

        public void setFourPlusRate(BigDecimal fourPlusRate) {
            this.fourPlusRate = fourPlusRate;
        }

        public String getStyleTag() {
            return styleTag;
        }

        public void setStyleTag(String styleTag) {
            this.styleTag = styleTag;
        }
    }

    // ==================== Getters & Setters ====================

    public Integer getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
    }

    public BigDecimal getAvgZeroGoalsOdds() {
        return avgZeroGoalsOdds;
    }

    public void setAvgZeroGoalsOdds(BigDecimal avgZeroGoalsOdds) {
        this.avgZeroGoalsOdds = avgZeroGoalsOdds;
    }

    public BigDecimal getAvgSevenGoalsOdds() {
        return avgSevenGoalsOdds;
    }

    public void setAvgSevenGoalsOdds(BigDecimal avgSevenGoalsOdds) {
        this.avgSevenGoalsOdds = avgSevenGoalsOdds;
    }

    public BigDecimal getAvgTotalGoals() {
        return avgTotalGoals;
    }

    public void setAvgTotalGoals(BigDecimal avgTotalGoals) {
        this.avgTotalGoals = avgTotalGoals;
    }

    public BigDecimal getAvgOddsRatio() {
        return avgOddsRatio;
    }

    public void setAvgOddsRatio(BigDecimal avgOddsRatio) {
        this.avgOddsRatio = avgOddsRatio;
    }

    public Integer getZeroOneCount() {
        return zeroOneCount;
    }

    public void setZeroOneCount(Integer zeroOneCount) {
        this.zeroOneCount = zeroOneCount;
    }

    public BigDecimal getZeroOneRate() {
        return zeroOneRate;
    }

    public void setZeroOneRate(BigDecimal zeroOneRate) {
        this.zeroOneRate = zeroOneRate;
    }

    public Integer getTwoThreeCount() {
        return twoThreeCount;
    }

    public void setTwoThreeCount(Integer twoThreeCount) {
        this.twoThreeCount = twoThreeCount;
    }

    public BigDecimal getTwoThreeRate() {
        return twoThreeRate;
    }

    public void setTwoThreeRate(BigDecimal twoThreeRate) {
        this.twoThreeRate = twoThreeRate;
    }

    public Integer getFourSixCount() {
        return fourSixCount;
    }

    public void setFourSixCount(Integer fourSixCount) {
        this.fourSixCount = fourSixCount;
    }

    public BigDecimal getFourSixRate() {
        return fourSixRate;
    }

    public void setFourSixRate(BigDecimal fourSixRate) {
        this.fourSixRate = fourSixRate;
    }

    public Integer getSevenPlusCount() {
        return sevenPlusCount;
    }

    public void setSevenPlusCount(Integer sevenPlusCount) {
        this.sevenPlusCount = sevenPlusCount;
    }

    public BigDecimal getSevenPlusRate() {
        return sevenPlusRate;
    }

    public void setSevenPlusRate(BigDecimal sevenPlusRate) {
        this.sevenPlusRate = sevenPlusRate;
    }

    public Map<String, Integer> getGoalsDistribution() {
        return goalsDistribution;
    }

    public void setGoalsDistribution(Map<String, Integer> goalsDistribution) {
        this.goalsDistribution = goalsDistribution;
    }

    public List<TierAnalysisItemVO> getZeroOddsTiers() {
        return zeroOddsTiers;
    }

    public void setZeroOddsTiers(List<TierAnalysisItemVO> zeroOddsTiers) {
        this.zeroOddsTiers = zeroOddsTiers;
    }

    public List<TierAnalysisItemVO> getSevenOddsTiers() {
        return sevenOddsTiers;
    }

    public void setSevenOddsTiers(List<TierAnalysisItemVO> sevenOddsTiers) {
        this.sevenOddsTiers = sevenOddsTiers;
    }

    public List<TierAnalysisItemVO> getRatioTiers() {
        return ratioTiers;
    }

    public void setRatioTiers(List<TierAnalysisItemVO> ratioTiers) {
        this.ratioTiers = ratioTiers;
    }

    public List<LeagueGoalFeatureVO> getLeagueRankings() {
        return leagueRankings;
    }

    public void setLeagueRankings(List<LeagueGoalFeatureVO> leagueRankings) {
        this.leagueRankings = leagueRankings;
    }
}
