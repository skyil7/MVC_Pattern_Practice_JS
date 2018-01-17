package mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.User;
import service.UserService;

public class UserHandler implements URIHandler {
	private static String preView = "/WEB-INF/views/";

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// uri 파싱부분
		String command = req.getRequestURI();
		String cmds[] = null;
		if (command.indexOf(req.getContextPath()) == 0) {
			// 컨텍스트 패스만큼 잘라낸다.
			command = command.substring(req.getContextPath().length());
			cmds = command.split("/");
		}
		/*
		 * cmds의 0:공백 1:user 2: join 또는 login 등등
		 */

		String view = null;
		String menu = null;
		if (cmds.length < 3) {
			menu = "login"; // 기본 값은 login으로
		} else {
			menu = cmds[2];
		}
		switch (menu) {
		case "join":
			view = joinUser(req, res);
			break;
		case "login":
			view = loginUser(req, res);
			break;
		case "logout":
			view = logoutUser(req, res);
			break;
		}
		return view;
	}

	private String joinUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) { // 회원 가입 요청데이터
			req.setCharacterEncoding("UTF-8");

			String id = req.getParameter("uid");
			String name = req.getParameter("uname");
			String pass1 = req.getParameter("pass1");

			User user = new User();
			user.setId(id);
			user.setName(name);
			user.setPassword(pass1);

			int result = UserService.getInstance().join(user);

			if (result < 0) {
				req.setAttribute("msg", "작업중 오류가 발생했습니다.");// alert 띄워줌
				return preView + "join.jsp";
			} else if (result == 0) {
				req.setAttribute("msg", "중복된 id가 있습니다.");
				return preView + "join.jsp";
			}
			res.sendRedirect("/");
			return null;
		} else { // 회원 가입 페이지 요청
			return preView + "user/join.jsp";
		}
	}

	private String loginUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) { // 회원 가입 요청데이터

			String id = req.getParameter("uid");
			String pass = req.getParameter("pass1");

			User user = UserService.getInstance().getUser(id);
			if (user == null) {
				req.setAttribute("msg", "해당 유저는 없습니다.");
				return preView + "login.jsp";
			} else if (!user.getPassword().equals(pass)) {
				req.setAttribute("msg", "비밀번호가 올바르지 않습니다.");
				return preView + "login.jsp";
			}

			req.getSession().setAttribute("login", user);

			res.sendRedirect("/");
			return null;

		} else { // 회원 가입 페이지 요청
			return preView + "user/login.jsp";
		}
	}

	private String logoutUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getSession().getAttribute("login") != null) {
			req.getSession().removeAttribute("login");
			res.sendRedirect("/");
			return null;
		}
		res.sendRedirect("/user/login");
		return null;
	}
}
