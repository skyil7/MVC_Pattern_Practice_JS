package mvc;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class URIRouter extends HttpServlet{
	private Map<String, URIHandler> uriHandlerMap = new HashMap();
	
	public void init() throws ServletException {
			//초기 설정 파일의 위치를 configFile변수에 담는다.
			String configFile = getInitParameter("configFile");
			Properties prop = new Properties();
			String configFilePath = getServletContext().getRealPath(configFile);
			//설정 파일을 파일리더로 읽어 Properties 객체에 담아준다.
			//담겨진 프로퍼티는 hello=mvc.HelloHandler을 읽었다면 key:hello, property:mvc.HelloHandler로 저장된다.
			
			try {
				FileReader fs = new FileReader(configFilePath);
				prop.load(fs);
			}catch(IOException ex) {
				throw new ServletException(ex);
			}
			
			//프로퍼티 객체에 읽어진 값을 이터레이터로 반복하며 uriHandlerMap에 넣어준다.
			Iterator keyIter = prop.keySet().iterator();
			while(keyIter.hasNext()) {
				String command = (String) keyIter.next();
				String handlerClassName = prop.getProperty(command);
				
				try {
					Class<?> handlerClass = Class.forName(handlerClassName);
					
					URIHandler handlerInstance = (URIHandler)handlerClass.newInstance();
					//command : /board 가 들어오면 HandlerInstance가 실행됨
					uriHandlerMap.put(command, handlerInstance);
				} catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					throw new ServletException(e);
				}
			}
	}//init 메소드 종료
	
	private void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		String command = req.getRequestURI();
		
		if(command.indexOf(req.getContextPath())==0) {//context path가 /가 아닌 경우 대비(/boardProject)
			command = command.substring(req.getContextPath().length());
		}
		
		int slashIndex = command.indexOf('/',1);
		if(slashIndex >=0) {// /board/write와 같은 경로를 /board로
			command=command.substring(0, slashIndex);
		}
		
		URIHandler handler = uriHandlerMap.get(command);
		if(handler == null) {
			handler = new NullHandler();
		}
		
		String viewPage = null;
		try {
			viewPage = handler.process(req, res);
		}catch(Exception e) {
			throw new ServletException(e);
		}
		
		if(viewPage != null) {
			RequestDispatcher dispatcher = req.getRequestDispatcher(viewPage);
			dispatcher.forward(req, res);
		}
	}//process 메소드 종료
	
	public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		process(req,res);
	}
	public void doPost (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		process(req,res);
	}
}
