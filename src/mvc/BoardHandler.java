package mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.Board;
import domain.Pager;
import domain.User;
import service.BoardService;

public class BoardHandler implements URIHandler {
	private static String preView = "/WEB-INF/views/board/";

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// URI 파싱 부분
		String command = req.getRequestURI();
		String cmds[] = null;
		if (command.indexOf(req.getContextPath()) == 0) {
			// 컨텍스트 패스만큼 잘라낸다.
			command = command.substring(req.getContextPath().length());
			cmds = command.split("/");
		}

		// cmds의 0:공백 1:board 2:list,write etc...

		String view = null;
		String menu = null;

		if (cmds.length < 3) {
			menu = "list";// 기본값은 list로
		} else {
			menu = cmds[2];
		}
		switch (menu) {
		case "list":
			view = listBoard(req, res);
			break;
		case "write":
			view = writeBoard(req, res);
			break;
		case "view":
			view = viewBoard(req, res);
			break;
		}
		return view;
	}

	private String viewBoard(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String idParam = req.getParameter("id");
		int id = idParam == null ? 0 : Integer.parseInt(idParam);

		if (id > 0) {
			Board data = BoardService.getInstance().read(id);
			if (data == null) {
				req.getSession().setAttribute("msg", "해당 글은 존재하지 않습니다.");
				res.sendRedirect("/board/list");
				return null;
			} else {
				req.setAttribute("board", data);
				return preView + "read.jsp";
			}
		} else {
			req.getSession().setAttribute("msg", "해당 글은 존재하지 않습니다.");
			res.sendRedirect("/board/list");
			return null;
		}
	}

	private String listBoard(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String pageParam = req.getParameter("start");
		int page;

		page = pageParam == null ? 1 : Integer.parseInt(pageParam);

		Pager pager = new Pager();
		int totalCnt = BoardService.getInstance().getTotalCnt();
		pager.setTotalCnt(totalCnt);
		pager.setNowPage(page);
		pager.calc();

		List<Board> list = BoardService.getInstance().getList((page - 1) * pager.getPerPageNum(),
				pager.getPerPageNum());

		req.setAttribute("pager", pager);
		req.setAttribute("boardList", list);
		req.setAttribute("title", "글 목록 보기");

		return "/WEB-INF/views/index.jsp";
	}

	private String writeBoard(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			req.setCharacterEncoding("UTF-8");

			String title = req.getParameter("title");
			String content = req.getParameter("content");

			User writer = (User) req.getSession().getAttribute("login");

			if (writer == null) {
				req.getSession().setAttribute("msg", "로그인 후 작성하세요.");
				res.sendRedirect("/user/login");
				return null;
			}

			String id = writer.getId();

			Board board = new Board();
			board.setTitle(title);
			board.setContent(content);
			board.setDate(new java.sql.Date(new java.util.Date().getTime()));
			board.setWriter(id);

			BoardService.getInstance().write(board);
			res.sendRedirect("/board/list");

			return null;
		}
		return "/WEB-INF/views/board/write.jsp";
	}

}
