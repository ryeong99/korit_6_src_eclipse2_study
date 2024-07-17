package sevlet.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDao;
import entity.User;

/*
 * Servlet 3대 저장소
 * 1.Application
 * - 전역 (모든 쓰레드가 접근할 수 있는 영역)
 * - 서블릿 설정관련 데이터 테이블을 보관
 * - 생명주기: 서버가 실행되어지고 종료되어질 때 까지
 * 2.Request
 * - 요청이 들어오고 Filter,,Servlet, JSp등 서로 Request 객체를 넘겨줄 때 사용
 * 
 *  
 */

@WebServlet("/api/login")
public class LoginServiceSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
	
		User user = UserDao.findUserByUsername(username);
		if(user == null) {
			responseLoginError(resp);
			return;
		}
		
		if(!BCrypt.checkpw(password, user.getPassword())) {
			responseLoginError(resp);
			return;
		}
		
		HttpSession session = req.getSession();
		session.setAttribute("authentication", user);
		resp.sendRedirect("/ssa/index");
		
	}
	private void responseLoginError(HttpServletResponse resp) throws IOException {
		StringBuilder responsebody = new StringBuilder();
		responsebody.append("<script>");
		responsebody.append("alert('사용자 정보를 확인해주세요.');");
		responsebody.append("history.back();");
		responsebody.append("</script>");
			
		resp.setContentType("text/html");
		resp.getWriter().println(responsebody.toString());
		
		return;
	}
}
