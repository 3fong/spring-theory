## spring web


### servlet

- web.xml
web的基础是servlet,而标准web项目中需要web.xml来定义servlet信息,实际使用注解@WebServlet也可以实现该功能

项目启动需要依赖tomcat,注意端口冲突问题

- shard libraries/runtimes pluggability

实际是spi的一个应用.

1 servlet容器启动会扫描当前应用里的每一个jar包
    ServletContainerInitializer实现
2 提供ServletContainerInitializer的实现类
    1) 必须绑定在 META-INF/services/javax.servlet.ServletContainerInitializer
    2) 文件内容是ServletContainerInitializer实现类的全类名

应用启动时,会运行onStartup方法:

1) 它会通过@HandlesTypes(value = {MyService.class})注入所有MyService的子类Set<Class<?>>;    
2) ServletContext: 当前web应用的上下文,一个web应用一个ServletContext
   1) web上下文中可注入拦截器,过滤器和servlet,实现和web.xml相同的配置效果
   2) 使用编码的方式,在项目启动是给ServletContext添加组件
      1) 必须在项目启动时添加,因为是上下文操作,需要在服务实际对外服务前完成初始化
      2) ServletContainerInitializer的ServletContext
      3) ServletContextListener 的实现类配置
      4) javax.servlet.Filter的实现类配置

tip: web项目注意项目的目录结构.META-INF要在项目代码源中才会生效

实际spring mvc也是基于这种方式实现.具体的实现方式如下:

### spring mvc

原理:

1) web容器启动时,扫描每个jar下的META-INF/services/javax.servlet.ServletContainerInitializer.这里是在spring-web包里实现.
2) 加载这个文件里指定的类: org.springframework.web.SpringServletContainerInitializer
3) spring的应用启动时会加载感兴趣的 WebApplicationInitializer 接口下的所有组件
4) 并且为 WebApplicationInitializer 组件创建对象(组件不是接口,不是抽象类)
   1) AbstractContextLoaderInitializer: 创建根容器 createRootApplicationContext()[abstract]
   2) AbstractDispatcherServletInitializer: 
      1) 创建一个web的ioc容器: createServletApplicationContext()[abstract]
      2) 创建DispatcherServlet: createDispatcherServlet(servletAppContext)
      3) 将创建的DispatcherServlet添加到ServletContext中
      4) 设置映射路径 this.getServletMappings()[abstract]
      5) 添加过滤器Filter[] filters = this.getServletFilters()
   3) AbstractAnnotationConfigDispatcherServletInitializer: 注解方式配置的分发器初始化
      1) 创建根容器: createRootApplicationContext()
         1) 获取配置类: getRootConfigClasses()[abstract]
      2) 创建web的ioc容器: createServletApplicationContext()
         1) 获取配置类:this.getServletConfigClasses();[abstract]
   4) AbstractReactiveWebInitializer: 另一种web启动方式

总结: 

    以注解方式来启动springmvc:
    
    1) 需要实现:AbstractAnnotationConfigDispatcherServletInitializer
    2) 实现抽象方法指定 DispatcherServlet 的配置信息


[web.xml配置方式](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)














