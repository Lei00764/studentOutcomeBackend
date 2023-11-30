# Student Outcome Backend

同济大学 2023 年软件工程课程项目

### File structure

```text
.
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src 
│   ├── main  # source code 
│   └── test  # unit tests
└── target
    ├── classes
    └── generated-sources
```

## src/main

流程：

1. 在 pom.xml 中引入 MyBatis 依赖 （只需进行一次）
2. 在 entity 中定义数据库表对应的类
3. 在 mapper 中定义数据库操作的接口
5. 在 controller 中实现接口
6. 在 service 中实现业务逻辑 （包括接口及具体的实现）

### Entity

定义表示数据库表的类

### Controller

处理简单逻辑，调用 service 层

### Service

Service层 = Service接口 + ServiceImpl 实现类

### resources

resources/mapper/xxx.xml 里面写动态 SQL

## 快捷键

去除无用的 import：`Ctrl + Alt + O`

## 封装说明

### 统一返回处理

ResponseVO 里面封装了返回的数据，包括状态码和数据

### 统一异常处理

通过 BusinessException 调用

## Code

```markdown
| code | description |
| ---- | ----------- |
| 200  |    请求成功  |
| 404  |    请求地址不存在  |
| 600  |    请求参数错误  |
| 601  |    信息已经存在  |
| 500  |   服务器返回错误，请联系关系员   |
```
