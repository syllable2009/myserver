不同的配置环境；格式为application-{profile}.properties/yml，其中{profile}对应你的环境标识，比如：
application-test.properties：测试环境
application-dev.properties：开发环境
application-prod.properties：生产环境
只需要我们在application.yml中加：
 spring:
  profiles:
    active: dev