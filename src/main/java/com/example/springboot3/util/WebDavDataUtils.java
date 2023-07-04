package com.example.springboot3.util;

import com.example.springboot3.constant.Constant;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;


public class WebDavDataUtils {

    public static List<String> list(CloseableHttpResponse response) throws IOException, ParserConfigurationException, SAXException {
        // 解析WebDAV XML响应
        Document document = getDocument(response);
        // 获取所有具有"D:href"的元素
        NodeList nodeList = getNodeList(document, Constant.DAV_TAG_D_HREF);
        List<String> result = new LinkedList<>();
        // 遍历元素列表并打印元素内容
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String href = node.getTextContent();
            String decodedUri = URLDecoder.decode(href, "UTF-8");
            decodedUri = decodedUri.replace("/dav/", "");
            if(!decodedUri.isEmpty() && !decodedUri.equals("")){
                result.add(decodedUri);
            }

        }
        return result;
    }

    public static void openFile(CloseableHttpResponse response, String fileType) throws IOException {
        try {
            // 在本地创建一个临时文件
            File tempFile = createTempFile(fileType);
            generateFile(tempFile, response);
            // 打开本地文件
            openFile(tempFile);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
    }

    public static File createTempFile(String fileType) throws IOException {
        return File.createTempFile("webdavfile", "." + fileType);
    }

    public static void openFile(File tempFile) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(tempFile);
            }
        }
    }

    public static Document getDocument(CloseableHttpResponse response) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document =null;
        try{
            document =  builder.parse(response.getEntity().getContent());
        }catch (Exception exception){
            System.out.println(response.getEntity().getContent().toString());
            System.out.println(exception.getMessage());
        }
        return document;
    }

    public static NodeList getNodeList(Document document, String tagName) {
        return document.getElementsByTagName(tagName);
    }

    public static void generateFile(File tempFile, CloseableHttpResponse response) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        InputStream inputStream = null;
        try {
            // 将WebDAV响应内容写入到本地文件中
            inputStream = generateInputStream(response);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {

        } finally {
            // 关闭输入输出流
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }

    }

    public static InputStream generateInputStream(CloseableHttpResponse response) throws IOException {
        return response.getEntity().getContent();
    }


}
