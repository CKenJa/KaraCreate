# KaraCreate プロジェクト概要

## Modの概要

**KaraCreate** は、Minecraft 1.20.1 用の Create mod アドオンである。日本の伝統的な建築要素（障子、襖、欄間）とメカニカルコンポスターを Create mod のシステムに統合する。

| 項目 | 値 |
|---|---|
| Mod ID | `karacreate` |
| Mod 名 | KaraCreate |
| バージョン | `0.1-SNAPSHOT` |
| ライセンス | MIT |
| 作者 | CKenJa |
| パッケージ | `mod.ckenja.karacreate` |
| 対象MC | 1.20.1 |
| Mod ローダー | Minecraft Forge (`javafml`) |

---

## コンテンツ一覧

### ブロック（6種）

| ブロック名 | 登録ID | 日本語名 | クラス | 概要 |
|---|---|---|---|---|
| Shoji Door | `karacreate:shoji_door` | 障子 | `PaperDoorBlock` | バナーの柄を適用できるスライドドア |
| Fusuma Door | `karacreate:fusuma_door` | 襖 | `PaperDoorBlock` | 不透明版のスライドドア |
| Snow Viewing Shoji Door | `karacreate:snow_viewing_shoji_door` | 雪見障子 | `SlidingDoorBlock` | つまみなしのスライドドア |
| Small Shoji Door | `karacreate:small_shoji_door` | 欄間障子 | `SlidingSmallDoorBlock` | 1ブロック高のスライド小ドア |
| Small Fusuma Door | `karacreate:small_fusuma_door` | 半襖 | `SlidingSmallDoorBlock` | 1ブロック高の不透明スライド小ドア |
| Mechanical Composter | `karacreate:mechanical_composter` | メカニカルコンポスター | `MechanicalComposterBlock` | 回転力で動くコンポスター |

### アイテム（1種）

| アイテム名 | 登録ID | 日本語名 | 概要 |
|---|---|---|---|
| Japanese Banner Pattern | `karacreate:japanese_banner_pattern` | 旗の伝統文様 | 日本の伝統文様をバナーに適用するパターンアイテム |

### バナーパターン（15種）

| 内部名 | 英語表示名 | 日本語表示名 |
|---|---|---|
| `checkered` | Checkered | 市松 |
| `hash` | Hash | 井桁 |
| `hash4` | Small Hash | 小さな市松 |
| `circle` | Circle | まる |
| `mountain` | Zen | 風景画 |
| `vert_left` | Vert Left | ※未翻訳（生成langに含まれず） |
| `vert_right` | Vert Right | ※未翻訳（生成langに含まれず） |
| `bamboo` | Bamboo | ※未翻訳（生成langに含まれず） |
| `hemp` | Hemp | ※未翻訳（生成langに含まれず） |
| `basket` | Basket | ※未翻訳（生成langに含まれず） |
| `vapor` | Vapor | ※未翻訳（生成langに含まれず） |
| `pine` | Pine | ※未翻訳（生成langに含まれず） |
| `pampas` | Pampas | ※未翻訳（生成langに含まれず） |
| `haze` | Haze | ※未翻訳（生成langに含まれず） |
| `arrow` | Arrow | ※未翻訳（生成langに含まれず） |

> **注意**: 生成された lang ファイル（en_us.json, ja_jp.json）には checkered, hash, hash4, circle, mountain の5パターンのみの翻訳が含まれている。残り10パターンは登録コードに存在するが翻訳が生成されていない。これは既知の不整合である。→ [KNOWN_ISSUES.md](KNOWN_ISSUES.md)

---

## ビルドシステム

### Gradle 構成

| 項目 | 値 |
|---|---|
| ビルドツール | Gradle (ForgeGradle `[6.0.16, 6.2)`) |
| Java バージョン | 17 |
| マッピング | Parchment `2023.09.03-1.20.1` |
| Mixin Gradle | `0.7+` |
| Mixin ライブラリ | `0.8.5` |
| Librarian (Parchment) | `1.+` |

### 依存関係

| ライブラリ | バージョン | 用途 |
|---|---|---|
| Minecraft Forge | `1.20.1-47.2.32` | Mod ローダー |
| Create | `0.5.1.f-26` (for 1.20.1) | 親Mod（スライドドア、回転機構） |
| Flywheel | `0.6.10-7` (for 1.20.1) | GPU アクセラレーションレンダリング |
| Registrate | `MC1.20-1.3.3` | 宣言的ブロック/アイテム登録 |
| JEI | `15.3.0.7` | レシピ表示UI（コンパイル時のみ） |

### Maven リポジトリ

| 名前 | URL | 提供物 |
|---|---|---|
| tterrag maven | `https://maven.tterrag.com/` | Create, Flywheel, Registrate |
| Blamejared | `https://maven.blamejared.com` | JEI |
| TheIllusiveC4 | `https://maven.theillusivec4.top/` | Curios API（現在未使用） |
| MinecraftForge | `https://maven.minecraftforge.net/` | Forge本体 |
| SpongePowered | `https://repo.spongepowered.org/repository/maven-public` | Mixin |
| ParchmentMC | `https://maven.parchmentmc.org` | Parchmentマッピング |

### Gradle タスク

| タスク | 説明 |
|---|---|
| `runClient` | 開発用クライアント起動 |
| `runServer` | 開発用サーバー起動（`--nogui`） |
| `runData` | データジェネレーション実行（出力: `src/generated/resources/`） |
| `runGameTestServer` | GameTest 実行 |
| `build` → `reobfJar` | JAR ビルド（reobfuscation 付き） |

### リソースパック

| 項目 | 値 |
|---|---|
| `pack_format` | 15（Minecraft 1.20.1） |
| `forge:server_data_pack_format` | 12 |

---

## ソースセット構成

```
src/
├── main/
│   ├── java/              ← Javaソースコード
│   └── resources/         ← 手動管理リソース（テクスチャ、レシピ、モデル等）
└── generated/
    └── resources/         ← データジェネレーター出力（ブロックステート、タグ、lang等）
```

`sourceSets.main.resources` は `src/generated/resources` も含むが、`assets/create` と `assets/minecraft` は除外されている（`build.gradle` の設定）。

---

## エントリポイント

### サーバー/共通: `KaraCreate.java`

Forge の `@Mod("karacreate")` アノテーション付き。コンストラクタで以下を初期化:

1. Forge イベントバスへのリスナー登録
2. `PaperDoorBehaviour` を Create の SlidingDoorBlockEntity に注入（`BlockEntityBehaviourEvent` 経由）
3. 全コンテンツの登録（Tags → PartialModels → Items → BlockEntityTypes → Blocks → RecipeTypes → BannerPatterns → CreativeModeTabs → RecipeSerializers）
4. データジェネレーション設定

### クライアント: `KaraCreateClient.java`

`FMLClientSetupEvent` でクライアント専用の初期化:

1. `PAPER_DOOR` モデルレイヤーの登録（`PaperDoorBlockRenderer.createBodyLayer()` 使用）

---

## 関連ドキュメント

- [ARCHITECTURE.md](ARCHITECTURE.md) — パッケージ構成と設計思想
- [features/PAPER_DOORS.md](features/PAPER_DOORS.md) — 障子・襖ドア仕様
- [features/SMALL_DOORS.md](features/SMALL_DOORS.md) — 欄間障子・半襖仕様
- [features/MECHANICAL_COMPOSTER.md](features/MECHANICAL_COMPOSTER.md) — メカニカルコンポスター仕様
- [features/BANNER_PATTERNS.md](features/BANNER_PATTERNS.md) — バナーパターン仕様
- [features/RECIPES.md](features/RECIPES.md) — レシピ仕様
- [TAGS.md](TAGS.md) — タグ体系
- [TEXTURES.md](TEXTURES.md) — テクスチャ一覧
- [MIXINS.md](MIXINS.md) — Mixin 一覧
- [DATA_GENERATION.md](DATA_GENERATION.md) — データ生成
- [LOCALIZATION.md](LOCALIZATION.md) — 多言語対応
- [KNOWN_ISSUES.md](KNOWN_ISSUES.md) — 既知の問題
