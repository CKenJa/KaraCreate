# テクスチャ一覧

## 概要

KaraCreate は3つの名前空間にテクスチャを配置する:
- `karacreate` — Mod独自のテクスチャ
- `minecraft` — バニラテクスチャの上書き
- `create` — Create modテクスチャの上書き

全テクスチャは `src/main/resources/assets/` 配下に配置。

> **重要**: `build.gradle` の `sourceSets.main.resources` 設定で `assets/create` と `assets/minecraft` は `src/generated/resources` から除外されている。ただし `src/main/resources` からは除外されていないため、ビルドJARには含まれる。

---

## karacreate 名前空間（14ファイル）

### ブロックテクスチャ (`textures/block/`)

| ファイル | 用途 | 対応ブロック |
|---|---|---|
| `shoji_door_bottom.png` | 障子 下半分 | `shoji_door` |
| `shoji_door_top.png` | 障子 上半分 | `shoji_door` |
| `shoji_door_side.png` | 障子 側面 | `shoji_door` |
| `fusuma_door_bottom.png` | 襖 下半分 | `fusuma_door` |
| `fusuma_door_top.png` | 襖 上半分 | `fusuma_door` |
| `fusuma_door_side.png` | 襖 側面 | `fusuma_door` |
| `snow_viewing_shoji_door_bottom.png` | 雪見障子 下半分 | `snow_viewing_shoji_door` |
| `snow_viewing_shoji_door_top.png` | 雪見障子 上半分 | `snow_viewing_shoji_door` |
| `snow_viewing_shoji_door_side.png` | 雪見障子 側面 | `snow_viewing_shoji_door` |
| `small_shoji_door.png` | 欄間障子 | `small_shoji_door` |
| `small_fusuma_door.png` | 半襖 | `small_fusuma_door` |
| `mechanical_composter.png` | コンポスター本体 | `mechanical_composter` |
| `composter_side.png` | コンポスター側面 | `mechanical_composter` |

### アイテムテクスチャ (`textures/item/`)

| ファイル | 用途 | 対応アイテム |
|---|---|---|
| `japanese_banner_pattern.png` | バナーパターンアイテム | `japanese_banner_pattern` |

### エンティティテクスチャ (`textures/entity/banner/`)

バナーパターンの描画に使用。Minecraft の `BannerRenderer` が参照する。

| ファイル | 対応パターン | enum に対応するか |
|---|---|---|
| `checkered.png` | 市松 | あり (`CHECKERED`) |
| `circle.png` | まる | あり (`CIRCLE`) |
| `hash.png` | 井桁 | あり (`HASH`) |
| `hash4.png` | 小さな市松 | あり (`HASH4`) |
| `mountain.png` | 風景画 | あり (`MOUNTAIN`) |
| `vert_left.png` | 縦左 | あり (`VERT_LEFT`) |
| `vert_right.png` | 縦右 | あり (`VERT_RIGHT`) |
| `bamboo.png` | 竹 | あり (`BAMBOO`) |
| `wave.png` | 波 | **なし**（余剰テクスチャ） |
| `cheese.png` | チーズ | **なし**（余剰テクスチャ） |
| `eternal.png` | 永遠 | **なし**（余剰テクスチャ） |

---

## minecraft 名前空間（5ファイル）

バニラのコンポスターブロックテクスチャを上書き。

| ファイル | 上書き対象 |
|---|---|
| `textures/blocks/composter_bottom.png` | コンポスター底面 |
| `textures/blocks/composter_compost.png` | コンポスト面 |
| `textures/blocks/composter_ready.png` | 完成面 |
| `textures/blocks/composter_side.png` | 側面 |
| `textures/blocks/composter_top.png` | 上面 |

> **注意**: パスが `textures/blocks/` （複数形）であるが、バニラの正式パスは `textures/block/`（単数形）。これは意図的なリソースパック上書きか、パスの誤りの可能性がある。→ [KNOWN_ISSUES.md](KNOWN_ISSUES.md)

---

## create 名前空間（7ファイル）

Create modのテクスチャを上書き。メカニカルコンポスターの歯車・シャフト描画に使用される。

| ファイル | 上書き対象 |
|---|---|
| `textures/block/axis.png` | シャフト |
| `textures/block/axis_top.png` | シャフト上面 |
| `textures/block/cogwheel_axis.png` | 歯車軸 |
| `textures/block/fan_blades.png` | ファンブレード |
| `textures/block/gauge.png` | ゲージ |
| `textures/block/large_cogwheel.png` | 大歯車 |
| `textures/block/stripped_spruce_log.png` | 皮剥ぎスプルース |

> **注意**: これらは Create mod のテクスチャをリソースパックレベルで上書きする。他のテクスチャパック/Modと競合する可能性あり。→ [KNOWN_ISSUES.md](KNOWN_ISSUES.md)

---

## テクスチャ総数

| 名前空間 | ファイル数 |
|---|---|
| karacreate (block) | 13 |
| karacreate (item) | 1 |
| karacreate (entity/banner) | 11 |
| minecraft | 5 |
| create | 7 |
| **合計** | **37** |
