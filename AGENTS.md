# KaraCreate — AI Agent Instructions

## プロジェクト概要

Minecraft 1.20.1 Forge Mod。Create mod のアドオンとして日本の建築要素を追加する。
- Mod ID: `karacreate`
- パッケージ: `mod.ckenja.karacreate`
- ライセンス: MIT
- 主要依存: Create 0.5.1.f-26, Flywheel 0.6.10-7, Registrate MC1.20-1.3.3

## コーディング規約

- Java 17
- エンコーディング: UTF-8
- Parchment マッピング（2023.09.03）使用
- Registrate パターンでブロック/アイテム/BlockEntity を登録
- 日本語翻訳は `LanguageManager` (`Japanese` 変数) 経由で `.onRegister(Japanese.block("名前"))` で追加
- 英語名は Registrate が ID から自動生成

## パッケージ構成

```
mod.ckenja.karacreate
├── KaraCreate.java          — エントリポイント、登録、イベントハンドラ
├── KaraCreateClient.java    — クライアント側初期化
├── foundation/register/     — ブロック、アイテム、タグ等の登録クラス
├── content/
│   ├── paperDoor/           — 障子・襖ドア（バナーパターン描画機能付き）
│   ├── smallDoor/           — 1ブロック高の小型ドア
│   └── composter/           — メカニカルコンポスター
├── compat/                  — JEI 連携
└── infrastructure/
    ├── mixin/               — 6つの Mixin
    ├── data/                — バナーパターンタグプロバイダー
    └── lang/                — 言語生成（LanguageManager, BannerPatternLangGenerators）
```

## ビルドとテスト

```bash
./gradlew runData     # データ生成
./gradlew runClient   # ゲーム起動
./gradlew build       # ビルド
```

## 新しいブロックを追加する際の手順

1. `content/` 下に新パッケージを作成しブロッククラスを実装
2. `KaraCreateBlocks` に `BlockEntry` を追加（Registrate パターン）
3. 必要に応じて `KaraCreateBlockEntityTypes` に BlockEntity を登録
4. 日本語名: `.onRegister(Japanese.block("名前"))` で追加
5. テクスチャを `assets/karacreate/textures/block/` に配置
6. レシピ JSON を `data/karacreate/recipes/` に配置
7. タグは `.tag()` で inline 登録
8. `./gradlew runData` でリソース再生成

## 新しいバナーパターンを追加する際の手順

1. `KaraCreateBannerPatterns.Patterns` enum にエントリ追加
2. テクスチャを `assets/karacreate/textures/entity/banner/` に配置
3. `BannerPatternLangGenerators` の `en_us()` と `ja_jp()` の `patternTranslations` Map にエントリ追加
4. `./gradlew runData` でタグ・言語ファイル再生成

## 既知の問題（修正時参照）

詳細は `docs/KNOWN_ISSUES.md` を参照。主要な問題:
- **KI-01**: karacreate.mixins.json に AllBlockEntityTypesMixin の重複エントリ
- **KI-04/05**: small_shoji_door, small_fusuma_door, mechanical_composter のクラフトレシピ欠落
- **KI-06**: small_shoji_door に axeOnly タグ欠落
- **KI-10**: Create mod テクスチャの上書きが全マシンに影響
- **KI-14**: `KaraCraeteRecipeSerializer` のタイプミス（Craete → Create）

## ドキュメント

全仕様書は `docs/` ディレクトリに配置。目次:
- `docs/PROJECT_OVERVIEW.md` — プロジェクト概要
- `docs/ARCHITECTURE.md` — アーキテクチャ・設計
- `docs/BUILD_ENVIRONMENT.md` — ビルド・開発環境
- `docs/features/PAPER_DOORS.md` — 障子・襖ドア仕様
- `docs/features/SMALL_DOORS.md` — 小型ドア仕様
- `docs/features/MECHANICAL_COMPOSTER.md` — メカニカルコンポスター仕様
- `docs/features/BANNER_PATTERNS.md` — バナーパターン仕様
- `docs/features/RECIPES.md` — レシピ一覧
- `docs/TAGS.md` — タグ構造
- `docs/TEXTURES.md` — テクスチャ一覧
- `docs/MIXINS.md` — Mixin 仕様
- `docs/DATA_GENERATION.md` — データ生成パイプライン
- `docs/LOCALIZATION.md` — ローカライゼーション
- `docs/KNOWN_ISSUES.md` — 既知の問題
