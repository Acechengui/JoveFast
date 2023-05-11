package com.jovefast.common.i18n.resolver;

import com.jovefast.common.core.utils.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
 
/**
 * 自定义国际化解析器
 * @author Acechengui
 **/
public class DefaultLocaleResolver implements LocaleResolver {
 
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(LANG);
        Locale locale = Locale.getDefault();
        if (StringUtils.isNotBlank(lang)){
            String[] language = lang.split("_");
            locale = new Locale(language[0], language[1]);
            HttpSession session = request.getSession();
            session.setAttribute(LANG_SESSION, locale);
        }else{
            HttpSession session = request.getSession();
            Locale localeInSession = (Locale) session.getAttribute(LANG_SESSION);
            if (localeInSession != null){
                locale = localeInSession;
            }
        }
        return locale;
    }
 
    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
    }
 
    /**
     * 请求header字段
     */
    private static final String LANG = "lang";
 
    /**
     * session
     */
    private static final String LANG_SESSION = "lang_session";
 
}