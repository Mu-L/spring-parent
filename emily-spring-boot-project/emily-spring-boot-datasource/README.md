### 一、pom依赖

```xml
            <dependency>
                <groupId>io.github.mingyang66</groupId>
                <artifactId>emily-spring-boot-datasource</artifactId>
                <version>${revision}</version>
            </dependency>
```



### 二、动态数据库多数据源组件

##### 特性：

- 支持指定默认数据源，如未标记使用哪个数据源则使用默认数据源；
- 支持动态切换不同的数据源；
- 支持扩展点能力，提供DataSourceCustomizer接口，AOP会根据拦截器的优先级判定使用优先级最高的拦截器；
- 提供@TargetDataSource注解切换不同的数据源；
- 可以通过配置更改默认数据源的标识；
- 支持从类、方法上读取切换不同数据源注解，方法上的注解标识优先级最高；
- 支持从父类、父接口及其方法上继承注解；
- druid数据库连接池支持的所有属性配置都支持；
- 支持宽松回退，即如果连接数据源事变，则回退到默认数据源；
- 属性配置示例

```properties
#https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8
#是否开启数据源组件, 默认：true
spring.emily.datasource.enabled=true
#是否拦截超类或者接口中的方法，默认：true
spring.emily.datasource.check-inherited=true
#是否对默认数据源执行宽松回退，即：当目标数据源找不到时回退到默认数据源，默认：true
spring.emily.datasource.lenient-fallback=false
#默认数据源配置，默认：default
spring.emily.datasource.default-config=mysql
#这一项可配可不配，如果不配置druid会根据url自动识别dbType，然后选择相应的driverClassName
spring.emily.datasource.druid.mysql.driver-class-name=com.mysql.cj.jdbc.Driver
#连接数据库的url，不同数据库不一样
spring.emily.datasource.druid.mysql.url=jdbc:mysql://127.0.0.1:3306/ocean_sky?characterEncoding=utf-8&rewriteBatchedStatements=true
#连接数据库的用户名
spring.emily.datasource.druid.mysql.username=root
#用户密码（smallgrain）
spring.emily.datasource.druid.mysql.password=Lj9+VdH9qVvuG2t7/FBnvCtFBiLauk9uFlk2EWiRgG6IUFfbKKFX5Xxw7O0i2u8QPjB3hWYZEwEeYMw/Yq89Zg==
#数据库连接池类型
spring.emily.datasource.druid.mysql.db-type=mysql
#初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时，默认：0
spring.emily.datasource.druid.mysql.initial-size=2
#最小连接池数，默认：0
spring.emily.datasource.druid.mysql.min-idle=2
#最大连接数，默认：8
spring.emily.datasource.druid.mysql.max-active=8
#获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁，默认：-1（推荐内网800ms,外网1200ms,因为tcp建立连接重试一般需要1s）
spring.emily.datasource.druid.mysql.max-wait=-1
#https://www.jianshu.com/p/6d19e0d7f81c?u_atoken=7e99c6ae-44de-439a-b78a-5732a4c9cd06&u_asession=01fyHSi0s-qm5jHD_UGgKuoCNhOS08bp7bqJZqIVP2g3ZiCPVHFF3jtlFrn9JG_A1VX0KNBwm7Lovlpxjd_P_q4JsKWYrT3W_NKPr8w6oU7K9yXw_swadf--Q9ML2NFkDmPn5sJEo90JdruCukG2OVYmBkFo3NEHBv0PZUm6pbxQU&u_asig=05qqfmDpV5jnzQ3zaOR-kKvioe07FBSVILyPt-3IHUuaOlqsQExHdvNgoRP9QTDOUFenZPaipnG7b3jm2WEafzXTqIHWMBKy1JvrzGFRhX568SsumygbmVSpauvHLbLcdE8Mmje8uMMLMpJWnqrom3_hc1DMS5NVo0g6Dqsxems-79JS7q8ZD7Xtz2Ly-b0kmuyAKRFSVJkkdwVUnyHAIJzZ-rDK6__AYZNGEJQgQGLOKJPEmNEomz-smxywLNcS33LoLNx2oKfcIBo1yjPF0Bne3h9VXwMyh6PgyDIVSG1W9h8S_9QWePREPPuPQcZ7RC8hwo_poDvzpKHaE4ZXLH-w5i_I-I-qXr3g8qEbybPL9z8URz6jucz_9RAhDuYVgimWspDxyAEEo4kbsryBKb9Q&u_aref=qZDOHcRyJj1JFfddfJliGF07TB8%3D
#建立连接时连接超时时间，默认：10000ms
spring.emily.datasource.druid.mysql.connect-timeout=10000
#数据库操作超时时间，默认：10000ms
spring.emily.datasource.druid.mysql.socket-timeout=10000
#设置JDBC驱动执行Statement语句的秒数，如果超过限制，则会抛出SQLTimeoutException，默认：0 单位：秒 无限制
spring.emily.datasource.druid.mysql.query-timeout=0
#设置JDBC驱动执行N个Statement语句的秒数（事务模式），如果超过限制，则会抛出SQLTimeoutException，默认：0 单位：秒 无限制
spring.emily.datasource.druid.mysql.transaction-query-timeout=0
#mysql默认使用ping模式,可以通过设置系统属性System.getProperties().setProperty("druid.mysql.usePingMethod", "false")更改为sql模式
#用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。默认：缺省
# mysql默认：SELECT 1  oracle默认：SELECT 'x' FROM DUAL
spring.emily.datasource.druid.mysql.validation-query=SELECT 1
#单位：秒，检测连接是否有效的超时时间。底层调用jdbc Statement对象的void setQueryTimeout(int seconds)方法，默认：-1
spring.emily.datasource.druid.mysql.validation-query-timeout=-1
#申请连接时执行validationQuery检测连接是否有效，这个配置会降低性能。默认：false (如果test-on-borrow为true,那么test-while-idle无效)
spring.emily.datasource.druid.mysql.test-on-borrow=false
#建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。默认：true
spring.emily.datasource.druid.mysql.test-while-idle=true
#归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。默认：false
spring.emily.datasource.druid.mysql.test-on-return=false
#是否对空闲连接进行保活，默认：false
spring.emily.datasource.druid.mysql.keep-alive=true
#触发心跳的间隔时间（DestroyTask守护线程检测连接的间隔时间），默认：60*1000 一分钟
spring.emily.datasource.druid.mysql.time-between-eviction-runs-millis=60000
#连接保持空闲而不被驱逐的最小时间（保活心跳只会对存活时间超过这个值的连接进行），默认：1000L * 60L * 30L
spring.emily.datasource.druid.mysql.min-evictable-idle-time-millis=1800000
#连接保持空闲最长时间（连接有任何操作，计时器重置，否则被驱逐），默认：1000L * 60L * 60L * 7
spring.emily.datasource.druid.mysql.max-evictable-idle-time-millis=25200000
#保活检查间隔时间，默认：60*1000*2毫秒，要求大于等于2分钟（要大于min-evictable-idle-time-millis）
spring.emily.datasource.druid.mysql.keep-alive-between-time-millis=120000
#https://github.com/alibaba/druid/wiki/%E8%BF%9E%E6%8E%A5%E6%B3%84%E6%BC%8F%E7%9B%91%E6%B5%8B
#连接池泄漏监测，当程序存在缺陷时，申请的连接忘记关闭，这时就存在连接泄漏了，开启后对性能有影响，建议生产关闭，默认：false
spring.emily.datasource.druid.mysql.remove-abandoned=false
#默认：300*1000
spring.emily.datasource.druid.mysql.remove-abandoned-timeout-millis=300000
#回收连接时打印日志，默认：false
spring.emily.datasource.druid.mysql.log-abandoned=true
#物理超时时间，默认：-1
spring.emily.datasource.druid.mysql.phy-timeout-millis=-1
#物理最大连接数，默认：-1（不建议配置）
spring.emily.datasource.druid.mysql.phy-max-use-count=-1
#是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。默认：false
spring.emily.datasource.druid.mysql.pool-prepared-statements=true
#要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100，默认：10
#默认：10，如果不设置此属性大于0，则PS缓存默认关闭
spring.emily.datasource.druid.mysql.max-pool-prepared-statement-per-connection-size=10
#数据库连接失败，是否退出重试，默认：false
spring.emily.datasource.druid.mysql.break-after-acquire-failure=false
#数据库连接失败重试几次之后允许终止或休眠一段时间再重试，默认：1
spring.emily.datasource.druid.mysql.connection-error-retry-attempts=1
#数据库连接失败，重试间隔多久，默认：500ms
spring.emily.datasource.druid.mysql.time-between-connect-error-millis=500
#是否开启StatViewServlet，默认：false
spring.datasource.druid.stat-view-servlet.enabled=true
#用户名
spring.datasource.druid.stat-view-servlet.login-username=admin
#密码
spring.datasource.druid.stat-view-servlet.login-password=admin
#访问URL
spring.datasource.druid.stat-view-servlet.url-pattern=/druid/*
#允许清空统计数据
spring.datasource.druid.stat-view-servlet.reset-enable=true
#拒绝访问的IP地址，多个用逗号分隔
spring.datasource.druid.stat-view-servlet.deny=
#允许访问的IP地址，多个用逗号分隔
spring.datasource.druid.stat-view-servlet.allow=127.0.0.1
#是否开启WebStatFilter，默认：false
spring.datasource.druid.web-stat-filter.enabled=true
#URI监控，排除指定的请求，默认：*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*
spring.datasource.druid.web-stat-filter.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*
#URI监控，指定监控URI路径，默认：/*
spring.datasource.druid.web-stat-filter.url-pattern=/*
#Session监控，是否开启，默认：null
spring.datasource.druid.web-stat-filter.session-stat-enable=true
#Session监控session数量，默认：null
spring.datasource.druid.web-stat-filter.session-stat-max-count=1000
#URI监控，是否开启单个URI调用链路，默认：false
spring.datasource.druid.web-stat-filter.profile-enable=true
#如何使用待探究
spring.datasource.druid.web-stat-filter.principal-cookie-name=admin.user
spring.datasource.druid.web-stat-filter.principal-session-name=admin.user
#Spring监控代码包路径，支持多个包，用逗号隔开
spring.datasource.druid.aop-patterns=com.emily.infrastructure.test.*
#开启指定的过滤器
spring.emily.datasource.druid.mysql.filters=stat,wall,log4j2,config
#druid.stat.mergeSql是否合并sql，默认：false
#druid.stat.slowSqlMillis慢sql查询阀值，默认：3秒
#druid.stat.logSlowSql是否开启慢sql打印，默认：false
#druid.stat.slowSqlLogLevel配置展示sql的日志级别,默认：ERROR
#config.decrypt是否开启密码秘钥解密，默认：false
#config.decrypt.key密码解密公钥
spring.emily.datasource.druid.mysql.connection-properties=\
  druid.stat.mergeSql=true;\
  druid.stat.slowSqlMillis=2;\
  druid.stat.logSlowSql=true;\
  druid.stat.slowSqlLogLevel=error;\
  config.decrypt=true;\
  config.decrypt.key=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKxbGuex8VbSRFn99y8xPkE+3uUzKFgQSl06RwEl+MC1o/Ghy9XxGs5y1jbOFwybi/OtyH52Cm8Ciq2USMzw8DUCAwEAAQ==
#密码解密回调类
spring.emily.datasource.druid.mysql.password-callback-class-name=com.alibaba.druid.util.DruidPasswordCallback
```

### 三、数据库多数据源组件使用案例

```java
@Mapper
public interface MysqlMapper {
    /**
     * 查询接口
     */
    @TargetDataSource("mysql")
    String findLocks(String lockName1);
}
```



#### 四、Mybatis埋点组件

- 扩展点MybatisCustomizer，AOP根据拦截器的优先级判定使用优先级最高的拦截器
- 属性配置：

```properties
#是否开启mybatis拦截组件, 默认：true
spring.emily.mybatis.enabled=true
#是否拦截超类或者接口中的方法，默认：true
spring.emily.mybatis.check-class-inherited=true