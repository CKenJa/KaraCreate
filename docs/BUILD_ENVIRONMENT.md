# ビルド・開発環境

## 前提環境

| 項目 | バージョン |
|---|---|
| Java | 17 |
| Gradle | Wrapper 使用（`gradle-wrapper.properties` で管理） |
| Minecraft | 1.20.1 |
| Forge | 47.2.32 |
| JVM 最大メモリ | 3GB（`org.gradle.jvmargs=-Xmx3G`） |
| Gradle Daemon | 無効（`org.gradle.daemon=false`） |

---

## 依存関係

### ビルドプラグイン

| プラグイン | バージョン |
|---|---|
| ForgeGradle | `[6.0.16, 6.2)` |
| Librarian (Parchment) | `1.+` |
| MixinGradle | `0.7+` |

### ランタイム依存

| ライブラリ | バージョン | 依存タイプ |
|---|---|---|
| Create | `0.5.1.f-26`（MC 1.20.1） | `implementation`（slim, transitive=false） |
| Flywheel | `0.6.10-7`（MC 1.20.1） | `implementation` |
| Registrate | `MC1.20-1.3.3` | `implementation` |
| JEI Common API | `15.3.0.7` | `compileOnly` |
| JEI Forge API | `15.3.0.7` | `compileOnly` |
| JEI Forge Runtime | `15.3.0.7` | `runtimeOnly` |
| Mixin | `0.8.5` | `annotationProcessor` |

### Maven リポジトリ

| 名前 | URL |
|---|---|
| tterrag | `https://maven.tterrag.com/` |
| blamejared | `https://maven.blamejared.com` |
| theillusivec4 | `https://maven.theillusivec4.top/` |

---

## マッピング

| 項目 | 値 |
|---|---|
| チャンネル | `parchment` |
| バージョン | `2023.09.03-1.20.1` |

Parchment マッピングは Mojang 公式マッピングの上にコミュニティによるパラメータ名・Javadoc を追加するもの。

---

## Gradle タスク

### 主要タスク

| タスク | 説明 |
|---|---|
| `./gradlew runClient` | Minecraft クライアントを起動（開発環境） |
| `./gradlew runServer` | Dedicated Server を起動（`--nogui`） |
| `./gradlew runData` | データ生成を実行（`src/generated/resources/` に出力） |
| `./gradlew runGameTestServer` | GameTest サーバーを起動 |
| `./gradlew build` | JAR をビルド |
| `./gradlew jar` | JAR 作成 → `reobfJar` が自動実行 |

### 実行設定（runs）

全実行設定に共通:
```
workingDirectory: run/
forge.logging.markers: REGISTRIES
forge.logging.console.level: debug
mixin.config: karacreate.mixins.json
mixin.env.remapRefMap: true
mixin.env.refMapRemappingFile: build/createSrgToMcp/output.srg
```

`data` 実行設定:
```
workingDirectory: run-data/
args: --mod karacreate --all
      --output src/generated/resources/
      --existing src/main/resources/
```

---

## ソースセット構成

```
src/main/java/           ← Java ソースコード
src/main/resources/      ← 手動配置リソース
src/generated/resources/ ← データ生成出力（srcDir として追加）
```

### generated/resources の除外設定

```groovy
sourceSets.main.resources {
    srcDir 'src/generated/resources'
    exclude 'assets/create'
    exclude 'assets/minecraft'
}
```

`src/generated/resources` 内の `assets/create/` と `assets/minecraft/` は **ソースセットから除外** されている。ただしこれは生成リソースの除外であり、`src/main/resources/assets/create/` と `src/main/resources/assets/minecraft/` は除外されない。

---

## リソース処理

`processResources` タスクで以下の変数が `META-INF/mods.toml` と `pack.mcmeta` 内の `${}` プレースホルダに展開される:

| 変数名 | 値 |
|---|---|
| `minecraft_version` | `1.20.1` |
| `minecraft_version_range` | `[1.20.1,1.21)` |
| `forge_version` | `47.2.32` |
| `forge_version_range` | `[47,)` |
| `loader_version_range` | `[47,)` |
| `mod_id` | `karacreate` |
| `mod_name` | `KaraCreate` |
| `mod_license` | `MIT` |
| `mod_version` | `0.1-SNAPSHOT` |
| `mod_authors` | `CKenJa` |
| `mod_description` | `Create mod addon about Japan` |

---

## JAR マニフェスト

```
Specification-Title: karacreate
Specification-Vendor: CKenJa
Specification-Version: 1
Implementation-Title: KaraCreate
Implementation-Version: (ビルド時に決定)
Implementation-Vendor: CKenJa
Implementation-Timestamp: (ビルド日時)
```

---

## ファイルエンコーディング

```groovy
tasks.withType(JavaCompile).configureEach {
    options.encoding = 'UTF-8'
}
```

---

## 開発ワークフロー

### 初回セットアップ

```bash
# リポジトリをクローン後
./gradlew build          # 依存関係のダウンロードとビルド
./gradlew runData        # データ生成（言語ファイル、タグ等）
./gradlew runClient      # ゲーム起動で動作確認
```

### 新しいコンテンツ追加後

```bash
./gradlew runData        # 生成リソースを更新
./gradlew runClient      # ゲーム内で確認
```

### リリースビルド

```bash
./gradlew build
# build/libs/ に JAR が出力される
```
