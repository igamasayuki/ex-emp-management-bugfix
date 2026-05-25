#!/usr/bin/env python3
"""解説 md に変更箇所の差分セクションを追記する（1回限りのスクリプト）."""

from pathlib import Path

BASE = Path(__file__).resolve().parent.parent / "docs" / "解き方と解説"

SECTIONS = {
    "1-1.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

> 修正前は研修生が触る **バグ入り初期状態**（`reference/starter/`）、修正後は **完成形**（`src/main/...`）の抜粋です。

### `AdministratorController.java` — `insert` メソッド

**修正前**

```java
@PostMapping("/insert")
public String insert(InsertAdministratorForm form) {
    Administrator administrator = new Administrator();
    BeanUtils.copyProperties(form, administrator);
    administratorService.insert(administrator);
    return "employee/list";  // ← 従業員一覧へフォワード（バグ）
}
```

**修正後**

```java
@PostMapping("/insert")
public String insert(@Validated @ModelAttribute InsertAdministratorForm form, BindingResult result,
        RedirectAttributes redirectAttributes) {
    // …バリデーション・重複チェック等（1-2, 1-3, 1-5）…
    if (result.hasErrors()) {
        return "administrator/insert";
    }
    Administrator administrator = new Administrator();
    BeanUtils.copyProperties(form, administrator);
    administratorService.insert(administrator);
    redirectAttributes.addFlashAttribute("infoMessage", "管理者の登録が完了しました。ログインしてください。");
    return "redirect:/";  // ← PRG：ログイン画面へリダイレクト
}
```

### `administrator/login.html` — 完了メッセージ表示

**修正前**（`infoMessage` の表示なし）

```html
<div class="form-group">
    <div th:if="${errorMessage}" class="alert alert-danger">
        <p th:text="${errorMessage}">…</p>
    </div>
</div>
```

**修正後**（追記）

```html
<div th:if="${param.error}" class="alert alert-danger">
    <p>メールアドレスまたはパスワードが不正です。</p>
</div>
<div th:if="${infoMessage}" class="alert alert-info">
    <p th:text="${infoMessage}">完了メッセージ</p>
</div>
```
''',
    "1-2.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `AdministratorController.java` — バリデーション実行

**修正前**

```java
@PostMapping("/insert")
public String insert(InsertAdministratorForm form) {
    // チェックなしでそのまま insert
}
```

**修正後**

```java
@PostMapping("/insert")
public String insert(@Validated @ModelAttribute InsertAdministratorForm form, BindingResult result,
        RedirectAttributes redirectAttributes) {
    // …1-3, 1-5 のチェック…
    if (result.hasErrors()) {
        return "administrator/insert";  // フォワード：入力値を保持
    }
    // 正常系…
}
```

### `InsertAdministratorForm.java` — アノテーション（抜粋）

Form にはもともとアノテーションがあっても、Controller で `@Validated` しないと効きません。

**修正後（Form 側の例）**

```java
@NotBlank(message = "氏名を入力してください")
@Size(max = 50, message = "氏名は50文字以内で入力してください")
private String name;

@NotBlank(message = "パスワードを入力してください")
@Size(min = 8, max = 16, message = "パスワードは8文字以上16文字以内で入力してください")
private String password;
```

### `administrator/insert.html` — エラー表示（抜粋）

**修正後**

```html
<label th:errors="*{name}" class="error-messages"></label>
<input type="text" th:field="*{name}" th:errorclass="error-input" class="form-control"/>
```
''',
    "1-3.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `AdministratorController.java` — 重複チェックの追加

**修正前**（重複時は DB 例外 → 500）

```java
@PostMapping("/insert")
public String insert(InsertAdministratorForm form) {
    administratorService.insert(administrator);
    return "employee/list";
}
```

**修正後**（`insert` 内に追記）

```java
if (administratorService.findByMailAddress(form.getMailAddress()) != null) {
    result.rejectValue("mailAddress", null, "そのメールアドレスはすでに登録されています");
}
if (result.hasErrors()) {
    return "administrator/insert";
}
```

### `AdministratorService.java` / `AdministratorRepository.java`

**修正後** — メール検索（プレースホルダ使用）

```java
// Service
public Administrator findByMailAddress(String mailAddress) {
    return administratorRepository.findByMailAddress(mailAddress);
}

// Repository
String sql = "select id,name,mail_address,password from administrators where mail_address=:mailAddress";
SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress);
```
''',
    "1-4.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `AdministratorController.java` — PRG（1-1 と共通）

**修正前**: `return "employee/list";`  
**修正後**: `return "redirect:/";`  
→ 登録成功後の **F5 再送信** を防ぐのはこちら。

### `administrator/insert.html` — ダブルクリック対策

**修正前**

```html
<form method="post" th:action="@{/insert}" th:object="${insertAdministratorForm}">
    …
    <button type="submit" class="btn btn-primary">登録</button>
</form>
```

**修正後**

```html
<form method="post" th:action="@{/insert}" th:object="${insertAdministratorForm}"
      onsubmit="document.getElementById('registerBtn').disabled=true;">
    …
    <button type="submit" id="registerBtn" class="btn btn-primary">登録</button>
</form>
```
''',
    "1-5.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `InsertAdministratorForm.java`

**修正前** — `password` のみ

```java
private String password;
```

**修正後** — 確認用を追加（DB には保存しない）

```java
private String password;
private String passwordConfirm;  // getter/setter も追加
```

### `administrator/insert.html`

**修正後**（追記）

```html
<label for="passwordConfirm">パスワード（確認用）:</label>
<input type="password" th:field="*{passwordConfirm}" id="passwordConfirm" class="form-control"/>
```

### `AdministratorController.java` — 一致チェック

**修正後**（`insert` 内）

```java
if (form.getPassword() != null && !form.getPassword().equals(form.getPasswordConfirm())) {
    result.rejectValue("password", null, "パスワードが一致しません");
}
```
''',
    "2-1.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `LoginSuccessHandler.java`（新規）

**修正後** — ログイン成功時にセッションへ名前を保存

```java
@Override
public void onAuthenticationSuccess(...) {
    Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
    if (administrator != null) {
        request.getSession().setAttribute("administratorName", administrator.getName());
    }
    response.sendRedirect(request.getContextPath() + "/employee/showList");
}
```

### `templates/common/header.html`（新規）

**修正後**

```html
<nav th:fragment="header" class="navbar navbar-default">
    …
    <span th:if="${session.administratorName != null}">
        <span th:text="${session.administratorName} + 'さん　こんにちは！'"></span>
    </span>
</nav>
```

### `employee/list.html` / `employee/detail.html`

**修正前** — ナビ内に `${administratorName}` や固定名

```html
<span th:text="${administratorName}">山田太郎</span>さんこんにちは！
```

**修正後** — 共通ヘッダーに差し替え

```html
<div th:replace="~{common/header :: header}"></div>
```

### `WebSecurityConfig.java`

**修正後** — カスタム成功ハンドラを利用

```java
.successHandler(loginSuccessHandler)
```
''',
    "3-1.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `EmployeeRepository.java` — `findAll`

**修正前**

```java
String sql = "SELECT id,name,image,... FROM employees";
// ORDER BY なし → 並びが不定
```

**修正後**

```java
String sql = "SELECT id,name,image,gender,hire_date,... FROM employees ORDER BY hire_date DESC";
```
''',
    "4-1.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `employee/list.html` / `employee/detail.html` — 入社日

**修正前**

```html
<span th:text="${employee.hireDate}">2016/12/1</span>
```

**修正後**

```html
<span th:text="${#dates.format(employee.hireDate, 'yyyy年MM月dd日')}">2016年12月01日</span>
```
''',
    "4-2.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `employee/detail.html` — 給料

**修正前**

```html
<span th:utext="${employee.salary + '円'}">400000円</span>
```

**修正後**

```html
<span th:text="${#numbers.formatInteger(employee.salary, 3, 'COMMA')} + '円'">400,000円</span>
```
''',
    "4-3.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `employee/detail.html` — パンくず

**修正前**（リンクなし）

```html
<ol class="breadcrumb">
    <li>従業員リスト</li>
    <li class="active">従業員詳細</li>
</ol>
```

**修正後**

```html
<ol class="breadcrumb">
    <li><a th:href="@{/employee/showList}">従業員リスト</a></li>
    <li class="active">従業員詳細</li>
</ol>
```
''',
    "5-1.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `AdministratorRepository.java` — ログイン用 SQL（脆弱箇所）

**修正前**（SQL インジェクションの温床）

```java
public Administrator findByMailAddressAndPassword(String mailAddress, String password) {
    String sql = "select ... where mail_address= '" + mailAddress
            + "' and password='" + password + "'";
    SqlParameterSource param = new MapSqlParameterSource();
    return template.query(sql, param, ADMINISTRATOR_ROW_MAPPER).get(0);
}
```

**修正後**

- 上記メソッドは **削除**（または未使用に）。
- ログインは **Spring Security** + `UserDetailsService` が DB からユーザーを取得。
- 他の検索はプレースホルダを使用：

```java
String sql = "select id,name,mail_address,password from administrators where mail_address=:mailAddress";
SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress);
```
''',
    "5-2.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `employee/detail.html`（ほか `th:utext` があるテンプレートも同様）

**修正前**（エスケープされない → XSS）

```html
<span th:utext="${employee.name}">山田花子</span>
<span th:utext="${employee.gender}">女性</span>
<span th:utext="${employee.mailAddress}">yamada@sample.com</span>
```

**修正後**

```html
<span th:text="${employee.name}">山田花子</span>
<span th:text="${employee.gender}">女性</span>
<span th:text="${employee.mailAddress}">yamada@sample.com</span>
```

> `detail.html` 内の **`th:utext` をすべて `th:text` に置換** してください。
''',
    "5-3.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `AdministratorService.java` — 登録時

**修正前**

```java
public void insert(Administrator administrator) {
    administratorRepository.insert(administrator);  // 平文のまま DB へ
}
```

**修正後**

```java
@Autowired
private PasswordEncoder passwordEncoder;

public void insert(Administrator administrator) {
    String encodedPassword = passwordEncoder.encode(administrator.getPassword());
    administrator.setPassword(encodedPassword);
    administratorRepository.insert(administrator);
}
```

### `WebSecurityConfig.java` — エンコーダ Bean

**修正後**

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

> ログイン照合は Security が `UserDetails` のハッシュと入力パスワードを比較します。既存の平文データは **再登録** が必要です。
''',
    "5-4.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `WebSecurityConfig.java`

**修正前**（未設定 or 不十分）— 直 URL で `/employee/showList` にアクセス可能

**修正後**（抜粋）

```java
http.authorizeHttpRequests(auth -> auth
    .requestMatchers("/", "/insert", "/toInsert", "/login").permitAll()
    .requestMatchers("/employee/**").authenticated()
    .anyRequest().authenticated()
)
.formLogin(login -> login
    .loginPage("/")
    .loginProcessingUrl("/login")
    .usernameParameter("mailAddress")
    .passwordParameter("password")
    .successHandler(loginSuccessHandler)
    .failureUrl("/?error=true")
);
```

### `LoginAdministratorDetailsService.java`（既存を利用）

```java
@Override
public UserDetails loadUserByUsername(String mailAddress) {
    Administrator admin = administratorRepository.findByMailAddress(mailAddress);
    if (admin == null) {
        throw new UsernameNotFoundException(...);
    }
    return new User(admin.getMailAddress(), admin.getPassword(), authorityList);
}
```

### `AdministratorController.java`

**修正前** — 独自 `@PostMapping("/login")` でセッション管理  
**修正後** — ログイン処理は Security に任せ、`/` はログイン画面表示のみ
''',
    "6-1.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### 新規ファイル（Spring Boot 規約）

**修正前** — `templates/error/` 配下なし → Whitelabel Error Page

**修正後**

```text
src/main/resources/templates/error/
  ├── 404.html
  └── 500.html
```

**`404.html` 抜粋**

```html
<h2>ページが見つかりません</h2>
<a th:href="@{/}" class="btn btn-primary">ログイン画面へ戻る</a>
```

> 存在しない URL（例: `/hoge`）やサーバエラーで表示を確認します。
''',
    "6-2.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `EmployeeRepository.java`

**修正後**（新規メソッド）

```java
public List<Employee> findByName(String name) {
    String sql = "SELECT ... FROM employees WHERE name LIKE :name ORDER BY hire_date DESC";
    SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
    return template.query(sql, param, EMPLOYEE_ROW_MAPPER);
}
```

### `EmployeeService.java`

**修正後**

```java
public List<Employee> findByName(String name) {
    if (name == null || name.isEmpty()) {
        return employeeRepository.findAll();
    }
    return employeeRepository.findByName(name);
}
```

### `EmployeeController.java`

**修正後**（新規）

```java
@GetMapping("/search")
public String search(String name, Model model) {
    List<Employee> employeeList = employeeService.findByName(name);
    if (employeeList.isEmpty()) {
        model.addAttribute("message", "１件もありませんでした");
        return showList(1, model);
    }
    model.addAttribute("employeeList", employeeList);
    return "employee/list";
}
```

### `employee/list.html`

**修正後** — 検索フォームを追加（一覧上部）
''',
    "6-3.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `EmployeeService.java`

**修正後**

```java
public synchronized void insert(Employee employee) {
    Integer maxId = employeeRepository.getMaxId();
    employee.setId(maxId == null ? 1 : maxId + 1);
    employeeRepository.insert(employee);
}
```

### `EmployeeController.java`

**修正後** — `toInsert` / `insert` を追加（画像・バリデーション含む）

### `templates/employee/insert.html`（新規）

- ヘッダに「従業員登録」リンク（`common/header.html`）
- `enctype="multipart/form-data"`
- AjaxZip3: `onkeyup="AjaxZip3.zip2addr('zipCode','','address','address');"`

### `EmployeeRepository.java`

**修正後** — `getMaxId()` / `insert(Employee)` を追加
''',
    "6-4.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `employee/list.html` — 検索欄

**修正前**

```html
<input type="text" name="name" id="name" class="form-control input-sm" placeholder="名前の一部を入力">
```

**修正後**（datalist）

```html
<input type="text" name="name" id="name" list="employeeNames" class="form-control input-sm"
       placeholder="名前の一部を入力"/>
<datalist id="employeeNames">
    <option th:each="empName : ${employeeNames}" th:value="${empName}"></option>
</datalist>
```

### `EmployeeController.java` — `showList`

**修正後** — Model に候補を渡す

```java
model.addAttribute("employeeNames",
    allEmployees.stream().map(Employee::getName).collect(Collectors.toList()));
```
''',
    "6-5.md": '''
---

## 変更箇所の差分（修正前 → 修正後）

### `EmployeeRepository.java`

**修正後**

```java
public List<Employee> findAll(int limit, int offset) {
    String sql = "SELECT ... FROM employees ORDER BY hire_date DESC LIMIT :limit OFFSET :offset";
    // limit=10, offset=(page-1)*10
}
public Integer count() { ... }
```

### `EmployeeController.java` — `showList`

**修正前**

```java
public String showList(Model model) {
    List<Employee> employeeList = employeeService.showList();
    ...
}
```

**修正後**

```java
public String showList(Integer page, Model model) {
    page = (page == null) ? 1 : page;
    List<Employee> employeeList = employeeService.showList(page, 10);
    // currentPage, totalPages, pageNumbers を Model にセット
}
```

### `employee/list.html`

**修正後** — ページネーション UI をテーブル下に追加

```html
<a th:href="@{/employee/showList(page=${p})}" th:text="${p}">1</a>
```
''',
}

for name, section in SECTIONS.items():
    path = BASE / name
    text = path.read_text(encoding="utf-8")
    if "## 変更箇所の差分" in text:
        print(f"skip (already): {name}")
        continue
    path.write_text(text.rstrip() + section, encoding="utf-8")
    print(f"appended: {name}")
