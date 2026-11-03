/**
 * 五大联赛及欧洲主要俱乐部数据字典与队标检索服务
 * 队标资源目录: src/assets/MSI2026/MSI2026
 * 覆盖全部 114 支球队，支持中文全称、简称、英文名、拼音缩写及常见别名模糊匹配
 */

// 批量加载 MSI2026 俱乐部高清队标 (512x512 PNG，透明背景)
const logoModules = import.meta.glob('../assets/MSI2026/MSI2026/**/*_big_square.png', {
  eager: true,
  import: 'default'
})

// 备用 SVG 矢量队标
const svgModules = import.meta.glob('../assets/MSI2026/MSI2026/**/*.svg', {
  eager: true,
  import: 'default'
})

// 按 slug 映射 logo URL
const logoUrlMap = {}
for (const [p, url] of Object.entries(logoModules)) {
  const match = p.match(/\/([^\/]+)_big_square\.png$/)
  if (match) {
    logoUrlMap[match[1]] = url
  }
}

const svgUrlMap = {}
for (const [p, url] of Object.entries(svgModules)) {
  const match = p.match(/\/([^\/]+)\.svg$/)
  if (match) {
    svgUrlMap[match[1]] = url
  }
}

// 联赛基础字典
export const LEAGUES = [
  { code: 'PL', nameZh: '英超', fullNameZh: '英格兰足球超级联赛', group: 'Premier League', icon: '🦁' },
  { code: 'LA_LIGA', nameZh: '西甲', fullNameZh: '西班牙足球甲级联赛', group: 'La Liga', icon: '🇪🇸' },
  { code: 'SERIE_A', nameZh: '意甲', fullNameZh: '意大利足球甲级联赛', group: 'Serie A', icon: '🇮🇹' },
  { code: 'BUNDESLIGA', nameZh: '德甲', fullNameZh: '德国足球甲级联赛', group: 'Bundesliga', icon: '🦅' },
  { code: 'LIGUE_1', nameZh: '法甲', fullNameZh: '法国足球甲级联赛', group: 'Ligue 1', icon: '🇫🇷' },
  { code: 'UCL', nameZh: '欧冠', fullNameZh: '欧洲冠军联赛', group: 'Champions League', icon: '🏆' },
  { code: 'PRT', nameZh: '葡超', fullNameZh: '葡萄牙足球超级联赛', group: 'Liga Portugal', icon: '🇵🇹' }
]

// 114 支球队基础元数据定义
const RAW_TEAMS = [
  {
    "slug": "1-fc-heidenheim",
    "nameEn": "1. FC Heidenheim",
    "nameZh": "海登海姆",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "海登海姆",
      "1. FC Heidenheim",
      "1-fc-heidenheim",
      "海登海姆1846",
      "1. FC Heidenheim",
      "Heidenheim"
    ],
    "file": "MSI2026/Bundesliga/1. FC Heidenheim/1-fc-heidenheim_big_square.png"
  },
  {
    "slug": "1-fc-koln",
    "nameEn": "1. FC Köln",
    "nameZh": "科隆",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "科隆",
      "1. FC Köln",
      "1-fc-koln",
      "山羊军团",
      "1. FC Koln",
      "Koln",
      "Cologne"
    ],
    "file": "MSI2026/Bundesliga/1. FC Köln/1-fc-koln_big_square.png"
  },
  {
    "slug": "1899-hoffenheim",
    "nameEn": "1899 Hoffenheim",
    "nameZh": "霍芬海姆",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "霍芬海姆",
      "1899 Hoffenheim",
      "1899-hoffenheim",
      "霍村",
      "TSG Hoffenheim",
      "1899 Hoffenheim",
      "Hoffenheim"
    ],
    "file": "MSI2026/Bundesliga/1899 Hoffenheim/1899-hoffenheim_big_square.png"
  },
  {
    "slug": "bayer-leverkusen",
    "nameEn": "Bayer Leverkusen",
    "nameZh": "勒沃库森",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "勒沃库森",
      "Bayer Leverkusen",
      "bayer-leverkusen",
      "拜耳勒沃库森",
      "药厂",
      "勒沃",
      "Bayer Leverkusen",
      "Leverkusen"
    ],
    "file": "MSI2026/Bundesliga/Bayer Leverkusen/bayer-leverkusen_big_square.png"
  },
  {
    "slug": "bayern-munchen",
    "nameEn": "Bayern München",
    "nameZh": "拜仁慕尼黑",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "拜仁慕尼黑",
      "Bayern München",
      "bayern-munchen",
      "拜仁",
      "南大王",
      "德甲霸主",
      "Bayern Munich",
      "Bayern Munchen",
      "Bayern"
    ],
    "file": "MSI2026/Bundesliga/Bayern München/bayern-munchen_big_square.png"
  },
  {
    "slug": "borussia-dortmund",
    "nameEn": "Borussia Dortmund",
    "nameZh": "多特蒙德",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "多特蒙德",
      "Borussia Dortmund",
      "borussia-dortmund",
      "多特",
      "大黄蜂",
      "Borussia Dortmund",
      "Dortmund",
      "BVB"
    ],
    "file": "MSI2026/Bundesliga/Borussia Dortmund/borussia-dortmund_big_square.png"
  },
  {
    "slug": "borussia-monchengladbach",
    "nameEn": "Borussia Mönchengladbach",
    "nameZh": "门兴格拉德巴赫",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "门兴格拉德巴赫",
      "Borussia Mönchengladbach",
      "borussia-monchengladbach",
      "门兴",
      "小马驹",
      "Borussia Monchengladbach",
      "Gladbach",
      "Monchengladbach"
    ],
    "file": "MSI2026/Bundesliga/Borussia Mönchengladbach/borussia-monchengladbach_big_square.png"
  },
  {
    "slug": "eintracht-frankfurt",
    "nameEn": "Eintracht Frankfurt",
    "nameZh": "法兰克福",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "法兰克福",
      "Eintracht Frankfurt",
      "eintracht-frankfurt",
      "雄鹰",
      "Eintracht Frankfurt",
      "Frankfurt"
    ],
    "file": "MSI2026/Bundesliga/Eintracht Frankfurt/eintracht-frankfurt_big_square.png"
  },
  {
    "slug": "fc-augsburg",
    "nameEn": "FC Augsburg",
    "nameZh": "奥格斯堡",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "奥格斯堡",
      "FC Augsburg",
      "fc-augsburg",
      "FC Augsburg",
      "Augsburg"
    ],
    "file": "MSI2026/Bundesliga/FC Augsburg/fc-augsburg_big_square.png"
  },
  {
    "slug": "fc-st-pauli",
    "nameEn": "FC St. Pauli",
    "nameZh": "圣保利",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "圣保利",
      "FC St. Pauli",
      "fc-st-pauli",
      "海盗",
      "FC St. Pauli",
      "St. Pauli"
    ],
    "file": "MSI2026/Bundesliga/FC St. Pauli/fc-st-pauli_big_square.png"
  },
  {
    "slug": "fsv-mainz-05",
    "nameEn": "FSV Mainz 05",
    "nameZh": "美因茨",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "美因茨",
      "FSV Mainz 05",
      "fsv-mainz-05",
      "美因茨05",
      "FSV Mainz 05",
      "Mainz"
    ],
    "file": "MSI2026/Bundesliga/FSV Mainz 05/fsv-mainz-05_big_square.png"
  },
  {
    "slug": "hamburger-sv",
    "nameEn": "Hamburger SV",
    "nameZh": "汉堡",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "汉堡",
      "Hamburger SV",
      "hamburger-sv",
      "汉堡SV",
      "恐龙",
      "Hamburger SV",
      "Hamburg"
    ],
    "file": "MSI2026/Bundesliga/Hamburger SV/hamburger-sv_big_square.png"
  },
  {
    "slug": "rb-leipzig",
    "nameEn": "RB Leipzig",
    "nameZh": "RB莱比锡",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "RB莱比锡",
      "RB Leipzig",
      "rb-leipzig",
      "莱比锡红牛",
      "莱比锡",
      "红牛",
      "RB Leipzig",
      "Leipzig"
    ],
    "file": "MSI2026/Bundesliga/RB Leipzig/rb-leipzig_big_square.png"
  },
  {
    "slug": "sc-freiburg",
    "nameEn": "SC Freiburg",
    "nameZh": "弗赖堡",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "弗赖堡",
      "SC Freiburg",
      "sc-freiburg",
      "弗莱堡",
      "黑森林",
      "SC Freiburg",
      "Freiburg"
    ],
    "file": "MSI2026/Bundesliga/SC Freiburg/sc-freiburg_big_square.png"
  },
  {
    "slug": "union-berlin",
    "nameEn": "Union Berlin",
    "nameZh": "柏林联合",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "柏林联合",
      "Union Berlin",
      "union-berlin",
      "柏林联",
      "钢铁联合",
      "Union Berlin",
      "1. FC Union Berlin"
    ],
    "file": "MSI2026/Bundesliga/Union Berlin/union-berlin_big_square.png"
  },
  {
    "slug": "vfb-stuttgart",
    "nameEn": "VfB Stuttgart",
    "nameZh": "斯图加特",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "斯图加特",
      "VfB Stuttgart",
      "vfb-stuttgart",
      "青年近卫军",
      "VfB Stuttgart",
      "Stuttgart"
    ],
    "file": "MSI2026/Bundesliga/VfB Stuttgart/vfb-stuttgart_big_square.png"
  },
  {
    "slug": "vfl-wolfsburg",
    "nameEn": "VfL Wolfsburg",
    "nameZh": "沃尔夫斯堡",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "沃尔夫斯堡",
      "VfL Wolfsburg",
      "vfl-wolfsburg",
      "狼堡",
      "VfL Wolfsburg",
      "Wolfsburg"
    ],
    "file": "MSI2026/Bundesliga/VfL Wolfsburg/vfl-wolfsburg_big_square.png"
  },
  {
    "slug": "werder-bremen",
    "nameEn": "Werder Bremen",
    "nameZh": "云达不莱梅",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "云达不莱梅",
      "Werder Bremen",
      "werder-bremen",
      "不莱梅",
      "鹦鹉军团",
      "Werder Bremen",
      "Bremen"
    ],
    "file": "MSI2026/Bundesliga/Werder Bremen/werder-bremen_big_square.png"
  },
  {
    "slug": "fc-schalke-04",
    "nameEn": "FC Schalke 04",
    "nameZh": "沙尔克04",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "沙尔克04",
      "FC Schalke 04",
      "fc-schalke-04",
      "沙尔克",
      "皇家蓝",
      "矿工",
      "FC Schalke 04",
      "Schalke"
    ],
    "file": "MSI2026/Bundesliga/FC Schalke 04/fc-schalke-04_big_square.png"
  },
  {
    "slug": "sc-paderborn-07",
    "nameEn": "SC Paderborn 07",
    "nameZh": "帕德博恩",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "帕德博恩",
      "SC Paderborn 07",
      "sc-paderborn-07",
      "帕德伯恩",
      "SC Paderborn 07",
      "Paderborn"
    ],
    "file": "MSI2026/Bundesliga/SC Paderborn 07/sc-paderborn-07_big_square.png"
  },
  {
    "slug": "sv-elversberg",
    "nameEn": "SV Elversberg",
    "nameZh": "埃尔弗斯贝格",
    "leagueGroup": "Bundesliga",
    "leagueCode": "BUNDESLIGA",
    "leagueNameZh": "德甲",
    "leagueFullNameZh": "德国足球甲级联赛",
    "aliases": [
      "埃尔弗斯贝格",
      "SV Elversberg",
      "sv-elversberg",
      "埃弗斯堡",
      "SV Elversberg",
      "Elversberg"
    ],
    "file": "MSI2026/Bundesliga/SV Elversberg/sv-elversberg_big_square.png"
  },
  {
    "slug": "bodo-glimt",
    "nameEn": "Bodo/Glimt",
    "nameZh": "博德闪耀",
    "leagueGroup": "Champions League",
    "leagueCode": "UCL",
    "leagueNameZh": "欧冠",
    "leagueFullNameZh": "欧洲冠军联赛",
    "aliases": [
      "博德闪耀",
      "Bodo/Glimt",
      "bodo-glimt",
      "博多格林特",
      "闪耀哥",
      "Bodo Glimt",
      "Bodo/Glimt",
      "FK Bodo/Glimt"
    ],
    "file": "MSI2026/Champions League/Bodo-Glimt/bodo-glimt_big_square.png"
  },
  {
    "slug": "galatasaray",
    "nameEn": "Galatasaray",
    "nameZh": "加拉塔萨雷",
    "leagueGroup": "Champions League",
    "leagueCode": "UCL",
    "leagueNameZh": "欧冠",
    "leagueFullNameZh": "欧洲冠军联赛",
    "aliases": [
      "加拉塔萨雷",
      "Galatasaray",
      "galatasaray",
      "雄狮军团",
      "Galatasaray"
    ],
    "file": "MSI2026/Champions League/Galatasaray/galatasaray_big_square.png"
  },
  {
    "slug": "alaves",
    "nameEn": "Alaves",
    "nameZh": "阿拉维斯",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "阿拉维斯",
      "Alaves",
      "alaves",
      "德波尔蒂沃阿拉维斯",
      "Alaves",
      "Deportivo Alaves"
    ],
    "file": "MSI2026/La Liga/Alaves/alaves_big_square.png"
  },
  {
    "slug": "athletic-club",
    "nameEn": "Athletic Club",
    "nameZh": "毕尔巴鄂竞技",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "毕尔巴鄂竞技",
      "Athletic Club",
      "athletic-club",
      "毕尔巴鄂",
      "毕包",
      "毕尔包",
      "雄狮",
      "Athletic Bilbao",
      "Athletic Club"
    ],
    "file": "MSI2026/La Liga/Athletic Club/athletic-club_big_square.png"
  },
  {
    "slug": "atletico-madrid",
    "nameEn": "Atletico Madrid",
    "nameZh": "马德里竞技",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "马德里竞技",
      "Atletico Madrid",
      "atletico-madrid",
      "马竞",
      "床单军团",
      "Atletico Madrid",
      "Atletico"
    ],
    "file": "MSI2026/La Liga/Atletico Madrid/atletico-madrid_big_square.png"
  },
  {
    "slug": "barcelona",
    "nameEn": "Barcelona",
    "nameZh": "巴塞罗那",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "巴塞罗那",
      "Barcelona",
      "barcelona",
      "巴萨",
      "红蓝军团",
      "宇宙队",
      "Barcelona",
      "Barca"
    ],
    "file": "MSI2026/La Liga/Barcelona/barcelona_big_square.png"
  },
  {
    "slug": "celta-vigo",
    "nameEn": "Celta Vigo",
    "nameZh": "维戈塞尔塔",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "维戈塞尔塔",
      "Celta Vigo",
      "celta-vigo",
      "塞尔塔",
      "天蓝军团",
      "Celta Vigo",
      "Celta"
    ],
    "file": "MSI2026/La Liga/Celta Vigo/celta-vigo_big_square.png"
  },
  {
    "slug": "elche",
    "nameEn": "Elche",
    "nameZh": "埃尔切",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "埃尔切",
      "Elche",
      "elche",
      "绿白军团",
      "Elche"
    ],
    "file": "MSI2026/La Liga/Elche/elche_big_square.png"
  },
  {
    "slug": "espanyol",
    "nameEn": "Espanyol",
    "nameZh": "西班牙人",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "西班牙人",
      "Espanyol",
      "espanyol",
      "皇家西班牙人",
      "鹦鹉军团",
      "Espanyol",
      "RCD Espanyol"
    ],
    "file": "MSI2026/La Liga/Espanyol/espanyol_big_square.png"
  },
  {
    "slug": "getafe",
    "nameEn": "Getafe",
    "nameZh": "赫塔菲",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "赫塔菲",
      "Getafe",
      "getafe",
      "赫塔费",
      "Getafe"
    ],
    "file": "MSI2026/La Liga/Getafe/getafe_big_square.png"
  },
  {
    "slug": "girona",
    "nameEn": "Girona",
    "nameZh": "赫罗纳",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "赫罗纳",
      "Girona",
      "girona",
      "吉罗纳",
      "Girona"
    ],
    "file": "MSI2026/La Liga/Girona/girona_big_square.png"
  },
  {
    "slug": "levante",
    "nameEn": "Levante",
    "nameZh": "莱万特",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "莱万特",
      "Levante",
      "levante",
      "青蛙军团",
      "蝙蝠青蛙",
      "Levante"
    ],
    "file": "MSI2026/La Liga/Levante/levante_big_square.png"
  },
  {
    "slug": "mallorca",
    "nameEn": "Mallorca",
    "nameZh": "马略卡",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "马略卡",
      "Mallorca",
      "mallorca",
      "马洛卡",
      "马略卡岛",
      "Mallorca",
      "RCD Mallorca"
    ],
    "file": "MSI2026/La Liga/Mallorca/mallorca_big_square.png"
  },
  {
    "slug": "osasuna",
    "nameEn": "Osasuna",
    "nameZh": "奥萨苏纳",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "奥萨苏纳",
      "Osasuna",
      "osasuna",
      "小红人",
      "Osasuna"
    ],
    "file": "MSI2026/La Liga/Osasuna/osasuna_big_square.png"
  },
  {
    "slug": "oviedo",
    "nameEn": "Oviedo",
    "nameZh": "皇家奥维耶多",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "皇家奥维耶多",
      "Oviedo",
      "oviedo",
      "奥维耶多",
      "Real Oviedo",
      "Oviedo"
    ],
    "file": "MSI2026/La Liga/Oviedo/oviedo_big_square.png"
  },
  {
    "slug": "rayo-vallecano",
    "nameEn": "Rayo Vallecano",
    "nameZh": "巴列卡诺",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "巴列卡诺",
      "Rayo Vallecano",
      "rayo-vallecano",
      "巴列卡诺闪电",
      "闪电军团",
      "Rayo Vallecano",
      "Rayo"
    ],
    "file": "MSI2026/La Liga/Rayo Vallecano/rayo-vallecano_big_square.png"
  },
  {
    "slug": "real-betis",
    "nameEn": "Real Betis",
    "nameZh": "皇家贝蒂斯",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "皇家贝蒂斯",
      "Real Betis",
      "real-betis",
      "贝蒂斯",
      "绿白军团",
      "Real Betis",
      "Betis"
    ],
    "file": "MSI2026/La Liga/Real Betis/real-betis_big_square.png"
  },
  {
    "slug": "real-madrid",
    "nameEn": "Real Madrid",
    "nameZh": "皇家马德里",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "皇家马德里",
      "Real Madrid",
      "real-madrid",
      "皇马",
      "银河战舰",
      "美凌格",
      "白衣军团",
      "Real Madrid"
    ],
    "file": "MSI2026/La Liga/Real Madrid/real-madrid_big_square.png"
  },
  {
    "slug": "real-sociedad",
    "nameEn": "Real Sociedad",
    "nameZh": "皇家社会",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "皇家社会",
      "Real Sociedad",
      "real-sociedad",
      "皇社",
      "Real Sociedad",
      "Sociedad"
    ],
    "file": "MSI2026/La Liga/Real Sociedad/real-sociedad_big_square.png"
  },
  {
    "slug": "sevilla",
    "nameEn": "Sevilla",
    "nameZh": "塞维利亚",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "塞维利亚",
      "Sevilla",
      "sevilla",
      "塞维",
      "欧联之王",
      "Sevilla"
    ],
    "file": "MSI2026/La Liga/Sevilla/sevilla_big_square.png"
  },
  {
    "slug": "valencia",
    "nameEn": "Valencia",
    "nameZh": "瓦伦西亚",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "瓦伦西亚",
      "Valencia",
      "valencia",
      "蝙蝠军团",
      "瓦伦",
      "Valencia"
    ],
    "file": "MSI2026/La Liga/Valencia/valencia_big_square.png"
  },
  {
    "slug": "villarreal",
    "nameEn": "Villarreal",
    "nameZh": "比利亚雷亚尔",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "比利亚雷亚尔",
      "Villarreal",
      "villarreal",
      "黄潜",
      "黄色潜水艇",
      "比利亚",
      "Villarreal"
    ],
    "file": "MSI2026/La Liga/Villarreal/villarreal_big_square.png"
  },
  {
    "slug": "deportivo-la-coruna",
    "nameEn": "Deportivo La Coruna",
    "nameZh": "拉科鲁尼亚",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "拉科鲁尼亚",
      "Deportivo La Coruna",
      "deportivo-la-coruna",
      "拉科",
      "超级拉科",
      "Deportivo",
      "Deportivo La Coruna"
    ],
    "file": "MSI2026/La Liga/Deportivo La Coruna/deportivo-la-coruna_big_square.png"
  },
  {
    "slug": "malaga",
    "nameEn": "Malaga",
    "nameZh": "马拉加",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "马拉加",
      "Malaga",
      "malaga",
      "鳀鱼军团",
      "Malaga"
    ],
    "file": "MSI2026/La Liga/Malaga/malaga_big_square.png"
  },
  {
    "slug": "racing-santander",
    "nameEn": "Racing Santander",
    "nameZh": "桑坦德竞技",
    "leagueGroup": "La Liga",
    "leagueCode": "LA_LIGA",
    "leagueNameZh": "西甲",
    "leagueFullNameZh": "西班牙足球甲级联赛",
    "aliases": [
      "桑坦德竞技",
      "Racing Santander",
      "racing-santander",
      "桑坦德",
      "Racing Santander",
      "Santander"
    ],
    "file": "MSI2026/La Liga/Racing Santander/racing-santander_big_square.png"
  },
  {
    "slug": "sporting-cp",
    "nameEn": "Sporting CP",
    "nameZh": "葡萄牙体育",
    "leagueGroup": "Liga Portugal",
    "leagueCode": "PRT",
    "leagueNameZh": "葡超",
    "leagueFullNameZh": "葡萄牙足球超级联赛",
    "aliases": [
      "葡萄牙体育",
      "Sporting CP",
      "sporting-cp",
      "里斯本竞技",
      "葡体",
      "绿白军团",
      "Sporting CP",
      "Sporting Lisbon"
    ],
    "file": "MSI2026/Liga Portugal/Sporting CP/sporting-cp_big_square.png"
  },
  {
    "slug": "angers",
    "nameEn": "Angers",
    "nameZh": "昂热",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "昂热",
      "Angers",
      "angers",
      "昂热SCO",
      "黑白军团",
      "Angers",
      "Angers SCO"
    ],
    "file": "MSI2026/Ligue 1/Angers/angers_big_square.png"
  },
  {
    "slug": "auxerre",
    "nameEn": "Auxerre",
    "nameZh": "欧塞尔",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "欧塞尔",
      "Auxerre",
      "auxerre",
      "青年军",
      "Auxerre",
      "AJ Auxerre"
    ],
    "file": "MSI2026/Ligue 1/Auxerre/auxerre_big_square.png"
  },
  {
    "slug": "le-havre",
    "nameEn": "Le Havre",
    "nameZh": "勒阿弗尔",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "勒阿弗尔",
      "Le Havre",
      "le-havre",
      "天蓝军团",
      "Le Havre",
      "Le Havre AC"
    ],
    "file": "MSI2026/Ligue 1/Le Havre/le-havre_big_square.png"
  },
  {
    "slug": "lens",
    "nameEn": "Lens",
    "nameZh": "朗斯",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "朗斯",
      "Lens",
      "lens",
      "血金军团",
      "Lens",
      "RC Lens"
    ],
    "file": "MSI2026/Ligue 1/Lens/lens_big_square.png"
  },
  {
    "slug": "lille",
    "nameEn": "Lille",
    "nameZh": "里尔",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "里尔",
      "Lille",
      "lille",
      "猎犬",
      "Lille",
      "LOSC Lille"
    ],
    "file": "MSI2026/Ligue 1/Lille/lille_big_square.png"
  },
  {
    "slug": "lorient",
    "nameEn": "Lorient",
    "nameZh": "洛里昂",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "洛里昂",
      "Lorient",
      "lorient",
      "大比目鱼",
      "Lorient",
      "FC Lorient"
    ],
    "file": "MSI2026/Ligue 1/Lorient/lorient_big_square.png"
  },
  {
    "slug": "lyon",
    "nameEn": "Lyon",
    "nameZh": "里昂",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "里昂",
      "Lyon",
      "lyon",
      "奥林匹克里昂",
      "Lyon",
      "Olympique Lyonnais"
    ],
    "file": "MSI2026/Ligue 1/Lyon/lyon_big_square.png"
  },
  {
    "slug": "marseille",
    "nameEn": "Marseille",
    "nameZh": "马赛",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "马赛",
      "Marseille",
      "marseille",
      "马赛奥林匹克",
      "Marseille",
      "Olympique de Marseille"
    ],
    "file": "MSI2026/Ligue 1/Marseille/marseille_big_square.png"
  },
  {
    "slug": "metz",
    "nameEn": "Metz",
    "nameZh": "梅斯",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "梅斯",
      "Metz",
      "metz",
      "石榴石军团",
      "Metz",
      "FC Metz"
    ],
    "file": "MSI2026/Ligue 1/Metz/metz_big_square.png"
  },
  {
    "slug": "monaco",
    "nameEn": "Monaco",
    "nameZh": "摩纳哥",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "摩纳哥",
      "Monaco",
      "monaco",
      "摩纳哥AS",
      "Monaco",
      "AS Monaco"
    ],
    "file": "MSI2026/Ligue 1/Monaco/monaco_big_square.png"
  },
  {
    "slug": "nantes",
    "nameEn": "Nantes",
    "nameZh": "南特",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "南特",
      "Nantes",
      "nantes",
      "金丝雀",
      "Nantes",
      "FC Nantes"
    ],
    "file": "MSI2026/Ligue 1/Nantes/nantes_big_square.png"
  },
  {
    "slug": "nice",
    "nameEn": "Nice",
    "nameZh": "尼斯",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "尼斯",
      "Nice",
      "nice",
      "雄鹰",
      "Nice",
      "OGC Nice"
    ],
    "file": "MSI2026/Ligue 1/Nice/nice_big_square.png"
  },
  {
    "slug": "paris-fc",
    "nameEn": "Paris FC",
    "nameZh": "巴黎FC",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "巴黎FC",
      "Paris FC",
      "paris-fc",
      "巴黎足球俱乐部",
      "Paris FC"
    ],
    "file": "MSI2026/Ligue 1/Paris FC/paris-fc_big_square.png"
  },
  {
    "slug": "paris-saint-germain",
    "nameEn": "Paris Saint Germain",
    "nameZh": "巴黎圣日耳曼",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "巴黎圣日耳曼",
      "Paris Saint Germain",
      "paris-saint-germain",
      "大巴黎",
      "巴黎",
      "圣日耳曼",
      "PSG",
      "Paris Saint-Germain",
      "Paris Saint Germain"
    ],
    "file": "MSI2026/Ligue 1/Paris Saint Germain/paris-saint-germain_big_square.png"
  },
  {
    "slug": "rennes",
    "nameEn": "Rennes",
    "nameZh": "雷恩",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "雷恩",
      "Rennes",
      "rennes",
      "红黑军团",
      "Rennes",
      "Stade Rennais"
    ],
    "file": "MSI2026/Ligue 1/Rennes/rennes_big_square.png"
  },
  {
    "slug": "stade-brestois-29",
    "nameEn": "Stade Brestois 29",
    "nameZh": "布雷斯特",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "布雷斯特",
      "Stade Brestois 29",
      "stade-brestois-29",
      "布雷斯特29",
      "海盗",
      "Stade Brestois 29",
      "Brest"
    ],
    "file": "MSI2026/Ligue 1/Stade Brestois 29/stade-brestois-29_big_square.png"
  },
  {
    "slug": "strasbourg",
    "nameEn": "Strasbourg",
    "nameZh": "斯特拉斯堡",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "斯特拉斯堡",
      "Strasbourg",
      "strasbourg",
      "蓝白军团",
      "Strasbourg",
      "RC Strasbourg"
    ],
    "file": "MSI2026/Ligue 1/Strasbourg/strasbourg_big_square.png"
  },
  {
    "slug": "toulouse",
    "nameEn": "Toulouse",
    "nameZh": "图卢兹",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "图卢兹",
      "Toulouse",
      "toulouse",
      "紫罗兰",
      "Toulouse",
      "Toulouse FC"
    ],
    "file": "MSI2026/Ligue 1/Toulouse/toulouse_big_square.png"
  },
  {
    "slug": "estac-troyes",
    "nameEn": "Estac Troyes",
    "nameZh": "特鲁瓦",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "特鲁瓦",
      "Estac Troyes",
      "estac-troyes",
      "Troyes",
      "Estac Troyes"
    ],
    "file": "MSI2026/Ligue 1/Estac Troyes/estac-troyes_big_square.png"
  },
  {
    "slug": "le-mans",
    "nameEn": "Le Mans",
    "nameZh": "勒芒",
    "leagueGroup": "Ligue 1",
    "leagueCode": "LIGUE_1",
    "leagueNameZh": "法甲",
    "leagueFullNameZh": "法国足球甲级联赛",
    "aliases": [
      "勒芒",
      "Le Mans",
      "le-mans",
      "Le Mans",
      "Le Mans FC"
    ],
    "file": "MSI2026/Ligue 1/Le Mans/le-mans_big_square.png"
  },
  {
    "slug": "arsenal",
    "nameEn": "Arsenal",
    "nameZh": "阿森纳",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "阿森纳",
      "Arsenal",
      "arsenal",
      "枪手",
      "兵工厂",
      "Arsenal"
    ],
    "file": "MSI2026/Premier League/Arsenal/arsenal_big_square.png"
  },
  {
    "slug": "aston-villa",
    "nameEn": "Aston Villa",
    "nameZh": "阿斯顿维拉",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "阿斯顿维拉",
      "Aston Villa",
      "aston-villa",
      "维拉",
      "阿斯顿维拉队",
      "Aston Villa"
    ],
    "file": "MSI2026/Premier League/Aston Villa/aston-villa_big_square.png"
  },
  {
    "slug": "bournemouth",
    "nameEn": "Bournemouth",
    "nameZh": "伯恩茅斯",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "伯恩茅斯",
      "Bournemouth",
      "bournemouth",
      "樱桃军团",
      "Bournemouth",
      "AFC Bournemouth"
    ],
    "file": "MSI2026/Premier League/Bournemouth/bournemouth_big_square.png"
  },
  {
    "slug": "brentford",
    "nameEn": "Brentford",
    "nameZh": "布伦特福德",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "布伦特福德",
      "Brentford",
      "brentford",
      "小蜜蜂",
      "Brentford"
    ],
    "file": "MSI2026/Premier League/Brentford/brentford_big_square.png"
  },
  {
    "slug": "brighton",
    "nameEn": "Brighton",
    "nameZh": "布莱顿",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "布莱顿",
      "Brighton",
      "brighton",
      "布莱顿霍夫",
      "海鸥",
      "Brighton",
      "Brighton & Hove Albion"
    ],
    "file": "MSI2026/Premier League/Brighton/brighton_big_square.png"
  },
  {
    "slug": "burnley",
    "nameEn": "Burnley",
    "nameZh": "伯恩利",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "伯恩利",
      "Burnley",
      "burnley",
      "红酒军团",
      "Burnley"
    ],
    "file": "MSI2026/Premier League/Burnley/burnley_big_square.png"
  },
  {
    "slug": "chelsea",
    "nameEn": "Chelsea",
    "nameZh": "切尔西",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "切尔西",
      "Chelsea",
      "chelsea",
      "蓝军",
      "车子",
      "Chelsea"
    ],
    "file": "MSI2026/Premier League/Chelsea/chelsea_big_square.png"
  },
  {
    "slug": "crystal-palace",
    "nameEn": "Crystal Palace",
    "nameZh": "水晶宫",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "水晶宫",
      "Crystal Palace",
      "crystal-palace",
      "老鹰",
      "水晶宫队",
      "Crystal Palace"
    ],
    "file": "MSI2026/Premier League/Crystal Palace/crystal-palace_big_square.png"
  },
  {
    "slug": "everton",
    "nameEn": "Everton",
    "nameZh": "埃弗顿",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "埃弗顿",
      "Everton",
      "everton",
      "太妃糖",
      "Everton"
    ],
    "file": "MSI2026/Premier League/Everton/everton_big_square.png"
  },
  {
    "slug": "fulham",
    "nameEn": "Fulham",
    "nameZh": "富勒姆",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "富勒姆",
      "Fulham",
      "fulham",
      "农场主",
      "Fulham"
    ],
    "file": "MSI2026/Premier League/Fulham/fulham_big_square.png"
  },
  {
    "slug": "leeds",
    "nameEn": "Leeds",
    "nameZh": "利兹联",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "利兹联",
      "Leeds",
      "leeds",
      "利兹",
      "白玫瑰",
      "Leeds",
      "Leeds United"
    ],
    "file": "MSI2026/Premier League/Leeds/leeds_big_square.png"
  },
  {
    "slug": "liverpool",
    "nameEn": "Liverpool",
    "nameZh": "利物浦",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "利物浦",
      "Liverpool",
      "liverpool",
      "红军",
      "Liverpool"
    ],
    "file": "MSI2026/Premier League/Liverpool/liverpool_big_square.png"
  },
  {
    "slug": "manchester-city",
    "nameEn": "Manchester City",
    "nameZh": "曼城",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "曼城",
      "Manchester City",
      "manchester-city",
      "曼彻斯特城",
      "蓝月亮",
      "Manchester City",
      "Man City"
    ],
    "file": "MSI2026/Premier League/Manchester City/manchester-city_big_square.png"
  },
  {
    "slug": "manchester-united",
    "nameEn": "Manchester United",
    "nameZh": "曼联",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "曼联",
      "Manchester United",
      "manchester-united",
      "曼彻斯特联",
      "红魔",
      "Manchester United",
      "Man Utd"
    ],
    "file": "MSI2026/Premier League/Manchester United/manchester-united_big_square.png"
  },
  {
    "slug": "newcastle",
    "nameEn": "Newcastle",
    "nameZh": "纽卡斯尔联",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "纽卡斯尔联",
      "Newcastle",
      "newcastle",
      "纽卡斯尔",
      "纽卡",
      "喜鹊",
      "喜鹊军团",
      "Newcastle",
      "Newcastle United"
    ],
    "file": "MSI2026/Premier League/Newcastle/newcastle_big_square.png"
  },
  {
    "slug": "nottingham-forest",
    "nameEn": "Nottingham Forest",
    "nameZh": "诺丁汉森林",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "诺丁汉森林",
      "Nottingham Forest",
      "nottingham-forest",
      "诺丁汉",
      "森林队",
      "Nottingham Forest",
      "Nottingham"
    ],
    "file": "MSI2026/Premier League/Nottingham Forest/nottingham-forest_big_square.png"
  },
  {
    "slug": "southampton",
    "nameEn": "Southampton",
    "nameZh": "南安普顿",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "南安普顿",
      "Southampton",
      "southampton",
      "圣徒",
      "Southampton"
    ],
    "file": "MSI2026/Premier League/Southampton/southampton_big_square.png"
  },
  {
    "slug": "sunderland",
    "nameEn": "Sunderland",
    "nameZh": "桑德兰",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "桑德兰",
      "Sunderland",
      "sunderland",
      "黑猫",
      "Sunderland"
    ],
    "file": "MSI2026/Premier League/Sunderland/sunderland_big_square.png"
  },
  {
    "slug": "tottenham",
    "nameEn": "Tottenham",
    "nameZh": "托特纳姆热刺",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "托特纳姆热刺",
      "Tottenham",
      "tottenham",
      "热刺",
      "白百合",
      "托特纳姆",
      "Tottenham",
      "Tottenham Hotspur",
      "Spurs"
    ],
    "file": "MSI2026/Premier League/Tottenham/tottenham_big_square.png"
  },
  {
    "slug": "west-ham",
    "nameEn": "West Ham",
    "nameZh": "西汉姆联",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "西汉姆联",
      "West Ham",
      "west-ham",
      "西汉姆",
      "铁锤帮",
      "West Ham",
      "West Ham United"
    ],
    "file": "MSI2026/Premier League/West Ham/west-ham_big_square.png"
  },
  {
    "slug": "wolves",
    "nameEn": "Wolves",
    "nameZh": "狼队",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "狼队",
      "Wolves",
      "wolves",
      "伍尔弗汉普顿流浪",
      "伍尔弗汉普顿",
      "Wolves",
      "Wolverhampton"
    ],
    "file": "MSI2026/Premier League/Wolves/wolves_big_square.png"
  },
  {
    "slug": "coventry",
    "nameEn": "Coventry",
    "nameZh": "考文垂",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "考文垂",
      "Coventry",
      "coventry",
      "天蓝军团",
      "考文垂城",
      "Coventry",
      "Coventry City"
    ],
    "file": "MSI2026/Premier League/Coventry/coventry_big_square.png"
  },
  {
    "slug": "hull-city",
    "nameEn": "Hull City",
    "nameZh": "赫尔城",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "赫尔城",
      "Hull City",
      "hull-city",
      "老虎城",
      "赫尔",
      "Hull City",
      "Hull"
    ],
    "file": "MSI2026/Premier League/Hull City/hull-city_big_square.png"
  },
  {
    "slug": "ipswich",
    "nameEn": "Ipswich",
    "nameZh": "伊普斯维奇",
    "leagueGroup": "Premier League",
    "leagueCode": "PL",
    "leagueNameZh": "英超",
    "leagueFullNameZh": "英格兰足球超级联赛",
    "aliases": [
      "伊普斯维奇",
      "Ipswich",
      "ipswich",
      "拖拉机男孩",
      "伊普斯威奇",
      "Ipswich",
      "Ipswich Town"
    ],
    "file": "MSI2026/Premier League/Ipswich/ipswich_big_square.png"
  },
  {
    "slug": "ac-milan",
    "nameEn": "AC Milan",
    "nameZh": "AC米兰",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "AC米兰",
      "AC Milan",
      "ac-milan",
      "米兰",
      "红黑军团",
      "AC Milan",
      "Milan"
    ],
    "file": "MSI2026/Serie A/AC Milan/ac-milan_big_square.png"
  },
  {
    "slug": "as-roma",
    "nameEn": "AS Roma",
    "nameZh": "罗马",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "罗马",
      "AS Roma",
      "as-roma",
      "红狼",
      "AS Roma",
      "Roma"
    ],
    "file": "MSI2026/Serie A/AS Roma/as-roma_big_square.png"
  },
  {
    "slug": "atalanta",
    "nameEn": "Atalanta",
    "nameZh": "亚特兰大",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "亚特兰大",
      "Atalanta",
      "atalanta",
      "真蓝黑",
      "女神",
      "Atalanta"
    ],
    "file": "MSI2026/Serie A/Atalanta/atalanta_big_square.png"
  },
  {
    "slug": "bologna",
    "nameEn": "Bologna",
    "nameZh": "博洛尼亚",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "博洛尼亚",
      "Bologna",
      "bologna",
      "博罗尼亚",
      "Bologna"
    ],
    "file": "MSI2026/Serie A/Bologna/bologna_big_square.png"
  },
  {
    "slug": "cagliari",
    "nameEn": "Cagliari",
    "nameZh": "卡利亚里",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "卡利亚里",
      "Cagliari",
      "cagliari",
      "撒丁岛",
      "Cagliari"
    ],
    "file": "MSI2026/Serie A/Cagliari/cagliari_big_square.png"
  },
  {
    "slug": "como",
    "nameEn": "Como",
    "nameZh": "科莫",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "科莫",
      "Como",
      "como",
      "Como"
    ],
    "file": "MSI2026/Serie A/Como/como_big_square.png"
  },
  {
    "slug": "cremonese",
    "nameEn": "Cremonese",
    "nameZh": "克雷莫内塞",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "克雷莫内塞",
      "Cremonese",
      "cremonese",
      "克雷莫纳",
      "灰红军团",
      "Cremonese"
    ],
    "file": "MSI2026/Serie A/Cremonese/cremonese_big_square.png"
  },
  {
    "slug": "fiorentina",
    "nameEn": "Fiorentina",
    "nameZh": "佛罗伦萨",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "佛罗伦萨",
      "Fiorentina",
      "fiorentina",
      "紫百合",
      "翡冷翠",
      "Fiorentina"
    ],
    "file": "MSI2026/Serie A/Fiorentina/fiorentina_big_square.png"
  },
  {
    "slug": "genoa",
    "nameEn": "Genoa",
    "nameZh": "热那亚",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "热那亚",
      "Genoa",
      "genoa",
      "狮鹫",
      "Genoa"
    ],
    "file": "MSI2026/Serie A/Genoa/genoa_big_square.png"
  },
  {
    "slug": "hellas-verona",
    "nameEn": "Hellas Verona",
    "nameZh": "维罗纳",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "维罗纳",
      "Hellas Verona",
      "hellas-verona",
      "赫拉斯维罗纳",
      "Hellas Verona",
      "Verona"
    ],
    "file": "MSI2026/Serie A/Hellas Verona/hellas-verona_big_square.png"
  },
  {
    "slug": "inter",
    "nameEn": "Inter",
    "nameZh": "国际米兰",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "国际米兰",
      "Inter",
      "inter",
      "国米",
      "蓝黑军团",
      "Inter",
      "Internazionale",
      "Inter Milan"
    ],
    "file": "MSI2026/Serie A/Inter/inter_big_square.png"
  },
  {
    "slug": "juventus",
    "nameEn": "Juventus",
    "nameZh": "尤文图斯",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "尤文图斯",
      "Juventus",
      "juventus",
      "尤文",
      "老妇人",
      "斑马军团",
      "Juventus",
      "Juve"
    ],
    "file": "MSI2026/Serie A/Juventus/juventus_big_square.png"
  },
  {
    "slug": "lazio",
    "nameEn": "Lazio",
    "nameZh": "拉齐奥",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "拉齐奥",
      "Lazio",
      "lazio",
      "蓝鹰",
      "蓝鹰军团",
      "Lazio"
    ],
    "file": "MSI2026/Serie A/Lazio/lazio_big_square.png"
  },
  {
    "slug": "lecce",
    "nameEn": "Lecce",
    "nameZh": "莱切",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "莱切",
      "Lecce",
      "lecce",
      "小辣椒",
      "Lecce"
    ],
    "file": "MSI2026/Serie A/Lecce/lecce_big_square.png"
  },
  {
    "slug": "napoli",
    "nameEn": "Napoli",
    "nameZh": "那不勒斯",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "那不勒斯",
      "Napoli",
      "napoli",
      "拿玻里",
      "闪电军团",
      "Napoli"
    ],
    "file": "MSI2026/Serie A/Napoli/napoli_big_square.png"
  },
  {
    "slug": "parma",
    "nameEn": "Parma",
    "nameZh": "帕尔马",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "帕尔马",
      "Parma",
      "parma",
      "十字军",
      "Parma"
    ],
    "file": "MSI2026/Serie A/Parma/parma_big_square.png"
  },
  {
    "slug": "pisa",
    "nameEn": "Pisa",
    "nameZh": "比萨",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "比萨",
      "Pisa",
      "pisa",
      "Pisa"
    ],
    "file": "MSI2026/Serie A/Pisa/pisa_big_square.png"
  },
  {
    "slug": "sassuolo",
    "nameEn": "Sassuolo",
    "nameZh": "萨索洛",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "萨索洛",
      "Sassuolo",
      "sassuolo",
      "绿黑军团",
      "萨索罗",
      "Sassuolo"
    ],
    "file": "MSI2026/Serie A/Sassuolo/sassuolo_big_square.png"
  },
  {
    "slug": "torino",
    "nameEn": "Torino",
    "nameZh": "都灵",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "都灵",
      "Torino",
      "torino",
      "公牛",
      "公牛军团",
      "Torino"
    ],
    "file": "MSI2026/Serie A/Torino/torino_big_square.png"
  },
  {
    "slug": "udinese",
    "nameEn": "Udinese",
    "nameZh": "乌迪内斯",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "乌迪内斯",
      "Udinese",
      "udinese",
      "乌鸡",
      "Udinese"
    ],
    "file": "MSI2026/Serie A/Udinese/udinese_big_square.png"
  },
  {
    "slug": "frosinone",
    "nameEn": "Frosinone",
    "nameZh": "弗罗西诺内",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "弗罗西诺内",
      "Frosinone",
      "frosinone",
      "弗洛西诺内",
      "金丝雀",
      "Frosinone"
    ],
    "file": "MSI2026/Serie A/Frosinone/frosinone_big_square.png"
  },
  {
    "slug": "monza",
    "nameEn": "Monza",
    "nameZh": "蒙扎",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "蒙扎",
      "Monza",
      "monza",
      "红白军团",
      "Monza"
    ],
    "file": "MSI2026/Serie A/Monza/monza_big_square.png"
  },
  {
    "slug": "venezia",
    "nameEn": "Venezia",
    "nameZh": "威尼斯",
    "leagueGroup": "Serie A",
    "leagueCode": "SERIE_A",
    "leagueNameZh": "意甲",
    "leagueFullNameZh": "意大利足球甲级联赛",
    "aliases": [
      "威尼斯",
      "Venezia",
      "venezia",
      "泻湖军团",
      "飞狮",
      "Venezia"
    ],
    "file": "MSI2026/Serie A/Venezia/venezia_big_square.png"
  }
]

// 组装最终带有真实 logoUrl 的球队对象列表
export const TEAMS = RAW_TEAMS.map(team => ({
  ...team,
  logoUrl: logoUrlMap[team.slug] || '',
  svgUrl: svgUrlMap[team.slug] || ''
}))

// 按 slug 快速索引
const teamsBySlug = new Map()
TEAMS.forEach(t => teamsBySlug.set(t.slug, t))

// 辅助清洗文本
function cleanStr(s) {
  if (!s) return ''
  return String(s).trim().toLowerCase().replace(/[\s\-_.]/g, '')
}

// 移除“队”、“FC”、“俱乐部”等后缀
function stripSuffix(s) {
  return String(s).replace(/(足球俱乐部|俱乐部|足球队|足球会|队|cf|fc)$/i, '').trim()
}

// 建立多维度精准索引
const exactIndex = new Map()

// 阶段 1: 注册所有显式名称与别名
TEAMS.forEach(team => {
  team.aliases.forEach(alias => {
    const k = cleanStr(alias)
    if (k && !exactIndex.has(k)) {
      exactIndex.set(k, team)
    }
  })
})

// 阶段 2: 注册剔除常见后缀后的词条 (如未被显式别名占用)
TEAMS.forEach(team => {
  team.aliases.forEach(alias => {
    const stripped = stripSuffix(alias)
    const k = cleanStr(stripped)
    if (k && k.length >= 2 && !exactIndex.has(k)) {
      exactIndex.set(k, team)
    }
  })
})

/**
 * 根据用户输入的名称（中文名、简称、英文名、别名）匹配最佳球队
 * @param {string} query 用户输入的球队名称
 * @param {string|null} preferredLeagueCode 优先联赛编码 (如 'PL', 'LA_LIGA')
 * @returns {object|null} 匹配到的球队对象 (包含 logoUrl) 或 null
 */
export function matchTeam(query, preferredLeagueCode = null) {
  if (!query || typeof query !== 'string') return null
  const raw = query.trim()
  if (!raw) return null

  const c = cleanStr(raw)
  const cStripped = cleanStr(stripSuffix(raw))

  // 1. 精确匹配
  if (exactIndex.has(c)) {
    const t = exactIndex.get(c)
    if (!preferredLeagueCode || t.leagueCode === preferredLeagueCode) return t
  }
  if (exactIndex.has(cStripped)) {
    const t = exactIndex.get(cStripped)
    if (!preferredLeagueCode || t.leagueCode === preferredLeagueCode) return t
  }

  // 2. 指定联赛优先匹配
  if (preferredLeagueCode) {
    for (const team of TEAMS) {
      if (team.leagueCode !== preferredLeagueCode) continue
      for (const a of team.aliases) {
        const ca = cleanStr(a)
        if (ca === c || ca === cStripped) return team
      }
    }
  }

  // 3. 子串与重合度匹配 (如 用户输入“阿森纳足球俱乐部”包含“阿森纳”，或“拜仁”包含在“拜仁慕尼黑”)
  let bestMatch = null
  let bestScore = 0

  for (const team of TEAMS) {
    const isPreferred = preferredLeagueCode && team.leagueCode === preferredLeagueCode
    const leagueBoost = isPreferred ? 6 : 0

    for (const a of team.aliases) {
      const ca = cleanStr(a)
      if (ca.length >= 2 && (c.includes(ca) || cStripped.includes(ca))) {
        // 用户输入包含了球队别名 (如 '阿森纳队' 包含 '阿森纳')
        const score = ca.length * 3 + leagueBoost
        if (score > bestScore) {
          bestScore = score
          bestMatch = team
        }
      } else if (c.length >= 2 && ca.includes(c)) {
        // 别名包含了用户输入 (如 '拜仁慕尼黑' 包含 '拜仁')
        const score = c.length * 2 + leagueBoost
        if (score > bestScore) {
          bestScore = score
          bestMatch = team
        }
      }
    }
  }

  return bestMatch
}

/**
 * 直接获取队标 URL
 * @param {string} query 球队名称
 * @param {string|null} preferredLeagueCode 优先联赛
 * @returns {string} 队标图片 URL 或空字符串
 */
export function getTeamLogo(query, preferredLeagueCode = null) {
  const team = matchTeam(query, preferredLeagueCode)
  return team ? (team.logoUrl || team.svgUrl || '') : ''
}

/**
 * 模糊搜索球队列表 (用于自动完成或下拉建议)
 * @param {string} keyword 搜索关键字
 * @param {string|null} leagueCode 联赛筛选
 * @param {number} limit 最大返回条数
 */
export function searchTeams(keyword, leagueCode = null, limit = 10) {
  if (!keyword || !keyword.trim()) {
    const list = leagueCode ? TEAMS.filter(t => t.leagueCode === leagueCode) : TEAMS
    return list.slice(0, limit)
  }

  const c = cleanStr(keyword)
  const results = []

  for (const team of TEAMS) {
    if (leagueCode && team.leagueCode !== leagueCode) continue

    let matched = false
    let matchWeight = 0

    if (cleanStr(team.nameZh) === c) {
      matched = true
      matchWeight = 100
    } else if (cleanStr(team.nameZh).startsWith(c)) {
      matched = true
      matchWeight = 80
    } else if (cleanStr(team.nameZh).includes(c)) {
      matched = true
      matchWeight = 60
    } else {
      for (const a of team.aliases) {
        const ca = cleanStr(a)
        if (ca === c) {
          matched = true
          matchWeight = 90
          break
        } else if (ca.includes(c)) {
          matched = true
          matchWeight = 40
          break
        }
      }
    }

    if (matched) {
      results.push({ team, weight: matchWeight })
    }
  }

  results.sort((a, b) => b.weight - a.weight)
  return results.slice(0, limit).map(r => r.team)
}

/**
 * 按联赛获取全部球队
 */
export function getTeamsByLeague(leagueCode) {
  if (!leagueCode) return TEAMS
  return TEAMS.filter(t => t.leagueCode === leagueCode)
}

/**
 * 获取所有球队按联赛分组的结构
 */
export function getTeamsGroupedByLeague() {
  const groups = {}
  LEAGUES.forEach(l => {
    groups[l.code] = {
      league: l,
      teams: TEAMS.filter(t => t.leagueCode === l.code)
    }
  })
  return groups
}

export default {
  TEAMS,
  LEAGUES,
  matchTeam,
  getTeamLogo,
  searchTeams,
  getTeamsByLeague,
  getTeamsGroupedByLeague
}
