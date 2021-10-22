package com.rongji.egov.journal.service.excel.input;

import com.rongji.egov.mybatis.base.sql.SQLCriteria;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BiFunction;

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
            count+=action(sheet.getRow(index), callback);
        }
        return count;
    }

    public int action(Row row, BiFunction<Class<?>, Map<String, Object>, Integer> callback) {
        Map<String, Object> values = new HashMap<>();
        SQLCriteria criteria;
        List<Object> value;
        Cell cell;
        for (Map.Entry<String, SQLCriteria> entry : criteriaMap.entrySet()) {
            criteria = entry.getValue();
            if ((value = criteria.getValue()) != null) {
                value = new ArrayList<>();
                for (Object obj : criteria.getValue()) {
                    if (obj instanceof Integer) {
                        cell = row.getCell((Integer) obj);
                        cell.setCellType(CellType.STRING);
                        value.add(cell.getStringCellValue());
                    } else {
                        value.add(obj);
                    }
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
