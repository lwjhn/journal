package com.rongji.egov.journal.service.excel.input;

import com.rongji.egov.mybatis.base.sql.SQLCriteria;
import com.rongji.egov.mybatis.base.utils.AutoCloseableBase;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ImportExecutor {
    private final ImportConfig importConfig;
    private final Map<String, SQLCriteria> criteriaMap;
    private final Class<?> model;

    public ImportExecutor(ImportConfig importConfig) {
        this.importConfig = importConfig;
        criteriaMap = importConfig.getValues();
        model = importConfig.getModel();
    }

    public int action(Workbook workbook, BiFunction<Class<?>, Map<String, Object>, Integer> callback) {
        Sheet sheet = workbook.getSheetAt(importConfig.getSheet());
        int first = sheet.getFirstRowNum();
        int last = sheet.getLastRowNum();
        int begin = importConfig.getBeginRow();
        int end = importConfig.getEndRow();
        int count = 0;
        if (begin > last || begin > end) {
            return count;
        }
        for (int index = Math.max(begin, first); index <= Math.min(last, end); index++) {
            count += action(sheet.getRow(index), callback);
        }
        return count;
    }

    public int action(Row row, BiFunction<Class<?>, Map<String, Object>, Integer> callback) {
        return action(index -> {
            Cell cell = row.getCell(index);
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        }, callback);
    }

    public int action(SheetXMLExecutor executor, BiFunction<Class<?>, Map<String, Object>, Integer> callback) {
        InputStream inputStream = null;
        final int[] count = {0};
        try {
            inputStream = executor.getSheet(importConfig.getSheet());

            final int begin = importConfig.getBeginRow();
            final int end = importConfig.getEndRow();
            executor.parse(inputStream, new ImportSheetContentsHandler(row -> {
                if (row > end) {
                    throw new SheetBreakException();
                }
                return row >= begin;
            }, (rowNum, cells) -> count[0] += action(
                    colNum -> ImportSheetContentsHandler.getCellStringValue(rowNum, colNum, cells), callback)
            ));
            return count[0];
        } catch (SheetBreakException e) {
            return count[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(inputStream);
        }
    }

    public int action(Function<Integer, String> getStringCellValue, BiFunction<Class<?>, Map<String, Object>, Integer> callback) {
        Map<String, Object> values = new HashMap<>();
        SQLCriteria criteria;
        List<Object> value;
        for (Map.Entry<String, SQLCriteria> entry : criteriaMap.entrySet()) {
            criteria = entry.getValue();
            if ((value = criteria.getValue()) != null) {
                value = new ArrayList<>();
                for (Object obj : criteria.getValue()) {
                    value.add(obj instanceof Integer
                            ? getStringCellValue.apply((Integer) obj) : obj);
                }
            }
            values.put(entry.getKey(), criteria.getExpression() == null
                    ? (CollectionUtils.isEmpty(value) ? null : value.get(0))
                    : new SQLCriteria(criteria.getExpression(), value)
            );
        }
        return callback.apply(model, values);
    }
}
