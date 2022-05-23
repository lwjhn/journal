package com.rongji.egov.journal.service.utils;

import com.rongji.egov.mybatis.base.utils.AutoCloseableBase;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class FileOperator {
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < Objects.requireNonNull(children).length; i++) {
                if (!deleteDir(new File(dir, children[i]))) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static boolean matchFilePath(String sPath) {
        return sPath.matches("^([A-Za-z]:)?[/\\\\][^:?\"><*]*");
    }

    /**
     * 文件拷贝
     *
     * @param src_name 源文件名
     * @param des_name 目标文件名
     */
    public static boolean copyFile(String src_name, String des_name) {
        InputStream is = null;
        OutputStream os = null;
        try {
            return copyStream(is = new FileInputStream(src_name), os = new FileOutputStream(des_name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(is, os);
        }
    }

    public static boolean copyStream(InputStream is, OutputStream os) {
        try {
            byte[] buff = new byte[1024];
            int rc;
            while ((rc = is.read(buff, 0, 1024)) > 0) {
                os.write(buff, 0, rc);
                os.flush();
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(os);
        }
    }

    @SuppressWarnings("all")
    public static boolean copyDir(String src_name, String des_name) {
        File src_file = new File(src_name);
        if (src_file.isFile()) {
            return copyFile(src_name, des_name);
        }

        File des_file = new File(des_name);
        if (!des_file.exists()) {
            des_file.mkdirs();
        }

        File[] fs = src_file.listFiles();
        for (File f : fs) {
            if (f.isFile()) {
                if (!copyFile(f.getPath(), des_name + "\\" + f.getName())) {
                    return false;
                }
            } else if (f.isDirectory()) {
                if (!copyDir(f.getPath(), des_name + "\\" + f.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 文件拷贝
     *
     * @param src_name 源文件名
     * @param des_name 目标文件名
     */
    public static boolean copyFileByChannel(String src_name, String des_name) {
        FileInputStream is = null;
        FileOutputStream os = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            is = new FileInputStream(src_name);
            os = new FileOutputStream(des_name);
            in = is.getChannel();
            out = os.getChannel();

            in.transferTo(0, in.size(), out);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(in, out, is, os);
        }
    }

    @SuppressWarnings("all")
    public static boolean copyDirByChannel(String src_name, String des_name) {
        File src_file = new File(src_name);
        if (src_file.isFile()) {
            return copyFileByChannel(src_name, des_name);
        }

        File des_file = new File(des_name);
        if (!des_file.exists()) {
            des_file.mkdirs();
        }

        File[] fs = src_file.listFiles();
        for (File f : fs) {
            if (f.isFile()) {
                if (!copyFileByChannel(f.getPath(), des_name + "\\" + f.getName())) {
                    return false;
                }
            } else if (f.isDirectory()) {
                if (!copyDirByChannel(f.getPath(), des_name + "\\" + f.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean newFile(String filename, InputStream is) {
        return newFile(new File(filename), is);
    }

    public static boolean newFile(String filename, byte[] buff) {
        return newFile(new File(filename), buff);
    }

    public static boolean newFile(File file, byte[] buff) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(buff);
            os.flush();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(os);
        }
    }

    public static boolean newFile(File file, InputStream is) {
        if (!file.exists() || !file.isFile()) return false;
        OutputStream os = null;
        try {
            return copyStream(is, os = new FileOutputStream(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(os);
        }
    }

    public static final String FileDeliRegex = "[/\\\\]+";
    public static final Pattern FileRootPattern = Pattern.compile("^([A-Za-z]:)?([/\\\\].*)");
    public static final String FileFolderPathRegex = "(^|[/\\\\])[^/\\\\]+$";
    public static final String FileNameRegex = ".*[/\\\\]";
    public static final String FileAliasRegex = "(.*[/\\\\])|(\\.[0-9a-z]+$)";
    public static final Pattern FileNamePattern = Pattern.compile("[`~!@#$%^&*<>?:'\"{},;/|\\\\]");

    public static boolean isInValidFileName(String name){
        return FileNamePattern.matcher(name).find();
    }

    public static boolean isValidFileName(String name){
        return !isInValidFileName(name);
    }

    public static String getValidFileName(String base) {
        return base == null ? "" : base.replaceAll("[`~!@#$%^&*<>?:'\"{},;/|\\\\]", "");
    }

    public static String getValidFilePath(String base) {
        return base == null ? "" : base.replaceAll("[`~!@#$%^&*<>?:'\"{},;|]", "").replaceAll(FileDeliRegex, "/");
    }

    public static String getAvailablePath(String... base) {
        return getAvailablePath(false, base);
    }

    public static String getAvailablePath(boolean filterDiagonal, String... elements) {
        StringJoiner joiner = new StringJoiner("/");
        String buf;
        Matcher matcher = FileRootPattern.matcher(buf = elements[0].replaceAll(FileDeliRegex, "/"));
        if (matcher.find()) {
            if (matcher.group(1) != null) joiner.add(matcher.group(1));
            buf = matcher.group(2);
        }
        joiner.add((filterDiagonal ? "/" + getValidFileName(buf) : getValidFilePath(buf))
                .replaceAll("[/\\\\]+$", ""));
        for (int index = 1; index < elements.length; index++) {
            buf = (filterDiagonal ? getValidFileName(elements[index]) : getValidFilePath(elements[index]))
                    .replaceAll("^[/\\\\]+|[/\\\\]+$", "");
            if ("".equals(buf)) continue;
            joiner.add(buf);
        }
        return joiner.toString();
    }

    public static String getFileFolderByRegex(String path) {
        return path.replaceAll(FileFolderPathRegex, "").replaceAll(FileDeliRegex, "/");
    }

    public static String getFileNameByRegex(String path) {
        return getValidFileName(path.replaceAll(FileNameRegex, ""));
    }

    public static String getFileAliasByRegex(String path) {
        return getValidFileName(path.replaceAll(FileAliasRegex, ""));
    }
}

