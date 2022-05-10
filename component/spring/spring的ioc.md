#加载Spring配置文件
通过XmlBeanFactory+配置文件来创建IOC容器
   //加载Spring的资源文件
        Resource resource = new ClassPathResource("applicationContext.xml");
   //创建IOC容器对象【IOC容器=工厂类+applicationContext.xml】
        BeanFactory beanFactory = new XmlBeanFactory(resource);
直接通过ClassPathXmlApplicationContext对象来获取
        // 得到IOC容器对象
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println(ac);  
通过配置类
@Component @Service ...
@Configuration
@ComponentScan(basePackages = "com.fgy")
@Import({JdbcConfig.class})
public class SpringConfiguration {
}
　　注意：新的问题产生了，由于没有配置文件了，如何获取容器呢？
ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        
#在Spring中总体来看可以通过三种方式来配置对象:
1.使用XML文件配置
 <bean id="user" class="User"/> 
使用注解来配置
1）先引入context名称空间
xmlns:context="http://www.springframework.org/schema/context"
2）开启注解扫描器
<context:component-scan base-package=""></context:component-scan>
2.第二种方法:也可以通过自定义扫描类以@CompoentScan修饰来扫描IOC容器的bean对象
@Component @Service ...
3.使用JavaConfig来配置  
由于Spring的自动装配并不能将第三方库组件装配到应用中，于是需要显式装配配置。显示装配有两种方式
通过java代码装配bean
编写配置类：
@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        System.out.println("我是在configuration中的"+userDao);
        return userDao;
    }
}
使用配置类创建bean:
使用@Bean来修饰方法，该方法返回一个对象。
不管方法体内的对象是怎么创建的，Spring可以获取得到对象就行了。
Spring内部会将该对象加入到Spring容器中，方法名默认为容器中bean的ID
#三种方式混合使用
如果JavaConfig的配置类是分散的，我们一般再创建一个更高级的配置类（root），然后使用@Import来将配置类进行组合
如果XML的配置文件是分散的，我们也是创建一个更高级的配置文件（root），然后使用来将配置文件组合
在JavaConfig引用XML
使用@ImportResource()
在XML引用JavaConfig
使用<bean>节点就行了

#ioc的演化史
#Spring提供了好几种的方式来给属性赋值
1) 通过构造函数
    <!--创建userService对象-->
    <bean id="userService" class="UserService">
        <!--要想在userService层中能够引用到userDao，就必须先创建userDao对象-->
        <constructor-arg index="0" name="userDao" type="UserDao" ref="userDao"></constructor-arg>
    </bean>
2) 通过set方法给属性注入值
    <bean id="userService" class="UserService">
        <property name="userDao" ref="userDao"/>
    </bean>
3) p名称空间
  <!--不用写property节点了，直接使用p名称空间-->
  <bean id="userService" class="UserService" p:userDao-ref="userDao"/>
4)自动装配(了解)
Spring还提供了自动装配的功能，能够非常简化我们的配置
自动装载默认是不打开的，自动装配常用的可分为两种：
根据名字来装配
  <bean id="userDao" class="UserDao"/>

    <!--
        1.通过名字来自动装配
        2.发现userService中有个叫userDao的属性
        3.看看IOC容器中没有叫userDao的对象
        4.如果有，就装配进去
    -->
    <bean id="userService" class="UserService" autowire="byName"/>
根据类型类装配
    <bean id="userService" class="UserService" autowire="byType"/>
5) 注解
    @Autowired  


             