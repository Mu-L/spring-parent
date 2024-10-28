package com.emily.infrastructure.web.filter.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 路由重定向
 *
 * @author Emily
 * @since Created in 2023/2/2 5:01 下午
 */
public class RoutingRedirectFilter implements Filter {

    private final RoutingRedirectCustomizer routingRedirectCustomizer;

    public RoutingRedirectFilter(RoutingRedirectCustomizer routingRedirectCustomizer) {
        this.routingRedirectCustomizer = routingRedirectCustomizer;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        if (this.routingRedirectCustomizer.isRouteRedirect(req)) {
            String newUrl = this.routingRedirectCustomizer.resolveSpecifiedLookupPath(req);
            if (StringUtils.equals(newUrl, req.getRequestURI())) {
                chain.doFilter(request, response);
            } else {
                request.getRequestDispatcher(newUrl).forward(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
