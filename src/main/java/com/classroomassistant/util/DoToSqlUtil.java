package com.classroomassistant.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 根据model实体类DO生成建表SQL
 * 注意：生成的SQL会有一定出入
 * @author zlin
 * @date 20200919
 */
public class DoToSqlUtil {

    public static void main(String[] args) {
        // 1.将本类放在待转换的项目中
        // 2.执行前先配置如下信息
        // 3.执行此方法
        // 需转换为SQL的实体类包路径
        String packageName = "com.classroomassistant.pojo";
        // 待转换的实体类是否都有int类型自动递增的"id"字段为主键，若配置为true将以id生成自增主键，若配置为false，则转换后的SQL无主键
        boolean idKey = false;
        // 生成结果sql文路径
        String filePath = "../result.sql";

        generate(packageName, filePath, idKey);
    }

    private static void generate(String packageName, String filePath, boolean idKey) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("\nSET NAMES utf8mb4;\n").append("SET FOREIGN_KEY_CHECKS = 0;\n");
        // 通过包名生成SQL
        sqlBuilder.append(generateSql(packageName, idKey));
        sqlBuilder.append("\nSET FOREIGN_KEY_CHECKS = 1;\n");
        System.out.println(sqlBuilder.toString());
        sql2File(sqlBuilder.toString(), filePath);
    }

    /**
     * 根据实体类生成建表语句
     */
    private static String generateSql(String packageName, boolean idKey) {
        StringBuilder sb = new StringBuilder();
        String targetPath = getTargetPathByPackageName(packageName);
        File targetDir = new File(targetPath);
        if (targetDir.exists()) {
            File[] files = targetDir.listFiles();
            if (null != files && files.length > 0) {
                for (File file : files) {
                    String fileFullName = file.getName();
                    if (file.isDirectory()) {
                        sb.append(generateSql(packageName + "." + file.getName(), idKey));
                        continue;
                    }
                    if (file.isFile() && fileFullName.endsWith(".class")) {
                        String simpleName = fileFullName.split("\\.")[0];
                        Class<?> clazz;
                        try {
                            clazz = Class.forName(packageName.replace("/", "\\.") + "." +  simpleName);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            continue;
                        }
                        Field[] fields = clazz.getDeclaredFields();
                        if (fields.length < 1) {
                            continue;
                        }
                        String tableName = simpleNameToTableName(simpleName);
                        StringBuilder column = new StringBuilder();
                        for (Field field : fields) {
                            column.append("\n  `").append(fieldNameToTableName(field.getName())).append("` ");
                            if (idKey && fieldIsIntId(field)) {
                                column.append("int(11) NOT NULL AUTO_INCREMENT").append(",");
                                continue;
                            }
                            column.append(TypeSqlEnum.getSqlByClass(field.getType())).append(",");
                        }
                        sb.append("\nDROP TABLE IF EXISTS `").append(tableName).append("`;")
                                .append("\nCREATE TABLE `").append(tableName).append("`  (");
                        if (idKey) {
                            sb.append(column)
                                    .append("\n  PRIMARY KEY (`id`) USING BTREE")
                                    .append("\n) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;\n");
                        }else {
                            String columnStr = column.toString();
                            sb.append(columnStr, 0, columnStr.length() - 1)
                                    .append("\n) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;\n");
                        }
                    }
                }
            }else {
                System.out.println("路径下无文件，path：" + targetPath);
                return "";
            }
        }else {
            System.out.println("路径不存在，path：" + targetPath);
            return "";
        }
        return sb.toString();
    }

    private static boolean fieldIsIntId(Field field) {
        return "id".equals(field.getName()) &&
                (field.getType().equals(Integer.class) || field.getType().equals(int.class));
    }

    private static String getTargetPathByPackageName(String packageName) {
        String curFilePath = DoToSqlUtil.class.getResource("").getPath();
        return curFilePath.substring(0, curFilePath.indexOf(packageName.split("\\.")[0])) + packageName.replaceAll("\\.", "/");
    }

    private static String simpleNameToTableName(String className) {
        return ("t_" + fieldNameToTableName(className));
    }

    private static String fieldNameToTableName(String fieldName) {
        StringBuilder sb = new StringBuilder();
        char[] chars = fieldName.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (i > 0 && isUpperCase(c) && isLowerCase(chars[i-1])) {
                sb.append("_");
            }
            sb.append(c);
        }
        return sb.toString().toLowerCase();
    }

    private static boolean isUpperCase(char c) {
        return c >='A' && c <= 'Z';
    }

    private static boolean isLowerCase(char c) {
        return c >='a' && c <= 'z';
    }

    /**
     * sql写入文件
     */
    private static void sql2File(String sql, String path) {
        byte[] sourceByte = sql.getBytes();
        try {
            Files.deleteIfExists(new File(path).toPath());
            Files.write(new File(path).toPath(), sourceByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum TypeSqlEnum {

        /**
         * 属性类型对应SQL
         */
        INTEGER(Integer.class, "int(11) NULL DEFAULT NULL"),
        INT(int.class, "int(11) NULL DEFAULT NULL"),
        DOUBLE(Double.class, "decimal(10, 2) NULL DEFAULT NULL"),
        Double(double.class, "decimal(10, 2) NULL DEFAULT NULL"),
        STRING(String.class, "varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL"),
        DATE(Date.class, "datetime(0) NULL DEFAULT NULL"),
        Date(Timestamp.class, "datetime(0) NULL DEFAULT NULL"),
        BOOLEAN(Boolean.class, "tinyint(1) NULL DEFAULT NULL"),
        Boolean(boolean.class, "tinyint(1) NULL DEFAULT NULL");

        private Class clazz;

        private String sql;

        TypeSqlEnum(Class clazz, String sql){
            this.clazz = clazz;
            this.sql = sql;
        }

        public static String getSqlByClass(Class clazz) {
            for (TypeSqlEnum typeSql : TypeSqlEnum.values()) {
                if (typeSql.getClazz().equals(clazz)) {
                    return typeSql.getSql();
                }
            }
            return STRING.getSql();
        }

        public Class getClazz() {
            return clazz;
        }

        public String getSql() {
            return sql;
        }
    }
}
