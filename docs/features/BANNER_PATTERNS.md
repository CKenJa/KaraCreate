# バナーパターン (Banner Patterns) 仕様

## 概要

日本の伝統文様15種をMinecraftのバナーパターンとして追加する。専用のパターンアイテム「旗の伝統文様 (Japanese Banner Pattern)」を織機（Loom）で使用して適用する。

---

## パターンアイテム

| 項目 | 値 |
|---|---|
| 登録ID | `karacreate:japanese_banner_pattern` |
| クラス | `BannerPatternItem` |
| 日本語名 | 旗の伝統文様 |
| 英語名 | Japanese Banner Pattern |
| 説明テキスト（en_us） | "Japan" |
| 説明テキスト（ja_jp） | "伝統文様" |
| タグ | `karacreate:pattern_item/japan`（BannerPatternタグ） |

---

## パターン一覧（15種）

`KaraCreateBannerPatterns.java` の `Patterns` enum で定義:

| # | enum名 | 登録名（小文字） | 英語名 | 日本語名 | テクスチャファイル | lang 翻訳有無 |
|---|---|---|---|---|---|---|
| 1 | `CHECKERED` | `checkered` | Checkered | 市松 | `entity/banner/checkered.png` | あり |
| 2 | `HASH` | `hash` | Hash | 井桁 | `entity/banner/hash.png` | あり |
| 3 | `HASH4` | `hash4` | Small Hash | 小さな市松 | `entity/banner/hash4.png` | あり |
| 4 | `CIRCLE` | `circle` | Circle | まる | `entity/banner/circle.png` | あり |
| 5 | `MOUNTAIN` | `mountain` | Zen | 風景画 | `entity/banner/mountain.png` | あり |
| 6 | `VERT_LEFT` | `vert_left` | (未確認) | (未確認) | (未確認) | **なし** |
| 7 | `VERT_RIGHT` | `vert_right` | (未確認) | (未確認) | (未確認) | **なし** |
| 8 | `BAMBOO` | `bamboo` | (未確認) | (未確認) | `entity/banner/bamboo.png` | **なし** |
| 9 | `HEMP` | `hemp` | (未確認) | (未確認) | (未確認) | **なし** |
| 10 | `BASKET` | `basket` | (未確認) | (未確認) | (未確認) | **なし** |
| 11 | `VAPOR` | `vapor` | (未確認) | (未確認) | (未確認) | **なし** |
| 12 | `PINE` | `pine` | (未確認) | (未確認) | (未確認) | **なし** |
| 13 | `PAMPAS` | `pampas` | (未確認) | (未確認) | (未確認) | **なし** |
| 14 | `HAZE` | `haze` | (未確認) | (未確認) | (未確認) | **なし** |
| 15 | `ARROW` | `arrow` | (未確認) | (未確認) | (未確認) | **なし** |

> **注意**: 生成された lang ファイルには 5 パターン（checkered, hash, hash4, circle, mountain）の翻訳のみが含まれている。残り 10 パターンは `BannerPatternLangGenerators` で翻訳が生成されていない。コードに登録はあるがテクスチャや翻訳の対応が不完全。 → [KNOWN_ISSUES.md](../KNOWN_ISSUES.md)

---

## 翻訳キーフォーマット

各パターン × 16色の翻訳が必要:

```
block.minecraft.banner.karacreate.{pattern}.{color}
```

例: `block.minecraft.banner.karacreate.checkered.red` → "Red Checkered" / "赤色の市松"

### 日本語の色名一覧

| DyeColor | 日本語 |
|---|---|
| `black` | 黒色 |
| `blue` | 青色 |
| `brown` | 茶色 |
| `cyan` | 青緑色 |
| `gray` | 灰色 |
| `green` | 緑色 |
| `light_blue` | 水色 |
| `light_gray` | 薄灰色 |
| `lime` | 黄緑色 |
| `magenta` | 赤紫色 |
| `orange` | 橙色 |
| `pink` | 桃色 |
| `purple` | 紫色 |
| `red` | 赤色 |
| `white` | 白色 |
| `yellow` | 黄色 |

---

## 登録の仕組み

**ソース**: `foundation/register/KaraCreateBannerPatterns.java`

`Patterns` enum は各パターンのDeferredRegisterエントリを保持:

```java
public enum Patterns {
    CHECKERED, HASH, HASH4, CIRCLE, MOUNTAIN,
    VERT_LEFT, VERT_RIGHT, BAMBOO, HEMP, BASKET,
    VAPOR, PINE, PAMPAS, HAZE, ARROW;
    // ...
    PatternEntry = BANNER_PATTERNS.register(
        "karacreate_" + name().toLowerCase(Locale.ROOT),
        () -> new BannerPattern("karacreate_" + name().toLowerCase(Locale.ROOT))
    );
}
```

登録名は `karacreate_{pattern_lowercase}` 形式。

---

## タグ

### BannerPattern タグ

| タグ | 内容 |
|---|---|
| `karacreate:pattern_item/japan` | 全15パターンを含む |

**データジェネレーター**: `KaraCreateBannerPatternTagsProvider`

このタグがパターンアイテム（`karacreate:japanese_banner_pattern`）と紐づけられ、織機で使用可能になる。

---

## テクスチャ

テクスチャは `assets/karacreate/textures/entity/banner/` に配置:

| ファイル | パターン |
|---|---|
| `checkered.png` | 市松 |
| `circle.png` | まる |
| `hash.png` | 井桁 |
| `hash4.png` | 小さな市松 |
| `mountain.png` | 風景画 |
| `wave.png` | ※enumには `WAVE` が存在しない（余剰テクスチャ？） |
| `vert_left.png` | ※確認必要 |
| `vert_right.png` | ※確認必要 |
| `bamboo.png` | 竹 |
| `cheese.png` | ※enumには `CHEESE` が存在しない（余剰テクスチャ？） |
| `eternal.png` | ※enumには `ETERNAL` が存在しない（余剰テクスチャ？） |

> **注意**: `wave.png`, `cheese.png`, `eternal.png` はテクスチャファイルとして存在するが、対応する enum エントリがない。逆に `hemp`, `basket`, `vapor`, `pine`, `pampas`, `haze`, `arrow` は enum にあるがテクスチャが確認できない。これは開発途中の不整合。→ [KNOWN_ISSUES.md](../KNOWN_ISSUES.md)

---

## 翻訳生成

**ソース**: `infrastructure/lang/BannerPatternLangGenerators.java`

`ja_jp()` と `en_us()` メソッドで翻訳を生成:
- 各パターン × 16色の組み合わせ
- 日本語: `"{色名}の{文様名}"` 形式
- 英語: `"{Color} {Pattern}"` 形式

---

## ソースファイル一覧

| ファイル | パス（`mod/ckenja/karacreate/` 基準） | 役割 |
|---|---|---|
| `KaraCreateBannerPatterns.java` | `foundation/register/` | 15パターンの登録 |
| `KaraCreateItems.java` | `foundation/register/` | パターンアイテム登録 |
| `KaraCreateBannerPatternTagsProvider.java` | `infrastructure/data/` | バナーパターンタグ生成 |
| `BannerPatternLangGenerators.java` | `infrastructure/lang/` | パターン翻訳生成 |

---

## 既知の問題

1. **翻訳不完全**: 15パターン中5パターンのみ翻訳が生成されている
2. **テクスチャとenumの不整合**: enum に存在しないテクスチャ (`wave`, `cheese`, `eternal`) と、enum にあるがテクスチャが見つからないパターンがある
3. **リネーム/削除の痕跡**: テクスチャファイル名と enum 名の対応が崩れており、開発途中のリネームが行われた可能性がある
