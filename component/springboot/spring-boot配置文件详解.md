Spring Boot优先于配置的惯例,不需要做太多的配置就能够让spring boot正常运行，在一些特殊情况下需要有自己的配置属性时：
1.在application.yml自定义一组属性:
my:
 name: syllable
读取配置文件的值只需要加@Value("${属性名:}")：冒号后面为默认值
  @Value("${my.name:default}")
  private String name; 
2.当我们有很多配置属性的时候，这时我们会把这些属性作为字段来创建一个javabean，并将属性值赋予给他们,比如  
my:
 name: syllable
 age: 12
 number:  ${random.int}
 uuid : ${random.uuid}
 max: ${random.int(10)}
 value: ${random.value}
 greeting: hi,i'm  ${my.name}
创建一个javabean如下： 
@PropertySource(value = "classpath:test.properties") // 当配置在自定义配置文件，比如test.properties时才需要此配置
@ConfigurationProperties(prefix = "my")
@Component
public class ConfigBean {

    private String name;
    private int age;
    private int number;
    private String uuid;
    private int max;
    private String value;
    private String greeting;
    
    省略了getter setter....
选填,作用是：     
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-configuration-processor</artifactId>
			<optional>true</optional>
</dependency>
另外需要在应用类或者application类，加EnableConfigurationProperties注解。
@EnableConfigurationProperties({ConfigBean.class,ConfigBean.class}) 
 