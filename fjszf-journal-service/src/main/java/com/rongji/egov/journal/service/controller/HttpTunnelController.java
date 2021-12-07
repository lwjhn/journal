package com.rongji.egov.journal.service.controller;

import com.rongji.egov.journal.service.utils.FileOperator;
import com.rongji.egov.mybatis.base.utils.AutoCloseableBase;
import com.rongji.egov.mybatis.base.utils.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("${spring.application.name}")
public class HttpTunnelController {
    private static final Pattern EscapeHeaderPattern = Pattern.compile("^x-", Pattern.CASE_INSENSITIVE);

    @RequestMapping(value = "/HttpTunnel", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
    public void httpTunnel(HttpServletRequest request, HttpServletResponse response) {
        HttpURLConnection http = null;
        InputStream isr = null, ish=null;
        OutputStream os = null;
        try {
            String url = request.getQueryString();
            http = (HttpURLConnection) (new URL(url.replaceAll("^(\\?)*(url=)*", ""))).openConnection();
            String key, val;
            for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
                if ((key = headerNames.nextElement()) != null && !EscapeHeaderPattern.matcher(key).find() && (val = request.getHeader(key)) != null) {
                    System.out.println(key + ":" +val);
                    http.setRequestProperty(key, val);
                }
            }

            http.setConnectTimeout(30000);
            http.setReadTimeout(30000);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setUseCaches(false);
            http.setRequestMethod(request.getMethod());
            http.setRequestProperty("Connection", "Close");
            FileOperator.copyStream(isr=request.getInputStream(), os = http.getOutputStream());

            for(Map.Entry<String,List<String>> header : http.getHeaderFields().entrySet()){
                if((key=header.getKey())==null || "Cookie".equalsIgnoreCase(key) || "Set-Cookie".equalsIgnoreCase(key)){
                    continue;
                }
                response.setHeader(header.getKey(), String.join(", ", header.getValue()));
            }
//            response.setHeader("Access-Control-Allow-Credentials", "true");
//            response.setHeader("Access-Control-Allow-Origin", StringUtils.isNotBlank(key = request.getHeader("origin")) ? key : "*");

            FileOperator.copyStream(ish = http.getInputStream(), os = response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(isr, ish, os);
            try {
                if (http != null) http.disconnect();
            } catch (Exception ignored) {
            }
        }
    }
}
