# ---- 第一階段：建置階段 (Build Stage) ----
# 使用官方的 Maven 映像檔作為建置環境，包含 JDK 17
FROM maven:3.8-openjdk-17 AS build

# 設定工作目錄
WORKDIR /app

# 複製 pom.xml 檔案，先下載依賴套件
COPY pom.xml .
RUN mvn dependency:go-offline

# 複製整個專案的原始碼
COPY src ./src

# 執行打包指令，產生可執行的 JAR 檔
# -DskipTests 會跳過測試，以加速 CI/CD 流程
RUN mvn package -DskipTests

# ---- 第二階段：正式運行階段 (Runtime Stage) ----
# 使用一個更輕量的 JRE 映像檔來運行應用程式，讓最終映像檔更小、更安全
FROM amazoncorretto:17-jre-slim

# 設定工作目錄
WORKDIR /app

# 從第一階段 (build) 中，將打包好的 JAR 檔複製到正式環境
COPY --from=build /app/target/*.jar app.jar

# 設定環境變數 PORT，讓應用程式監聽 Cloud Run 指定的端口
# Cloud Run 會自動注入這個環境變數，您的應用程式需要讀取它
ENV PORT 8080

# 設定容器啟動時要執行的指令
# 啟動 Java 應用程式
ENTRYPOINT ["java", "-jar", "app.jar"]