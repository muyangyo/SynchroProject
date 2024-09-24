package com.muyangyo.wechatsmallprogram.global.temp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(GraphDBController.DELEGATE_PREFIX)
public class GraphDBController {
    public static final String DELEGATE_PREFIX = "/graphdb";

    @Autowired
    private RedirectUtils redirectUtils;

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> catchAll(HttpServletRequest request) {
        String url = " ";
        return redirectUtils.redirect(request, url, DELEGATE_PREFIX);
    }
}
