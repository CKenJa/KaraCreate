# ローカライゼーション

## 概要

KaraCreate は3言語をサポートする:

| 言語 | ロケール | 生成方式 | エントリ数 |
|---|---|---|---|
| English (US) | `en_us` | Registrate 生成 | 94 |
| 日本語 | `ja_jp` | LanguageManager 生成 | 93 |
| English (Upside Down) | `en_ud` | Registrate 自動生成 | 256 |

---

## 翻訳キー一覧

### ブロック名

| キー | en_us | ja_jp |
|---|---|---|
| `block.karacreate.shoji_door` | Shoji Door | 障子 |
| `block.karacreate.fusuma_door` | Fusuma Door | 襖 |
| `block.karacreate.snow_viewing_shoji_door` | Snow Viewing Shoji Door | 雪見障子 |
| `block.karacreate.small_shoji_door` | Small Shoji Door | 欄間障子 |
| `block.karacreate.small_fusuma_door` | Small Fusuma Door | 半襖 |
| `block.karacreate.mechanical_composter` | Mechanical Composter | メカニカルコンポスター |

### アイテム名

| キー | en_us | ja_jp |
|---|---|---|
| `item.karacreate.japanese_banner_pattern` | Japanese Banner Pattern | 旗の伝統文様 |

### UI / その他

| キー | en_us | ja_jp |
|---|---|---|
| `itemGroup.karacreate` | KaraCreate | KaraCreate |
| `item.karacreate.japanese_banner_pattern.desc` | Japan | 伝統文様 |
| `karacreate.recipe.composting` | Composting | 堆肥化 |
| `karacreate.jei.range_of_composting` | %1$s~%2$s RPM | **欠落** |

### バナーパターン名（翻訳あり：5パターン × 16色 = 80エントリ）

キーフォーマット: `block.minecraft.banner.karacreate.{pattern}.{color}`

| パターンID | en_us | ja_jp |
|---|---|---|
| `checkered` | Checkered | 市松 |
| `hash` | Hash | 井桁 |
| `hash4` | Small Hash | 小さな市松 |
| `circle` | Circle | まる |
| `mountain` | Zen | 風景画 |

出力例: `block.minecraft.banner.karacreate.checkered.black` → `"Black Checkered"` / `"黒色の市松"`

### バナーパターン名（翻訳なし：10パターン）

以下のパターンは enum に存在するがバナーパターン翻訳が生成されない:

| パターンID | enum名 |
|---|---|
| `vert_left` | `VERT_LEFT` |
| `vert_right` | `VERT_RIGHT` |
| `bamboo` | `BAMBOO` |
| `hemp` | `HEMP` |
| `basket` | `BASKET` |
| `vapor` | `VAPOR` |
| `pine` | `PINE` |
| `pampas` | `PAMPAS` |
| `haze` | `HAZE` |
| `arrow` | `ARROW` |

これらのパターンはゲーム内でバナーに適用した場合、翻訳キーがそのまま表示される。

---

## 翻訳生成の仕組み

### en_us の生成フロー

```
KaraCreate() コンストラクタ
  │
  ├── REGISTRATE.addDataGenerator(ProviderType.LANG, ...)
  │    └── provideDefaultLang(prov::add, "karacreate", "en_us")
  │         └── assets/karacreate/lang/default/en_us.json を読み取り
  │              → 4エントリ追加
  │
  ├── REGISTRATE.addDataGenerator(ProviderType.LANG, BannerPatternLangGenerators::en_us)
  │    └── 16色 × 5パターン = 80エントリ追加
  │
  └── Registrate 自動生成
       └── ブロック6 + アイテム1 = 7エントリ追加
       └── (名前は Registrate が ID から自動推測: "shoji_door" → "Shoji Door")
```

合計: 4 + 80 + 7 + 3（重複分の上書き） = **94エントリ**

### ja_jp の生成フロー

```
KaraCreate() コンストラクタ
  │
  ├── Japanese = new LanguageManager("karacreate", "ja_jp")
  │    └── provideDefaultLang → default/ja_jp.json 読み取り
  │         → 3エントリ追加
  │
  ├── Japanese.addGenerator(BannerPatternLangGenerators::ja_jp)
  │    └── 16色 × 5パターン = 80エントリ追加
  │
  └── onRegister コールバック
       └── Japanese.block("障子") 等 × 6ブロック + 1アイテム
       └── 7エントリ追加
```

合計: 3 + 80 + 7 + 3（重複分の上書き） = **93エントリ**

> **差分**: ja_jp には `karacreate.jei.range_of_composting` キーが存在しない。JEI のレシピ表示で RPM 範囲が英語のまま表示される。

### en_ud の生成

Registrate が自動的に en_us の全エントリを上下反転テキストに変換して `en_ud.json` を生成。

---

## ソースファイルの配置

| ファイル | 役割 |
|---|---|
| `src/main/resources/assets/karacreate/lang/default/en_us.json` | 英語デフォルト（4エントリ） |
| `src/main/resources/assets/karacreate/lang/default/ja_jp.json` | 日本語デフォルト（3エントリ） |
| `src/main/java/.../infrastructure/lang/LanguageManager.java` | 日本語用言語プロバイダー管理 |
| `src/main/java/.../infrastructure/lang/BannerPatternLangGenerators.java` | バナーパターン翻訳の動的生成 |

### 生成された出力

| ファイル | エントリ数 |
|---|---|
| `src/generated/resources/assets/karacreate/lang/en_us.json` | 94 |
| `src/generated/resources/assets/karacreate/lang/ja_jp.json` | 93 |
| `src/generated/resources/assets/karacreate/lang/en_ud.json` | 256 |

---

## 新しい翻訳を追加する方法

### 静的エントリ（ブロック/アイテム以外）
1. `src/main/resources/assets/karacreate/lang/default/en_us.json` にキーと英語値を追加
2. `src/main/resources/assets/karacreate/lang/default/ja_jp.json` にキーと日本語値を追加
3. `./gradlew runData` を実行

### ブロック/アイテム名
- en_us: Registrate が ID から自動生成（`shoji_door` → `"Shoji Door"`）
- ja_jp: 登録時に `.onRegister(Japanese.block("日本語名"))` で指定

### バナーパターン翻訳
`BannerPatternLangGenerators` の `patternTranslations` Map にエントリを追加。
