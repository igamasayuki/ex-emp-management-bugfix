# 完成形リファレンス（講師用）

研修生向けの **バグ入りスターター** と、講師が動作確認・レビュー比較に使う **完成形（本番パス）** の関係をまとめます。

## 構成

| 種別 | 場所 | 用途 |
|------|------|------|
| **完成形（実行するコード）** | `src/main/java/com/example/` 配下の本番クラス、`templates/administrator/`, `templates/employee/`, `templates/common/`, `templates/error/` | 1-1〜6-5 を満たした状態。`./gradlew bootRun` で確認 |
| **スターター（バグ入り）** | `reference/starter/` | 研修配布用の初期状態のコピー。必要ならここから `src/` へ戻して再配布 |

## 完成形で対応している課題（1-1〜6-5）

| ID | 主な変更箇所 |
|----|----------------|
| 1-1 | `AdministratorController` PRG + `administrator/login.html` フラッシュ |
| 1-2 | `@Validated` / `BindingResult` / `th:errors` |
| 1-3 | メール重複 `rejectValue` |
| 1-4 | `insert.html` onsubmit + PRG |
| 1-5 | `passwordConfirm` |
| 2-1 | `LoginSuccessHandler` + `common/header.html` |
| 3-1 | `EmployeeRepository` `ORDER BY hire_date DESC` |
| 4-1 | `#dates.format` 一覧・詳細 |
| 4-2 | `#numbers.formatInteger` 詳細 |
| 4-3 | パンくず `th:href="@{/employee/showList}"` |
| 5-1 | SQL プレースホルダ（脆弱な連結メソッドは削除） |
| 5-2 | `th:text` 統一 |
| 5-3 | `AdministratorService` + `BCryptPasswordEncoder` |
| 5-4 | `WebSecurityConfig` + `UserDetailsService` |
| 6-1 | `templates/error/404.html`, `500.html` |
| 6-2 | `/employee/search` |
| 6-3 | `/employee/toInsert`, `insert`, 画像・AjaxZip3・`synchronized` |
| 6-4 | `datalist` オートコンプリート |
| 6-5 | 10件ページング |

**未含む**: (7-1) JUnit、(6-6) 独自機能

## 追加課題完成形（step5_beyond・講師比較用）

本番パス（`/employee`）は **本編 6-5 まで**。追加課題 8-1〜33-1 の完成例は **独立系統** の `step5_beyond` に実装しています。

| 項目 | 値 |
|------|-----|
| ログイン | `http://localhost:8080/beyond/` |
| 従業員一覧 | `http://localhost:8080/beyondEmployee/showList` |
| Java | `controller/service/repository/step5_beyond` |
| 11-1 ソート（Enum 別解） | `domain/BeyondEmployeeSortKey.java`（if 別解は `BeyondEmployeeSort.java`） |
| HTML | `templates/step5_beyond/` |
| 17-1 テスト | `src/test/java/.../step5_beyond/` + `application-test.yml` |
| 33-1 手順書 | `docs/deploy/デプロイ手順書.md`（テンプレート） |

対応課題: 8-1, 9-1, 11-1, 12-1, 13-3, 17-1, 18-1/30-1（`messages.properties`）, 22-1, 23-1, 27-1, 29-1, 33-1（手順書）

## 注意（既存DBデータ）

5-3 適用後はパスワードが **BCrypt ハッシュ** になります。既存の平文パスワード管理者ではログインできないため、**新規管理者登録** するか、DB の password 列を更新してください。

## 動作確認の流れ（例）

1. PostgreSQL を起動し `application.yml` の接続情報を合わせる
2. `./gradlew bootRun`
3. `http://localhost:8080/` → 管理者登録 → ログイン
4. 従業員一覧・検索・登録・詳細・ログアウト
5. 未ログインで `http://localhost:8080/employee/showList` → ログインへ（5-4）

## スターターへ戻す場合

研修生に配布するバグ入り状態に戻すときは、`reference/starter/` から該当ファイルを `src/` にコピーしてください。

```bash
# 例: 管理者コントローラのみ戻す
cp reference/starter/java/com/example/controller/AdministratorController.java \
   src/main/java/com/example/controller/
```

## レビュー・指導時

- 研修生の提出物は [02_レビュー判定基準.md](../02_レビュー判定基準.md) の Must と照合する。
- 各課題の **修正前／修正後コード** は [解き方と解説/](../解き方と解説/) 各ファイル末尾の「変更箇所の差分」を参照（修正前 ≒ `reference/starter/`）。
