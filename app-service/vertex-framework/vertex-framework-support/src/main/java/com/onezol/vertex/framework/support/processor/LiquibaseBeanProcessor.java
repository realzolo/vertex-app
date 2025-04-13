//package com.onezol.vertex.framework.support.processor;
//
//import com.onezol.vertex.framework.common.mvc.mapper.CommonMapper;
//import com.onezol.vertex.framework.common.util.ResourceUtils;
//import com.onezol.vertex.framework.common.util.StringUtils;
//import liquibase.integration.spring.SpringLiquibase;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.enumeration.Autowired;
//import org.springframework.beans.factory.enumeration.Value;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
//import org.springframework.context.enumeration.DependsOn;
//import org.springframework.jdbc.BadSqlGrammarException;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//@DependsOn({"sqlSessionTemplate"})
//public class LiquibaseBeanProcessor implements BeanPostProcessor {
//
//    @Value("${application.database.init-sql-file:}")
//    private String initSqlFilePath;
////
//    @Autowired
//    private CommonMapper commonMapper;
//
//    @Override
//    @SuppressWarnings("ALL")
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if (bean instanceof SpringLiquibase) {
//            // 初始化数据库表
//            try {
//                this.initDatabaseTable();
//            } catch (IOException e) {
//                log.error("初始化数据库表失败", e);
//            }
//            return bean;
//        }
//        return bean;
//    }
//
//
//    @Override
//    @SuppressWarnings("ALL")
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
//    }
//
//    /**
//     * 初始化数据库表<br>
//     * 读取SQL文件，执行SQL语句，创建表<br>
//     * 不使用Liquibase提供的初始化功能是因为: 后续在initSqlFile.sql中新增的表，Liquibase因为存在执行记录，不会再次创建
//     */
//    private void initDatabaseTable() throws IOException {
//        if (StringUtils.isBlank(initSqlFilePath)) return;
//
//        log.info("开始初始化数据库, 读取SQL文件: {}", initSqlFilePath);
//
//        String sqlText = ResourceUtils.readAsText(initSqlFilePath);
//        Set<String> createTableStatements = Arrays.stream(sqlText.split(";"))
//                .map(String::trim)
//                .filter(sql -> StringUtils.isNotBlank(sql.trim()))
//                .collect(Collectors.toSet());
//        log.info("共读取到 {} 条SQL语句", createTableStatements.size());
//
//        int executeCount = 0;
//        for (String createTableStatement : createTableStatements) {
//            if (StringUtils.isBlank(createTableStatement)) continue;
//
//            // 移除注释
//            createTableStatement = this.removeSQLComments(createTableStatement);
//
//            // 获取表名
//            String tableName = StringUtils.substringBetween(createTableStatement, "CREATE TABLE ", "(");
//            tableName = tableName.trim();
//
//            // 检查表是否存在
//            if (commonMapper.checkTableExistence(tableName) > 0) {
//                log.info("表 {} 已存在, 无需处理, 自动跳过", tableName);
//                continue;
//            }
//
//            // 执行SQL
//            try {
//                commonMapper.execute(createTableStatement);
//                executeCount++;
//                log.info("创建表 {} 成功", tableName);
//                log.debug("执行SQL: \n{}", createTableStatement);
//            } catch (BadSqlGrammarException e) {
//                if (e.getMessage().contains("already exists")) {
//                    log.info("检测到表 {} 已存在, 无需处理, 自动跳过", tableName);
//                } else {
//                    throw e;
//                }
//            }
//        }
//
//        log.info("数据库初始化完成, 共扫描到 {} 张表， 其中 {} 张表已创建，新增 {} 张表", createTableStatements.size(), createTableStatements.size() - executeCount, executeCount);
//    }
//
//    /**
//     * 移除SQL注释
//     *
//     * @param sql SQL
//     * @return 移除注释后的SQL
//     */
//    private String removeSQLComments(String sql) {
//        if (StringUtils.isBlank(sql)) return sql;
//
//        // SQL 注释可能为 # 或者 -- 或者 /* */
//        // 移除 /* */ 类型的注释
//        sql = sql.replaceAll("/\\*.*?\\*/", "");
//        // 移除 # 类型的注释
//        sql = sql.replaceAll("#.*?\\n", "");
//        // 移除 -- 类型的注释
//        sql = sql.replaceAll("--.*?\\n", "");
//
//        return sql;
//    }
//}
