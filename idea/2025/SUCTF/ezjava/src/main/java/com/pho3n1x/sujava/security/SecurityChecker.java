package com.pho3n1x.sujava.security;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;

/* loaded from: suJava-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/pho3n1x/sujava/security/SecurityChecker.class */
public class SecurityChecker {
    private static final String AND_SYMBOL = "&";
    private static final String EQUAL_SIGN = "=";
    private static final String COMMA = ",";
    private static final String BLACKLIST_REGEX = "autodeserialize|allowloadlocalinfile|allowurlinlocalinfile|allowloadlocalinfileinpath";
    public static String MYSQL_SECURITY_CHECK_ENABLE = "true";
    public static String MYSQL_CONNECT_URL = "jdbc:mysql://%s:%s/%s";
    public static String JDBC_MYSQL_PROTOCOL = "jdbc:mysql";
    public static String JDBC_MATCH_REGEX = "(?i)jdbc:(?i)(mysql)://([^:]+)(:[0-9]+)?(/[a-zA-Z0-9_-]*[\\.\\-]?)?";
    public static String MYSQL_SENSITIVE_PARAMS = "allowLoadLocalInfile,autoDeserialize,allowLocalInfile,allowUrlInLocalInfile,#";

    public static void checkJdbcConnParams(String host, Integer port, String username, String password, String database, Map<String, Object> extraParams) throws Exception {
        if (Boolean.valueOf(MYSQL_SECURITY_CHECK_ENABLE).booleanValue()) {
            if (StringUtils.isAnyBlank(host, username)) {
                throw new Exception("Invalid mysql connection params.");
            }
            String url = String.format(MYSQL_CONNECT_URL, host.trim(), port, database.trim());
            checkHost(host.trim());
            checkUrl(url);
            checkParams(extraParams);
            checkUrlIsSafe(url);
        }
    }

    public static void checkHost(String host) throws Exception {
        if (host == null) {
            return;
        }
        if (host.startsWith("(") || host.endsWith(")")) {
            throw new Exception("Invalid host");
        }
    }

    public static void checkUrl(String url) throws Exception {
        if (url != null && !url.toLowerCase().startsWith(JDBC_MYSQL_PROTOCOL)) {
            return;
        }
        Pattern regex = Pattern.compile(JDBC_MATCH_REGEX);
        Matcher matcher = regex.matcher(url);
        if (!matcher.matches()) {
            throw new Exception();
        }
    }

    private static Map<String, Object> parseMysqlUrlParamsToMap(String paramsUrl) {
        if (StringUtils.isBlank(paramsUrl)) {
            return new HashMap();
        }
        String[] params = paramsUrl.split("&");
        Map<String, Object> map = new HashMap<>(params.length);
        for (String param : params) {
            String[] item = param.split(EQUAL_SIGN);
            if (item.length == 2) {
                map.put(item[0], item[1]);
            }
        }
        return map;
    }

    public static String parseParamsMapToMysqlParamUrl(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        return (String) params.entrySet().stream().map(e -> {
            return String.join(EQUAL_SIGN, (CharSequence) e.getKey(), String.valueOf(e.getValue()));
        }).collect(Collectors.joining("&"));
    }

    private static void checkParams(Map<String, Object> paramsMap) throws Exception {
        if (paramsMap == null || paramsMap.isEmpty()) {
            return;
        }
        String paramUrl = parseParamsMapToMysqlParamUrl(paramsMap);
        try {
            Map<String, Object> newParamsMap = parseMysqlUrlParamsToMap(URLDecoder.decode(paramUrl, CharEncoding.UTF_8));
            paramsMap.clear();
            paramsMap.putAll(newParamsMap);
            Iterator<Map.Entry<String, Object>> iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                if (StringUtils.isBlank(key) || value == null || StringUtils.isBlank(value.toString())) {
                    iterator.remove();
                } else if (isNotSecurity(key, value.toString())) {
                    throw new Exception("Invalid mysql connection parameters: " + parseParamsMapToMysqlParamUrl(paramsMap));
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new Exception("mysql connection cul decode error: " + e);
        }
    }

    private static boolean isNotSecurity(String key, String value) {
        boolean res = true;
        String sensitiveParamsStr = MYSQL_SENSITIVE_PARAMS;
        if (StringUtils.isBlank(sensitiveParamsStr)) {
            return false;
        }
        String[] forceParams = sensitiveParamsStr.split(",");
        int length = forceParams.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String forceParam = forceParams[i];
            if (!isNotSecurity(key, value, forceParam)) {
                i++;
            } else {
                res = false;
                break;
            }
        }
        return !res;
    }

    private static boolean isNotSecurity(String key, String value, String param) {
        return key.toLowerCase().contains(param.toLowerCase()) || value.toLowerCase().contains(param.toLowerCase());
    }

    public static void checkUrlIsSafe(String url) throws Exception {
        try {
            String lowercaseURL = url.toLowerCase();
            Pattern pattern = Pattern.compile(BLACKLIST_REGEX);
            Matcher matcher = pattern.matcher(lowercaseURL);
            StringBuilder foundKeywords = new StringBuilder();
            while (matcher.find()) {
                if (foundKeywords.length() > 0) {
                    foundKeywords.append(", ");
                }
                foundKeywords.append(matcher.group());
            }
            if (foundKeywords.length() > 0) {
                throw new Exception("url contains blacklisted characters: " + ((Object) foundKeywords));
            }
        } catch (Exception e) {
            throw new Exception("error occurred during url security check: " + e);
        }
    }

    public static void appendMysqlForceParams(Map<String, Object> extraParams) {
        extraParams.putAll(parseMysqlUrlParamsToMap("allowLoadLocalInfile=false&autoDeserialize=false&allowLocalInfile=false&allowUrlInLocalInfile=false"));
    }
}
