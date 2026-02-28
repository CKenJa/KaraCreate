# 障子・襖ドア（PaperDoor 系）仕様

## 概要

日本の伝統的な引き戸をCreate modのスライドドアシステムに統合したブロック群。
バナーの模様をドア面に描画できる「PaperDoor」タイプと、模様なしの「SlidingDoor」タイプがある。

---

## ブロック一覧

### 1. 障子 (Shoji Door)

| 項目 | 値 |
|---|---|
| 登録ID | `karacreate:shoji_door` |
| クラス | `PaperDoorBlock` (extends `SlidingDoorBlock`) |
| 日本語名 | 障子 |
| MapColor | `MapColor.WOOD` |
| 初期プロパティ元 | `Blocks.BAMBOO_DOOR` |
| 硬さ/爆発耐性 | `3.0F` / `6.0F` |
| 必要ツール | 斧 (`axeOnly`) |
| ノブ（取っ手）| なし (`knob = false`) |
| レンダータイプ | `RenderType.cutoutMipped` |
| BlockSetType | `KARACREATE_WOOD` (竹木の音) |
| バナー柄対応 | あり（`PaperDoorBehaviour`） |
| Create連携 | `DoorMovingInteraction` + `SlidingDoorMovementBehaviour` |

### 2. 襖 (Fusuma Door)

| 項目 | 値 |
|---|---|
| 登録ID | `karacreate:fusuma_door` |
| クラス | `PaperDoorBlock` (extends `SlidingDoorBlock`) |
| 日本語名 | 襖 |
| MapColor | `MapColor.COLOR_BLACK` |
| 初期プロパティ元 | `Blocks.BAMBOO_DOOR` |
| 硬さ/爆発耐性 | `3.0F` / `6.0F` |
| 必要ツール | 斧 (`axeOnly`) |
| ノブ（取っ手）| あり (`knob = true`) |
| レンダータイプ | `RenderType.cutoutMipped` |
| BlockSetType | `KARACREATE_WOOD` (竹木の音) |
| バナー柄対応 | あり（`PaperDoorBehaviour`） |
| Create連携 | `DoorMovingInteraction` + `SlidingDoorMovementBehaviour` |

### 3. 雪見障子 (Snow Viewing Shoji Door)

| 項目 | 値 |
|---|---|
| 登録ID | `karacreate:snow_viewing_shoji_door` |
| クラス | `SlidingDoorBlock`（Create 本体クラス直接使用） |
| 日本語名 | 雪見障子 |
| MapColor | `MapColor.WOOD` |
| 初期プロパティ元 | `Blocks.BAMBOO_DOOR` |
| 硬さ/爆発耐性 | `3.0F` / `6.0F` |
| 必要ツール | 斧 (`axeOnly`) |
| ノブ（取っ手）| なし (`knob = false`) |
| レンダータイプ | `RenderType.cutoutMipped` |
| BlockSetType | `KARACREATE_WOOD` (竹木の音) |
| バナー柄対応 | なし（`SlidingDoorBlock` 直接のため） |
| Create連携 | `DoorMovingInteraction` + `SlidingDoorMovementBehaviour` |

---

## ブロックステートプロパティ

全ドアは `DoorBlock` / `SlidingDoorBlock` のプロパティを持つ:

| プロパティ | 型 | 値 |
|---|---|---|
| `facing` | `Direction` | `NORTH`, `SOUTH`, `EAST`, `WEST` |
| `hinge` | `DoorHingeSide` | `LEFT`, `RIGHT` |
| `half` | `DoubleBlockHalf` | `LOWER`, `UPPER` |
| `open` | `boolean` | `true`, `false` |
| `powered` | `boolean` | `true`, `false` |
| `visible` | `boolean` | `true`, `false`（SlidingDoorBlock固有、Create のスライドアニメーション制御用） |

ブロックステートバリアント数: 4方向 × 2ヒンジ × 2上下 × 2開閉 × 2可視 = **64バリアント**

---

## BlockSetType: KARACREATE_WOOD

```java
new BlockSetType("paper", true, SoundType.BAMBOO_WOOD,
    SoundEvents.BAMBOO_WOOD_DOOR_CLOSE,
    SoundEvents.BAMBOO_WOOD_DOOR_OPEN,
    SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE,
    SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN,
    SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF,
    SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON,
    SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF,
    SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON
)
```

| パラメータ | 値 | 意味 |
|---|---|---|
| 名前 | `"paper"` | 登録名 |
| 手で開けるか | `true` | プレイヤーが手で開閉できる |
| 音タイプ | `SoundType.BAMBOO_WOOD` | 竹木の足音・設置音 |
| ドア開閉音 | 竹木ドアの開閉音 | 日本建築風 |

---

## PaperDoorBehaviour（バナー柄データ管理）

PaperDoorBlock 設置時に、手持ちのアイテムからバナー柄データを読み取り保存する。

**ソース**: `content/paperDoor/PaperDoorBehaviour.java`

### データ構造

| フィールド | 型 | NBTキー | 説明 |
|---|---|---|---|
| `itemPatterns` | `ListTag` (nullable) | `Patterns` | バナー柄の配列 |
| `color` | `DyeColor` (nullable) | `Base` | ベース染料色 |
| `patterns` | キャッシュ | なし | `List<Pair<Holder<BannerPattern>, DyeColor>>` |

### fromItem の動作

1. `BannerBlockEntity.getItemPatterns(item)` でアイテムのパターンを取得
2. `ShieldItem.getColor(item)` でベース色を取得
3. `patterns` キャッシュをクリア（次回 `getPatterns()` 呼び出し時に再生成）

### getPatterns の動作

1. キャッシュが null なら `BannerBlockEntity.createPatterns(color, itemPatterns)` で生成
2. `color` が null の場合は `DyeColor.WHITE` をデフォルトとして使用

---

## PaperDoorDecorationRecipe（装飾レシピ）

バナーアイテム + PaperDoorアイテム → 柄付きドア のクラフトレシピ。

**ソース**: `content/paperDoor/PaperDoorDecorationRecipe.java`
**レシピタイプ**: `karacreate:crafting_paper_door_decoration`
**最小クラフトサイズ**: 2スロット以上 (`pWidth * pHeight >= 2`)

### matches の条件

1. クラフトグリッド内にバナーアイテム（`BannerItem`）がちょうど1個
2. クラフトグリッド内に `karacreate:paper_door` タグ付きアイテムがちょうど1個
3. ドアアイテムに既存のBlockEntityData がないこと（装飾済みドアは不可）
4. その他のスロットは空であること

### assemble の出力

1. バナーの `BlockEntityData` を取得
2. `Base` (ベース色ID) を追加
3. ドアアイテムに `BlockEntityType.BANNER` タイプで BlockEntityData を設定
4. 出力数: 1

---

## レンダリング

### PaperDoorBlockRenderer

**ソース**: `content/paperDoor/PaperDoorBlockRenderer.java`
**タイプ**: `BlockEntityRenderer<SlidingDoorBlockEntity>`

#### モデル構造 (createBodyLayer)

- テクスチャサイズ: 64×64
- 2つのクワッド（前面と背面）で構成
- ドアの面にバナー柄を描画

#### 描画フロー

1. `SlidingDoorBlockEntityAccessor` からアニメーション値を取得
2. ドアのオフセットを計算（ヒンジ方向と開閉アニメーション考慮）
3. 標準のドアブロックモデルを描画
4. `PaperDoorBehaviour` からパターンデータを取得
5. Minecraft 標準の `BannerRenderer` を利用してバナー柄を描画
6. 上半分/下半分の制御あり

### PaperDoorItemRenderer

**ソース**: `content/paperDoor/PaperDoorItemRenderer.java`
**タイプ**: `BlockEntityWithoutLevelRenderer`

インベントリ/手持ち描画用。一時的な `PaperDoorBlockEntity` を生成し、アイテムスタックからバナーデータを抽出して描画する。

### PaperDoorBlockItem

**ソース**: `content/paperDoor/PaperDoorBlockItem.java`
**タイプ**: `BlockItem` (extends)

`IClientItemExtensions` を通じて `PaperDoorItemRenderer` をカスタムレンダラーとして登録。

---

## タグ

| タグ | 含まれるブロック/アイテム |
|---|---|
| `minecraft:doors` (Block) | 障子、襖、雪見障子 |
| `minecraft:wooden_doors` (Block) | 障子、襖、雪見障子 |
| `quark:non_double_door` (Block) | 障子、襖、雪見障子 |
| `karacreate:paper_door` (Block) | 障子、雪見障子、襖 |
| `karacreate:paper_door` (Item) | 障子、襖（雪見障子は `useRender=false` のため含まれない） |
| `minecraft:doors` (Item) | 障子、襖、雪見障子 |
| `create:contraption_controlled` (Item) | 障子、襖、雪見障子 |

---

## テクスチャ

| ファイル | 用途 |
|---|---|
| `textures/block/shoji_door_bottom.png` | 障子 下半分 |
| `textures/block/shoji_door_top.png` | 障子 上半分 |
| `textures/block/shoji_door_side.png` | 障子 側面 |
| `textures/block/fusuma_door_bottom.png` | 襖 下半分 |
| `textures/block/fusuma_door_top.png` | 襖 上半分 |
| `textures/block/fusuma_door_side.png` | 襖 側面 |
| `textures/block/snow_viewing_shoji_door_bottom.png` | 雪見障子 下半分 |
| `textures/block/snow_viewing_shoji_door_top.png` | 雪見障子 上半分 |
| `textures/block/snow_viewing_shoji_door_side.png` | 雪見障子 側面 |

---

## レシピ

→ [RECIPES.md](RECIPES.md) に詳細

| レシピ | タイプ | 入力 | 出力 |
|---|---|---|---|
| 障子クラフト | shaped | オーク板材×4 + 紙×2 | 障子×1 |
| 襖クラフト | shaped | ダークオーク板材×4 + 紙×2 | 襖×1 |
| 雪見障子クラフト | shaped | 竹×4 + 紙×2 | 雪見障子×1 |
| ドア装飾 | `crafting_paper_door_decoration` | バナー + PaperDoor | 柄付きドア×1 |

---

## ブロックステートモデル

### ノブあり（fusuma_door）
- `bottom_left`, `bottom_right`, `top_left`, `top_right` の4モデル
- `doorBlock()` でヒンジ/向き/上下に応じてモデルを切り替え

### ノブなし（shoji_door, snow_viewing_shoji_door）
- `bottom`, `top` の2モデル
- 左右の区別なし

---

## ソースファイル一覧

| ファイル | パス（`content/paperDoor/` 基準） | 役割 |
|---|---|---|
| `PaperDoorBlock.java` | `content/paperDoor/` | ブロック定義、設置時のバナーデータ初期化 |
| `PaperDoorBehaviour.java` | `content/paperDoor/` | バナー柄データの保持・NBT永続化 |
| `PaperDoorBlockRenderer.java` | `content/paperDoor/` | ワールド内描画（ドア面にバナー柄） |
| `PaperDoorBlockItem.java` | `content/paperDoor/` | カスタムアイテムクラス（描画委譲） |
| `PaperDoorItemRenderer.java` | `content/paperDoor/` | インベントリ/手持ち描画 |
| `PaperDoorDecorationRecipe.java` | `content/paperDoor/` | バナー→ドアの装飾クラフトレシピ |
| `KaraCreateBlocks.java` | `foundation/register/` | ブロック登録定義 |
| `KaraCreateBlockEntityTypes.java` | `foundation/register/` | PAPER_DOOR BlockEntity登録 |
| `KaraCreateModelLayers.java` | `foundation/render/` | PAPER_DOOR モデルレイヤー定義 |

---

## 既知の問題

- 雪見障子は `SlidingDoorBlock` を直接使用しており `PaperDoorBlock` ではないため、バナー柄の適用不可。ただし `karacreate:paper_door` ブロックタグには含まれている（名前の不整合）
- `KARACREATE_WOOD` の登録名が `"paper"` であり、BlockSetType 名とModの対応が不明瞭
