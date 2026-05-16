# AI 晚餐决策搭子后端

Spring Boot 3 + Java 17 MVP。

## 运行

```bash
mvn -s maven-settings.xml spring-boot:run
```

默认端口：`8080`。

未配置 `LLM_API_KEY` 时，推荐接口会自动使用本地 fallback。

## 数据库

MySQL 建表脚本在 `database/schema.sql`。当前 MVP 默认使用内存仓储，方便本地先跑通主流程。
