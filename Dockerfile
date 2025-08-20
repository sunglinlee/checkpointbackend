# ---- 第一階段：建置階段 (Build Stage) ----
# 使用官方的 Maven 映像檔作為建置環境，包含 JDK 17
FROM maven:3.8-openjdk-17 AS build

# 設定工作目錄
WORKDIR /app

# 複製 pom.xml 檔案，先下載依賴套件
# 這樣做可以充分利用 Docker 層緩存，當 pom.xml 沒變更時可以跳過依賴下載
COPY pom.xml .

# 下載依賴套件並緩存
# 使用 --batch-mode 避免不必要的輸出，--update-snapshots 確保快照版本更新
RUN mvn dependency:go-offline --batch-mode --update-snapshots

# 複製整個專案的原始碼
# 將這步驟放在依賴下載之後，這樣當程式碼變更但依賴沒變時，可以重用依賴層
COPY src ./src

# 複製其他可能影響建置的文件
COPY . .

# 執行打包指令，產生可執行的 JAR 檔
# 添加更多優化參數
RUN mvn package -DskipTests \
    --batch-mode \
    --update-snapshots \
    -Dspring-boot.repackage.skip=false \
    -Dmaven.compile.fork=true

# ---- 第二階段：正式運行階段 (Runtime Stage) ----
# 使用一個更輕量的 JRE 映像檔來運行應用程式
FROM eclipse-temurin:17-jre-focal

# 建立非 root 使用者以提升安全性
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 設定工作目錄
WORKDIR /app

# 從第一階段中複製打包好的 JAR 檔
# 使用更具體的路徑模式來確保複製正確的檔案
COPY --from=build /app/target/*.jar app.jar

# 變更檔案擁有者為非 root 使用者
RUN chown appuser:appuser app.jar

# 切換到非 root 使用者
USER appuser

# 設定環境變數
ENV PORT=8080
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# 健康檢查端點（如果您的應用有的話）
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT}/actuator/health || exit 1

# 暴露端口
EXPOSE ${PORT}

# 設定容器啟動指令
# 使用環境變數來允許運行時調整 JVM 參數
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]