# appframework-java

Appframework-java 是飞书开放平台应用开发接口Java版本SDK，用来帮助开发者快速搭建飞书app。
SDK中封装了飞书开放台的主要的接口，提供了事件回调框架，让开发着只需要关注自己的业务逻辑，减少在api、协议、安全校验等问题上花费的精力。

# 1. 快速开始

### 1.1 依赖

```xml
<dependency>
    <artifactId>appframework-sdk</artifactId>
    <groupId>com.larksuite.appframework</groupId>
    <version>${version}</version>
</dependency>
```


### 1.2 配置

每个飞书应用都有一个唯一的appId和对应的appSecret. AppId是一个飞书应用的唯一标识，但是其是开放平台随机生成的一个字符串，不利于开发者用来记忆和区分应用。
所以我们让开发者为自己的app设定一个短名字，后续开发中对app的标识主要使用此名字。

开始开发之前，我们需要先从开放平台开发者后台收集app相关的配置项，生成一个AppConfiguration对象。

```java

AppConfiguration ac = new AppConfiguration();
ac.setAppShortName("myAppName");    // app name, will be used to identify a app, should be unique
ac.setAppId(appId);
ac.setAppSecret(appSecret);
ac.setEncryptKey(encryptKey);
ac.setVerificationToken(verificationToken);
ac.setIsIsv(Boolean.parseBoolean(isIsv));
```

以下为一种稍简单的方式用来从配置文件读取配置并生成AppConfiguration对象。

```java
AppConfiguration conf = AppConfiguration.loadFromProperties(String appShortName, Properties properties);
```

配置文件示例如下，配置中需要使用到自己定义的应用名
```properties

# larksuite.appframework.${appShortName}.appId
larksuite.appframework.test-app1-isv.appId=cli_xxxxxxxxxx
larksuite.appframework.test-app1-isv.appSecret=xxxxxxxxxxxxxxxx
larksuite.appframework.test-app1-isv.encryptKey=xxxxxxxxxxxx
larksuite.appframework.test-app1-isv.verificationToken=xxxxxxxxxxxx
larksuite.appframework.test-app1-isv.isIsv=true
```

### 1.3 创建LarkAppInstance

LarkAppInstance对应的是一个app应用实例，是几乎所有应用操作的入口，可以认为其为整个框架入口。
通常我们使用LarkAppInstanceFactory来构建一个LarkAppInstance，示例代码如下

```java
AppConfiguration appConfiguration = AppConfiguration.loadFromProperties("test-app1-isv", properties); //test-app1-isv is a app short name

LarkAppInstanceFactory.AppEventListener myTestAppListener1 = LarkAppInstanceFactory
        .createAppEventCallbackListener()
        .onMessageEvent(new App1MessageEventHandler())
        .onBotInvitedEvent(new App1AddRobotEventHandler());

LarkAppInstance ins = LarkAppInstanceFactory
       .builder(appConfiguration)
       .appTicketStorage(new RedisAppTicketStorage())
       .registerAppEventCallbackListener(myTestAppListener1)
       .create();
```

如果要开发ISV应用，开发者需要自己提供一个AppTicketStorage接口的实现。App ticket是用来获取应用access token的必要参数，是开放平台主动推送给应用的。
所以开发者需要使用一个持久化（通常Redis或Mysql）的实现，保证接收到的app ticket能被存储。对于内部应用，忽略这个配置项即可。


### 1.4 注册事件处理器到AppEventListener

BotAppsServerFactory.AppEventListener提供几乎全部飞书事件处理逻辑的注册接口，如果没有提供对应事件的处理逻辑，当应用接收到相应事件时，框架会静默地忽略掉。
构造完AppEventListener后设置到LarkAppInstance中即可。

### 1.5 接收飞书开放平台事件

下面是接收飞书开放平台事件的一个例子。开发框架没有提供http服务的能力，开发者需要将代码运行在一款web server上，可以按java j2ee标准的HttpServlet的接口提供一个url来接收webhook的事件回调。
LarkAppInstance.receiveLarkNotify方法接收原始的报文，完成业务处理后，会同步返回一些数据给开发平台，需要返回的数据是依具体接口而不同。

```java
public class App1EventServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestString = com.google.common.io.CharStreams.toString(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));

        System.out.println("LarkNotifyApp1Servlet requestString: " + requestString);

        String respData = Global.botApp1.receiveLarkNotify(requestString);

        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(respData);
    }
}

```

### 1.6 使用LarkClient发送请求

LarkClient封装了所有主动向开放平台发送请求的方法，LarkAppInstance.getLarkClient可以获得一个全局唯一的client实例，handler中提供的InstanceContext参数也可以通过getLarkClient方法获得该实例。
以下例子展示了向id为"123456"的用户发送文本消息的方式。

```java
Message msg = new TextMessage("Hello");
larkAppInstance.getLarkClient().sendChatMessage(MessageDestinations.UserId("123456"), msg);
```

# 2. Function details
### 2.1 Supported Events

目前为止，已支持的回调事件如下

| Event type | Event class |
| :---: | :---: |
| app_open | AppEnabledEvent |
| approval | ApprovalEvent |
| app_status_change | AppStatusChangeEvent |
| app_ticket | AppTicketEvent |
| add_bot | BotInvitedEvent|
| remove_bot| BotRemovedEvent |
| user_add | ContactsUpdatesEvent|
| leave_approval| LeaveApprovalEvent|
| message|MessageEvent |
| order_paid| OrderPaidEvent|
| work_approval| OvertimeApprovalEvent|
| p2p_chat_create| P2pChatCreateEvent|
| remedy_approval|RemedyApprovalEvent |
| shift_approval | ShiftApprovalEvent|
| trip_approval | TripApprovalEvent|


### 2.2 LarkClient提供的方法

| Function name | Remark |
| :---: | :---: |
| app_open | AppOpenEvent |
| approval | ApprovalEvent |
| app_status_change | AppStatusChangeEvent |
| app_ticket | AppTicketEvent |
| add_bot | AddBotEvent|
| remove_bot| RemovedBotEvent |
| user_add | UserAddEvent|
| leave_approval| LeaveApprovalEvent|
| message|MessageEvent |
| order_paid| OrderPaidEvent|
| work_approval| workApprovalEvent|
| p2p_chat_create| P2pChatCreateEvent|
| remedy_approval|RemedyApprovalEvent |
| shift_approval | ShiftApprovalEvent|
| trip_approval | TripApprovalEvent|

### 2.3 机器人发送消息类型

#### 2.3.1 Text message 文本消息

TextMessage

#### 2.3.2 Image message 图片消息

ImageMessage

#### 2.3.3 Share group message 分享群名片

ShareGroupMessage

#### 2.3.4 Post message 富文本消息

PostMessage

Usage example
```java
PostBuilder.Post post = PostBuilder.newPost();
PostBuilder.Language zhCn = post.createZhCnLanguage("I'm the title");

zhCn.createLine()
    .createTextTag("Line one", false) // unEscape: false
    .creatATag("go to github", "https://github.com");

zhCn.createLine()
    .createTextTag("Line two", false)
    .creatAtTag("123456")   // @Someone useId: 123456
    .createImgTag("xxxxxx", 300, 400); // append a image of key: xxxxxx, width:300, height: 400

Message msg = new PostMessage(post.toContent());
```

#### 2.3.5 Card message 卡片消息

CardMessage

卡片消息可能会很复杂，这里展示了一个构建卡片消息的简单例子

```java
Card card = new Card(new Config(true), new Header(new Text(Text.Mode.PLAIN_TEXT, "I am the title"))); // config and header are necessary

Button button = new Button("TestCard.btn", new Text(Text.Mode.PLAIN_TEXT, "submit btn")).setValue(ImmutableMap.of("k1", "v1"));

Action action = new Action(Lists.newArrayList(button));

card.setModules(Lists.newArrayList(
                    div,
                    new Hr(),
                    action
));

Message msg = new CardMessage(card.toObjectForJson());
```

交互式的卡片元素（按钮、选择列表、日历等）的构造方法第一个参数都是该元素的名字，这类似于html form中的元素name，用来区分提交到服务端的数据的。
当接收到Card交互事件时，可以通过CardActionUtils.getActionMethodName(CardEvent cardEvent)方式获得元素name。

# 3. SpringBoot 支持

Spring boot是目前主流的Java开发框架，我们提供了SpringBoot风格的插件以便开发者更容易地在SpringBoot框架中开发和集成飞书应用，减少一些重复代码。
开发者直接引入appframework的springboot starter包即可。

### 3.1 Maven 依赖

```xml
<dependency>
    <artifactId>appframework-spring-boot-starter</artifactId>
    <groupId>com.larksuite.appframework</groupId>
    <version>${version}</version>
</dependency>
```
版本号一般取最新版本号即可。

### 3.2 配置文件

为了支持单个应用中实现多个app，配置文件只能支持yml格式，以下是一个完整的配置文件例子，一般写在application.yml中。

```yaml
spring:
  application:
    name: example

server:
  port: 7070

larksuite:
  appframework:
    feishu: true
    notify:
      basePath: /notify
    apps[0]:
      appShortName: app1Name
      appId: cli_xxx
      appSecret: xxxxxxxxxxxx
      encryptKey: xxxxxxxxxxxx
      verificationToken: xxxxxxxxxxxx
      isIsv: false
    apps[1]:
      appShortName: app2Name
      appId: cli_xxx
      appSecret: xxxxxxxxxxxx
      encryptKey:
      verificationToken:
      isIsv: false
```

#### 3.2.1 larksuite.appframework.feishu

appframework默认对应的是海外lark环境，飞书用户通过配置"larksuite.appframework.feishu"来选择环境，设置该配置项为"true"，即为国内飞书环境。

#### 3.2.2 larksuite.appframework.notify.basePath

larksuite.appframework.notify.basePath配置后会开启一个http servlet，接收以该地址开头的所有请求，但只处理固定格式uri的请求。
如果设置该值为"/notify"，如下两个uri的请求会被处理。如果进程内配置了多个app，就会有多组url的请求被监听。

1. "/notify/event/${appName}" 回调事件
2. "/notify/card/${appName}" 卡片事件

项目启动时，可以在启动日志里找到对应的url信息。找到后加上http(s)和域名，组成完整url后，配置到开发者后台即可（事件订阅和机器人消息卡片）。

### 3.3 监听回调事件

```java
@LarkEventHandlers(appName="someAppName")
public class EventHandlers {

    @Handler
    public Object onRobotAdd(AddBotEvent event, LarkClient larkClient) {

        try {
            larkClient.sendChatMessage(
                    MessageDestinations.ChatId(event.getOpenChatId()),
                    new TextMessage("Hello, I'm Echo Robot, try say to me."));
        } catch (LarkClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Handler
    public Object onMessageEvent(TextMessageEvent event, InstanceContext ic) {
        return null;
    }
}
```

LarkEventHandlers注解可以加在class上，会激活spring包扫描，从此class下找到所有被"Handler" "CardAction"注解的方法，并转换成EventHandler。
LarkEventHandlers如果指定了appName，该class下所有转换而成的EventHandler都会绑定到该app。Handler CardAction都可以单独指定appName，并会覆盖LarkEventHandlers中的appName。
对于单app的应用程序，可以完全忽略这个参数。但是多app时，必须让包扫描程序能为每个Handler/CardAction找到一个存在的appName。


标识了"@Handler"的方法，会根据方法参数列表中的Event类型来响应不同类型的事件。如果需要用到LarkClient/InstanceContext实例，在参数列表中增加其声明即可。


### 3.3 监听卡片交互事件

```java
@LarkEventHandlers(appName="someAppName")
public class CardEventHandlers {
    @Handler
    public Card onCardEvent(CardEvent event) {
        
        return null;
    }

    @CardAction(methodName = "testCard.btn1")
    public Card onActionMethod1(CardEvent cardEvent, LarkClient larkClient) {
        return null;
    }

    @CardAction(methodName = "testCard.btn2")
    public Card onActionMethod2(CardEvent cardEvent, LarkClient larkClient) {
        return null;
    }
}
```

以上例子提供了两种方式来响应卡片事件。
 1. @CardAction  CardAction注解需要methodName，那么该方法会响应此methodName对应的交互元素的事件。
 2. @Handler + CardEvent  所有没有被CardAction根据methodName匹配到的事件都会被此方法处理。
  

```java
Card testCard = new Card(new Config(true), new Header(new Text(Text.Mode.PLAIN_TEXT, "I am the title"))); // config and header are necessary

Action action = new Action(Lists.newArrayList(
    new Button("testCard.btn1", new Text(Text.Mode.PLAIN_TEXT, "button-1")),
    new Button("testCard.btn2", new Text(Text.Mode.PLAIN_TEXT, "button-2")),
    new Button("testCard.btn3", new Text(Text.Mode.PLAIN_TEXT, "button-3"))
));

card.setModules(Lists.newArrayList(action));

Message msg = new CardMessage(card.toObjectForJson());
```

上述例子中，我们为卡片设置了3个按钮，并想为这3个按钮分别响应其点击事件。在以上CardEventHandlers类中，onActionMethod1方法会处理"testCard.btn1"按钮的点击事件，
onActionMethod2方法处理"testCard.btn1"按钮的点击事件，剩下的事件都会由onCardEvent方法处理。


### 3.4 获得LarkClient实例

在一些场景中，我们需要主动获取LarkClient实例。回调事件handler的参数列表中提供了LarkClient实例，但对于非回调事件的场景，我们可以用spring context注入的方式获得实例。
例子如下：

```java
@Component
public class MyBusinessService {
    
    @Autowired
    private LarkClient larkClient;

    @Autowired
    private LarkAppInstance larkAppInstance;
    
    // ...
}
```

对于多app的项目，上面的代码会在启动中出错。原因也很明显，因为自动注入过程存在多个候选者。所以我们需要指定注入的bean的名字。例如，如果我们有两个app:"app1","app2"。
下面是一组例子，bean的规则为"${appName}${ClassName}"。LarkClient的注入我们声明的名字为app1LarkClient和app2LarkClient，LarkAppInstance的注入，声明的名字为app1LarkAppInstance和app2LarkAppInstance。

```java
@Component
public class MyBusinessService {
    
    @Autowired
    private LarkClient app1LarkClient;  // variable name must be: "${appName}LarkClient"

    @Resource(name="app2LarkAppInstance")  // "${appName}LarkAppInstance"
    private LarkAppInstance app2LarkAppInstance; // arbitrary variable name

    // ...
}
``` 


# 4. Examples

appframework-jetty-example

appframework-springboot-example

