#拦截器的实现
1.编写拦截器类实现HandlerInterceptor接口
三个必须实现的方法
preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) 
（第一步：在请求被处理之前进行调用 是否需要将当前的请求拦截下来，如果返回false，请求将会终止，返回true，请求将会继续Object arg2表示拦截的控制器的目标方法实例）
当进入拦截器链中的某个拦截器，并执行preHandle方法后
postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,ModelAndView arg3) 
（第二步：在请求被处理之后进行调用ModelAndView arg3是指将被呈现在网页上的对象，可以通过修改这个对象实现不同角色跳向不同的网页或不同的消息提示）

afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2，Exception arg3) 
（第三步：在请求结束之后调用 一般用于关闭流、资源连接等 比较少用）
#过滤器的实现filter


#拦截器和过滤器比较
①拦截器是基于Java的反射机制的，而过滤器是基于函数回调。
②过滤器filter是servlet规范的，只能用于web程序中，依赖servlet容器，拦截器是spring容器管理的，是spring框架支持的，不依赖与servlet容器。
③过滤器只能在servlet前后起作用，拦截器能深入到方法前后，异常抛出前后。
过滤器和拦截器触发时机不一样，过滤器是在请求进入容器后，但请求进入servlet之前进行预处理的。请求结束返回也是，是在servlet处理完后，返回给前端之前。
拦截器可以获取IOC容器中的各个bean，而过滤器就不行，因为拦截器是spring提供并管理的，spring的功能可以被拦截器使用，在拦截器里注入一个service，可以调用业务逻辑。而过滤器是JavaEE标准，只需依赖servlet api ，不需要依赖spring。

