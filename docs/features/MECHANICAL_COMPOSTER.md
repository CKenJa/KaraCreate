# メカニカルコンポスター (Mechanical Composter) 仕様

## 概要

Create mod の回転力システムで動作する自動コンポスター。アイテムを上に落とすかホッパーで入力し、回転速度に応じたレシピで加工して出力する。バニラコンポスターの見た目と挙動を拡張し、回転力によるレシピ分岐を持つ。

---

## ブロック定義

| 項目 | 値 |
|---|---|
| 登録ID | `karacreate:mechanical_composter` |
| クラス | `MechanicalComposterBlock` (extends `RotatedPillarKineticBlock`, implements `IBE<ComposterBlockEntity>`, `ICogWheel`) |
| 日本語名 | メカニカルコンポスター |
| MapColor | `MapColor.METAL` |
| 初期プロパティ元 | `SharedProperties.stone` |
| 透過 | `noOcclusion()` |
| 必要ツール | ツルハシ (`pickaxeOnly`) |
| ストレスインパクト | `4.0` |
| レンダータイプ | `RenderType.cutoutMipped` |
| 歯車タイプ | **大歯車** (`isLargeCog() = true`) |
| 回転軸 | Y軸固定 (`getRotationAxis() = Direction.Axis.Y`) |
| シャフト方向 | 上面のみ (`hasShaftTowards() = face == Direction.UP`) |
| レッドストーンコンパレータ | 対応（`LEVEL` 値をそのまま出力） |
| レンチ | `InteractionResult.SUCCESS`（回転なし） |

---

## ブロックステートプロパティ

| プロパティ | 型 | 値範囲 | 元 |
|---|---|---|---|
| `LEVEL` | `IntegerProperty` | 0～8 | `ComposterBlock.LEVEL` を流用 |
| `AXIS` | `EnumProperty<Direction.Axis>` | `X`, `Y`, `Z` | `RotatedPillarKineticBlock` から（ただしY固定） |

> **注意**: `getStateForPlacement` は常に `defaultBlockState()` を返すため、`AXIS` は常に `Y`。`rotate` も状態を変更しない。

---

## VoxelShape

コンポストレベルに応じて形状が変化:

```java
SHAPES[i] = Shapes.join(
    Shapes.block(),
    Block.box(2.0D, Math.max(2, 1 + i * 2), 2.0D, 14.0D, 16.0D, 14.0D),
    BooleanOp.ONLY_FIRST
)
```

| LEVEL | 内側底面 Y (min) | 説明 |
|---|---|---|
| 0 | 2 | 空 |
| 1 | 3 | 少量 |
| 2 | 5 | |
| 3 | 7 | |
| 4 | 9 | |
| 5 | 11 | |
| 6 | 13 | |
| 7 | 15 | ほぼ満杯 |
| 8 | 15 (= SHAPES[7]) | 完成（7と同じ形状） |

---

## インタラクション

### アイテム落下（上面投下）

`updateEntityAfterFallOn` で処理:

1. 落ちたエンティティが `ItemEntity` かつ生存中であることを確認
2. `Iterate.hereAndBelow` で現在位置と1ブロック下をチェック
3. `ForgeCapabilities.ITEM_HANDLER` でアイテムを `insertItem(0, ...)` で入力
4. 完全に挿入できた場合は `ItemEntity` を破棄、部分的な場合は残りをセット

### プレイヤー右クリック（空手）

`use` メソッドで処理:

1. 手持ちが空でない場合は `PASS`
2. 出力インベントリの全スロットをプレイヤーインベントリに移動
3. 出力が空だった場合は入力インベントリも回収
4. `setChanged()` + `sendData()` でNBT同期

---

## ComposterBlockEntity（ブロックエンティティ）

**ソース**: `content/composter/ComposterBlockEntity.java`

### インベントリ

| インベントリ | スロット数 | 上限 | 用途 |
|---|---|---|---|
| `inputInv` | 1 | デフォルト（64） | コンポスト可能アイテムの入力 |
| `outputInv` | 9 | デフォルト（64） | レシピ出力 |

### Forge Capability

`ComposterInventoryHandler` (extends `CombinedInvWrapper`) を提供:

| 操作 | 制約 |
|---|---|
| `isItemValid` | 出力スロットへの挿入を拒否。`COMPOSTABLES` に含まれるアイテムのみ許可 |
| `insertItem` | 出力スロットへの挿入を拒否。無効アイテムを拒否 |
| `extractItem` | 入力スロットからの抽出を拒否（出力スロットのみ抽出可） |

### 処理フロー（tick）

```
tick()
  ├── speed == 0 → 何もしない
  ├── 出力インベントリが全て最大スタック → 何もしない
  ├── timer > 0 → timer を processingSpeed 分減算
  │   └── timer <= 0 → process() 実行
  └── timer == 0 → レシピ検索
      ├── レシピ見つからず → timer = 100, sendData
      └── レシピあり → timer = recipe.processingDuration, sendData
```

### process() の動作

```
process()
  ├── LEVEL != 8（コンポスト進行中）
  │   ├── inputInv[0] のアイテムの COMPOSTABLES 値を取得
  │   ├── LEVEL == 0 かつ値 > 0 → 確定でレベル+1
  │   ├── LEVEL > 0 → random.nextDouble() < 値 の確率でレベル+1
  │   ├── レベル+1 時: setBlock, gameEvent, 効果音, アイテム1個消費
  │   └── 確率不成立時: 何もしない
  └── LEVEL == 8（コンポスト完了）
      ├── レシピを再検索（速度変更対応）
      ├── LEVEL を 0 にリセット
      └── recipe.rollResults() を outputInv に挿入
```

### 処理速度

```java
int getProcessingSpeed() {
    return Mth.clamp((int) Math.abs(getSpeed() / 16f) * 8, 1, 512);
}
```

| RPM | 計算 | 処理速度(tick/tick) |
|---|---|---|
| 16 | `(16/16)*8 = 8` | 8 |
| 32 | `(32/16)*8 = 16` | 16 |
| 64 | `(64/16)*8 = 32` | 32 |
| 128 | `(128/16)*8 = 64` | 64 |
| 256 | `(256/16)*8 = 128` | 128 |
| 1024+ | クランプ | 512 |

### NBT データ

| キー | 型 | 説明 |
|---|---|---|
| `Timer` | int | 残り処理時間 |
| `InputInventory` | CompoundTag | 入力スロットのNBT |
| `OutputInventory` | CompoundTag | 出力スロットのNBT |

---

## CompostingRecipe（コンポスティングレシピ）

**ソース**: `content/composter/CompostingRecipe.java`

### フィールド

| フィールド | 型 | 説明 |
|---|---|---|
| `minSpeed` | float | 最低動作速度(RPM) |
| `maxSpeed` | float | 最高動作速度(RPM) |
| `processingDuration` | int | 処理時間(tick) |
| `results` | `NonNullList<ProcessingOutput>` | 出力リスト |

### 速度マッチング

```java
boolean appliesTo(float speed) {
    return minSpeed <= Math.abs(speed) && Math.abs(speed) <= maxSpeed;
}
```

回転方向は問わない（`Math.abs` で絶対値比較）。

### レシピ検索

`CompostingRecipe.find(Level, float speed)`:
1. `RecipeManager.getAllRecipesFor(COMPOSTING)` で全レシピ取得
2. 最初に `appliesTo(speed)` を満たすレシピを返す
3. リスト順で優先度が決まる（最初のマッチ）

### JSON フォーマット

```json
{
  "type": "karacreate:composting",
  "minSpeed": 0.0,
  "maxSpeed": 128.0,
  "processingTime": 100,
  "results": [
    { "item": "minecraft:bone_meal", "count": 2 }
  ]
}
```

### 現存レシピ

| ファイル | minSpeed | maxSpeed | 処理時間 | 出力 |
|---|---|---|---|---|
| `composting_test.json` | 0.0 | 128.0 | 100 tick | 骨粉 ×2 |

> **注意**: レシピ名に `test` が含まれており、テスト用途の可能性がある。入力アイテムの指定がレシピJSONに含まれていない（バニラの `COMPOSTABLES` マップに依存）。

---

## レンダリング

### ComposterInstance（Flywheel GPU レンダリング）

**ソース**: `content/composter/ComposterInstance.java`
**親クラス**: `SingleRotatingInstance<ComposterBlockEntity>`

描画パーツ:
1. `KaraCreatePartialModels.COMPOSTER_COG` — 歯車モデル（`block/mechanical_composter/cog`）
2. `AllPartialModels.SHAFT_HALF` — Create のハーフシャフトモデル

### ComposterRenderer（CPU フォールバック）

**ソース**: `content/composter/ComposterRenderer.java`
**親クラス**: `KineticBlockEntityRenderer<ComposterBlockEntity>`

Flywheel 非対応環境で使用。歯車とシャフトを CPU 側で描画。

---

## タグ

Composter は自動タグ付けされていない。`pickaxeOnly()` による `minecraft:mineable/pickaxe` への登録のみ。

---

## テクスチャ

| ファイル | 用途 |
|---|---|
| `karacreate/textures/block/mechanical_composter.png` | コンポスター本体 |
| `karacreate/textures/block/composter_side.png` | コンポスター側面 |
| `minecraft/textures/blocks/composter_bottom.png` | バニラ上書き: 底面 |
| `minecraft/textures/blocks/composter_compost.png` | バニラ上書き: 堆肥面 |
| `minecraft/textures/blocks/composter_ready.png` | バニラ上書き: 完成面 |
| `minecraft/textures/blocks/composter_side.png` | バニラ上書き: 側面 |
| `minecraft/textures/blocks/composter_top.png` | バニラ上書き: 上面 |
| `create/textures/block/axis.png` | Create上書き: シャフト |
| `create/textures/block/axis_top.png` | Create上書き: シャフト上面 |
| `create/textures/block/cogwheel_axis.png` | Create上書き: 歯車軸 |
| `create/textures/block/large_cogwheel.png` | Create上書き: 大歯車 |

> **注意**: `minecraft` と `create` 名前空間のテクスチャを上書きしている。これらは `sourceSets` の `exclude` 設定で最終ビルドから除外される可能性がある。→ [KNOWN_ISSUES.md](../KNOWN_ISSUES.md)

---

## JEI 連携

### CompostingCategory

**ソース**: `compat/jei/CompostingCategory.java`

- Create の `CreateRecipeCategory` を拡張
- 出力表示: 最大4スロット
- 速度範囲表示: `"%s~%s RPM"` フォーマット（`karacreate.jei.range_of_composting`）
- アニメーション: `AnimatedComposter`（回転する歯車を表示）

### AnimatedComposter

**ソース**: `compat/jei/AnimatedComposter.java`

- Create の `AnimatedKinetics` を拡張
- 回転する歯車 + コンポスターブロックを描画
- 影とスケール効果あり

---

## ソースファイル一覧

| ファイル | パス（`mod/ckenja/karacreate/` 基準） | 役割 |
|---|---|---|
| `MechanicalComposterBlock.java` | `content/composter/` | ブロック定義、インタラクション |
| `ComposterBlockEntity.java` | `content/composter/` | 処理ロジック、インベントリ管理 |
| `ComposterInstance.java` | `content/composter/` | Flywheel GPU レンダリング |
| `ComposterRenderer.java` | `content/composter/` | CPU フォールバックレンダリング |
| `CompostingRecipe.java` | `content/composter/` | レシピ定義 |
| `CompostingRecipeSerializer.java` | `content/composter/` | JSON ↔ レシピ変換 |
| `KaraCreateBlocks.java` | `foundation/register/` | ブロック登録 |
| `KaraCreateBlockEntityTypes.java` | `foundation/register/` | COMPOSTER BlockEntity登録 |
| `KaraCreatePartialModels.java` | `foundation/register/` | COMPOSTER_COG パーシャルモデル |
| `KaraCreateRecipeTypes.java` | `foundation/register/` | COMPOSTING レシピタイプ登録 |

---

## 既知の問題

1. **レシピに入力指定がない**: `composting_test.json` には `results` のみで入力アイテムの指定がない。バニラの `COMPOSTABLES` マップに依存するため、レシピ側で入力を指定する仕組みがない
2. **テストレシピのみ**: `composting_test.json` のファイル名が示すように、本番用レシピが未定義
3. **コンポスト確率問題**: `process()` で `LEVEL == 0` かつ `COMPOSTABLES` 値 > 0 の場合は確定でレベルが上がるが、`LEVEL > 0` の場合は確率に依存。アイテムは確率不成立でも消費されない
4. **outputInv 満杯判定**: `tick()` の満杯チェックは全スロットが `getSlotLimit(i)` に達した場合のみ。異種アイテムで各スロットが部分的に埋まっている場合の挙動が正しくない可能性がある
5. **minecraft/create テクスチャ上書き**: バニラと Create のテクスチャを上書きしており、他Modとの競合の原因になりうる
