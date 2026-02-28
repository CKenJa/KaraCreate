# レシピ仕様

## 概要

KaraCreate は3つのレシピシステムを使用する:
1. **Shaped Crafting** — バニラの形定めクラフト
2. **Custom Crafting** — カスタムクラフトロジック（ドア装飾）
3. **Composting** — 独自レシピタイプ（メカニカルコンポスター用）

---

## クラフトレシピ（Shaped Crafting）

### 1. 障子 (Shoji Door)

**ファイル**: `data/karacreate/recipes/shoji_door.json`

| 項目 | 値 |
|---|---|
| タイプ | `minecraft:crafting_shaped` |
| 出力 | `karacreate:shoji_door` ×1 |

**パターン**:
```
PP
WW
PP
```

| キー | アイテム |
|---|---|
| `P` | `minecraft:oak_planks` (オーク板材) |
| `W` | `minecraft:paper` (紙) |

### 2. 襖 (Fusuma Door)

**ファイル**: `data/karacreate/recipes/fusuma_door.json`

| 項目 | 値 |
|---|---|
| タイプ | `minecraft:crafting_shaped` |
| 出力 | `karacreate:fusuma_door` ×1 |

**パターン**:
```
PP
WW
PP
```

| キー | アイテム |
|---|---|
| `P` | `minecraft:dark_oak_planks` (ダークオーク板材) |
| `W` | `minecraft:paper` (紙) |

### 3. 雪見障子 (Snow Viewing Shoji Door)

**ファイル**: `data/karacreate/recipes/snow_viewing_shoji_door.json`

| 項目 | 値 |
|---|---|
| タイプ | `minecraft:crafting_shaped` |
| 出力 | `karacreate:snow_viewing_shoji_door` ×1 |

**パターン**:
```
PP
WW
PP
```

| キー | アイテム |
|---|---|
| `P` | `minecraft:bamboo` (竹) |
| `W` | `minecraft:paper` (紙) |

---

## カスタムクラフトレシピ（ドア装飾）

### 4. ドア装飾 (Paper Door Decoration)

**ファイル**: `data/karacreate/recipes/paper_door_decoration.json`

| 項目 | 値 |
|---|---|
| タイプ | `karacreate:crafting_paper_door_decoration` |
| カテゴリ | `misc` |
| シリアライザ | `KaraCraeteRecipeSerializer.PAPER_DOOR_DECORATION` |
| ロジック | `PaperDoorDecorationRecipe.java` |

**入力**: バナーアイテム（任意色・任意柄）+ `karacreate:paper_door` タグ付きドア
**出力**: バナーの色と柄がコピーされたドアアイテム ×1

**制約**:
- 既に装飾済みのドア（BlockEntityData がある）は入力不可
- 入力ドアが消費され、新しいドアアイテムが出力される

**JEI 表示**: `PaperDoorDecorationRecipeMaker` が動的に全色のバナー+ドアの組み合わせを生成してJEIに表示。

---

## コンポスティングレシピ

### 5. コンポスティングテスト (Composting Test)

**ファイル**: `data/karacreate/recipes/composting_test.json`

| 項目 | 値 |
|---|---|
| タイプ | `karacreate:composting` |
| minSpeed | `0.0` RPM |
| maxSpeed | `128.0` RPM |
| processingTime | `100` tick (5秒) |

**出力**:
| アイテム | 数量 |
|---|---|
| `minecraft:bone_meal` | 2 |

**入力**: 明示的な入力指定なし。バニラの `COMPOSTABLES` マップに含まれるアイテムであれば何でも投入可能。

---

## レシピシリアライザ

| シリアライザ | 登録名 | 対象 |
|---|---|---|
| `SimpleCraftingRecipeSerializer<PaperDoorDecorationRecipe>` | `karacreate:crafting_paper_door_decoration` | ドア装飾 |
| `CompostingRecipeSerializer` | `karacreate:composting` | コンポスティング |

### CompostingRecipeSerializer JSON 解析

```json
{
  "type": "karacreate:composting",
  "minSpeed": float,       // 必須: 最低RPM
  "maxSpeed": float,       // 必須: 最高RPM
  "processingTime": int,   // 必須: 処理時間(tick)
  "results": [             // 必須: 出力配列
    {
      "item": "modid:item_id",
      "count": int
      // ProcessingOutput 形式（Create 準拠）
    }
  ]
}
```

---

## レシピ未定義のコンテンツ

以下のブロックにはクラフトレシピが存在しない:

| ブロック | 備考 |
|---|---|
| `karacreate:small_shoji_door` | レシピJSON無し |
| `karacreate:small_fusuma_door` | レシピJSON無し |
| `karacreate:mechanical_composter` | レシピJSON無し |

---

## ソースファイル一覧

| ファイル | 役割 |
|---|---|
| `content/paperDoor/PaperDoorDecorationRecipe.java` | ドア装飾レシピロジック |
| `content/composter/CompostingRecipe.java` | コンポスティングレシピ定義 |
| `content/composter/CompostingRecipeSerializer.java` | コンポスティングレシピJSON解析 |
| `foundation/register/KaraCreateRecipeTypes.java` | レシピタイプ登録 |
| `foundation/register/KaraCraeteRecipeSerializer.java` | シリアライザ登録 |
| `compat/jei/PaperDoorDecorationRecipeMaker.java` | JEI用レシピ動的生成 |
