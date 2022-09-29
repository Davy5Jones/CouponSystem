package db;

import beans.Category;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kobis on 01 Sep, 2022
 */
public class DatabaseManager {


    private static final String CREATE_SCHEMA = "CREATE SCHEMA `java-151-cs1`";
    private static final String DROP_SCHEMA = "drop schema `java-151-cs1`";
    private static final String CREATE_TABLE_COMPANIES = "CREATE TABLE `java-151-cs1`.`companies` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `Name` VARCHAR(45) NULL unique,\n" +
            "  `Email` VARCHAR(45) NULL UNIQUE,\n" +
            "  `Password` VARCHAR(45) NULL,\n" +
            "  PRIMARY KEY (`id`));\n";

    private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE `java-151-cs1`.`customers` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `FIRST_NAME` VARCHAR(45) NULL,\n" +
            "  `LAST_NAME` VARCHAR(45) NULL,\n" +
            "  `EMAIL` VARCHAR(45) NULL UNIQUE,\n" +
            "  `Password` VARCHAR(45) NULL,\n" +
            "  PRIMARY KEY (`id`));\n";
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE `java-151-cs1`.`categories` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(45) NULL,\n" +
            "  PRIMARY KEY (`id`));\n";
    private static final String CREATE_TABLE_COUPONS = "CREATE TABLE `java-151-cs1`.`coupons` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `COMPANY_ID` INT NULL,\n" +
            "  `CATEGORY_ID` INT NULL,\n" +
            "  `TITLE` VARCHAR(45) NULL,\n" +
            "  `DESCRIPTION` VARCHAR(45) NULL,\n" +
            "  `START_DATE` DATE NULL,\n" +
            "  `END_DATE` DATE NULL,\n" +
            "  `AMOUNT` INT NULL,\n" +
            "  `price` DOUBLE NULL,\n" +
            "  `image` VARCHAR(45) NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  CONSTRAINT `COMPANY_ID`\n" +
            "    FOREIGN KEY (`COMPANY_ID`)\n" +
            "    REFERENCES `java-151-cs1`.`companies` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `CATEGORY_ID`\n" +
            "    FOREIGN KEY (`CATEGORY_ID`)\n" +
            "    REFERENCES `java-151-cs1`.`categories` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);";
    private static final String CREATE_CUSTOMER_VS_COUPONS = "CREATE TABLE `java-151-cs1`.`customers_vs_coupons` (\n" +
            "  `CUSTOMER_ID` INT NOT NULL,\n" +
            "  `COUPON_ID` INT NOT NULL,\n" +
            "  PRIMARY KEY (`CUSTOMER_ID`, `COUPON_ID`),\n" +
            "  INDEX `COUPON_ID_idx` (`COUPON_ID` ASC) VISIBLE,\n" +
            "  CONSTRAINT `CUSTOMER_ID`\n" +
            "    FOREIGN KEY (`CUSTOMER_ID`)\n" +
            "    REFERENCES `java-151-cs1`.`customers` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `COUPON_ID`\n" +
            "    FOREIGN KEY (`COUPON_ID`)\n" +
            "    REFERENCES `java-151-cs1`.`coupons` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);\n";
    private static final String newCatQuery = "INSERT INTO `java-151-cs1`.`categories` (`name`) VALUES (?);\n";

    public static void dropAndCreateStrategy() throws SQLException {
        JDBCUtils.runQuery(DROP_SCHEMA);
        JDBCUtils.runQuery(CREATE_SCHEMA);
        JDBCUtils.runQuery(CREATE_TABLE_COMPANIES);
        JDBCUtils.runQuery(CREATE_TABLE_CUSTOMERS);
        JDBCUtils.runQuery(CREATE_TABLE_CATEGORIES);
        JDBCUtils.runQuery(CREATE_TABLE_COUPONS);
        for (Category cat : Category.values()) {
            Map<Integer, Object> map = new HashMap<>();
            map.put(1, cat.name());
            JDBCUtils.runQuery(newCatQuery, map);
        }
        JDBCUtils.runQuery(CREATE_CUSTOMER_VS_COUPONS);
    }
}
