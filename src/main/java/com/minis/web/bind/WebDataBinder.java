package com.minis.web.bind;

import com.minis.beans.PropertyEditor;
import com.minis.core.PropertyValues;
import com.minis.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author luoruiqing
 */
public class WebDataBinder {
    private Object target;
    private Class<?> clz;
    private String objectName;

    private BeanWrapperImpl propertyAccessor;

    public WebDataBinder(Object target) {
        this(target, "");
    }

    public WebDataBinder(Object target, String objectName) {
        this.target = target;
        this.objectName = objectName;
        this.clz = this.target.getClass();
        try {
            this.propertyAccessor = new BeanWrapperImpl(this.target);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void bind(HttpServletRequest request) throws Exception {
        PropertyValues mpvs = assignParameters(request);
        addBindValues(mpvs, request);
        doBind(mpvs);
    }

    private void doBind(PropertyValues mpvs) throws Exception {
        applyPropertyValues(mpvs);
    }

    private void applyPropertyValues(PropertyValues mpvs) throws Exception {
        getPropertyAccessor().setPropertyValues(mpvs);
    }

    private BeanWrapperImpl getPropertyAccessor() throws Exception {
        return this.propertyAccessor;
    }

    private void addBindValues(PropertyValues mpvs, HttpServletRequest request) {

    }

    private PropertyValues assignParameters(HttpServletRequest request) {
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }

    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) throws Exception {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);

    }
}
