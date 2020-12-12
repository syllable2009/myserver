final、finally、finalize的区别：

final： java中的修饰符。final修饰的类不能被继承，final修饰的方法不能被重写，final修饰的变量初始化之后不能被修改(当然这条不是绝对的，java中有一些手段可以修改)。
finally： java异常处理的组成部分，finally代码块中的代码一定会执行（如果真是这样本文也没必要存在了）。常用于释放资源。
finalize：Object类中的一个方法，垃圾收集器删除对象之前会调用这个对象的finalize方法。

public static int getNum(){
        int a = 10;
        try {
            a = 20;
            // a = a/0;
            return a;
        } catch (Exception e) {
            a = 30;
            return a;
        }finally{
            a = 40;
            //return a;
        }
    }//调用该方法返回20
因为在执行finally之前，程序将return结果赋值给临时变量，然后执行finally代码块，最后将临时变量返回。
当然如果在finally代码块中有return语句，最终生效的是finally代码块中的return。
public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "zhangsan");     
        try {
            map.put("name", "lisi");
            return map;
        }
        catch (Exception e) {
            map.put("name", "wangwu");
        }
        finally {
            map.put("name", "zhaoliu");
            map = null;
        }         
        return map;
    }
 System.out.println(getMap().get("name").toString());
 //打印zhaoliu    
 
 
 1.与finally相对应的try语句得到执行的情况下，finally才有可能执行。
 2.finally执行前，程序或线程终止，finally不会执行。例如try中System.exit或者主线程终止时，守护线程终止。
 
 
 #值传递与引用传递：

 值 传 递：指在调用函数时将实际参数复制一份传递到函数中，这样在函数中如果对参数进行修改，将不会影响到实际参数。
 引用传递：指在调用函数时将实际参数的地址直接传递到函数中，那么在函数中对参数所进行的修改，将影响到实际参数。
 
 #java中的栈与堆
 
 栈：存放基本类型的局部变量，与对象的引用，方法执行结束后栈中的变量和对象的引用消失。
 堆：存放对象的实例。java中的数组和new出来的对象都是放在堆中的，堆中的对象没有任何引用(就是在栈中没有任何一个变量指向该对象)时会被GC回收。
 
 #java中只有值传递
 1.参数传递的时候是拷贝实参的副本传递给形参。
 2.在方法内只有修改了实参所指向的对象的内容，对实参才有影响。

 