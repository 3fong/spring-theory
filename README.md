## spring ioc流程

### ioc创建核心方法: refresh() 创建刷新

- 刷新前预处理:Prepare this context for refreshing.
prepareRefresh();
1) initPropertySources();: 初始化一些属性参数,子类自定义个性化的属性设置方法
2) getEnvironment().validateRequiredProperties();: 属性合法性校验
3) this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);: 获取容器监听器
4) this.earlyApplicationEvents = new LinkedHashSet<>();: 保存容器早期事件

- 获取BeanFactory: Tell the subclass to refresh the internal bean factory.
ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
1) refreshBeanFactory(); 刷新创建BeanFactory
   1) 创建了一个BeanFactory并设置id:
      1) DefaultListableBeanFactory beanFactory = createBeanFactory();
      2) beanFactory.setSerializationId(getId());
2) getBeanFactory(); 返回 GenericApplicationContext 创建的 BeanFactory 对象
3) 将创建BeanFactory[DefaultListableBeanFactory]返回

- BeanFactory 的预准备工作(进行配置): Prepare the bean factory for use in this context.
prepareBeanFactory(beanFactory);
1) 设置BeanFactory的类加载器,支持表达式解析器....
2) 添加部分BeanPostProcessor(new ApplicationContextAwareProcessor(this));
3) 设置忽略的自动装配的接口:EnvironmentAware,EmbeddedValueResolverAware...
4) 注册可以解析的自动装配,我们能直接在任何组件中自动注入: BeanFactory,ResourceLoader,ApplicationEventPublisher,ApplicationContext.class
5) 添加BeanPostProcessor(new ApplicationListenerDetector(this));
6) 如果存在loadTimeWeaver,则添加它的BeanPostProcessor和临时类加载器
7) 给 BeanFactory 注册一些能用的组件
   1) environment
   2) systemProperties
   3) systemEnvironment

- BeanFactory准备工作完成后进行的后置处理工作(子类扩展接口): Allows post-processing of the bean factory in context subclasses.
postProcessBeanFactory(beanFactory);
1) 子类通过重写这个方法在BeanFactory创建并预准备完成后做进一步的设置

***
**以上是BeanFactory的创建及准备工作**
*** 


- 执行BeanFactoryPostProcessor的方法: Invoke factory processors registered as beans in the context.
invokeBeanFactoryPostProcessors(beanFactory);    
BeanFactoryPostProcessor: BeanFactory后置处理器.在BeanFactory标准初始化后执行    
两个接口: BeanFactoryPostProcessor,BeanDefinitionRegistryPostProcessor
1) 执行 BeanFactoryPostProcessor 的方法
   1)  先执行 BeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry
      1)  获取所有的 BeanDefinitionRegistryPostProcessor
      2)  先执行实现了 PriorityOrdered 接口的 BeanDefinitionRegistryPostProcessor 的方法
      3)  再执行实现了 Ordered 接口的 BeanDefinitionRegistryPostProcessor 的方法
      4)  最后执行没有实现任何优先级或顺序接口的 BeanDefinitionRegistryPostProcessor 的方法
   2) 再执行 BeanFactoryPostProcessor.postProcessBeanFactory() 方法
      1) 获取所有的 BeanFactoryPostProcessor
      2) 先执行实现了 PriorityOrdered 接口的 BeanFactoryPostProcessor 的方法
      3) 再执行实现了 Ordered 接口的 BeanFactoryPostProcessor 的方法
      4) 最后执行没有实现任何优先级或顺序接口的 BeanFactoryPostProcessor 的方法



- 注册bean后置处理器 BeanPostProcessor: Register bean processors that intercept bean creation.
registerBeanPostProcessors(beanFactory);    
1) 不同接口类型的 BeanPostProcessor 在bean创建前后的执行时机不一样
   1) BeanPostProcessor 的核心实现类
      1) InstantiationAwareBeanPostProcessor: 前置实例化回调后置处理器
      2) DestructionAwareBeanPostProcessor: 前置销毁回调后置处理器
      3) MergedBeanDefinitionPostProcessor: 运行时合并bean定义回调接口后置处理器
      4) SmartInstantiationAwareBeanPostProcessor: 预测进程bean最终类型回调后置处理器
   2) 获取所有的 BeanPostProcessor: 后置处理器都默认可以通过PriorityOrdered,Ordered接口来执行优先级
   3) 先注册 PriorityOrdered 优先级接口的 BeanPostProcessor
      1) 把每一个 BeanPostProcessor 添加到 BeanFactory 中:beanFactory.addBeanPostProcessor(postProcessor);
   4) 再注册 Ordered 优先级接口的 BeanPostProcessor
   5) 最后注册没有实现任何优先级或顺序接口
   6) 最终注册 MergedBeanDefinitionPostProcessor 
   7) 注册一个 ApplicationListenerDetector : 用于在bean创建完成后检查是否是ApplicationListener
      1) 是: this.applicationContext.addApplicationListener((ApplicationListener<?>) bean)

- 初始化MessageSource组件(国际化,消息绑定,消息解析): Initialize message source for this context.
initMessageSource();    
1) 获取 BeanFactory
2) 看容器中是否有id="messageSource"类型的MessageSource组件
   1) 有赋值给messageSource;没有则新建一个 DelegatingMessageSource
      1) MessageSource: 取出国际化配置文件中的某个key的值;能安装区域信息获取
3) 把创建好的 MessageSource 注册到容器中,以后获取国际化配置文件值时,可以自动注入 MessageSource.
   1) beanFactory.registerSingleton(MESSAGE_SOURCE_BEAN_NAME, this.messageSource)
   2) 通过  MessageSource.getMessage()获取值

- 初始化事件派发器: Initialize event multicaster for this context.
initApplicationEventMulticaster();
1) 获取 BeanFactory
2) 从 BeanFactory 中获取 id="applicationEventMulticaster"的ApplicationEventMulticaster
3) 如果没有则新建 SimpleApplicationEventMulticaster
4) 将创建的的ApplicationEventMulticaster组件添加到 BeanFactory 中,以供自动注入

- 子容器实例化扩展接口: Initialize other special beans in specific context subclasses.
onRefresh(): 子类重写找个方法,在容器刷新时可自定义逻辑

- 注册容器中所有的监听器 ApplicationListener : Check for listener beans and register them.
registerListeners();    
1) 从容器中拿到所有的 ApplicationListener 
2) 将每个监听器添加到事件派发器中:getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
3) 派发之前步骤产生的事件

- 初始化所有剩下的单实例bean: Instantiate all remaining (non-lazy-init) singletons.
finishBeanFactoryInitialization(beanFactory);    
1) beanFactory.preInstantiateSingletons(): 初始化所有剩下的单实例bean
   1) 获取容器中所有的bean,依次进行初始化和创建对象
   2) 获取bean的定义信息: RootBeanDefinition
   3) bean不是抽象的,是单例的,是懒加载
      1) 判断是否是FactoryBean;是否是实现 FactoryBean接口的bean
      2) 不是FactoryBean,利用getBean(beanName)创建对象
         1) getBean(beanName): ioc.getBean()
         2) doGetBean(name, null, null, false);
         3) 先获取缓存中保存的单实例bean,如果有值则说明bean已创建(所有的创建的单实例bean会被缓存)
            1) private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
         4) 缓存中没有,开始bean的创建对象流程
         5) 标记当前bean已被创建
         6) 获取bean的定义信息
         7) 获取当前bean依赖的其他bean.通过getBean()先创建依赖bean
         8) 启动单实例bean的创建流程
            1) createBean(beanName, mbd, args)
               1) 执行BeanPostProcessor实例化前置处理: resolveBeforeInstantiation(beanName, mbdToUse);[InstantiationAwareBeanPostProcessor]
                  1) 先触发后置处理器前置实例化: postProcessBeforeInstantiation()
                  2) 如果有返回值: 触发postProcessAfterInitialization()
               2) 如果前面的 InstantiationAwareBeanPostProcessor 没有返回代理对象,直接进行下一步->对象创建
               3) 对象创建: doCreateBean(beanName, mbdToUse, args)
                  1) 创建bean实例: createBeanInstance(beanName, mbd, args).
                     1) 利用工厂方法或对象构造器创建出bean实例
                  2) applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName): 调用 MergedBeanDefinitionPostProcessor.postProcessMergedBeanDefinition
                  3) bean属性赋值: populateBean(beanName, mbd, instanceWrapper)
                     1) 赋值前1: InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation.执行自定义字段注入
                     2) 赋值前2: InstantiationAwareBeanPostProcessor.postProcessPropertyValues.自定义参数属性值替换
                     3) 应用bean属性值:通过setter进行属性赋值:applyPropertyValues(beanName, mbd, bw, pvs);
                  4) bean初始化: initializeBean(beanName, exposedObject, mbd)
                     1) 执行Aware接口方法: invokeAwareMethods(beanName, bean)
                        1) 执行xxxAware接口方法[BeanNameAware,BeanClassLoaderAware,BeanClassLoaderAware]
                        2) 执行后置处理器前置初始化: applyBeanPostProcessorsBeforeInitialization
                           1) BeanPostProcessor.postProcessBeforeInitialization(result, beanName)
                        3) 执行初始化方法: invokeInitMethods(beanName, wrappedBean, mbd)
                           1) 是否是 InitializingBean 接口的实现: 执行接口规定的初始化
                           2) 是否自定义初始化方法
                        4) 执行后置处理器后置初始化: applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName)
                           1) BeanPostProcessor.postProcessAfterInitialization(result, beanName)
                  5) 注册bean的销毁方法: registerDisposableBeanIfNecessary(beanName, bean, mbd)
      3) 所有的bean都利用getBean创建完成后
         1) 检查所有的bean是否是 SmartInitializingSingleton 接口
         2) 如果是: 触发回调: SmartInitializingSingleton.afterSingletonsInstantiated()


- 完成BeanFactory的初始化创建工作,IOC容器创建完成:Last step: publish corresponding event.
finishRefresh();    
1) initLifecycleProcessor(): 初始化和生命周期相关的后置处理器
   1) 默认从容器中找是否有组件id="lifecycleProcessor"的LifecycleProcessor组件
      1) 没有则新建 DefaultLifecycleProcessor 后放入容器
2) getLifecycleProcessor().onRefresh();
   1) 拿到前面定义的生命周期处理器BeanFactory,回调onRefresh()
3) publishEvent(new ContextRefreshedEvent(this));发布容器刷新完成事件
4) LiveBeansView.registerApplicationContext(this);


***
ioc容器就是这些map:很多的map里保存了单实例bean,环境信息
***

### 总结
1) spring容器在启动时,先保存所有注册进来的bean定义信息
   1) xml注册bean: <bean>
   2) 注解注册bean: @Component,@Service,@...
2) spring 容器会在合适的时机创建这些bean
   1) 用到这个bean时,利用getBean创建bean;创建好后保存在容器中
   2) 统一创建剩下所有的bean: finishBeanFactoryInitialization
3) 后置处理器: BeanPostProcessor
   1) 每一个bean创建完成,都会使用各种后置处理器进行处理,来增强bean的功能
      1) AutowiredAnnotationBeanPostProcessor: 处理自动注入
      2) AnnotationAwareAspectJAutoProxyCreator: 做AOP功能
      3) ...
      4) AsyncAnnotationBeanPostProcessor: 增强功能注释
4) 事件驱动模型
   1) ApplicationListener: 事件监听
   2) ApplicationEventMulticaster: 事件派发










