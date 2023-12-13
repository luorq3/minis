package com.minis.web;

import com.minis.web.method.HandlerMethod;
import com.minis.web.method.annotation.RequestMappingHandlerAdapter;
import com.minis.web.method.annotation.RequestMappingHandlerMapping;
import com.minis.web.servlet.HandlerAdapter;
import com.minis.web.servlet.HandlerMapping;
import com.minis.web.servlet.ModelAndView;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author luoruiqing
 */
public class DispatcherServlet extends HttpServlet {

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    private String sContextConfigLocation;
    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private WebApplicationContext webApplicationContext;
    private WebApplicationContext parentApplicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.parentApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        sContextConfigLocation = config.getInitParameter("contextConfigLocation");

        this.webApplicationContext = new AnnotationConfigWebApplicationContext(sContextConfigLocation, this.parentApplicationContext);

        refresh();
    }

    protected void refresh() {
        initHandlerMapping(this.webApplicationContext);
        initHandlerAdapter(this.webApplicationContext);
    }

    private void initHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.handlerAdapter = new RequestMappingHandlerAdapter(webApplicationContext);
    }

    private void initHandlerMapping(WebApplicationContext webApplicationContext) {
        this.handlerMapping = new RequestMappingHandlerMapping(webApplicationContext);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod = this.handlerMapping.getHandler(request);
        if (handlerMethod == null) {
            return;
        }
        HandlerAdapter handlerAdapter = this.handlerAdapter;
        ModelAndView mav = handlerAdapter.handle(request, response, handlerMethod);
        render(request, response, mav);
    }

    protected void render(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        //获取model，写到request的Attribute中：
        Map<String, Object> modelMap = mv.getModel();
        for (Map.Entry<String, Object> e : modelMap.entrySet()) {
            request.setAttribute(e.getKey(), e.getValue());
        }
        //输出到目标JSP
        String sTarget = mv.getViewName();
        String sPath = "/" + sTarget + ".jsp";
        request.getRequestDispatcher(sPath).forward(request, response);
    }
}
