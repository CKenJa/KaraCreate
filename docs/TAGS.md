# タグ体系

## 概要

KaraCreate は自前のタグ、バニラタグへの追加、Create タグへの追加、Quark タグへの追加を行う。

---

## KaraCreate 独自タグ

### ブロックタグ

#### `karacreate:paper_door`

PaperDoor系ブロックを集約するタグ。装飾レシピの入力判定に使用。

| ブロック |
|---|
| `karacreate:shoji_door` |
| `karacreate:snow_viewing_shoji_door` |
| `karacreate:fusuma_door` |

**定義元**: `KaraCreateBlocks.paperDoorItem()` → `.tag(KaraCreateTags.BlockTags.PAPER_DOOR.tag)`
**生成先**: `src/generated/resources/data/karacreate/tags/blocks/paper_door.json`

#### `karacreate:small_door`

SmallDoor系ブロックを集約するタグ。

| ブロック |
|---|
| `karacreate:small_shoji_door` |
| `karacreate:small_fusuma_door` |

**定義元**: `KaraCreateBlocks.smallDoor()` → `.tag(KaraCreateTags.BlockTags.SMALL_DOOR.tag)`
**生成先**: `src/generated/resources/data/karacreate/tags/blocks/small_door.json`

### アイテムタグ

#### `karacreate:paper_door`

PaperDoorBlock のアイテム版。`useRender=true` のドアのみ。カスタムレンダラー(PaperDoorItemRenderer)によるインベントリ描画の対象判定に使用。

| アイテム |
|---|
| `karacreate:shoji_door` |
| `karacreate:fusuma_door` |

> **注意**: `snow_viewing_shoji_door` は `useRender=false` で登録されるため、このアイテムタグには**含まれない**（ブロックタグには含まれる）。

**定義元**: `KaraCreateBlocks.paperDoorItem()` → `useRender=true` の場合のみ `.tag(KaraCreateTags.ItemTags.PAPER_DOOR.tag)`
**生成先**: `src/generated/resources/data/karacreate/tags/items/paper_door.json`

#### `karacreate:small_door`

SmallDoor系のアイテムタグ。

| アイテム |
|---|
| `karacreate:small_shoji_door` |
| `karacreate:small_fusuma_door` |

**定義元**: `KaraCreateBlocks.smallDoor()` → `.tag(KaraCreateTags.ItemTags.SMALL_DOOR.tag)`
**生成先**: `src/generated/resources/data/karacreate/tags/items/small_door.json`

### バナーパターンタグ

#### `karacreate:pattern_item/japan`

全15パターンを束ねるタグ。パターンアイテムが織機で使用可能になるのはこのタグによる。

| パターン |
|---|
| `karacreate:karacreate_checkered` |
| `karacreate:karacreate_hash` |
| `karacreate:karacreate_hash4` |
| `karacreate:karacreate_circle` |
| `karacreate:karacreate_mountain` |
| `karacreate:karacreate_vert_left` |
| `karacreate:karacreate_vert_right` |
| `karacreate:karacreate_bamboo` |
| `karacreate:karacreate_hemp` |
| `karacreate:karacreate_basket` |
| `karacreate:karacreate_vapor` |
| `karacreate:karacreate_pine` |
| `karacreate:karacreate_pampas` |
| `karacreate:karacreate_haze` |
| `karacreate:karacreate_arrow` |

**定義元**: `KaraCreateBannerPatternTagsProvider`
**生成先**: `src/generated/resources/data/karacreate/tags/banner_pattern/pattern_item/japan.json`（ディレクトリは存在するがファイル内容は生成時に作成）

---

## バニラタグへの追加

### `minecraft:doors` (Block)

| 追加ブロック |
|---|
| `karacreate:shoji_door` |
| `karacreate:fusuma_door` |
| `karacreate:snow_viewing_shoji_door` |

> 小ドア（small_shoji_door, small_fusuma_door）は **含まれない**

**定義元**: `KaraCreateBlocks.woodenSlidingDoor()` → `.tag(BlockTags.DOORS)`
**生成先**: `src/generated/resources/data/minecraft/tags/blocks/doors.json`

### `minecraft:wooden_doors` (Block)

`minecraft:doors` と同一のブロック。村人AIのドア認識に使用。

**定義元**: `KaraCreateBlocks.woodenSlidingDoor()` → `.tag(BlockTags.WOODEN_DOORS)`
**生成先**: `src/generated/resources/data/minecraft/tags/blocks/wooden_doors.json`

### `minecraft:doors` (Item)

| 追加アイテム |
|---|
| `karacreate:shoji_door` |
| `karacreate:fusuma_door` |
| `karacreate:snow_viewing_shoji_door` |

**定義元**: `KaraCreateBlocks.paperDoorItem()` → `.tag(ItemTags.DOORS)`
**生成先**: `src/generated/resources/data/minecraft/tags/items/doors.json`

### `minecraft:mineable/axe` (Block)

| 追加ブロック |
|---|
| `karacreate:shoji_door` |
| `karacreate:fusuma_door` |
| `karacreate:snow_viewing_shoji_door` |
| `karacreate:small_fusuma_door` |

> `small_shoji_door` は `axeOnly` が未適用なので **含まれない**

**定義元**: `woodenSlidingDoor()` → `axeOnly()` / `SMALL_FUSUMA_DOOR` → `.transform(axeOnly())`
**生成先**: `src/generated/resources/data/minecraft/tags/blocks/mineable/axe.json`（推定）

### `minecraft:mineable/pickaxe` (Block)

| 追加ブロック |
|---|
| `karacreate:mechanical_composter` |

**定義元**: `COMPOSTER` → `pickaxeOnly()`

---

## 外部Modタグへの追加

### `create:contraption_controlled` (Item)

Createのコントラプション（移動構造体）で制御可能なドアを示すタグ。

| 追加アイテム |
|---|
| `karacreate:shoji_door` |
| `karacreate:fusuma_door` |
| `karacreate:snow_viewing_shoji_door` |

**定義元**: `KaraCreateBlocks.paperDoorItem()` → `.tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)`
**生成先**: `src/generated/resources/data/create/tags/items/contraption_controlled.json`

### `quark:non_double_door` (Block)

Quark modの「ダブルドア連動」機能から除外するタグ。スライドドアがダブルドアとして連動しないようにする。

| 追加ブロック |
|---|
| `karacreate:shoji_door` |
| `karacreate:fusuma_door` |
| `karacreate:snow_viewing_shoji_door` |

**定義元**: `KaraCreateBlocks.woodenSlidingDoor()` → `.tag(AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag)`
**生成先**: `src/generated/resources/data/quark/tags/blocks/non_double_door.json`

---

## タグ定義ソース

**ソース**: `foundation/register/KaraCreateTags.java`

```java
public class KaraCreateTags {
    public enum BlockTags {
        SMALL_DOOR,
        PAPER_DOOR;
        // tag フィールドあり
    }
    
    public enum ItemTags {
        SMALL_DOOR,
        PAPER_DOOR;
        // tag フィールドあり
    }
    
    // BannerPattern タグも定義
    JAPAN_BANNER_PATTERN
}
```

---

## タグ関係図

```
minecraft:doors (Block)
  ├── karacreate:shoji_door
  ├── karacreate:fusuma_door
  └── karacreate:snow_viewing_shoji_door

minecraft:wooden_doors (Block) = minecraft:doors (Block) と同一

karacreate:paper_door (Block)
  ├── karacreate:shoji_door
  ├── karacreate:snow_viewing_shoji_door
  └── karacreate:fusuma_door

karacreate:paper_door (Item)      ← snow_viewing_shoji_door は含まれない
  ├── karacreate:shoji_door
  └── karacreate:fusuma_door

karacreate:small_door (Block & Item)
  ├── karacreate:small_shoji_door
  └── karacreate:small_fusuma_door

create:contraption_controlled (Item)
  ├── karacreate:shoji_door
  ├── karacreate:fusuma_door
  └── karacreate:snow_viewing_shoji_door
```
