# 既知の問題・不整合

本ドキュメントはコードベース調査で発見された問題・不整合を記録する。

---

## KI-01: Mixin 設定の重複エントリ

**ファイル**: `src/main/resources/karacreate.mixins.json`

**問題**: `AllBlockEntityTypesMixin` が `mixins` 配列に **2回** 登録されている。

```json
"mixins": [
    "AllBlockEntityTypesMixin",
    "AllBlockEntityTypesMixin",  // ← 重複
    ...
]
```

**影響**: Mixin フレームワークは重複を無視している模様で、実行時エラーは発生していない。しかし意図しない動作の原因となる可能性がある。

**修正**: 2行目の `"AllBlockEntityTypesMixin"` を削除する。

**重大度**: 低

---

## KI-02: バナーパターン翻訳の欠落（10/15パターン）

**ファイル**: `infrastructure/lang/BannerPatternLangGenerators.java`

**問題**: 15パターン中5パターン（checkered, hash, hash4, circle, mountain）のみ翻訳が生成される。以下の10パターンは翻訳がない:

| enum | 翻訳キー | 状態 |
|---|---|---|
| `VERT_LEFT` | `block.minecraft.banner.karacreate.vert_left.*` | 翻訳なし |
| `VERT_RIGHT` | `block.minecraft.banner.karacreate.vert_right.*` | 翻訳なし |
| `BAMBOO` | `block.minecraft.banner.karacreate.bamboo.*` | 翻訳なし |
| `HEMP` | `block.minecraft.banner.karacreate.hemp.*` | 翻訳なし |
| `BASKET` | `block.minecraft.banner.karacreate.basket.*` | 翻訳なし |
| `VAPOR` | `block.minecraft.banner.karacreate.vapor.*` | 翻訳なし |
| `PINE` | `block.minecraft.banner.karacreate.pine.*` | 翻訳なし |
| `PAMPAS` | `block.minecraft.banner.karacreate.pampas.*` | 翻訳なし |
| `HAZE` | `block.minecraft.banner.karacreate.haze.*` | 翻訳なし |
| `ARROW` | `block.minecraft.banner.karacreate.arrow.*` | 翻訳なし |

**影響**: ゲーム内でこれらのパターンをバナーに適用すると翻訳キーがそのまま表示される。

**修正**: `BannerPatternLangGenerators` の `patternTranslations`（`en_us` / `ja_jp` 両方）にエントリを追加する。

**重大度**: 中

---

## KI-03: バナーテクスチャと enum の不整合

**問題**: テクスチャファイルと enum の対応が一致しない。

### テクスチャあり・enum なし（余剰テクスチャ）

| テクスチャ | enum |
|---|---|
| `textures/entity/banner/wave.png` | なし |
| `textures/entity/banner/cheese.png` | なし |
| `textures/entity/banner/eternal.png` | なし（コメントアウト済み: `//ETERNAL`） |

### enum あり・テクスチャ未確認

| enum | 期待されるテクスチャ |
|---|---|
| `HEMP` | `hemp.png` |
| `BASKET` | `basket.png` |
| `VAPOR` | `vapor.png` |
| `PINE` | `pine.png` |
| `PAMPAS` | `pampas.png` |
| `HAZE` | `haze.png` |
| `ARROW` | `arrow.png` |
| `VERT_LEFT` | `vert_left.png` |
| `VERT_RIGHT` | `vert_right.png` |
| `BAMBOO` | `bamboo.png` |

> 注: `vert_left.png`, `vert_right.png`, `bamboo.png` はテクスチャが確認されている。残り7パターンは `textures/entity/banner/` にファイルが存在しない可能性がある（プロジェクトのファイル一覧では確認されていない）。

**影響**: 余剰テクスチャは使用されないが容量を消費する。テクスチャなしパターンはゲーム内でミッシングテクスチャ（紫黒）が表示される。

**修正**: 
1. `wave.png`, `cheese.png` の使途を確認し、不要なら削除
2. `eternal.png` は `ETERNAL` enum のコメントアウト解除で使用可能にするか、削除
3. テクスチャのないパターンにテクスチャを追加

**重大度**: 中

---

## KI-04: 小型ドアのクラフトレシピ欠落

**問題**: `small_shoji_door` と `small_fusuma_door` のクラフトレシピが `data/karacreate/recipes/` に存在しない。

**影響**: サバイバルモードでは入手不可（クリエイティブタブまたはコマンドのみ）。

**修正**: レシピ JSON を追加する。

**重大度**: 高

---

## KI-05: メカニカルコンポスターのクラフトレシピ欠落

**問題**: `mechanical_composter` のクラフトレシピが存在しない。

**影響**: サバイバルモードでは入手不可。

**修正**: レシピ JSON を追加する。

**重大度**: 高

---

## KI-06: small_shoji_door の axeOnly タグ欠落

**ファイル**: `foundation/register/KaraCreateBlocks.java`

**問題**: `SMALL_FUSUMA_DOOR` には `.transform(axeOnly())` があるが、`SMALL_SHOJI_DOOR` にはない。

```java
// small_shoji_door — axeOnly なし
REGISTRATE.block("small_shoji_door", ...)
    .transform(smallDoor(false))
    ...

// small_fusuma_door — axeOnly あり
REGISTRATE.block("small_fusuma_door", ...)
    .transform(smallDoor(true))
    .transform(axeOnly())  // ← これ
    ...
```

**影響**: `small_shoji_door` は `mineable/axe` タグに含まれず、どの道具でも同じ速度で破壊される。

**修正**: `SMALL_SHOJI_DOOR` の登録に `.transform(axeOnly())` を追加する。

**重大度**: 中

---

## KI-07: 小型ドアが minecraft:doors タグに未登録

**問題**: `small_shoji_door` と `small_fusuma_door` は `minecraft:doors` / `minecraft:wooden_doors` タグに含まれていない（`karacreate:small_door` タグのみ）。

**影響**: 
- バニラの村人AIがこれらをドアと認識しない
- `#minecraft:doors` を参照するレシピや条件に一致しない

**修正**: `smallDoor()` メソッド内に `.tag(BlockTags.DOORS)` を追加する（設計判断次第）。

**重大度**: 低（意図的な可能性あり — 1ブロック高のドアは通常のドアと異なるため）

---

## KI-08: テスト用コンポスティングレシピ

**ファイル**: `data/karacreate/recipes/composting_test.json`

**問題**: ファイル名に `test` が含まれ、テスト用であることを示唆。入力指定がなく `minecraft:bone_meal` × 2 を出力する。

**影響**: リリースビルドに含まれるとゲームバランスに影響する可能性がある。

**修正**: 本番レシピとして整備するか、リリース前に削除する。

**重大度**: 中

---

## KI-09: minecraft テクスチャのパスが blocks（複数形）

**ファイル**: `src/main/resources/assets/minecraft/textures/blocks/`

**問題**: バニラの正式パスは `textures/block/`（単数形）だが、KaraCreate は `textures/blocks/`（複数形）を使用。

**影響**: リソースパックレベルでの上書きが意図通り動作しない可能性がある。ただし、一部の Minecraft バージョン/ローダーでは `blocks/` も認識される場合がある。

**修正**: パスを `textures/block/` に変更して検証する。

**重大度**: 要調査

---

## KI-10: Create mod テクスチャの上書き

**ファイル**: `src/main/resources/assets/create/textures/block/`

**問題**: 7つの Create mod テクスチャをリソースパックレベルで上書きしている（axis.png, axis_top.png, cogwheel_axis.png, fan_blades.png, gauge.png, large_cogwheel.png, stripped_spruce_log.png）。

**影響**: 
- 他のリソースパックや Mod と競合する可能性
- Create mod のアップデートでテクスチャが変更された場合に不整合が生じる
- 全ての歯車/シャフトの見た目が変わるため、コンポスター以外のマシンにも影響

**修正**: カスタムモデルで参照先を独自テクスチャに変更し、Create のデフォルトテクスチャを上書きしないようにする。

**重大度**: 高

---

## KI-11: snow_viewing_shoji_door のバナーサポート不整合

**問題**: `snow_viewing_shoji_door` は `SlidingDoorBlock` を使用しているが（`PaperDoorBlock` ではない）、`karacreate:paper_door` ブロックタグには含まれている。

```java
// PaperDoorBlock を使用 → バナー描画可
public static final BlockEntry<PaperDoorBlock> SHOJI_DOOR = ...
public static final BlockEntry<PaperDoorBlock> FUSUMA_DOOR = ...

// SlidingDoorBlock を使用 → バナー描画不可
public static final BlockEntry<SlidingDoorBlock> SNOW_VIEWING_SHOJI_DOOR = ...
```

ただし `karacreate:paper_door` **アイテムタグ**には含まれていない（`paperDoorItem(false)` で `useRender=false` のため）。

**影響**: ブロックタグの `paper_door` に含まれているが、実際にはバナーパターン適用ができない。タグの意味が曖昧になる。

**修正**: タグの意味を再定義するか、`snow_viewing_shoji_door` を `paper_door` ブロックタグから除外する。

**重大度**: 低

---

## KI-12: BlockSetType 名 "paper" の曖昧さ

**ファイル**: `foundation/register/KaraCreateBlocks.java`

```java
public static final BlockSetType KARACREATE_WOOD = new BlockSetType("paper", ...);
```

**問題**: `BlockSetType` の名前が `"paper"` だが、Mod ID やプレフィックスがない。他の Mod が同名の `BlockSetType` を登録した場合に競合する可能性がある。

**修正**: `"karacreate_paper"` のように Mod ID プレフィックスを付与する。

**重大度**: 低

---

## KI-13: ja_jp に JEI RPM 範囲翻訳キー欠落

**ファイル**: `src/main/resources/assets/karacreate/lang/default/ja_jp.json`

**問題**: `karacreate.jei.range_of_composting` キーが ja_jp のデフォルト言語ファイルに存在しない。

**影響**: 日本語環境で JEI のコンポスティングレシピ表示時に RPM 範囲が英語のまま。

**修正**: `ja_jp.json` に追加:
```json
"karacreate.jei.range_of_composting": "%1$s~%2$s RPM"
```

**重大度**: 低

---

## KI-14: KaraCraeteRecipeSerializer のタイプミス

**ファイル**: `foundation/register/KaraCraeteRecipeSerializer.java`

**問題**: クラス名が `KaraCraeteRecipeSerializer`（`Create` ではなく `Craete`）。

**影響**: 機能に影響なし。コードの可読性の問題。

**修正**: `KaraCreateRecipeSerializer` にリネーム（import 元全てを更新）。

**重大度**: 低

---

## 重大度サマリ

| 重大度 | 件数 | 問題 |
|---|---|---|
| 高 | 3 | KI-04, KI-05, KI-10 |
| 中 | 4 | KI-02, KI-03, KI-06, KI-08 |
| 低 | 6 | KI-01, KI-07, KI-11, KI-12, KI-13, KI-14 |
| 要調査 | 1 | KI-09 |
