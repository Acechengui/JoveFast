package com.jovefast.common.core.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.IdUtil;
import com.jovefast.common.core.exception.CheckedException;
import org.apache.commons.lang3.ArrayUtils;
import com.jovefast.common.core.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理工具类
 *
 * @author Acechengui
 */
public class FileUtils {
    /**
     * 字符常量：斜杠 {@code '/'}
     */
    public static final char SLASH = '/';

    /**
     * 字符常量：反斜杠 {@code '\\'}
     */
    public static final char BACKSLASH = '\\';

    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (String child : Objects.requireNonNull(children)) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 删除文件
     *
     * @param filePathAndName 指定得路径
     */
    public static void delFile(String filePathAndName) {
        try {
            java.io.File delFile = new java.io.File(filePathAndName);
            delFile.delete();
        } catch (Exception e) {
            throw new CheckedException("文件删除异常");
        }
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 检查文件是否可下载
     *
     * @param resource 需要下载的文件
     * @return true 正常 false 非法
     */
    public static boolean checkAllowDownload(String resource) {
        // 禁止目录上跳级别
        if (StringUtils.contains(resource, "..")) {
            return false;
        }

        // 检查允许下载的文件规则
        return ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FileTypeUtils.getFileType(resource));
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * 返回文件名
     *
     * @param filePath 文件
     * @return 文件名
     */
    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (isFileSeparator(filePath.charAt(len - 1))) {
            // 以分隔符结尾的去掉结尾分隔符
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }

        return filePath.substring(begin, len);
    }

    /**
     * 是否为Windows或者Linux（Unix）文件分隔符<br>
     * Windows平台下分隔符为\，Linux（Unix）为/
     *
     * @param c 字符
     * @return 是否为Windows或者Linux（Unix）文件分隔符
     */
    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     * @return
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {
        String percentEncodedFileName = percentEncode(realFileName);

        String contentDispositionValue = "attachment; filename=" +
                percentEncodedFileName +
                ";" +
                "filename*=" +
                "utf-8''" +
                percentEncodedFileName;

        response.setHeader("Content-disposition", contentDispositionValue);
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    /**
     * MultipartFile 文件上传
     *
     * @param file 文件
     * @param type 文件类型
     * @return 路径
     */
    public static String multipartFile(MultipartFile file, String type) throws IOException {
        //上传到 target 文件夹
//        final String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload/";
        //上传到当前项目根目录
        final String filePath = System.getProperty("user.dir") + "/upload/";
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName != null ? fileName.substring(fileName.lastIndexOf(".")) : null;
        if (suffixName == null) {
            return null;
        }

        boolean is = false;
        String[] attr = type.split("\\|");
        for (String s : attr) {
            if (suffixName.contains(s)) {
                is = true;
                break;
            }
        }
        if (!is) {
            return null;
        }

        //创建唯一文件名 + 路径
        String fileNewName = filePath + IdUtil.simpleUUID() + suffixName;

        // 文件上传后的路径
        File dest = new File(fileNewName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        return fileNewName;
    }

    /**
     * 设置文件导出浏览器响应头
     *
     * @param response
     * @param fileName
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 循环读取文件夹下的文件
     *
     * @param path 文件夹完整绝对路径
     * @return 文件路径集合
     */
    public static synchronized List<String> forFile(String path) {
        //记录多文件的路径
        List<String> list = new ArrayList<>();
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                list.addAll(forFile(file.getPath()));
            } else {
                //获取路径
                list.add(file.getPath());
            }
        }
        return list;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param path 被删除目录的文件路径
     * @return 目录删除成功返回true, 否则返回false
     */
    public static boolean delAllFile(String path, boolean type) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (String s : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + s);
            } else {
                temp = new File(path + File.separator + s);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                // 先删除文件夹里面的
                delAllFile(path + "/" + s, type);
                // 再删除空文件夹
                if (type) {
                    delFolder(path + "/" + s);
                }
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹完整绝对路径
     */
    public static void delFolder(String folderPath) {
        try {
            // 删除完里面所有内容
            delAllFile(folderPath, true);
            java.io.File myFilePath = new java.io.File(folderPath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据提供的目录路径判断，不存在就创建
     *
     * @param directory 目录路径
     * @return
     */
    public static boolean createDirectory(String directory) {

        //是否成功
        boolean start = false;

        //成功数量
        int okCount = 0;

        //非空判断
        if (directory != null && directory.length() > 0) {

            String directoryRegex = "[a-zA-Z]:(\\\\[\\w\\u4e00-\\u9fa5\\s]+)+";
            Pattern pattern = Pattern.compile(directoryRegex);
            Matcher matcher = pattern.matcher(directory);

            //正则表达式验证路径是否合法
            if (matcher.matches()) {

                //获取路径中的盘符
                String drive = directory.substring(0, 3);

                //获取路径中的子目录
                String[] directoryArray = directory.substring(3).split("\\\\");

                StringBuilder sbURL = new StringBuilder(drive);

                //循环子路径子目录
                for (int i = 0; i < directoryArray.length; i++) {

                    //拼接子目录
                    sbURL.append(i > 0 ? "\\" : "");
                    sbURL.append(directoryArray[i]);

                    //如果不存在子目录就创建
                    File file = new File(sbURL.toString());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    okCount++;
                }

                //更新状态
                start = okCount == directoryArray.length;
            }
        }
        return start;
    }

}
