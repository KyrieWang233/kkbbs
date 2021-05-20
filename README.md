## 基于SpringBoot的论坛网站

# 技术栈
|  技术   |  链接   |
| --- | --- |
|  Spring Boot   |  http://projects.spring.io/spring-boot/#quick-start   |
|   MyBatis  |  https://mybatis.org/mybatis-3/zh/index.html   |
|   MyBatis Generator  |  http://mybatis.org/generator/   |
|   H2  |   http://www.h2database.com/html/main.html  |
|   Flyway  |   https://flywaydb.org/getstarted/firststeps/maven  |
|Lombok| https://www.projectlombok.org |
|Bootstrap|https://v3.bootcss.com/getting-started/|
|Github OAuth|https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/|
|QCloud-cos|https://cloud.tencent.com/document/product/436/10199|
|Bootstrap|https://v3.bootcss.com/getting-started/|


运行 migrate 和 generator 的命令
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```

需要自己添加的配置
```properties
github.client.id=
github.client.secret=
github.redirect.url=
qcloud.secretId=
qcloud.secretKey=
```