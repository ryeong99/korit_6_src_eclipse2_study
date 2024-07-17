package filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import entity.User;
//mapping 순서대로 filter 동작
//@WebFilter({"/mypage","/mypage/password"})
@WebFilter(filterName = "securityFilter")
public class SecurityFilter extends HttpFilter implements javax.servlet.Filter {
  
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession();
		
		System.out.println("시큐리티 필터");
		
		User user = (User) session.getAttribute("authentication");
		if(user == null) {
			StringBuilder responsBody = new StringBuilder();
			responsBody.append("<script>");
			responsBody.append("alert('잘못된 접근입니다.\\n로그인 후에 이용바랍니다.');");
			responsBody.append("location.href='/ssa/login';");
			responsBody.append("</script>");
			response.setContentType("text/html");
			response.getWriter().println(responsBody.toString());
			return;
		}
		
		// Servlet 호출
		chain.doFilter(request, response);
	}

}
