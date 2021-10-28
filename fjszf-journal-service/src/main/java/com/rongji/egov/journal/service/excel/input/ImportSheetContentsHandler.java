package com.rongji.egov.journal.service.excel.input;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * rowNum >= 0
 * colNum >= 0
 */
public class ImportSheetContentsHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
    private int rowNum = -1;
    private Map<String, String> cells;

    private final Function<Integer, Boolean> filter;
    private final BiConsumer<Integer, Map<String, String>> action;

    public ImportSheetContentsHandler(Function<Integer, Boolean> filter, BiConsumer<Integer, Map<String, String>> action) {
        this.filter = filter;
        this.action = action;
    }

    @Override
    public void startRow(int rowNum) {
        cells = filter.apply(this.rowNum = rowNum) ? new HashMap<>() : null;
    }

    @Override
    public void endRow(int rowNum) {
        if (cells != null) {
            action.accept(this.rowNum, cells);
        }
        this.rowNum = -1;
        cells = null;
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
        if (cells != null) {
            cells.put(cellReference, formattedValue);
        }
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {

    }

    public static String getCellStringValue(int rowNum, int colNum, Map<String, String> cells) {
        return cells.get(toTwentySix(colNum + 1) + (rowNum + 1));
    }

    public static String toTwentySix(int n) {
        if (n == 0) {
            return "0";
        }
        if (n < 0) {
            return "-" + toTwentySix(-n);
        }
        n--;
        StringBuilder s = new StringBuilder();
        int offset = 0;
        do {
            int m = n % 26;
            s.insert(0, (char) (m + offset + 'A'));
            n = (n - m) / 26;
            offset = -1;
        } while (n > 0);
        return s.toString();
    }
}
