package com.example.springboot3.webdav;

import com.example.springboot3.exception.ConnectException;
import com.example.springboot3.util.WebDavDataUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.jackrabbit.webdav.client.methods.HttpOptions;
import org.apache.jackrabbit.webdav.client.methods.HttpPropfind;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

@Component
public class WebDavUtils {

    @Autowired
    public CloseableHttpClient httpClient;

    public void getHttpOptions(String webdavUrl) throws IOException {
        HttpUriRequest method = new HttpOptions(webdavUrl);
        CloseableHttpResponse response = httpClient.execute(method);
        Header[] headers = response.getHeaders("Allow");
        for (Header header : headers) {
            System.out.println(header.getValue());
        }
    }

    public List<String> list(String webdavUrl) {
        List<String> list = null;
        try {
            URI uri = new URIBuilder(webdavUrl).build();
            DavPropertyNameSet names = new DavPropertyNameSet();
            names.add(DavPropertyName.DISPLAYNAME);
            int depth = 1;
            HttpPropfind propfind = new HttpPropfind(uri, names, depth);
            StringEntity entity = new StringEntity("", ContentType.create("text/xml"));
            propfind.setEntity(entity);
            CloseableHttpResponse httpResponse = httpClient.execute(propfind);
            list = WebDavDataUtils.list(httpResponse);
        } catch (Exception e) {
            throw new ConnectException(500,e.getMessage());
        }
        return list;
    }

    public CloseableHttpResponse get(String webdavUrl) throws IOException {
        HttpGet request = new HttpGet(webdavUrl);
        return httpClient.execute(request);
    }
}
