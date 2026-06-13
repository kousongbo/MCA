# MyCleanApp (MCA)

一个现代化的 Android 应用项目，采用 Clean Architecture 设计理念，使用 Java 和 Gradle Kotlin DSL 构建。

## 📋 目录

- [项目概述](#项目概述)
- [技术栈](#技术栈)
- [系统要求](#系统要求)
- [快速开始](#快速开始)
- [项目结构](#项目结构)
- [依赖管理](#依赖管理)
- [构建配置](#构建配置)
- [测试](#测试)
- [开发指南](#开发指南)
- [贡献指南](#贡献指南)

## 项目概述

**MyCleanApp** 是一个遵循 Clean Architecture 原则的 Android 应用框架。该项目提供了一个良好的基础结构，适合开发现代化、可维护性强的 Android 应用。

### 主要特性

- ✅ **现代化架构**: 采用 Clean Architecture 分层设计
- ✅ **Kotlin DSL**: 使用 Gradle Kotlin DSL 进行构建配置
- ✅ **版本管理**: 集中式依赖版本管理 (libs.versions.toml)
- ✅ **性能优化**: 启用 Gradle 配置缓存提升构建速度
- ✅ **AndroidX 支持**: 使用最新的 AndroidX 库
- ✅ **Material Design**: 集成 Material Design 3 支持

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| **编程语言** | Java | Android 官方推荐语言 |
| **Build 工具** | Gradle 9.4.1 | 使用官方 Gradle 包装器 |
| **AGP** | 9.2.1 | Android Gradle Plugin |
| **Compile SDK** | 36 | 基于 Android 15 |
| **Min SDK** | 24 | Android 7.0 (API 24) |
| **Target SDK** | 36 | Android 15 (API 36) |
| **Java 版本** | 11 | 编译和运行时版本 |
| **JDK** | 21 | 开发工具链 |

### 核心依赖

```
// UI 框架
- androidx.appcompat:appcompat:1.6.1
- com.google.android.material:material:1.10.0
- androidx.constraintlayout:constraintlayout:2.1.4
- androidx.activity:activity-ktx:1.8.0

// 测试
- junit:junit:4.13.2
- androidx.test.espresso:espresso-core:3.5.1
- androidx.test.ext:junit:1.1.5
```

## 系统要求

- **操作系统**: macOS 10.15+、Linux 或 Windows 10+
- **Java**: JDK 21 或更高版本
- **Android Studio**: Jellyfish (2023.3.1) 或更高版本
- **Android SDK**: 
  - Compile SDK 36
  - Build Tools 36.x.x
  - Min SDK 24

## 快速开始

### 1. 克隆仓库

```bash
git clone https://github.com/kousongbo/MCA.git
cd MCA
```

### 2. 配置开发环境

确保已安装 Android Studio 和 JDK 21：

```bash
# 检查 Java 版本
java -version

# 设置 JAVA_HOME (如果需要)
export JAVA_HOME=/path/to/jdk-21
```

### 3. 构建项目

```bash
# 使用 Gradle 包装器构建
./gradlew build

# 或在 Windows 上
gradlew.bat build
```

### 4. 运行应用

```bash
# 在模拟器或真机上运行
./gradlew installDebug

# 启动应用 (使用 adb)
adb shell am start -n com.example.mycleanapp/.MainActivity
```

### 5. 在 IDE 中打开

```bash
# Android Studio 会自动识别项目
# 使用 Android Studio 打开项目根目录
open -a "Android Studio" .
```

## 项目结构

```
MCA/
├── app/                                  # 应用模块
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/mycleanapp/
│   │   │   ├── res/                     # 资源文件
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                        # 单元测试
│   │   └── androidTest/                 # 集成测试
│   └── build.gradle.kts                 # 应用构建配置
│
├── gradle/
│   ├── wrapper/
│   │   ├── gradle-wrapper.jar           # Gradle 包装器
│   │   └── gradle-wrapper.properties    # Gradle 配置
│   ├── gradle-daemon-jvm.properties     # JVM 配置
│   └── libs.versions.toml               # 依赖版本目录
│
├── .idea/                               # IDE 配置 (Git 忽略)
├── build.gradle.kts                     # 顶级构建脚本
├── settings.gradle.kts                  # 项目设置
├── gradle.properties                    # 全局 Gradle 配置
├── gradlew                              # Linux/macOS 构建脚本
├── gradlew.bat                          # Windows 构建脚本
└── README.md                            # 本文件
```

## 依赖管理

项目使用 **TOML 格式的版本目录** (`gradle/libs.versions.toml`) 进行集中式依赖版本管理。

### 查看所有依赖

```bash
cat gradle/libs.versions.toml
```

### 添加新依赖

1. 在 `gradle/libs.versions.toml` 中添加版本和库引用：

```toml
[versions]
newLibVersion = "1.0.0"

[libraries]
new-lib = { group = "com.example", name = "lib", version.ref = "newLibVersion" }
```

2. 在 `app/build.gradle.kts` 中使用：

```kotlin
dependencies {
    implementation(libs.new.lib)
}
```

3. 同步 Gradle 配置。

## 构建配置

### 编译配置

```kotlin
android {
    namespace = "com.example.mycleanapp"
    compileSdk = 36                       // 基于 Android 15
    
    defaultConfig {
        applicationId = "com.example.mycleanapp"
        minSdk = 24                       // Android 7.0+
        targetSdk = 36                    // Android 15
        versionCode = 1
        versionName = "1.0"
    }
}
```

### Gradle 性能优化

项目启用了以下优化选项 (`gradle.properties`):

```properties
# 配置缓存 - 跳过配置阶段，加快构建速度
org.gradle.configuration-cache=true

# JVM 内存配置
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
```

## 测试

### 单元测试

```bash
# 运行所有单元测试
./gradlew test

# 运行特定测试
./gradlew test --tests="com.example.mycleanapp.ExampleUnitTest"
```

### 集成测试 (Instrumented Tests)

```bash
# 运行所有 Android 测试
./gradlew connectedAndroidTest

# 需要连接真机或启动模拟器
```

### 测试框架

- **单元测试**: JUnit 4
- **Android 测试**: Espresso 3.5.1
- **运行器**: AndroidJUnitRunner

## 开发指南

### 代码风格

本项目遵循 [Kotlin 官方代码规范](https://kotlinlang.org/docs/coding-conventions.html)。

### 提交规范

使用以下格式提交代码：

```
<type>(<scope>): <subject>

<body>

<footer>
```

**类型**:
- `feat`: 新功能
- `fix`: 问题修复
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建/工具链

**示例**:
```
feat(auth): 添加用户登录功能

实现了基于 JWT 的用户认证系统

Closes #123
```

### 调试建议

#### 启用 Gradle 调试输出

```bash
./gradlew build --debug
```

#### 查看依赖树

```bash
./gradlew dependencies
```

#### 分析构建性能

```bash
./gradlew build --profile
```

### 常见问题

**Q: 构建时报 "JAVA_HOME not set"**  
A: 设置 `JAVA_HOME` 环境变量指向 JDK 21 的安装目录。

**Q: Gradle 同步失败**  
A: 尝试以下步骤：
```bash
./gradlew clean
./gradlew --refresh-dependencies
```

**Q: 模拟器或真机无法连接**  
A: 检查 ADB 连接状态：
```bash
adb devices
```

## 贡献指南

我们欢迎所有形式的贡献！

### 如何贡献

1. **Fork** 本仓库
2. 创建功能分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'feat: add amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 开启 Pull Request

### 报告 Bug

如果发现 bug，请通过 [Issues](https://github.com/kousongbo/MCA/issues) 向我们报告。

请包含：
- bug 的详细描述
- 复现步骤
- 预期行为
- 实际行为
- 环境信息（Android 版本、设备型号等）

## 许可证

本项目采用 Apache License 2.0 许可证。详见 [LICENSE](LICENSE) 文件。

## 联系方式

- **作者**: kousongbo
- **Email**: 联系方式可通过 GitHub 个人资料获取
- **GitHub**: [@kousongbo](https://github.com/kousongbo)

---

**最后更新**: 2026 年 6 月 13 日

如有任何问题或建议，欢迎提交 Issue 或 Pull Request！
