# Mixin 仕様

## 概要

KaraCreate は6つのMixinを使用して Create mod および Minecraft のクラスを拡張する。
Mixin設定は `src/main/resources/karacreate.mixins.json` で管理。

| 項目 | 値 |
|---|---|
| パッケージ | `mod.ckenja.karacreate.infrastructure.mixin` |
| 最低バージョン | `0.8` |
| 互換性レベル | `JAVA_8` |
| refmap | `karacreate.refmap.json` |

> **既知の問題**: `karacreate.mixins.json` 内で `AllBlockEntityTypesMixin` が **2回** 登録されている（重複エントリ）。→ [KNOWN_ISSUES.md](KNOWN_ISSUES.md)

---

## Mixin 一覧

| Mixin | 種別 | 対象クラス | 手法 |
|---|---|---|---|
| `AllBlockEntityTypesMixin` | Class | `AllBlockEntityTypes`（Create） | `@Redirect` |
| `DoorBlockAccessor` | Interface | `DoorBlock`（Minecraft） | `@Invoker` |
| `RecipeManagerAccessor` | Interface | `RecipeManager`（Minecraft） | `@Accessor` |
| `SlidingDoorBlockEntityAccessor` | Interface | `SlidingDoorBlockEntity`（Create） | `@Accessor` / `@Invoker` |
| `SlidingDoorBlockEntityMixin` | Class | `SlidingDoorBlockEntity`（Create） | `@Redirect` |
| `SlidingDoorRendererMixin` | Class | `SlidingDoorRenderer`（Create） | `@Redirect` |

---

## 各 Mixin の詳細

### 1. AllBlockEntityTypesMixin

**目的**: Create の `SlidingDoorBlockEntity` に KaraCreate のドアブロックを追加登録する。

**対象メソッド**: `<clinit>`（静的初期化子）

**手法**: `@Redirect` + `@Slice`

```
@Slice: "track_station" → "copycat" の間の BlockEntityBuilder.validBlocks() 呼び出しをリダイレクト
```

**動作**:
1. 元の `validBlocks()` 呼び出しを横取り
2. KaraCreate の3ブロックを先に `validBlocks()` で追加:
   - `KaraCreateBlocks.SNOW_VIEWING_SHOJI_DOOR`
   - `KaraCreateBlocks.SMALL_SHOJI_DOOR`
   - `KaraCreateBlocks.SMALL_FUSUMA_DOOR`
3. 元の引数で `validBlocks()` を呼び返す

**remap**: `false`

**影響**: Create の `SLIDING_DOOR` BlockEntityType に KaraCreate のドアブロックが有効ブロックとして認識される。これによりこれらのブロックに `SlidingDoorBlockEntity` が自動的にアタッチされる。

**注意**: 障子ドア (`SHOJI_DOOR`)・襖ドア (`FUSUMA_DOOR`) はここで登録されていない。これらは `PaperDoorBlock` を使用し、別途 `PaperDoorBlockEntity` を持つため。

---

### 2. DoorBlockAccessor

**目的**: `DoorBlock.playSound()` (protected) を外部から呼び出し可能にする。

**手法**: `@Invoker`

```java
@Invoker("playSound")
void invokePlaySound(Entity entity, Level level, BlockPos blockPos, boolean bl);
```

**使用箇所**: `SlidingSmallDoorBlock.neighborChanged()` — 小型ドアが開閉する際にドアのサウンドを再生するために使用。

---

### 3. RecipeManagerAccessor

**目的**: `RecipeManager.recipes` (private) フィールドへの読み取りアクセスを提供する。

**手法**: `@Accessor`

```java
@Accessor("recipes")
Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> getRecipes();
```

**使用箇所**: `MechanicalComposterBlockEntity` — コンポスティングレシピの検索に使用される可能性がある（レシピマネージャーの内部マップに直接アクセス）。

---

### 4. SlidingDoorBlockEntityAccessor

**目的**: `SlidingDoorBlockEntity` の private フィールド・メソッドへのアクセスを提供する。

**手法**: `@Accessor` × 2 + `@Invoker` × 1

```java
@Accessor("animation")     LerpedFloat getAnimation();       // アニメーション値
@Accessor("deferUpdate")   boolean getDeferUpdate();          // 更新遅延フラグ
@Invoker("shouldRenderSpecial") boolean invokeShouldRenderSpecial(BlockState state);
```

**remap**: `false`（`value = SlidingDoorBlockEntity.class, remap = false`）

**使用箇所**:
- `getAnimation()` → `PaperDoorBlockRenderer` — ドアのアニメーション状態を取得してバナー描画に反映
- `getDeferUpdate()` → `SlidingSmallDoorBlock.neighborChanged()` — 更新遅延中は近傍更新を無視
- `invokeShouldRenderSpecial()` → `PaperDoorBlockRenderer` — 特殊レンダリングが必要か判定

---

### 5. SlidingDoorBlockEntityMixin

**目的**: Create のスライドドアが閉じる際のサウンドをドアの `BlockSetType` に応じた正しいサウンドに差し替える。

**対象メソッド**: `showBlockModel()`

**手法**: `@Redirect`

**対象**: `SoundEvents.IRON_DOOR_CLOSE` のフィールドアクセスをリダイレクト

**動作**:
```java
if (getBlockState().getBlock() instanceof DoorBlock door)
    return door.type().doorClose();  // ブロックの型に応じたサウンド
return SoundEvents.IRON_DOOR_CLOSE;  // フォールバック
```

**効果**: Create のオリジナルコードでは全てのスライドドアが鉄ドアの閉じるサウンドを使用するが、このMixinにより木製ドアは木のドアサウンドを、KaraCreate のドアは `KARACREATE_WOOD` の竹サウンドを使用するようになる。

**継承**: `SmartBlockEntity` を継承（`getBlockState()` にアクセスするため）。

---

### 6. SlidingDoorRendererMixin

**目的**: 小型ドア（1ブロック高）のレンダリング時に上半分の描画をスキップする。

**対象メソッド**: `renderSafe()`

**手法**: `@Redirect`

**対象**: `DoubleBlockHalf.values()` の呼び出しをリダイレクト

**remap**: `false`

**動作**:
```java
if (be.getBlockState().getBlock() instanceof SlidingSmallDoorBlock)
    return new DoubleBlockHalf[]{DoubleBlockHalf.LOWER};  // 下半分のみ
return DoubleBlockHalf.values();  // 通常は上下両方
```

**効果**: Create の `SlidingDoorRenderer` は通常ドアの上半分・下半分を両方レンダリングするが、小型ドアでは下半分のみレンダリングするよう制限。

---

## Mixin 依存関係マップ

```
AllBlockEntityTypesMixin ──→ AllBlockEntityTypes (Create)
    └─ 追加: SNOW_VIEWING_SHOJI_DOOR, SMALL_SHOJI_DOOR, SMALL_FUSUMA_DOOR

DoorBlockAccessor ──→ DoorBlock (Minecraft)
    └─ 使用者: SlidingSmallDoorBlock

RecipeManagerAccessor ──→ RecipeManager (Minecraft)
    └─ 使用者: MechanicalComposterBlockEntity

SlidingDoorBlockEntityAccessor ──→ SlidingDoorBlockEntity (Create)
    └─ 使用者: PaperDoorBlockRenderer, SlidingSmallDoorBlock

SlidingDoorBlockEntityMixin ──→ SlidingDoorBlockEntity (Create)
    └─ 効果: ドア閉鎖サウンドをBlockSetType準拠に

SlidingDoorRendererMixin ──→ SlidingDoorRenderer (Create)
    └─ 効果: 小型ドアの上半分描画スキップ
```

---

## 更新時の注意点

1. **Create mod のバージョンアップ時**: `AllBlockEntityTypesMixin` の `@Slice` は `"track_station"` から `"copycat"` までの範囲を指定している。Create の `AllBlockEntityTypes` の定数順序が変更されるとMixinが失敗する。
2. **SlidingDoorBlockEntityMixin**: `showBlockModel()` のメソッドシグネチャ変更に弱い。
3. **SlidingDoorRendererMixin**: `renderSafe()` のシグネチャが変更されると壊れる。
4. **重複エントリ**: `AllBlockEntityTypesMixin` が2回登録されているが、実行時エラーにはなっていない模様（Mixin側で重複を無視している可能性）。ただし修正すべき。
