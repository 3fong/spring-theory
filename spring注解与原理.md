## spring注解 


### 常用注解

- 注解与xml标签映射关系
@Configuration==<beans>;
@Bean==<bean>;
@ComponentScan(value="package path")==<context:component-scan> 扫描指定路径下的spring bean组件.
@Configuration中@bean使用方法名作为bean在上下文中的名称.可以使用@Bean(name="alias")指定别名


- @Scope:bean作用域
```
prototype: 多实例.ioc容器启动时不会创建对象,只有每次请求时调用创建方法;    
singleton:单实例.ioc容器启动时创建对象,并放入ioc容器中;后续每次获取都从缓存中取    
request: 同一次请求创建一个实例    
session: 同一个session创建一个实例
```

- @Lazy 懒加载:    

单实例bean,默认在容器启动时创建;    
懒加载:容器启动时不创建对象,第一次使用bean时创建对象并初始化;

- 向容器中注册组件方式

1) 包扫描+组件注解(@Controller@Service@Repository@Component)    
2) @Bean[引入第三方包内组件]
3) @Import[快速给容器中导入组件]    
```
	1 @Import:容器会自动注册该组件,id默认是全类名    
	2 ImportSeletor: 返回需要导入的组件的全类名数组    
	3 ImportBeanDefinitionRegister: 手动注册bean到容器    
```
4) 使用spring提供的FactoryBean
```
1 默认获取FactoryBean.getObject()创建的对象    
2 获取FactoryBean,需要id前加&    
	Object bean = application.getBean("colorFactoryBean")
	bean.getgetClass():获取的是FactoryBean中getObject方法定义的具体创建对象的类型
	Object bean = application.getBean("&colorFactoryBean")
	bean.getgetClass():获取的是FactoryBean自身的类型	
```

- 读取配置文件    
```
@PropertySource(value={""})== <context:property-placeholder location=""/>读取外部配置文件中的k/v保存都运行的环境变量中
		配置@Value注解进行取值
context.getEnvironment().getProperty("配置文件key");
```

@Value赋值
```
1 基本数值    
2 写spel: #{}    
3 写${}:取配置文件properties中的值(运行时环境变量里的值)
```

- 自动注入/自动装配

CarService {
	@Autowired/@Resource/@Inject
	@Qualifier("carDaoId")
	CarDao carDao;
}

@Autowired/@Resource/@Inject: 自动装配注解,进行自动装配的标识    
CarDao:自动装配类型    
carDao: 自动装配对象属性名称    
@Qualifier: 多个可装配类时,指定生效的装配类    
carDaoId: 自动装配对象组件id

@Autowired:spring原生注解
```
1) 默认优先按照类型去容器中找对应组件:ApplicationContext.getBean(CarDao.class)
2) 如果找到多个相同类型的组件,再按属性名称作为组件id去容器中查找:ApplicationContext.getBean("carDao")
3) @Qualifier("carDao"):执行装配组件id,不使用属性名装配
4) 自动装配(@Autowired)默认必须有注入类,否则报错
   1) 可以使用@Autowired(required=false)
5) @Primary:自动装配时,默认首选的bean.@Qualifier也是一种优先级bean指定注解
```
@Resource(JSR250),@Inject(JSR330):java规范注解

@Resource
```
1) 用于自动装配,默认按组件名称进行装配
2) 不支持spring原始注解.即不支持@Primary
3) 注入对象必须存在,不支持required=false
```

@Inject
```
1) 需要引入javax.inject依赖    
2) 注入对象必须存在,不支持required=false
```

- 自动注入方式
  
AutowiredAnnotationBeanPostProcessor:解析完成自动装配功能
@Autowired进行自动注入的方式:构造器,参数,方法,属性;都是从容器中获取参数组件的值

1) 方法上注入:方法参数上是需要注入的bean,参数从容器中获取;@Autowired可省略
2) 构造器注入:如果构造器只有一个有参构造器,@Autowired可省略
3) 参数注入:@Autowired不可省


- 激活配置环境

@Profile:组件在指定环境下才注册到容器中.默认是default,不进行注册限制    

1) 加了环境标识的bean,只有在该环境下才进行bean注册
2) 协助配置类上,指定环境下才加载该配置类

激活方式:    
1) -Dspring.profiles.active=test
2)applicationContext.getEnvironment().setActiveProfiles("dev")

### bean的生命周期

```
bean创建->初始化->销毁
```
- 容器管理bean的生命周期:    

支持自定义初始化和销毁方法;    
容器在bean进行到当前声明周期的时候调用自定义生命周期方法    
```
构造(对象创建)	
	单实例:在容器启动时创建对象    
	多实例:在每次获取时创建对象


BeanPostProcessor.postProcessBeforeInitialization
初始化:
	对象创建完成;赋值;调用初始化方法    
BeanPostProcessor.postProcessAfterInitialization

销毁:    
	单实例:容器关闭时    
	多实例:容器不会管理这个bean,不会调用销毁方法;由jdk进行垃圾回收

```
1) 指定初始化和销毁方法:    
   1) 通过@Bean指定init-method,destroy-method
2) 通过让bean实现InitializingBean(定义初始化逻辑),DisposableBean(定义销毁逻辑)        	
3) 使用JSR250:
   1) @PostConstruct: 在bean创建完成并属性赋值完成后,执行初始化方法    
   2) @PreDestroy:在容器销毁bean前执行
4) BeanPostProcessor:bean后置处理器
   1) 在bean初始化前后进行一些处理
   2) postProcessBeforeInitialization():初始化前执行
   3) postProcessAfterInitialization():初始化后执行

- BeanPostProcessor

spring中应用点:    
	bean赋值;注入其他组件;@Autowired;声明周期注解功能;@Async;xxxBeanPostProcessor     


原理:

在初始化方法的前后循环遍历对应的方法.    
	初始化前置操作:applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
		遍历得到容器中所有的BeanPostProcessor实现方法postProcessBeforeInitialization()    
		一旦返回null,跳出for循环,不再执行后面的applyBeanPostProcessorsBeforeInitialization();
	进行初始化:invokeInitMethods(beanName, wrappedBean, mbd);    
	初始化后置操作:applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

具体触发方法:org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#initializeBean(java.lang.String, java.lang.Object, org.springframework.beans.factory.support.RootBeanDefinition)

populateBean(beanName, mbd, instanceWrapper):进行bean属性赋值    
initializeBean:    
```
applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
invokeInitMethods(beanName, wrappedBean, mbd); 
applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
```


- spring扩展:自定义组件

自定义组件想要使用spring容器底层的一些组件(ApplicationContext,BeanFactory,xxx)

1) 自定义组件实现xxxAware:在创建对象时,会调用接口规定的方法注入相关组件
2) 把spring底层一些组件注入到自定义bean中
   1) xxxAware: 功能使用xxxProcessor实现:ApplicationContextAware -> ApplicationContextAwareProcessor



### AOP

在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式.    
优点:解耦.解决非业务代码与业务代码的耦合问题;    
缺点:粒度取决与方法,无法做到更细粒度的控制.

- 使用方式

1) 导入aop模块:spring aop(spring-aspects)
2) 定义要给业务逻辑类(CarService):在业务逻辑运行时进行非业务代码执行(日志等)
3) 声明切面类(@Aspect):切面里方法需要动态告知CarService.run()方法运行点,给切面类的目标方法标注合适何地运行(通知注解)
通知方法:
```
前置通知@Before:在目标方法执行前切入.
后置通知@After:在目标方法执行后切入.
返回通知@AfterReturning:在目标方法正常返回后切入.
异常通知@AfterThrowing:在目标方法异常后切入.
环绕通知@Around:动态代理,手动推进目标方法运行
```
4) 将切面类和业务逻辑类加入容器中
5) 启用切面.给配置类中加@EnableAspectJAutoProxy

#### 常用注解

@Aspect:声明是切面类.类注解    
@Pointcut("execution(public int com.ll.service.*)"):公共切入点.方法注解        
@Before("pointCut()"):切点表达式(指定在哪个方法切入[可以使用公告切入点]).方法注解        
@After:在目标方法执行后切入.    
@AfterReturning:在目标方法正常返回后切入.    
@AfterThrowing:在目标方法异常后切入.    
@Around:jointPoint.procced()    

JoinPoint必须在自定义切面方法的第一个参数,否则无法识别

#### AOP原理

看给容器中注册了什么组件,这个组件什么时候工作,这个组件的功能是什么?

- @EnableAspectJAutoProxy

@Import(AspectJAutoProxyRegistrar.class):给容器中导入AspectJAutoProxyRegistrar    
	利用AspectJAutoProxyRegistrar自定义给容器中注册bean    
	内部实现类是:AnnotationAwareAspectJAutoProxyCreator

- AnnotationAwareAspectJAutoProxyCreator

AnnotationAwareAspectJAutoProxyCreator
	-> AspectJAwareAdvisorAutoProxyCreator
		-> AbstractAdvisorAutoProxyCreator
			-> AbstractAutoProxyCreator
				-> SmartInstantiationAwareBeanPostProcessor,BeanFactoryAware
			实现了后置处理器(bean初始化前后要做的事情),自动装配BeanFactory

核心方法:    
AbstractAutoProxyCreator.setBeanFactory:自动装配    
AbstractAutoProxyCreator.postProcessBeforeInstantiation:后置处理器前方法    
AbstractAutoProxyCreator.postProcessAfterInitialization:后置处理器后方法    
AbstractAdvisorAutoProxyCreator.setBeanFactory()->initBeanFactory()    
AnnotationAwareAspectJAutoProxyCreator.initBeanFactory()


- 创建和注册AnnotationAwareAspectJAutoProxyCreator过程

1) 传入配置,创建ioc容器
2) 注册配置类,调用refresh()刷新容器
3) registerBeanPostProcessors(beanFactory):注册bean的后置处理器来进行bean的初始化拦截
	1) 获取ioc容器已定义的需要创建对象的所有BeanPostProcessor
	2) 给容器中加入所有的BeanPostProcessor
	3) 优先注册实现了PriorityOrdered接口的BeanPostProcessor
	4) 注册实现了Ordered接口的BeanPostProcessor
	5) 注册未实现优先级接口的BeanPostProcessor
	6) 注册BeanPostProcessor
	7) 实际每此遍历时获取bean对象就是创建BeanPostProcessor对象,并保存在容器中.beanFactory.getBean(ppName, BeanPostProcessor.class)
		创建internalAutoProxyCreator的BeanPostProcessor[AnnotationAwareAspectJAutoProxyCreator]
		1) 创建Bean实例
		2) populateBean(beanName, mbd, instanceWrapper):进行bean属性赋值    	
		3) initializeBean:初始化bean
			1) invokeAwareMethods: 处理Aware接口的方法回调
			2) applyBeanPostProcessorsBeforeInitialization:应用后置处理器前置方法
			3) invokeInitMethods:初始化
			4) applyBeanPostProcessorsAfterInitialization:应用后置处理器后置方法
		4) BeanPostProcessor(AnnotationAwareAspectJAutoProxyCreator)
	8) 把BeanPostProcessor注册到BeanFactory中:
		beanFactory.addBeanPostProcessor(postProcessor)

4) this.finishBeanFactoryInitialization(beanFactory): BeanFactory初始化.
   触发自[AnnotationAwareAspectJAutoProxyCreator -> InstantiationAwareBeanPostProcessor]
   1) 遍历获取容器中所有的bean,依次创建对象getBean(beanName);getBean->doGetBean()->getSingleton()
   2) 创建bean
	  AnnotationAwareAspectJAutoProxyCreator 在所有bean创建前会有一个拦截
	   1) 线程缓存中获取当前bean,如果能获取到,直接使用;否则再创建.只要创建好的bean都会被缓存起来.
	   2) createBean():创建
		  AnnotationAwareAspectJAutoProxyCreator 在创建任何bean前都尝试
		  BeanPostProcessor: 是在Bean对象创建完成初始化前后调用    
		  InstantiationAwareBeanPostProcessor: 在创建bean实例之前先尝试用后置处理器返回对象的
   			1) resolveBeforeInstantiation(beanName, mbdToUse): 解析BeforeInstantiation
			希望后置处理器在此能返回一个代理对象,如果能返回代理对象就使用,如果不能就继续
		   		1) 后置处理器先尝试返回对象
					bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
				   		拿到多有后置处理器,如果是InstantiationAwareBeanPostProcessor 
				   		就执行postProcessBeforeInstantiation
				    if (bean != null) {
				   		bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
				    }
				2) doCreateBean(beanName, mbdToUse, args);真正去创建一个bean 
				3) 



- AnnotationAwareAspectJAutoProxyCreator[InstantiationAwareBeanPostProcessor]

1) 每一个bean创建前,调用postProcessBeforeInstantiation
	关系切面类和被注入类的场景
   1) 判断当前bean是否在
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
















