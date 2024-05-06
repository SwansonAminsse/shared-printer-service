#### 一、项目结构
````
├── gradle ----------------------------gradle
├── src -------------------------------主项目代码
    ├── main/java ---------------------主项目代码
        ├── com/yn/printer/service ----主项目代码
            ├── advice ----------------
            ├── aop -------------------切面文件
            ├── common ----------------通用文件
            ├── config ----------------配置文件
            ├── interceptor -----------拦截器文件
            ├── modules ---------------模块目录
                ├── auth --------------auth模块
                    ├── controller ----控制层
                        ├── user ------user端控制层
                    ├── dto -----------数据传输对象
                    ├── entity --------实体层（后续若存在多个服务可将实体迁移到公共项目）
                    ├── enums ---------枚举（后续若存在多个服务可将枚举到公共项目）
                    ├── repository ----持久层
                    ├── service -------服务层
                    ├── vo ------------前端展示对象
            ├── Application -----------项目启动入口
    ├── main/resources ----------------静态资源目录
        ├── static --------------------静态文件
        ├── templates -----------------模板文件
        ├── application.yml -----------配置文件
        ├── application-dev.yml -------开发环境配置文件
        ├── application-local.yml -----本地环境配置文件
        ├── application-prod.yml ------生产环境配置文件
        ├── logback-dev.xml -----------开发环境日志文件
        ├── logback-local.xml ---------本地环境日志文件
        ├── logback-prod.xml ----------生产环境日志文件
├── .gitignore ------------------------git忽略文件
├── build.gradle ----------------------gradle配置文件
├── gradlew ---------------------------gradle wrapper
├── gradlew.bat -----------------------Windows下的批处理文件
├── readme.md -------------------------项目帮助文档
└── settings.gradle -------------------gradle项目关系配置
````
#### 二、版本管理
### master
- 稳定版主分支
- 版本稳定合并到该分支

### release
- 公测版分支，发布分支
- hotfix/xxxx  紧急修复分支，发布后合并到release分支并删除该分支
- patch/x.x.x 临时版分支，发布后合并到release分支并删除该分支

### develop
- 开发版分支
- patch/x.x.x  开发版分支，开发完后合并到develop分支并删除该分支
- featured/xxxx 某个模块功能的特性分支，开发完后合并到develop分支并删除该分支

### test
- 测试版分支

#### 三、项目说明
- modules为模块目录，其下以模块名称进行区分,比如auth模块
- 模块目录下分为controller,dto,entity,enums,repository,service,vo等目录
- controller解释：
  控制层中分为公共端控制层和用户端控制层，例如auth控制层中的userController为公共端，user目录下的UserUserController为用户端，所有用户端已做了登录校验
- vo解释：
  vo为前端展示对象，vo中的字段需要编写对应的注解，以便前端人员可以直接使用swagger对接，无关字段不需要返回
- dto解释：
  dto为数据传输对象，dto中的字段需要编写对应的注解。
