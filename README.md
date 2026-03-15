# micro-Spring（迷你版 Spring 框架）

这是一个紧凑的、用于学习与演示的类 Spring 框架实现。仓库包含一个小型的 IoC 容器、基于注解的配置、轻量级 Web MVC 层、属性解析器、简单的 JDBC 工具、AOP 支持和若干工具类。

本 README 将概述核心实现、重要类及其功能、如何运行示例 `hello-webapp`、一段可直接用于简历的描述，以及针对该项目的详尽面试问答，帮助你梳理实现细节并准备面试。

---

## 项目结构（高层）

- `framework/` - 框架核心模块（micro-aop、micro-context、micro-jdbc、micro-web、micro-boot、micro-build、micro-parent）
- `micro/` - 示例、工具和更小的子模块（bean-definitions、bean-instances、bean-post-processor、hello-webapp、hello-boot、init-beans、jdbc-template、property-resolver、proxy-resolver、resource-resolver、web-app-context 等）

重要模块职责：
- `micro-context` / `micro-parent` - 核心 DI/IoC 与配置管理
- `micro-web` / `web-app-context` - 轻量级 MVC 与 Servlet 集成
- `micro-aop` / `proxy-resolver` - 方法级拦截/代理支持
- `property-resolver` - 属性加载与表达式解析
- `jdbc-template` / `micro-jdbc` - 简易 JDBC 帮助函数与连接池集成
- `hello-webapp` - 示例 Web 应用（打包为 WAR）

---

## 核心概念与实现要点

下面列出项目的主要功能以及对应的重要类，侧重实现思路和代码定位，便于快速理解与扩展。

### 1）属性解析（Property resolution）

- 关键类: `org.xiaoyu.micro.io.PropertyResolver` (见 `micro/property-resolver` 和其在 `web-app-context` 和 `framework` 模块中的使用)。

功能描述:
- 从 classpath 上的 `/application.yml` 或 `/application.properties` 加载属性，并与环境变量合并。
- 支持 `${key:default}` 形式的表达式。解析器将解析并返回配置的值或提供的默认值。也支持通过 `getProperty(String key, Class<T> type)` 转换为类型化值，并内置转换器用于原语和 `java.time` 类。

重要性:
- 中央配置 API，由 `ContextLoaderListener`、`DispatcherServlet` 和其他组件使用，以适应框架行为（例如 `summer.web.static-path`、`summer.web.favicon-path`）。

### 2) 轻量级 ApplicationContext & IoC 容器

- 关键类: `org.xiaoyu.micro.context.AnnotationConfigApplicationContext`、`org.xiaoyu.micro.context.ApplicationContext`、`org.xiaoyu.micro.context.ConfigurableApplicationContext` (在 `framework/micro-context` 和 `micro/` 下的模块中查找测试和辅助实现)。

功能描述:
- 扫描配置类和 bean 定义。
- 支持按类型/名称注册和检索 bean。
- 提供生命周期钩子和上下文工具（`ApplicationContextUtils`），以从 Web 工具访问运行中的上下文。

实现要点:
- 使用注解扫描查找 `@Controller`、`@RestController`、`@Configuration`、`@Bean` 风格的定义（项目包含这些注解的小型版本，在 `org.xiaoyu.micro.annotation` 下）。
- Bean 定义被存储和管理；bean 被创建（并在应用 AOP 功能时代理）并根据需要注入。

### 3) Web MVC & Servlet 集成

- 关键类: `org.xiaoyu.micro.web.DispatcherServlet`、`org.xiaoyu.micro.web.utils.WebUtils`、`org.xiaoyu.micro.web.ContextLoaderListener`。

功能描述:
- `ContextLoaderListener` 是一个 `ServletContextListener`，在 Web 应用启动时初始化 `ApplicationContext`，从属性设置字符编码，并注册 `DispatcherServlet`。
- `DispatcherServlet` 是中央 servlet，它：
  - 处理传入的 HTTP 请求。
  - 将请求路由到带有 `@Controller` 或 `@RestController` 注解的控制器，以及带有 `@GetMapping` / `@PostMapping` 注解的方法。
  - 使用配置的属性处理静态资源和 favicon 路径（`summer.web.static-path`、`summer.web.favicon-path`）。
  - 支持 MVC `ModelAndView` 和 REST 响应（带有 `@ResponseBody`）。
- `WebUtils.createPropertyResolver()` 尝试首先加载 `application.yml`（通过 `YamlUtils.loadYamlAsPlainMap`），然后回退到 `application.properties`。

重要行为:
- 静态文件处理: `DispatcherServlet` 检查请求 URI 是否与 `resourcePath`（例如 `/static/`）匹配，并通过 `ServletContext.getResourceAsStream(url)` 服务资源（因此文件必须存在于 WAR 中的该路径下）。
- Servlet 通过编译映射模式来映射控制器（见 `Dispatcher` 中使用的 `PathUtils.compile()`）。

### 4) 请求映射和参数绑定

- 在 `DispatcherServlet` 的嵌套 `Dispatcher` 和 `Param` 类中:
  - 请求方法与从映射字符串编译的基于正则表达式的模式匹配。
  - 参数解析器支持 `@PathVariable`、`@RequestParam`、`@RequestBody`，以及 servlet 特定的注入（`HttpServletRequest`、`HttpServletResponse`、`HttpSession`、`ServletContext`）。
  - 原语和包装器的简单类型转换内联发生（String->int、boolean 等），以及使用 `JsonUtils` 的请求体的 JSON 解析。

### 5) 视图解析

- `ViewResolver`（接口）和实现类（在模块中搜索实际实现）提供 MVC 的视图渲染；典型行为是渲染模板（FreeMarker 作为 `micro-web` 模块中的依赖存在）或处理重定向。

### 6) AOP 和代理（方法拦截）

- 关键模块/类: `micro-aop`、`proxy-resolver`，跨模块使用。
- 实现思路:
  - 提供注册方法拦截器和为需要 AOP 的 bean 创建代理的方式（例如，事务处理、日志记录或其他横切关注点）。
  - 使用 JDK 动态代理或字节码库创建代理（仓库在 pom 属性中包含 bytebuddy 以供参考）。

### 7) JDBC 工具

- 模块: `micro-jdbc`、`jdbc-template` 实现小型 JDBC 帮助层:
  - 提供 `JdbcTemplate` 风格的帮助器来运行查询、更新语句和映射结果。
  - 集成 HikariCP 数据源依赖（通过属性解析器配置）用于连接池。

## 重要类的位置

- `org.xiaoyu.micro.io.PropertyResolver` — `micro/property-resolver/src/main/java/org/xiaoyu/micro/io/PropertyResolver.java`
- `org.xiaoyu.micro.web.DispatcherServlet` — `framework/micro-web/src/main/java/org/xiaoyu/micro/web/DispatcherServlet.java`
- `org.xiaoyu.micro.web.utils.WebUtils` — `framework/micro-web/src/main/java/org/xiaoyu/micro/web/utils/WebUtils.java`
- `org.xiaoyu.micro.web.ContextLoaderListener` — `micro/web-app-context/src/main/java/org/xiaoyu/micro/web/ContextLoaderListener.java`
- `AnnotationConfigApplicationContext` 及相关上下文类 — 在 `framework/micro-context` 和 `micro/*` 模块中搜索 `org.xiaoyu.micro.context` 包。

（使用你的 IDE 打开这些文件并跟随小型代码库；它是故意紧凑且可读的。）

---

## 如何运行示例 webapp (`hello-webapp`)

1. 构建框架模块（从项目根目录或显式模块）:

```powershell
cd D:\xiaoyu\micro-Spring\framework\micro-build
mvn -DskipTests "-Dgpg.skip=true" clean install
```

2. 构建 `hello-webapp` 并生成 war:

```powershell
cd D:\xiaoyu\micro-Spring\micro\hello-webapp
mvn -U clean package
```

3. 部署到 Tomcat 10.x（Tomcat 10 使用 Jakarta Servlet API 并匹配项目 servlet 依赖）:
- 将 `target/hello-webapp.war` 复制到 Tomcat 的 `webapps` 目录，并（可选）重命名为 `ROOT.war` 以在 `/` 上服务。
- 启动 Tomcat 并访问 `http://localhost:8080/`（或你在 `conf/server.xml` 中配置的 Tomcat 端口）。

故障排除提示:
- 如果由于缺少本地工件而打包失败，请确保步骤 (1) 成功运行并将 `org.xiaoyu:micro-*` 工件安装到你的本地 Maven 仓库。
- 如果静态资源的文件路径错误，请检查 classpath 上的 `application.yml` 或 `application.properties` 中的键 `summer.web.static-path` 和 `summer.web.favicon-path`。

---

## 示例 `application.yml`（推荐）

```yaml
summer:
  web:
    static-path: /static/
    favicon-path: /favicon.ico
    character-encoding: UTF-8
  datasource:
    jdbc-url: jdbc:sqlite:test.db
    username:
    password:
```

如果需要覆盖默认值，请将其放在 webapp / demo 模块的 `src/main/resources` 下。

---

## 简历 / CV 段落和要点（复制粘贴）

简历摘要（短段落）:

> 实现了一个轻量级的类 Spring Java 框架（"micro-Spring"），具有注解驱动的 ApplicationContext、属性解析、最小化的 Web MVC 分发 servlet、AOP 能力的 bean 代理，以及带有连接池支持的小型 JDBC 模板。设计并实现了核心模块，支持快速迭代并打包为 WAR 文件以用于 Jakarta Servlet 容器（Tomcat 10）。专注于干净的 API 设计、可测试性和模块化结构，以支持学习和小生产概念验证。

关键要点（用于要点列表）:

- 设计并实现了一个紧凑的 IoC 容器，具有基于注解的配置和 bean 生命周期管理。
- 构建了一个轻量级的 `DispatcherServlet`，支持 `@Controller` / `@RestController`、`@GetMapping` / `@PostMapping`、请求参数绑定和 JSON 体解析。
- 实现了 `PropertyResolver`，支持 `${key:default}` 表达式和常见 Java 类型的类型化转换。
- 实现了带有 HikariCP 集成和 DB 操作测试的小型 `JdbcTemplate` 帮助器。
- 添加了 AOP/代理支持，以将拦截器应用于 bean 并启用横切关注点。
- 打包并部署演示 `hello-webapp` 到 Tomcat 10；验证了静态资源解析和 MVC 路由。

---

## 面试问题（详细） — 可能/预期的问题和建议答案

本节很长，旨在为你准备关于设计、实现细节、权衡和如何扩展项目的问题。

1) 高层: 这个项目实现了什么，为什么？
- 答案: 这是一个最小的类 Spring 框架，演示核心功能: IoC 容器（注解驱动）、属性解析、基本 Web MVC、AOP 代理和 JDBC 帮助器。目标是在更简单的、教育性的代码库中学习和演示依赖注入、请求路由和横切关注点的概念。

2) Bean 发现和注册如何工作？
- 答案: `AnnotationConfigApplicationContext` 扫描配置类和包（或处理显式配置类型）。它为发现的类型（带有 `@Configuration`、`@Controller` 或 `@Component` 等效注解）创建 `BeanDefinition` 样的结构。在上下文刷新期间，它实例化 bean（尊重单例范围），注入依赖（按类型或名称），并根据需要应用后处理器和 AOP 代理。

3) 配置属性如何加载和解析？
- 答案: `WebUtils.createPropertyResolver()` 尝试首先加载 `/application.yml`（转换为平面映射），然后 `/application.properties`。`PropertyResolver` 将这些值与环境变量合并，支持 `${key:default}` 表达式，并提供类型化转换函数。当键缺失时返回默认值。

4) 解释 `DispatcherServlet` 中的请求路由。
- 答案: 在初始化时，servlet 发现控制器 bean 并检查带有 `@GetMapping`/`@PostMapping` 注解的方法。映射被编译为正则表达式模式（通过 `PathUtils.compile`）。对于每个传入请求，servlet 检查 URL 是否匹配静态资源路径或动态路由。如果它匹配分发映射，`Dispatcher` 将绑定参数（路径变量、请求参数、体）并调用处理程序方法。根据返回类型适当地处理返回类型作为 REST（JSON 或响应体）或 MVC（`ModelAndView`/视图）。

5) 静态资源如何服务？
- 答案: 属性 `summer.web.static-path` 定义 URL 前缀（默认 `/static/`）。URI 以此前缀开头的请求通过调用 `ServletContext.getResourceAsStream(url)` 服务，因此静态文件必须存在于 WAR 中的该路径下（例如 `src/main/webapp/static/`）。

6) 参数绑定如何工作？支持哪些类型？
- 答案: `Param` 解析器识别 `@PathVariable`、`@RequestParam` 和 `@RequestBody`。对于 `@RequestParam`，可以提供默认值，缺失参数可能抛出异常。对于 `@RequestBody`，JSON 被解析为请求的类型。对于路径变量和请求参数，实现到原语类型和包装器（int、long、boolean 等）的简单转换。

7) AOP 如何实现，支持哪些选项？
- 答案: 项目提供代理基础设施以用拦截器包装 bean。代理可以使用 JDK 动态代理或字节码库（ByteBuddy）创建，基于项目的 pom。拦截器可以用于事务、日志记录或重试机制。对于方法匹配，可以使用注解或切点表达式（简单） — 核心思想是围绕目标方法调用的拦截器链。

8) 你将如何实现构造函数 vs setter 注入的依赖注入？
- 答案: 对于构造函数注入，检测带有 `@Inject` 或具有最多可解析参数的构造函数，并通过传递解析的依赖来创建实例。对于 setter 注入，扫描带有 `@Autowired`（或类似）注解的方法，在实例化后调用它们并传递解析的 bean。上下文需要解析循环依赖: 基于构造函数的循环引用更难，通常需要代理或重构；基于 setter 的可以通过首先创建 bean 实例然后填充依赖来解析。

9) 你如何处理 bean 后处理 / 生命周期回调？
- 答案: 框架可以提供 `BeanPostProcessor` 接口或注解驱动的生命周期方法（例如 `@PostConstruct`）。在 bean 创建期间，在实例化和依赖注入之后，上下文迭代注册的 `BeanPostProcessor` 以应用修改，并最终调用生命周期回调。

10) 如何添加对 `@Transactional` 的支持？
- 答案: 实现一个在方法调用前启动事务并根据异常提交或回滚的拦截器。使用 AOP 将拦截器应用于带有 `@Transactional` 注解的 bean 或方法。拦截器需要访问 `PlatformTransactionManager` 抽象，它可以从配置的数据源（例如 HikariCP）征用连接。

11) 你如何测试这个框架？存在哪些测试？
- 答案: 小组件的单元测试（例如 `PropertyResolverTest`、`DispatcherServletTest`）验证解析、映射和绑定行为（见 `framework/*/src/test/java`）。集成测试可以通过打包演示 webapp 并在嵌入式容器或模拟的 ServletContext 中运行它们来完成。

12) 如何扩展路由以支持模板引擎或静态文件缓存？
- 答案: 添加带有缓存头和最后修改处理的 `ResourceHandler`，或集成静态资源缓存。对于模板，`ViewResolver` 实现可以使用 FreeMarker 或其他引擎 — 项目已经在 `micro-web` 中包含 FreeMarker 依赖，`ViewResolver` 可以委托渲染。

13) 这个方法 vs Spring Framework 的主要权衡是什么？
- 答案: 这个迷你框架故意简单，省略了复杂功能，如高级 DI 范围、丰富生命周期、健壮的 AOP 切点语言、注解处理器、条件 bean、自动配置、高级事务管理、健壮的属性源顺序和第三方集成。它更容易推理、更小，并对学习有用，但对企业生产使用而言功能不完整。

14) 安全考虑和线程模型？
- 答案: 标准 Servlet 线程模型适用: 单个 servlet 实例在多个线程上处理并发请求。共享的可变状态必须是线程安全的。对于基于文件的静态资源，读取没问题，但任何缓存都应该是线程安全的。对于 DB 连接，使用池化的 DataSource（HikariCP 已包含），不要跨线程共享 `Connection` 实例。

15) 如何调试不匹配的控制器映射？
- 答案: 启用框架日志记录（SLF4J + Logback），检查 `DispatcherServlet` 初始化日志以查看映射的模式，并检查 `PathUtils.compile()` 结果。也验证请求 URI 和上下文路径 — ServletRequest.getRequestURI() 包括上下文路径；如果必要，确保你的匹配逻辑考虑上下文路径。

---

## 扩展想法和下一步（可选）

- 添加属性源优先级: 系统属性、环境、application.properties/yaml、命令行参数。
- 添加配置文件支持（dev/prod）和条件 bean。
- 添加更丰富的类型转换和 `Converter` 注册机制。
- 添加对 `@Autowired`/构造函数注入和通过代理的循环依赖解析的支持。
- 添加集成的嵌入式服务器运行器，用于更快的本地测试（例如嵌入式 Tomcat 运行器模块）。

---

## 最终说明

这个 README 旨在既作为快速参考又是面试准备指南。如果你愿意，我可以:

- 生成一个更短的 `README-compact.md`，仅包含快速启动步骤。
- 添加一个 `docs/` 文件夹，详细描述每个模块，或为关键包生成 Javadoc。
- 创建一个 `build-all.ps1` 脚本，以可靠地构建所有模块并打包演示 webapp。

告诉我你接下来想要上述哪个，我会直接添加到仓库中。
