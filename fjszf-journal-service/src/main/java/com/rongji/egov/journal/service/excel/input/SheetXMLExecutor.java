package com.rongji.egov.journal.service.excel.input;

import com.rongji.egov.mybatis.base.utils.AutoCloseableBase;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SheetXMLExecutor implements AutoCloseable {
    private final OPCPackage opcPackage;
    private final XSSFReader xssfReader;
    private final StylesTable stylesTable;
    private final ReadOnlySharedStringsTable strings;
    private final XMLReader sheetParser;

    public SheetXMLExecutor(String file, PackageAccess access) throws Exception {
        this(new File(file), access);
    }

    public SheetXMLExecutor(File file, PackageAccess access) throws Exception {
        opcPackage = OPCPackage.open(file, access);
        strings = new ReadOnlySharedStringsTable(opcPackage);
        xssfReader = new XSSFReader(opcPackage);
        stylesTable = xssfReader.getStylesTable();
        sheetParser = SAXHelper.newXMLReader();
    }

    public InputStream getSheet(int index) throws IOException, InvalidFormatException {
        return xssfReader.getSheet("rId" + (index + 1));
    }

    public XSSFReader.SheetIterator getSheetIterator() throws IOException, InvalidFormatException {
        return (XSSFReader.SheetIterator) xssfReader.getSheetsData();
    }

    public void parse(InputStream sheetStream, SheetContentsHandler handler) throws Exception {
        sheetParser.setContentHandler(new XSSFSheetXMLHandler(stylesTable, null,
                strings, handler, new DataFormatter(), false));
        sheetParser.parse(new InputSource(sheetStream));
    }

    @Override
    public void close() throws Exception {
        AutoCloseableBase.close(opcPackage);
    }
}
