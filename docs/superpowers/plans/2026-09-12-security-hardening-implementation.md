# Skin Disease Diagnosis App Security Hardening Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a clean-history, interview-ready public copy of the skin-disease diagnosis app with server-side EasyDL integration, enforced authentication, safe password flows, parameterized queries, secure image upload, sanitized data, and reproducible verification.

**Architecture:** Preserve the existing Uni-app and Spring Boot/MyBatis-Plus structure while moving secrets and model calls behind a focused backend adapter. Central services own authentication, password reset, query validation, and image storage so controllers do not duplicate security decisions. The source graduation-project folder remains read-only; all edits and Git history live in the safe-public copy.

**Tech Stack:** Java 8, Spring Boot 2.7.18, MyBatis-Plus 3.5.7.1, Redis, MySQL 8, BCrypt, JUnit 5, Mockito, Uni-app/Vue 2, EasyDL HTTP API

**Spec:** `docs/superpowers/specs/2026-09-12-security-hardening-design.md`

## Global Constraints

- Do not modify `D:/项目/毕业设计/皮肤病智诊影析小程序/`.
- Do not modify the depression project, resume files, or any unrelated directory.
- Work only in `C:/Users/20030528z'j'z/Documents/Codex/2026-09-10/qin/skin-disease-diagnosis-app-safe-public`.
- Keep Java 8 compatibility and do not migrate to Spring Boot 3.
- Do not place a real password, private key, mail authorization code, EasyDL credential, personal record, historical token, or verification code in the safe repository.
- Do not publish, rename, delete, or overwrite a GitHub repository during implementation.
- Use a failing test before each behavior change and commit only after the focused tests pass.

---

### Task 1: Create a sanitized source baseline without touching the original

**Files:**
- Create outside Git: `../skin-disease-original-source.sha256`
- Create: `.gitignore`
- Create: `.env.example`
- Modify: `project/src/main/resources/application.yml`
- Modify: `client_home/src/pagesC/skin_detection/edit.vue`
- Delete from safe copy: `project/src/main/java/com/project/demo/util/RsaUtils.java`
- Replace: `project.sql`

**Interfaces:**
- Consumes: the read-only original folder.
- Produces: a source-only safe copy with no known credential or personal-data literals and a hash manifest used in Task 10.

- [ ] **Step 1: Record the original source hash manifest**

Run a PowerShell manifest over maintained files while excluding generated dependency and build folders:

```powershell
$source = 'D:\项目\毕业设计\皮肤病智诊影析小程序'
Get-ChildItem -LiteralPath $source -Recurse -File |
  Where-Object { $_.FullName -notmatch '\\(node_modules|target|dist|unpackage|\.idea)\\' } |
  ForEach-Object {
    $relative = $_.FullName.Substring($source.Length + 1)
    $hash = (Get-FileHash -LiteralPath $_.FullName -Algorithm SHA256).Hash
    "$hash  $relative"
  } | Sort-Object | Set-Content -LiteralPath '..\skin-disease-original-source.sha256' -Encoding UTF8
```

- [ ] **Step 2: Copy maintained source only**

Copy `client_home` and `project` while excluding `node_modules`, `target`, `dist`, `unpackage`, `.idea`, and `*.iml`. Copy `project.sql` for local sanitization only. Do not add copied files to Git yet.

- [ ] **Step 3: Add repository-wide ignore rules**

Create `.gitignore` with this minimum content:

```gitignore
.idea/
*.iml
**/target/
**/node_modules/
**/dist/
**/unpackage/
.env
.env.*
!.env.example
*.local
*.key
*.pem
*.p12
*.jks
logs/
*.log
uploads/
*.dump
*.bak
application-local.yml
```

Create `.env.example` with non-secret placeholders only:

```dotenv
DB_URL=jdbc:mysql://127.0.0.1:3306/project_db?serverTimezone=Asia/Shanghai&useSSL=false&characterEncoding=utf-8
DB_USERNAME=project_app
DB_PASSWORD=replace_with_local_password
REDIS_HOST=127.0.0.1
REDIS_PORT=6379
REDIS_PASSWORD=
MAIL_USERNAME=replace_with_sender_address
MAIL_PASSWORD=replace_with_mail_authorization_code
EASYDL_ENABLED=false
EASYDL_API_KEY=replace_with_rotated_api_key
EASYDL_SECRET_KEY=replace_with_rotated_secret_key
EASYDL_MODEL_URL=replace_with_model_endpoint
PASSWORD_RESET_ENABLED=false
PASSWORD_RESET_HMAC_KEY=replace_with_at_least_32_random_bytes
APP_UPLOAD_DIR=./uploads
APP_ALLOWED_ORIGINS=http://localhost:8080
```

- [ ] **Step 4: Remove known secret-bearing code before the first source commit**

Delete the EasyDL constants and direct Baidu OAuth/model requests from `edit.vue`. Replace `startDiagnosis()` with `uni.uploadFile` to `~/api/ai/skin-diagnosis`, multipart field `file`, and the existing `X-Auth-Token`; preserve the page's loading/result/error state. Task 8 supplies this endpoint. Delete `RsaUtils.java` and remove its imports/usages from `UserController`. Replace every sensitive YAML value with an environment expression and no secret fallback:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://127.0.0.1:3306/project_db?serverTimezone=Asia/Shanghai&useSSL=false&characterEncoding=utf-8}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  mail:
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
  redis:
    host: ${REDIS_HOST:127.0.0.1}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
app:
  easydl:
    enabled: ${EASYDL_ENABLED:false}
    api-key: ${EASYDL_API_KEY:}
    secret-key: ${EASYDL_SECRET_KEY:}
    model-url: ${EASYDL_MODEL_URL:}
  upload:
    dir: ${APP_UPLOAD_DIR:./uploads}
    max-bytes: 5242880
  cors:
    allowed-origins: ${APP_ALLOWED_ORIGINS:http://localhost:8080}
  password-reset:
    enabled: ${PASSWORD_RESET_ENABLED:false}
    hmac-key: ${PASSWORD_RESET_HMAC_KEY:}
```

- [ ] **Step 5: Replace the database dump with a schema-only script**

Keep `CREATE TABLE`, indexes, and non-personal role/category seed rows. Remove all rows from `user`, `registered_user`, `online_consultation`, `skin_detection`, `access_token`, `code_token`, and any table containing phone, email, free text, images, passwords, or session values.

- [ ] **Step 6: Run the first sensitive-information gate**

Run searches over tracked candidates, excluding dependency/build folders:

```powershell
rg -n -i --glob '!**/node_modules/**' --glob '!**/target/**' --glob '!**/dist/**' --glob '!**/unpackage/**' '(api[_-]?key\s*[=:]\s*["''][^$<]|secret[_-]?key\s*[=:]\s*["''][^$<]|private[ _-]?key|BEGIN [A-Z ]*PRIVATE KEY|mail\.password\s*:|access[_-]?token\s*[=:]\s*["''][A-Za-z0-9_-]{16,})' .
```

Expected: only documentation, property names, environment placeholders, and test fixtures explicitly marked as fake; no credential-shaped literal.

- [ ] **Step 7: Commit the sanitized baseline**

```bash
git add .gitignore .env.example client_home project project.sql
git commit -m "chore: create sanitized public source baseline"
```

### Task 2: Establish a maintainable Java 8 build and safe configuration model

**Files:**
- Modify: `project/pom.xml`
- Modify: `project/src/main/java/com/project/demo/Application.java`
- Modify: `project/src/main/resources/application.yml`
- Create: `project/src/main/java/com/project/demo/config/AppSecurityProperties.java`
- Create: `project/src/test/java/com/project/demo/config/AppSecurityPropertiesTest.java`

**Interfaces:**
- Consumes: environment variable names from Task 1.
- Produces: `AppSecurityProperties` with nested `Cors`, `Upload`, and `EasyDl` configuration objects used by Tasks 3, 7, and 8.

- [ ] **Step 1: Write a failing configuration binding test**

```java
@Test
void rejectsBlankEasyDlSecretsWhenDiagnosisIsEnabled() {
    AppSecurityProperties properties = new AppSecurityProperties();
    properties.getEasyDl().setEnabled(true);
    assertThrows(IllegalStateException.class, properties::validate);
}
```

- [ ] **Step 2: Run the focused test and verify the class is missing**

Run: `mvn -f project/pom.xml -Dtest=AppSecurityPropertiesTest test`

Expected: FAIL because `AppSecurityProperties` does not exist.

- [ ] **Step 3: Update dependencies and add typed configuration**

Set Spring Boot to `2.7.18`, retain Java source/target 8, set MyBatis-Plus to `3.5.7.1`, add `spring-security-crypto`, and remove unused JPA, Alipay, Aliyun image-recognition, Baidu SDK, duplicate mail, and devtools dependencies after confirming no maintained source imports them. Remove `DataSourceAutoConfiguration` exclusion and `@EnableJpaRepositories` from `Application` so Spring Boot configures the datasource normally.

Implement:

```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppSecurityProperties {
    private final Cors cors = new Cors();
    private final Upload upload = new Upload();
    private final EasyDl easyDl = new EasyDl();
    private final PasswordReset passwordReset = new PasswordReset();
    public void validate() {
        if (easyDl.enabled && (isBlank(easyDl.apiKey) || isBlank(easyDl.secretKey) || isBlank(easyDl.modelUrl))) {
            throw new IllegalStateException("EasyDL is enabled but required environment variables are missing");
        }
    }
}
```

Use Lombok `@Getter`/`@Setter` on this configuration class and the nested `Cors`, `Upload`, `EasyDl`, and `PasswordReset` classes. Give `EasyDl.enabled` and `PasswordReset.enabled` the safe default `false`, `Upload.maxBytes` the value `5242880`, and `Cors.allowedOrigins` the default singleton list `http://localhost:8080`. Credential strings default to empty only so disabled integrations can start; `validate()` rejects empty credentials whenever the corresponding integration is enabled.

- [ ] **Step 4: Run configuration and compile tests**

Run: `mvn -f project/pom.xml -Dtest=AppSecurityPropertiesTest test`

Expected: PASS.

- [ ] **Step 5: Commit the build baseline**

```bash
git add project/pom.xml project/src/main/java/com/project/demo/Application.java project/src/main/resources/application.yml project/src/main/java/com/project/demo/config/AppSecurityProperties.java project/src/test/java/com/project/demo/config/AppSecurityPropertiesTest.java
git commit -m "build: modernize Java 8 dependencies and configuration"
```

### Task 3: Implement BCrypt password handling and non-sensitive user responses

**Files:**
- Create: `project/src/main/java/com/project/demo/security/PasswordService.java`
- Create: `project/src/main/java/com/project/demo/dto/auth/LoginRequest.java`
- Create: `project/src/main/java/com/project/demo/dto/auth/RegisterRequest.java`
- Create: `project/src/main/java/com/project/demo/dto/auth/UserResponse.java`
- Modify: `project/src/main/java/com/project/demo/entity/User.java`
- Modify: `project/src/main/java/com/project/demo/controller/UserController.java`
- Test: `project/src/test/java/com/project/demo/security/PasswordServiceTest.java`
- Test: `project/src/test/java/com/project/demo/dto/auth/UserResponseTest.java`

**Interfaces:**
- Produces: `String PasswordService.hash(String rawPassword)`, `boolean PasswordService.matches(String rawPassword, String encodedPassword)`, and `UserResponse UserResponse.from(User user)`.
- Consumes: `UserMapper`/`UserService` for persistence; Task 4 adds session issuance to login.

- [ ] **Step 1: Write failing BCrypt and serialization tests**

```java
@Test
void hashesAndMatchesWithoutStoringPlaintext() {
    String hash = passwordService.hash("SafePass123!");
    assertNotEquals("SafePass123!", hash);
    assertTrue(passwordService.matches("SafePass123!", hash));
    assertFalse(passwordService.matches("wrong", hash));
}

@Test
void userResponseContainsNoPasswordProperty() throws Exception {
    User user = new User();
    user.setUserId(7);
    user.setUsername("demo-user");
    user.setPassword("must-not-appear");
    String json = objectMapper.writeValueAsString(UserResponse.from(user));
    assertFalse(json.contains("password"));
}
```

- [ ] **Step 2: Run tests and verify they fail**

Run: `mvn -f project/pom.xml -Dtest=PasswordServiceTest,UserResponseTest test`

Expected: FAIL because the password service and response DTO do not exist.

- [ ] **Step 3: Implement password validation and BCrypt**

Reject null, whitespace-only, shorter-than-8, and longer-than-72-byte passwords. Use `BCryptPasswordEncoder(12)`. Mark `User.password` write-only with Jackson and stop converting entire `User` objects through Fastjson for API responses.

- [ ] **Step 4: Convert register/login/change-password endpoints to DTOs**

Register hashes the password before insert. Login finds by username/email/phone using parameterized service methods and calls `matches`. Change password resolves the authenticated user ID from Task 4 request context and hashes the replacement. All successful responses use `UserResponse`.

- [ ] **Step 5: Run focused tests**

Run: `mvn -f project/pom.xml -Dtest=PasswordServiceTest,UserResponseTest test`

Expected: PASS.

- [ ] **Step 6: Commit password security**

```bash
git add project/src/main/java/com/project/demo/security project/src/main/java/com/project/demo/dto/auth project/src/main/java/com/project/demo/entity/User.java project/src/main/java/com/project/demo/controller/UserController.java project/src/test/java/com/project/demo/security project/src/test/java/com/project/demo/dto/auth
git commit -m "feat: secure passwords and user responses"
```

### Task 4: Enforce Redis-backed authentication and role-aware endpoint policy

**Files:**
- Create: `project/src/main/java/com/project/demo/security/SessionUser.java`
- Create: `project/src/main/java/com/project/demo/security/IssuedToken.java`
- Create: `project/src/main/java/com/project/demo/security/AccessLevel.java`
- Create: `project/src/main/java/com/project/demo/security/TokenService.java`
- Create: `project/src/main/java/com/project/demo/security/EndpointAccessPolicy.java`
- Create: `project/src/main/java/com/project/demo/security/RequestUser.java`
- Modify: `project/src/main/java/com/project/demo/interceptor/LoginInterceptor.java`
- Modify: `project/src/main/java/com/project/demo/config/WebAppConfig.java`
- Modify: `project/src/main/java/com/project/demo/controller/UserController.java`
- Test: `project/src/test/java/com/project/demo/security/TokenServiceTest.java`
- Test: `project/src/test/java/com/project/demo/interceptor/LoginInterceptorTest.java`

**Interfaces:**
- Produces: `IssuedToken TokenService.issue(SessionUser user)`, `Optional<SessionUser> TokenService.resolve(String rawToken)`, `void TokenService.revoke(String rawToken)`, `AccessLevel EndpointAccessPolicy.required(String method, String path)`, and request attributes `authenticatedUserId`/`authenticatedUserGroup`.
- Consumes: `AppSecurityProperties` from Task 2 and `UserResponse` from Task 3.

- [ ] **Step 1: Write failing authentication policy tests**

```java
@Test
void anonymousRequestToDiagnosisIsRejected() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/ai/skin-diagnosis");
    request.setContextPath("/api");
    MockHttpServletResponse response = new MockHttpServletResponse();
    assertFalse(interceptor.preHandle(request, response, handler));
    assertEquals(401, response.getStatus());
}

@Test
void publicLoginAndOptionsRemainAccessible() {
    assertEquals(PUBLIC, policy.required("POST", "/user/login"));
    assertEquals(PUBLIC, policy.required("OPTIONS", "/anything"));
}
```

- [ ] **Step 2: Run tests and verify the current interceptor fails them**

Run: `mvn -f project/pom.xml -Dtest=TokenServiceTest,LoginInterceptorTest test`

Expected: FAIL because the current interceptor always returns `true`.

- [ ] **Step 3: Implement session tokens without logging raw values**

Generate 32 random bytes with `SecureRandom`, encode Base64 URL-safe without padding, and store `SessionUser` under `session:` plus SHA-256 of the raw token. Set a two-hour TTL. Never persist session tokens to SQL and never log the raw token.

- [ ] **Step 4: Implement endpoint policy**

Use explicit path/method rules. Public access is limited to login, registration, reset request/confirm, OPTIONS, static resources, and GET reads for article, knowledge, notice, slides, hospital, forum and public type endpoints. Authenticated access covers diagnosis, personal profile, consultations, skin records, comments, praise and collection. Admin access covers `user_group`, `auth`, user administration, and mutations of public content. Default to `AUTHENTICATED`, not `PUBLIC`.

- [ ] **Step 5: Replace reflected CORS and redirect behavior**

Configure allowed origins from `app.cors.allowed-origins`; return structured JSON with 401 or 403. Set request user attributes after a successful lookup. Do not overwrite successful response status in the interceptor.

- [ ] **Step 6: Run focused tests**

Run: `mvn -f project/pom.xml -Dtest=TokenServiceTest,LoginInterceptorTest test`

Expected: PASS.

- [ ] **Step 7: Commit enforced authentication**

```bash
git add project/src/main/java/com/project/demo/security project/src/main/java/com/project/demo/interceptor/LoginInterceptor.java project/src/main/java/com/project/demo/config/WebAppConfig.java project/src/main/java/com/project/demo/controller/UserController.java project/src/test/java/com/project/demo/security project/src/test/java/com/project/demo/interceptor
git commit -m "feat: enforce Redis session authentication"
```

### Task 5: Replace the password-reset vulnerability with a one-time server-side flow

**Files:**
- Create: `project/src/main/java/com/project/demo/security/PasswordResetService.java`
- Create: `project/src/main/java/com/project/demo/security/VerificationCodeGenerator.java`
- Create: `project/src/main/java/com/project/demo/security/InvalidResetCodeException.java`
- Create: `project/src/main/java/com/project/demo/dto/auth/PasswordResetRequest.java`
- Create: `project/src/main/java/com/project/demo/dto/auth/PasswordResetConfirmRequest.java`
- Modify: `project/src/main/java/com/project/demo/controller/CodeTokenController.java`
- Modify: `project/src/main/java/com/project/demo/controller/UserController.java`
- Modify: `client_home/src/pagesB/account/forgot.vue`
- Test: `project/src/test/java/com/project/demo/security/PasswordResetServiceTest.java`

**Interfaces:**
- Produces: `String VerificationCodeGenerator.generate()`, `void PasswordResetService.request(String email, String sourceKey)`, and `void PasswordResetService.confirm(String email, String code, String newPassword)`.
- Consumes: `PasswordService` from Task 3, Redis, `JavaMailSender`, and user lookup/update methods.

- [ ] **Step 1: Write failing expiry, retry and one-time-use tests**

```java
@Test
void validCodeCanBeUsedOnlyOnce() {
    when(codeGenerator.generate()).thenReturn("123456");
    service.request("demo@example.invalid", "127.0.0.1");
    service.confirm("demo@example.invalid", "123456", "SafePass123!");
    assertThrows(InvalidResetCodeException.class,
        () -> service.confirm("demo@example.invalid", "123456", "AnotherPass123!"));
}

@Test
void requestResponseNeverContainsTheCode() throws Exception {
    mockMvc.perform(post("/code_token/password-reset/request")
        .contentType(APPLICATION_JSON)
        .content("{\"email\":\"demo@example.invalid\"}"))
        .andExpect(status().isOk())
        .andExpect(content().string(not(containsString("123456"))));
}
```

- [ ] **Step 2: Run the test and confirm the old flow fails**

Run: `mvn -f project/pom.xml -Dtest=PasswordResetServiceTest test`

Expected: FAIL because the old GET endpoint returns a reset token and the password endpoint does not verify the code.

- [ ] **Step 3: Implement the Redis reset record**

Store an HMAC-SHA-256 digest of the code using an application secret supplied through `PASSWORD_RESET_HMAC_KEY`, plus normalized email, purpose, remaining attempts, and ten-minute TTL. Add a sixty-second send cooldown and a per-hour cap. Implement `VerificationCodeGenerator` with `SecureRandom` and a zero-padded six-digit result. Send mail only after the Redis record is created; delete the record if sending fails.

- [ ] **Step 4: Replace public reset endpoints and update the page**

Expose POST `/code_token/password-reset/request` and POST `/user/password-reset/confirm`. Return the same request response for existing and nonexistent accounts. Remove `send_email`, `verify_email_code`, and the insecure `forget_password` behavior. Update `forgot.vue` to submit email/code/new password directly to the confirm endpoint.

- [ ] **Step 5: Run reset tests**

Run: `mvn -f project/pom.xml -Dtest=PasswordResetServiceTest test`

Expected: PASS.

- [ ] **Step 6: Commit password reset security**

```bash
git add project/src/main/java/com/project/demo/security/PasswordResetService.java project/src/main/java/com/project/demo/dto/auth project/src/main/java/com/project/demo/controller/CodeTokenController.java project/src/main/java/com/project/demo/controller/UserController.java client_home/src/pagesB/account/forgot.vue project/src/test/java/com/project/demo/security/PasswordResetServiceTest.java
git commit -m "feat: add one-time password reset flow"
```

### Task 6: Remove raw SQL execution and validate all query identifiers

**Files:**
- Modify: `project/src/main/java/com/project/demo/dao/base/BaseMapper.java`
- Delete: `project/src/main/resources/mapper/BaseMapper.xml`
- Create: `project/src/main/java/com/project/demo/query/EntityFieldRegistry.java`
- Create: `project/src/main/java/com/project/demo/query/QuerySpec.java`
- Create: `project/src/main/java/com/project/demo/query/QuerySpecParser.java`
- Create: `project/src/main/java/com/project/demo/query/InvalidQueryException.java`
- Modify: `project/src/main/java/com/project/demo/constant/FindConfig.java`
- Rewrite: `project/src/main/java/com/project/demo/service/base/BaseService.java`
- Modify: `project/src/main/java/com/project/demo/controller/base/BaseController.java`
- Modify: `project/src/main/java/com/project/demo/controller/UserController.java`
- Modify: `project/src/main/java/com/project/demo/entity/HospitalInformation.java`
- Modify: `project/src/main/java/com/project/demo/entity/KnowledgePopularization.java`
- Modify: `project/src/main/java/com/project/demo/entity/OnlineConsultation.java`
- Modify: `project/src/main/java/com/project/demo/entity/SkinDetection.java`
- Modify: `project/src/main/java/com/project/demo/entity/RegisteredUser.java`
- Modify: `project/pom.xml`
- Test: `project/src/test/java/com/project/demo/query/QuerySpecParserTest.java`
- Test: `project/src/test/java/com/project/demo/service/base/BaseServiceTest.java`

**Interfaces:**
- Produces: `QuerySpec QuerySpecParser.parse(Class<?> entityType, HttpServletRequest request)`, `QueryWrapper<E> BaseService.buildWhere(QuerySpec spec)`, `Map<String,Object> BaseService.selectToPage(QuerySpec spec)`, and parameterized aggregate methods.
- Consumes: MyBatis-Plus `BaseMapper<E>` and entity `@TableField`/`@TableId` metadata.

- [ ] **Step 1: Write failing identifier-rejection tests**

```java
@Test
void rejectsUnknownOrderColumnAndRawSqlParameter() {
    request.setParameter("orderby", "username desc; drop table user");
    assertThrows(InvalidQueryException.class,
        () -> parser.parse(User.class, request));
    request = new MockHttpServletRequest();
    request.setParameter("sqlwhere", "1=1");
    assertThrows(InvalidQueryException.class,
        () -> parser.parse(User.class, request));
}

@Test
void bindsValuesInsteadOfEmbeddingThem() {
    request.setParameter("username", "a' OR '1'='1");
    QueryWrapper<User> wrapper = service.buildWhere(parser.parse(User.class, request));
    assertFalse(wrapper.getSqlSegment().contains("a' OR '1'='1"));
}
```

- [ ] **Step 2: Run tests and verify the old string builder fails**

Run: `mvn -f project/pom.xml -Dtest=QuerySpecParserTest,BaseServiceTest test`

Expected: FAIL because `sqlwhere`, arbitrary field names, and string-concatenated values are currently accepted.

- [ ] **Step 3: Reduce the custom mapper to parameterized MyBatis-Plus operations**

Make the custom mapper extend MyBatis-Plus `BaseMapper<E>` without methods that accept complete SQL strings. Delete `BaseMapper.xml` and remove `selectBaseList(String)`, `selectBaseCount(String)`, `updateBaseSql(String)`, and `deleteBaseSql(String)`.

- [ ] **Step 4: Implement field metadata and request parsing**

Build an immutable map from Java property name and snake-case column name to validated column name. Accept page size in `1..100`, page number at least 1, `like` as a boolean, and order direction only as `asc`/`desc`. Reject `sqlwhere` whenever present. Reject unknown filter, field, order, and group names with HTTP 400.

- [ ] **Step 5: Rewrite query and aggregate operations**

Use `QueryWrapper.eq`, `like`, `ge`, `le`, `orderByAsc`, `orderByDesc`, and `groupBy` only with validated columns. Use `selectPage`, `selectList`, `selectMaps`, `selectCount`, `insert`, `update`, and `delete` from MyBatis-Plus. Do not log rendered SQL or entity contents.

- [ ] **Step 6: Update generic controller methods**

Use `QuerySpecParser` for read/update/delete operations, constrain generic mutations through the endpoint policy, and return HTTP 400 for invalid query specifications. Remove the inherited generic file-upload method; Task 7 provides the only upload endpoint.

- [ ] **Step 7: Remove Fastjson from maintained backend code**

Inject Jackson `ObjectMapper` where object-to-entity conversion is still needed, use Jackson `JsonNode` or typed DTOs for API responses, and remove Fastjson imports from `BaseController`, `BaseService`, `UserController`, `HospitalInformation`, `KnowledgePopularization`, `OnlineConsultation`, `SkinDetection`, and `RegisteredUser`. Remove the Fastjson dependency only after `rg -n 'com\.alibaba\.fastjson' project/src/main` returns no matches.

- [ ] **Step 8: Run query tests and raw-SQL scan**

Run: `mvn -f project/pom.xml -Dtest=QuerySpecParserTest,BaseServiceTest test`

Then run:

```powershell
rg -n '\$\{(sql|select|count)\}|sqlwhere|updateBaseSql|deleteBaseSql' project/src/main
```

Expected: tests PASS; the raw-SQL scan returns no executable path; the Fastjson import scan returns no matches.

- [ ] **Step 9: Commit query safety**

```bash
git add project/pom.xml project/src/main/java/com/project/demo/dao/base/BaseMapper.java project/src/main/java/com/project/demo/query project/src/main/java/com/project/demo/constant/FindConfig.java project/src/main/java/com/project/demo/service/base/BaseService.java project/src/main/java/com/project/demo/controller project/src/main/java/com/project/demo/entity project/src/test/java/com/project/demo/query project/src/test/java/com/project/demo/service/base
git rm project/src/main/resources/mapper/BaseMapper.xml
git commit -m "refactor: replace raw SQL with validated query wrappers"
```

### Task 7: Create one secure image-upload boundary

**Files:**
- Create: `project/src/main/java/com/project/demo/storage/StoredImage.java`
- Create: `project/src/main/java/com/project/demo/storage/ImageStorageService.java`
- Create: `project/src/main/java/com/project/demo/storage/InvalidImageException.java`
- Rewrite: `project/src/main/java/com/project/demo/controller/UploadController.java`
- Modify: `project/src/main/java/com/project/demo/config/WebAppConfig.java`
- Modify: `project/src/main/resources/application.yml`
- Test: `project/src/test/java/com/project/demo/storage/ImageStorageServiceTest.java`
- Test: `project/src/test/java/com/project/demo/controller/UploadControllerTest.java`

**Interfaces:**
- Produces: `StoredImage ImageStorageService.store(MultipartFile file)` and POST `/api/upload/image` returning `{ "result": { "url": "/api/upload/files/<uuid>.<ext>" } }`.
- Consumes: `AppSecurityProperties.Upload` and authenticated request context from Task 4.

- [ ] **Step 1: Write failing malicious-upload tests**

```java
@Test
void rejectsTraversalNameAndFakeImageContent() {
    MockMultipartFile fake = new MockMultipartFile(
        "file", "../../shell.jpg", "image/jpeg", "not-an-image".getBytes(UTF_8));
    assertThrows(InvalidImageException.class, () -> storage.store(fake));
}

@Test
void storesValidPngUnderGeneratedName() {
    StoredImage stored = storage.store(validOnePixelPng("same.png"));
    assertTrue(stored.getFileName().matches("[0-9a-f-]{36}\\.png"));
    assertFalse(stored.getFileName().contains("same"));
}

private MockMultipartFile validOnePixelPng(String name) {
    byte[] png = Base64.getDecoder().decode(
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAusB9Y9ZlWsAAAAASUVORK5CYII=");
    return new MockMultipartFile("file", name, "image/png", png);
}
```

- [ ] **Step 2: Run tests and verify the inherited upload fails**

Run: `mvn -f project/pom.xml -Dtest=ImageStorageServiceTest,UploadControllerTest test`

Expected: FAIL because the service does not exist and the old endpoint trusts the original name/content type.

- [ ] **Step 3: Implement safe image storage**

Reject empty files and files larger than 5 MB. Decode with `ImageIO` and accept only PNG/JPEG plus explicitly verified WebP support if a compatible decoder is installed; otherwise reject WebP rather than trusting its MIME. Resolve `uploadRoot.resolve(uuid + extension).normalize()` and verify it starts with the normalized upload root before writing.

- [ ] **Step 4: Expose authenticated upload and controlled read endpoints**

Remove upload inheritance from `BaseController`. Require authentication for POST `/upload/image`. To preserve existing pages while routing every write through one implementation, map the same controller method to the explicit legacy aliases `/user/upload`, `/registered_user/upload`, `/skin_detection/upload`, `/hospital_information/upload`, `/forum/upload`, `/comment/upload`, `/online_consultation/upload`, and `/knowledge_popularization/upload`. Serve only UUID-shaped filenames from `/upload/files/{fileName}` and return 404 for other names. Set `X-Content-Type-Options: nosniff` and the validated image content type.

- [ ] **Step 5: Run secure-upload tests**

Run: `mvn -f project/pom.xml -Dtest=ImageStorageServiceTest,UploadControllerTest test`

Expected: PASS.

- [ ] **Step 6: Commit image upload security**

```bash
git add project/src/main/java/com/project/demo/storage project/src/main/java/com/project/demo/controller/UploadController.java project/src/main/java/com/project/demo/config/WebAppConfig.java project/src/main/resources/application.yml project/src/test/java/com/project/demo/storage project/src/test/java/com/project/demo/controller/UploadControllerTest.java
git commit -m "feat: add validated image storage boundary"
```

### Task 8: Move EasyDL credentials and inference behind the backend

**Files:**
- Create: `project/src/main/java/com/project/demo/easydl/EasyDlTokenService.java`
- Create: `project/src/main/java/com/project/demo/easydl/EasyDlClient.java`
- Create: `project/src/main/java/com/project/demo/easydl/DiagnosisResult.java`
- Create: `project/src/main/java/com/project/demo/controller/SkinDiagnosisController.java`
- Modify: `client_home/src/pagesC/skin_detection/edit.vue`
- Test: `project/src/test/java/com/project/demo/easydl/EasyDlTokenServiceTest.java`
- Test: `project/src/test/java/com/project/demo/easydl/EasyDlClientTest.java`
- Test: `project/src/test/java/com/project/demo/controller/SkinDiagnosisControllerTest.java`

**Interfaces:**
- Produces: `String EasyDlTokenService.getToken()`, `DiagnosisResult EasyDlClient.diagnose(byte[] imageBytes)`, and authenticated POST `/api/ai/skin-diagnosis` accepting multipart field `file`.
- Consumes: `AppSecurityProperties.EasyDl`, Redis, `ImageStorageService` validation rules, and the authenticated request context.

- [ ] **Step 1: Write failing cache and response-normalization tests**

```java
@Test
void reusesCachedAccessToken() {
    when(redis.opsForValue().get("easydl:access-token")).thenReturn("cached-token");
    assertEquals("cached-token", tokenService.getToken());
    verifyNoInteractions(restTemplate);
}

@Test
void controllerReturnsOnlyNameAndScore() throws Exception {
    when(client.diagnose(any())).thenReturn(new DiagnosisResult("eczema", 0.91d));
    byte[] png = Base64.getDecoder().decode(
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAusB9Y9ZlWsAAAAASUVORK5CYII=");
    MockMultipartFile image = new MockMultipartFile("file", "skin.png", "image/png", png);
    mockMvc.perform(multipart("/ai/skin-diagnosis").file(image).header("X-Auth-Token", "valid"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result.name").value("eczema"))
        .andExpect(jsonPath("$.result.score").value(0.91))
        .andExpect(jsonPath("$.result.access_token").doesNotExist());
}
```

- [ ] **Step 2: Run tests and verify the backend adapter is missing**

Run: `mvn -f project/pom.xml -Dtest=EasyDlTokenServiceTest,EasyDlClientTest,SkinDiagnosisControllerTest test`

Expected: FAIL because the adapter and controller do not exist.

- [ ] **Step 3: Implement token caching and model calls**

Use `RestTemplate` with connect/read timeouts. Send credentials only to the configured Baidu token endpoint, cache the returned token for `expires_in - 300` seconds, and retry the model call once only when Baidu reports token expiration. Map upstream timeouts/unavailability to a sanitized 502 response. Never log request images, credentials, tokens, or complete upstream bodies.

- [ ] **Step 4: Replace the frontend direct-call flow**

Remove `API_KEY`, `SECRET_KEY`, `EASYDL_URL`, `accessToken`, `tokenExpireTime`, `getAccessToken()`, `imageToBase64()`, and `handleApiError()`. Upload the selected temporary image to `~/api/ai/skin-diagnosis` using `uni.uploadFile`; rely on the shared HTTP layer to attach `X-Auth-Token`. Preserve loading, error, result display, and form autofill behavior.

- [ ] **Step 5: Run EasyDL tests and frontend secret scan**

Run: `mvn -f project/pom.xml -Dtest=EasyDlTokenServiceTest,EasyDlClientTest,SkinDiagnosisControllerTest test`

Then run:

```powershell
rg -n '(oauth/2\.0/token|client_secret|SECRET_KEY|API_KEY|accessToken)' client_home/src
```

Expected: backend tests PASS; frontend scan has no EasyDL credential/token logic.

- [ ] **Step 6: Commit backend inference proxy**

```bash
git add project/src/main/java/com/project/demo/easydl project/src/main/java/com/project/demo/controller/SkinDiagnosisController.java client_home/src/pagesC/skin_detection/edit.vue project/src/test/java/com/project/demo/easydl project/src/test/java/com/project/demo/controller/SkinDiagnosisControllerTest.java
git commit -m "feat: proxy EasyDL inference through backend"
```

### Task 9: Add portfolio documentation and repository security automation

**Files:**
- Replace: `README.md`
- Create: `SECURITY.md`
- Create: `docs/architecture.md`
- Create: `.github/dependabot.yml`
- Create: `.github/workflows/security-check.yml`
- Modify: `.env.example`

**Interfaces:**
- Consumes: final commands, variables, endpoints, and test names from Tasks 1-8.
- Produces: interview-ready setup/security documentation and CI checks for builds plus secret-pattern scans.

- [ ] **Step 1: Write README with exact local setup**

Document JDK 8, Maven, Node, MySQL, Redis, schema import, environment variables, backend/frontend commands, fake-data policy, and medical disclaimer. Include a module tree and core-code links for authentication, query safety, upload, and EasyDL proxy.

- [ ] **Step 2: Add architecture and security documentation**

Document the request flows, access levels, Token TTL, reset-code TTL/rate limits, file constraints, and responsible disclosure. State that real EasyDL credentials are never included and that model integration is disabled when the required environment variables are absent.

- [ ] **Step 3: Add CI and dependency update configuration**

Configure Dependabot for Maven and npm. Configure CI to run Maven tests, frontend build, and a repository scan that fails on private-key blocks and known secret assignment patterns while excluding lockfile integrity hashes and test literals explicitly named `FAKE_*`.

- [ ] **Step 4: Validate documentation commands locally**

Run each command exactly as documented. Correct the docs if a path, environment variable, or script name differs from the actual project.

- [ ] **Step 5: Commit portfolio documentation**

```bash
git add README.md SECURITY.md docs/architecture.md .github .env.example
git commit -m "docs: explain secure architecture and local setup"
```

### Task 10: Perform complete verification and prove original-folder integrity

**Files:**
- Create: `docs/security-verification.md`
- Read only: `../skin-disease-original-source.sha256`

**Interfaces:**
- Consumes: all implementation tasks and the original manifest from Task 1.
- Produces: an evidence-backed verification report and a locally clean Git repository ready for user review.

- [ ] **Step 1: Run the complete backend test suite and package**

Run:

```bash
mvn -f project/pom.xml clean test
mvn -f project/pom.xml -DskipTests package
```

Expected: both commands exit 0; the report records the exact number of tests.

- [ ] **Step 2: Install frontend dependencies reproducibly and build**

Run `npm ci` and `npm run build` from `client_home`. If the legacy lockfile requires the matching Node LTS version, record the version used in README rather than altering dependencies ad hoc.

Expected: both commands exit 0.

- [ ] **Step 3: Scan the final working tree and Git history**

Search for credential-shaped literals, private-key blocks, personal phone/email patterns, raw SQL substitution, public password fields, and tracked generated directories. Inspect `git log -p --all` because a clean current tree alone is insufficient.

Expected: no real secrets/data; no executable raw SQL; no `node_modules`, `target`, `dist`, `.idea`, `.env`, upload, or log files tracked.

- [ ] **Step 4: Recompute and compare the original source hash manifest**

Run the same read-only manifest command from Task 1 into a temporary second file and compare it byte-for-byte with `../skin-disease-original-source.sha256`.

Expected: no difference, proving the original maintained source was not modified.

- [ ] **Step 5: Write the verification report**

Record the date, Git commit, backend test count, frontend build result, scans performed, original-hash comparison, known limitations, and the fact that real EasyDL integration requires user-supplied environment variables and was tested with mocked HTTP responses.

- [ ] **Step 6: Commit verification evidence**

```bash
git add docs/security-verification.md
git commit -m "test: document security verification results"
```

- [ ] **Step 7: Stop before remote operations**

Show the user the safe-public folder, Git log, verification report, and remaining limitations. Request separate explicit authorization before any GitHub rename, deletion, visibility change, or push.
