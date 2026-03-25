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
