# 欄間障子・半襖（SmallDoor 系）仕様

## 概要

1ブロック高（通常ドアの半分の高さ）のスライドドア。欄間（らんま＝天井と鴨居の間の開口）や半襖に使用する。通常の `DoorBlock` の2ブロック高の仕組みを1ブロックで機能するよう改造している。

---

## ブロック一覧

### 1. 欄間障子 (Small Shoji Door)

| 項目 | 値 |
|---|---|
| 登録ID | `karacreate:small_shoji_door` |
| クラス | `SlidingSmallDoorBlock` (extends `SlidingDoorBlock`) |
| 日本語名 | 欄間障子 |
| MapColor | `MapColor.WOOD` |
| 初期プロパティ元 | `Blocks.BAMBOO_DOOR` |
| 必要ツール | なし（`smallDoor` では `axeOnly` が未適用。ただし SMALL_FUSUMA_DOOR のみ `axeOnly` あり） |
| ノブ（取っ手）| なし (`knob = false`) |
| BlockSetType | `KARACREATE_WOOD` (竹木の音) |
| `folds` | `false` |

### 2. 半襖 (Small Fusuma Door)

| 項目 | 値 |
|---|---|
| 登録ID | `karacreate:small_fusuma_door` |
| クラス | `SlidingSmallDoorBlock` (extends `SlidingDoorBlock`) |
| 日本語名 | 半襖 |
| MapColor | `MapColor.COLOR_BLACK` |
| 初期プロパティ元 | `Blocks.BAMBOO_DOOR` |
| 必要ツール | 斧 (`axeOnly`) |
| ノブ（取っ手）| あり (`knob = true`) |
| BlockSetType | `KARACREATE_WOOD` (竹木の音) |
| `folds` | `false` |

---

## 通常ドアとの違い

SmallDoor 系は `DoorBlock`/`SlidingDoorBlock` を継承しつつ、以下のメソッドをオーバーライドして1ブロック高を実現:

| メソッド | 通常のDoorBlock | SmallDoor での変更 |
|---|---|---|
| `updateShape` | 上下ブロックの状態を同期 | **何もしない**（`return pState;`） |
| `getStateForPlacement` | 上部ブロックも配置 | **下部のみ配置** (`HALF = LOWER` 固定) |
| `setPlacedBy` | 上部ブロックを設置 | **何もしない**（空メソッド） |
| `canSurvive` | 上下ペアの存在を検証 | **常に `HALF == LOWER` のみ** |
| `neighborChanged` | 上下ペアで信号共有 | **単独ブロックで信号処理** |

---

## SlidingSmallDoorBlock の neighborChanged（レッドストーン処理）

**ソース**: `content/smallDoor/SlidingSmallDoorBlock.java`

1. `pLevel.hasNeighborSignal(pPos)` でレッドストーン信号を確認
2. 同ブロック自身からの更新は無視 (`defaultBlockState().is(pBlock)`)
3. `SlidingDoorBlockEntityAccessor.getDeferUpdate()` が true の場合は無視（Create のアニメーション遅延処理中）
4. 信号変化時:
   - `POWERED` と `OPEN` を信号に合わせて更新
   - `VISIBLE = false`（信号ON時、ドアが見えなくなるCreateのスライド動作）
   - ドア音を再生（`DoorBlockAccessor.invokePlaySound()`）
   - `GameEvent` を発信

---

## SmallDoorBlock のヒンジ計算

**メソッド**: `SmallDoorBlock.getHinge(BlockPlaceContext, Block)`

静的メソッドとして定義され、`SlidingSmallDoorBlock` からも呼び出される。

1. 左右の隣接ブロックの衝突形状をチェック
2. 左右に同じドアがあるかをチェック
3. 決定順位:
   - 片側に同じドアがある → 反対側にヒンジ
   - 片側に完全ブロックがある → その側にヒンジ
   - どちらも同条件 → クリック位置で判定

---

## ブロックステートモデル

### ノブあり (small_fusuma_door)
- `block/small_fusuma_door/right` と `block/small_fusuma_door/left` の2モデル
- ヒンジ側で切り替え
- 向きで Y 回転 (`facing.toYRot() + 180`)

### ノブなし (small_shoji_door)
- 単一モデル（`AssetLookup.standardModel`）
- 向きで Y 回転のみ

### ブロックステートバリアント

`TrapDoorBlock.POWERED` と `TrapDoorBlock.WATERLOGGED` は blockstate バリアントから除外（全状態で同じモデル）。

---

## タグ

| タグ | 含まれるブロック/アイテム |
|---|---|
| `karacreate:small_door` (Block) | 欄間障子、半襖 |
| `karacreate:small_door` (Item) | 欄間障子、半襖 |
| `minecraft:mineable/axe` (Block) | 半襖のみ（欄間障子には未適用） |
| `minecraft:doors` (Block) | **含まれない**（`woodenSlidingDoor` 変換を使っていないため） |

> **注意**: 小ドアは `minecraft:doors` タグに**含まれていない**。`smallDoor()` 変換ではドアタグが付与されない。これは意図的な設計か不整合かは不明。→ [KNOWN_ISSUES.md](../KNOWN_ISSUES.md)

---

## テクスチャ

| ファイル | 用途 |
|---|---|
| `textures/block/small_shoji_door.png` | 欄間障子 |
| `textures/block/small_fusuma_door.png` | 半襖 |

---

## レシピ

**レシピ定義ファイルが存在しない。**

`small_shoji_door` と `small_fusuma_door` には `/data/karacreate/recipes/` にレシピJSONが見当たらない。クラフト方法未実装の可能性あり。→ [KNOWN_ISSUES.md](../KNOWN_ISSUES.md)

---

## Mixin 依存

SmallDoor 系は Create mod の内部クラスを改変するために以下の Mixin を必要とする:

| Mixin | 用途 |
|---|---|
| `AllBlockEntityTypesMixin` | `SNOW_VIEWING_SHOJI_DOOR`, `SMALL_SHOJI_DOOR`, `SMALL_FUSUMA_DOOR` を Create の SlidingDoorBlockEntity の有効ブロックリストに追加 |
| `SlidingDoorRendererMixin` | 小ドアの場合、上半分の描画を抑制 |
| `SlidingDoorBlockEntityMixin` | ドア閉じ音を正しい音に修正 |
| `DoorBlockAccessor` | `DoorBlock.playSound()` プライベートメソッドへのアクセス |
| `SlidingDoorBlockEntityAccessor` | `animation`, `deferUpdate` フィールドへのアクセス |

---

## ソースファイル一覧

| ファイル | パス（`mod/ckenja/karacreate/` 基準） | 役割 |
|---|---|---|
| `SmallDoorBlock.java` | `content/smallDoor/` | 1ブロック高ドアの基底実装（DoorBlock拡張） |
| `SlidingSmallDoorBlock.java` | `content/smallDoor/` | Create のスライド機構 + 1ブロック高（SlidingDoorBlock拡張） |
| `KaraCreateBlocks.java` | `foundation/register/` | ブロック登録（`smallDoor()` 変換） |

---

## 既知の問題

1. **欄間障子にツール制約がない**: `SMALL_FUSUMA_DOOR` には `axeOnly()` が適用されているが、`SMALL_SHOJI_DOOR` には適用されていない。`smallDoor()` 変換に含まれておらず、個別に追加する必要がある
2. **ドアタグ未付与**: `minecraft:doors` / `minecraft:wooden_doors` タグに含まれていない
3. **レシピ未定義**: クラフトレシピが存在しない（クリエイティブ限定またはTODO）
4. **ルートテーブル**: 生成された loot table JSON は存在するが、`smallDoor()` 変換内で `loot()` 定義が見当たらない。Registrate のデフォルト動作に依存している可能性あり
