# データ生成（Data Generation）

## 概要

KaraCreate はデータ生成パイプラインを使って以下のリソースを自動生成する:
- ブロックステート
- ルートテーブル
- タグ
- 言語ファイル
- アイテムモデル

生成物は `src/generated/resources/` に出力される。

---

## データ生成の全体構造

```
KaraCreate コンストラクタ
 │
 ├── REGISTRATE (CreateRegistrate) ← 大半のデータ生成
 │    ├── ProviderType.LANG → en_us（デフォルト + バナー）
 │    ├── ProviderType.BLOCKSTATE → 各ブロックの blockstate/model
 │    ├── ProviderType.LOOT → ルートテーブル
 │    └── ProviderType.ITEM_MODEL → アイテムモデル
 │
 ├── Japanese (LanguageManager) → ja_jp 言語ファイル
 │    ├── default/ja_jp.json（静的エントリ）
 │    ├── ブロック/アイテム名（onRegister 経由）
 │    └── BannerPatternLangGenerators.ja_jp（動的生成）
 │
 └── KaraCreateBannerPatternTagsProvider → バナーパターンタグ
      └── karacreate:pattern_item/japan
```

---

## Registrate によるデータ生成

### ブロックステート生成

各ブロックの登録時に `.blockstate()` で生成ロジックを指定。

| ブロック | 生成方式 | ソース |
|---|---|---|
| `shoji_door` | `doorBlockProvider(false)` — ノブなし | `KaraCreateBlocks.woodenSlidingDoor` |
| `fusuma_door` | `doorBlockProvider(true)` — ノブあり | `KaraCreateBlocks.woodenSlidingDoor` |
| `snow_viewing_shoji_door` | `doorBlockProvider(false)` — ノブなし | `KaraCreateBlocks.woodenSlidingDoor` |
| `small_shoji_door` | `smallDoor(false)` — ノブなし | `KaraCreateBlocks.smallDoor` |
| `small_fusuma_door` | `smallDoor(true)` — ノブあり | `KaraCreateBlocks.smallDoor` |
| `mechanical_composter` | `customItemModel()` + 手動 blockstate | JSON ファイル直接配置 |

#### doorBlockProvider の動作

```java
// knob=true の場合: 4モデル (bottom_left, bottom_right, top_left, top_right)
// knob=false の場合: 2モデル (bottom, top) — 左右同じ
p.doorBlock(c.get(), ...);
```

出力: 64バリアント（facing × hinge × half × open）

#### smallDoor の動作

```java
// knob=true: "right"/"left" モデルを hinge で切替
// knob=false: 単一モデル
// facing → yRot = facing.toYRot() + 180
// POWERED, WATERLOGGED は除外
```

### ルートテーブル生成

| ブロック | ルートテーブル方式 |
|---|---|
| `shoji_door` | `lr.createDoorTable(block)` — ドア用条件付き |
| `fusuma_door` | `lr.createDoorTable(block)` |
| `snow_viewing_shoji_door` | `lr.createDoorTable(block)` |
| `small_shoji_door` | Registrate デフォルト（直接ドロップ） |
| `small_fusuma_door` | Registrate デフォルト（直接ドロップ） |
| `mechanical_composter` | Registrate デフォルト（直接ドロップ） |

`createDoorTable` はドアの下半分ブロックのみからドロップする条件を付与。

### タグ生成

Registrate の `.tag()` メソッドで inline 登録:

```java
.tag(BlockTags.DOORS)
.tag(BlockTags.WOODEN_DOORS)
.tag(AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag)
.tag(KaraCreateTags.BlockTags.PAPER_DOOR.tag)
```

生成されるタグファイル:
- `data/minecraft/tags/blocks/doors.json`
- `data/minecraft/tags/blocks/wooden_doors.json`
- `data/minecraft/tags/blocks/mineable/axe.json`
- `data/minecraft/tags/blocks/mineable/pickaxe.json`
- `data/minecraft/tags/items/doors.json`
- `data/karacreate/tags/blocks/paper_door.json`
- `data/karacreate/tags/blocks/small_door.json`
- `data/karacreate/tags/items/paper_door.json`
- `data/karacreate/tags/items/small_door.json`
- `data/create/tags/items/contraption_controlled.json`
- `data/quark/tags/blocks/non_double_door.json`

### アイテムモデル生成

| アイテム | モデル生成 |
|---|---|
| Paper Door系 | `template_paper_door` を親モデルとして参照 |
| Small Door (ノブあり) | `block/<name>/block_right` を親モデルとして参照 |
| Small Door (ノブなし) | `block/<name>` を親モデルとして参照 |
| Mechanical Composter | `customItemModel()` |
| Japanese Banner Pattern | Registrate デフォルト |

---

## 言語ファイル生成

### 生成パイプライン

```
en_us:
  REGISTRATE.addDataGenerator(ProviderType.LANG, ...)
    ├── provideDefaultLang("en_us") → default/en_us.json を読み取り
    ├── BannerPatternLangGenerators.en_us() → 80エントリ生成
    └── Registrate 自動生成 → ブロック名・アイテム名

ja_jp:
  Japanese (LanguageManager)
    ├── default/ja_jp.json を読み取り
    ├── BannerPatternLangGenerators.ja_jp() → 80エントリ生成
    └── onRegister コールバック → ブロック名・アイテム名

en_ud:
  Registrate 内部 → 上下反転テキスト自動生成
```

### LanguageManager の仕組み

**ソース**: `infrastructure/lang/LanguageManager.java`

```
LanguageManager(modid, locale)
  └── translationGenerators リストに provideDefaultLang を追加

addGenerator(consumer)
  └── translationGenerators に追加

initializeProvider(event)
  └── LanguageProvider を生成、全 generator を順次実行

block(name) / item(name)
  └── onRegister コールバックで翻訳を追加
```

**静的エントリ**: `provideDefaultLang()` が `FilesHelper.loadJsonResource()` を使って `assets/karacreate/lang/default/{locale}.json` を読み取り、全エントリを `LanguageProvider` に追加。

**動的エントリ**: `onRegister` コールバック（`Japanese.block("名前")`）で各ブロック/アイテムの翻訳を追加。

### BannerPatternLangGenerators の仕組み

**ソース**: `infrastructure/lang/BannerPatternLangGenerators.java`

**キーフォーマット**: `block.minecraft.banner.karacreate.{pattern}.{color}`

**色数**: 16色（black, blue, brown, cyan, gray, green, light_blue, light_gray, lime, magenta, orange, pink, purple, red, white, yellow）

**パターン数**: 5パターン（checkered, hash, hash4, circle, mountain）

**生成エントリ数**: 16 × 5 = **80エントリ**（色 × パターン）

**出力フォーマット**:
- en_us: `"%s %s"` → `"Black Checkered"`
- ja_jp: `"%sの%s"` → `"黒色の市松"`

**注意**: 15パターン中5パターンのみ翻訳が生成される。残り10パターン（VERT_LEFT, VERT_RIGHT, BAMBOO, HEMP, BASKET, VAPOR, PINE, PAMPAS, HAZE, ARROW）は翻訳なし。

### デフォルト言語ファイル

#### default/en_us.json（4エントリ）
| キー | 値 |
|---|---|
| `itemGroup.karacreate` | `KaraCreate` |
| `item.karacreate.japanese_banner_pattern.desc` | `Japan` |
| `karacreate.recipe.composting` | `Composting` |
| `karacreate.jei.range_of_composting` | `%1$s~%2$s RPM` |

#### default/ja_jp.json（3エントリ）
| キー | 値 |
|---|---|
| `itemGroup.karacreate` | `KaraCreate` |
| `item.karacreate.japanese_banner_pattern.desc` | `伝統文様` |
| `karacreate.recipe.composting` | `堆肥化` |

> **注意**: ja_jp に `karacreate.jei.range_of_composting` キーがない。→ [KNOWN_ISSUES.md](KNOWN_ISSUES.md)

---

## バナーパターンタグ生成

**ソース**: `infrastructure/data/KaraCreateBannerPatternTagsProvider.java`

**登録**: `modEventBus.addListener(EventPriority.LOWEST, KaraCreateBannerPatternTagsProvider::gatherData)`

**出力**: `data/karacreate/tags/banner_pattern/pattern_item/japan.json`

**内容**: `KaraCreateBannerPatterns.Patterns` の全15パターンを `karacreate:pattern_item/japan` タグに追加。

---

## 手動配置リソース（非生成）

以下は自動生成ではなく手動配置:

| リソース | パス |
|---|---|
| Mechanical Composter blockstate | `assets/karacreate/blockstates/mechanical_composter.json` |
| 全テクスチャ | `assets/*/textures/` |
| ブロックモデル（JSON） | `assets/karacreate/models/block/` |
| テンプレートアイテムモデル | `assets/karacreate/models/item/template_paper_door.json` |
| レシピ（JSON） | `data/karacreate/recipes/` |
| Mixin 設定 | `karacreate.mixins.json` |
| Mod メタデータ | `META-INF/mods.toml`, `pack.mcmeta` |

---

## データ生成の実行

```bash
./gradlew runData
```

`build.gradle` の `runs.data` 設定:
- ワーキングディレクトリ: `run`
- Mod クラス: `karacreate`
- 引数: `--mod karacreate --all --output <generated/resources> --existing <main/resources>`
- 除外: `assets/create/`, `assets/minecraft/`（`src/generated/resources` の `sourceSets` 設定から除外）
