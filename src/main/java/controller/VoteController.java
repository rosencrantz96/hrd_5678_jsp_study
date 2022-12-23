package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.MemberDAO;

/**
 * Servlet implementation class VoteController
 */
@WebServlet("/")
public class VoteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VoteController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doPro(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doPro(request, response);
	}
	
	protected void doPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String context = request.getContextPath(); // context: 톰캣이 생성하는 애! contextPath(): 톰캣의 context path를 가져온다. 
		String command = request.getServletPath(); // 경로의 맨 끝 파일명을 가져온다. 
		String site = null;
		
		MemberDAO member = new MemberDAO();
		
		switch (command) {
		case "/home":
			site = "index.jsp";
			break;
		case "/lookUp":
			site = member.selectMember(request, response);
			break;
		 case "/memberList" : 
			 site = member.memberSelect(request, response);
			 break;
		 case "/voteResult" : 
			 site = member.resultSelect(request, response);
			 break;
		}
		getServletContext().getRequestDispatcher("/" + site).forward(request, response);
	}

}
