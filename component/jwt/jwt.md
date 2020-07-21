#1.历史背景
Internet服务的身份验正过程是这样的：客户端向服务器发送登录名和登录密码，服务器验证后将对应的相关信息保存到当前会话中，这些信息包括权限、角色等数据。
服务器向客户端返回SessionId，SessionId信息都会写入到客户端的Cookie中，后面的请求都会从Cookie中读取 SessionId 发送给服务器，
服务器在收到 SessionId 后会对比保存的数据来确认客户端身份。
#2.问题
上述模式存在一个问题，无法横向扩展。在服务器集群或者面向服务且跨域的结构中，需要redis等数据库来保存 Session 会话，实现服务器之间的会话数据共享。
#3.JWT 简述
JWT 英文名是 Json Web Token ，是一种用于通信双方之间传递安全信息的简洁的、URL安全的表述性声明规范，经常用在跨域身份验证。
JWT 以 JSON 对象的形式安全传递信息。因为存在数字签名，因此所传递的信息是安全的。

客户端身份经过服务器验证通过后，会生成带有签名的 JSON 对象并将它返回给客户端。客户端在收到这个 JSON 对象后存储起来。
在以后的请求中客户端将 JSON 对象连同请求内容一起发送给服务器，服务器收到请求后通过 JSON 对象标识用户。
验证不通过的情况有很多，比如签名不正确、无权限等。在 JWT 中服务器不保存任何会话数据，使得服务器更加容易扩展。

#4.JWT 组成结构
JWT 是由三段字符串和两个 . 组成，每个字符串和字符串之间没有换行（类似于这样：xxxxxx.yyyyyy.zzzzzz），每个字符串代表了不同的功能，我们将这三个字符串的功能按顺序列出来并讲解
1. JWT 头
JWT 头描述了 JWT 元数据，是一个 JSON 对象，它的格式如下：
json{"alg":"HS256","typ":"JWT"}
这里的 alg 属性表示签名所使用的算法，JWT 签名默认的算法为 HMAC SHA256 ， alg 属性值 HS256 就是 HMAC SHA256 算法。typ 属性表示令牌类型，这里就是 JWT。
2. 有效载荷
有效载荷是 JWT 的主体，同样也是个 JSON 对象。有效载荷包含三个部分：
标准注册声明
标准注册声明不是强制使用是的，但是我建议使用。它一般包括以下内容：
iss：jwt的签发者/发行人；
sub：主题；
aud：接收方；
exp：jwt过期时间；
nbf：jwt生效时间；
iat：签发时间
jti：jwt唯一身份标识，可以避免重放攻击
公共声明：
可以在公共声明添加任何信息，我们一般会在里面添加用户信息和业务信息，但是不建议添加敏感信息，因为公共声明部分可以在客户端解密。
私有声明：
私有声明是服务器和客户端共同定义的声明，同样这里不建议添加敏感信息。
下面这个代码段就是定义了一个有效载荷：
json{"exp":"201909181230","role":"admin","isShow":false}
3. 哈希签名
哈希签名的算法主要是确保数据不会被篡改。它主要是对前面所讲的两个部分进行签名，通过 JWT 头定义的算法生成哈希。哈希签名的过程如下:
1. 指定密码，密码保存在服务器中，不能向客户端公开；
2. 使用 JWT 头指定的算法进行签名，进行签名前需要对 JWT 头和有效载荷进行 Base64URL 编码，JWT 头和邮箱载荷编码后的结果之间需要用 . 来连接。
简单示例如下：
HMACSHA256(base64UrlEncode(JWT 头) + "." + base64UrlEncode(有效载荷),密码)
最终结果如下：
base64UrlEncode(JWT 头)+"."+base64UrlEncode(有效载荷)+"."+HMACSHA256(base64UrlEncode(JWT头) + "." + base64UrlEncode(有效载荷),密码)

Base64URL 算法。这个算法和 Base64 算法类似，但是有一点区别。
我们通过名字可以得知这个算法使用于 URL 的，因此它将 Base64 中的 + 、 / 、 = 三个字符替换成了 - 、 _ ，删除掉了 = 。因为这个三个字符在 URL 中有特殊含义。

#5.JWT的工作流程
下面是一个JWT的工作流程图。模拟一下实际的流程是这样的（假设受保护的API在/protected中）

1.用户导航到登录页，输入用户名、密码，进行登录
2.服务器验证登录鉴权，如果改用户合法，根据用户的信息和服务器的规则生成JWT Token
3.服务器将该token以json形式返回（不一定要json形式，这里说的是一种常见的做法）
4.用户得到token，存在localStorage、cookie或其它数据存储形式中。
5.以后用户请求/protected中的API时，在请求的header中加入 Authorization: Bearer xxxx(token)。此处注意token之前有一个7字符长度的 Bearer
6.服务器端对此token进行检验，如果合法就解析其中内容，根据其拥有的权限和自己的业务逻辑给出对应的响应结果。
7.用户取得结果

#6.Spring Security + JWT 前后端分离 code-time
参考：https://www.jianshu.com/p/5b9f1f4de88d
参考：https://www.jianshu.com/p/6307c89fe3fa
Spring Security就是一组拦截器，当点击登陆时，要是进行 username 和 password 请求值的获取，
然后再生成一个UsernamePasswordAuthenticationToken 对象，进行验证。
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <!--jjwt比较成熟的JWT类库,用于Java和Android的JWT token的生成和验证-->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.0</version>
        </dependency>

JWT的生成与验证工具类：

创建一个 JwtUser 实现 UserDetails

