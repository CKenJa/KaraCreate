# KaraCreate アーキテクチャ・設計

## パッケージ構成

```
mod.ckenja.karacreate/
├── KaraCreate.java                 ← メインMod クラス（@Mod エントリポイント）
├── KaraCreateClient.java           ← クライアント専用初期化
│
├── foundation/                     ← 基盤層：登録・レンダリング・共通定義
│   ├── register/                   ← Forge レジストリへの登録
│   │   ├── KaraCreateBlocks.java          Block 登録（6ブロック）
│   │   ├── KaraCreateItems.java           Item 登録（1アイテム）
│   │   ├── KaraCreateBlockEntityTypes.java BlockEntity 登録（2タイプ）
│   │   ├── KaraCreateCreativeModeTabs.java クリエイティブタブ登録
│   │   ├── KaraCreateTags.java            タグ定義
│   │   ├── KaraCreatePartialModels.java   Flywheel パーシャルモデル
│   │   ├── KaraCreateRecipeTypes.java     レシピタイプ登録
│   │   ├── KaraCraeteRecipeSerializer.java レシピシリアライザ登録
│   │   └── KaraCreateBannerPatterns.java  バナーパターン登録（15種）
│   └── render/
│       └── KaraCreateModelLayers.java     モデルレイヤー定義
│
├── content/                        ← コンテンツ層：各機能の実装
│   ├── composter/                  ← メカニカルコンポスター
│   │   ├── MechanicalComposterBlock.java  ブロック（RotatedPillarKineticBlock）
│   │   ├── ComposterBlockEntity.java      ブロックエンティティ（処理ロジック）
│   │   ├── ComposterInstance.java         Flywheel インスタンスレンダリング
│   │   ├── ComposterRenderer.java         フォールバック CPU レンダラー
│   │   ├── CompostingRecipe.java          コンポスティングレシピ定義
│   │   └── CompostingRecipeSerializer.java レシピ JSON 解析
│   ├── paperDoor/                  ← 障子・襖ドア
│   │   ├── PaperDoorBlock.java            ブロック（SlidingDoorBlock 拡張）
│   │   ├── PaperDoorBehaviour.java        バナー柄データの保持・同期
│   │   ├── PaperDoorBlockRenderer.java    ドア + バナー柄のカスタム描画
│   │   ├── PaperDoorBlockItem.java        カスタムアイテム（描画委譲）
│   │   ├── PaperDoorItemRenderer.java     インベントリ内ドア描画
│   │   └── PaperDoorDecorationRecipe.java バナー→ドアの装飾レシピ
│   └── smallDoor/                  ← 欄間障子・半襖
│       ├── SmallDoorBlock.java            1ブロック高ドア（DoorBlock 拡張）
│       └── SlidingSmallDoorBlock.java     1ブロック高スライドドア
│
├── compat/                         ← 互換性層：外部Mod連携
│   └── jei/                        ← JEI (Just Enough Items) 統合
│       ├── KaraCreateJEI.java             JEI プラグイン登録
│       ├── CompostingCategory.java        コンポスティングレシピカテゴリ
│       ├── AnimatedComposter.java         JEI 内アニメーション
│       └── PaperDoorDecorationRecipeMaker.java JEI 用レシピ動的生成
│
└── infrastructure/                 ← 基盤インフラ層：データ生成・Mixin
    ├── data/
    │   └── KaraCreateBannerPatternTagsProvider.java バナーパターンタグ生成
    ├── lang/
    │   ├── LanguageManager.java           多言語データ生成管理
    │   └── BannerPatternLangGenerators.java バナーパターン翻訳生成
    └── mixin/
        ├── AllBlockEntityTypesMixin.java         小ドアをSlidingDoorBEに注入
        ├── DoorBlockAccessor.java                DoorBlock.playSound アクセス
        ├── RecipeManagerAccessor.java            レシピマネージャ内部アクセス
        ├── SlidingDoorBlockEntityAccessor.java   SlidingDoorBE 内部フィールドアクセス
        ├── SlidingDoorBlockEntityMixin.java       ドア閉じ音の修正
        └── SlidingDoorRendererMixin.java          小ドアの上部描画抑制
```

---

## 設計思想

### レイヤー分離

| レイヤー | パッケージ | 責務 |
|---|---|---|
| **Foundation** | `foundation/` | Forge レジストリへの登録、共通定義。他レイヤーに依存しない |
| **Content** | `content/` | 各機能の実装。Foundation に依存するが Content 間は独立 |
| **Compat** | `compat/` | 外部Modとの連携。Content に依存。対象Modが無くても動作する |
| **Infrastructure** | `infrastructure/` | データ生成、Mixin。ビルド時/起動時に動作する基盤 |

### Registrate パターン

ブロック・アイテムの登録には Create mod が使用する **Registrate** ライブラリを採用している。`KaraCreate.REGISTRATE` を通して宣言的にブロックを定義:

```
REGISTRATE.block("shoji_door", PaperDoorBlock::new)
    .properties(...)
    .blockstate(...)
    .item(PaperDoorBlockItem::new)
    .register()
```

理由: Create mod のエコシステムに統合するため、Create と同じ登録パターンを踏襲する。

### Create mod との統合方式

| 統合方式 | 対象 | 説明 |
|---|---|---|
| **継承** | `PaperDoorBlock extends SlidingDoorBlock` | Create のスライドドア機構を再利用 |
| **継承** | `MechanicalComposterBlock extends RotatedPillarKineticBlock` | Create の回転力システムに接続 |
| **Behaviour** | `PaperDoorBehaviour extends BlockEntityBehaviour` | Create の行動パターンシステムでバナー柄データを管理 |
| **Mixin** | `AllBlockEntityTypesMixin` | 小ドアを Create の SlidingDoorBlockEntity の有効ブロックに追加 |
| **イベント** | `BlockEntityBehaviourEvent` | PaperDoorBehaviour を SlidingDoorBlockEntity に注入 |

### レンダリングアーキテクチャ

```
描画パス:
  ├── 標準ブロック描画 ← 大半のブロック
  ├── BlockEntityRenderer ← PaperDoorBlockRenderer (バナー柄描画)
  │                       ← ComposterRenderer (フォールバック)
  └── Flywheel Instance ← ComposterInstance (GPU アクセラレーション)
```

- **PaperDoor**: BlockEntityRenderer で Minecraft 標準の `BannerRenderer` を流用し、ドア面にバナー柄を描画
- **Composter**: Flywheel の `SingleRotatingInstance` で歯車の回転をGPU描画。Flywheel 非対応環境では `ComposterRenderer` にフォールバック

### BlockSetType

ドア系ブロックは全てカスタムの `KARACREATE_WOOD` BlockSetType を使用:

```java
BlockSetType.register(new BlockSetType(
    "karacreate_wood",
    /* canOpenByHand */ true,
    /* canOpenByWindCharge */ true,
    /* canButtonBeActivatedByArrows */ true,
    SoundType.BAMBOO_WOOD,          // 竹木の音
    SoundEvents.BAMBOO_WOOD_DOOR_CLOSE,
    SoundEvents.BAMBOO_WOOD_DOOR_OPEN,
    ...
))
```

理由: 日本建築風のドアに竹木の効果音を割り当てるため、バニラの木材 BlockSetType ではなくカスタムを定義。

---

## クラス間の依存関係

### PaperDoor 系の関係

```
KaraCreate (初期化)
  │
  ├─→ KaraCreateBlocks.SHOJI_DOOR / FUSUMA_DOOR (PaperDoorBlock)
  │     │
  │     ├─→ PaperDoorBlockItem (アイテム)
  │     │     └─→ PaperDoorItemRenderer (インベントリ描画)
  │     │
  │     └─→ PaperDoorBehaviour (バナー柄データ)
  │           └─→ PaperDoorBlockRenderer (ワールド描画)
  │
  ├─→ PaperDoorDecorationRecipe (クラフトレシピ)
  │
  └─→ KaraCreateJEI → PaperDoorDecorationRecipeMaker (JEI表示)
```

### Composter 系の関係

```
KaraCreate (初期化)
  │
  ├─→ KaraCreateBlocks.COMPOSTER (MechanicalComposterBlock)
  │     │
  │     └─→ ComposterBlockEntity (処理ロジック)
  │           ├─→ ComposterInstance (Flywheel GPU描画)
  │           └─→ ComposterRenderer (CPU フォールバック描画)
  │
  ├─→ CompostingRecipe + CompostingRecipeSerializer (レシピ)
  │
  └─→ KaraCreateJEI → CompostingCategory + AnimatedComposter (JEI表示)
```

### SmallDoor 系の関係

```
KaraCreate (初期化)
  │
  ├─→ KaraCreateBlocks.SMALL_SHOJI_DOOR / SMALL_FUSUMA_DOOR (SlidingSmallDoorBlock)
  │
  ├─→ AllBlockEntityTypesMixin (Create の BlockEntity に小ドアを登録)
  ├─→ SlidingDoorRendererMixin (上半分の描画を抑制)
  └─→ SlidingDoorBlockEntityMixin (ドア閉じ音の修正)
```

---

## 関連ドキュメント

- [PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md) — プロジェクト概要
- [MIXINS.md](MIXINS.md) — Mixin 一覧と詳細
- [features/](features/) — 各機能の仕様
