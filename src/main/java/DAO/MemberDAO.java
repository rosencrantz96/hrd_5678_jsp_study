package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DTO.Member;
import DTO.Party;
import DTO.Vote;

public class MemberDAO {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	// 데이터 베이스 연결
	public static Connection getConnection() throws Exception {
		Class.forName("oracle.jdbc.OracleDriver");
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xe", "system", "sys1234");
		return con;
	}

	// 후보조회 화면
	public String selectMember(HttpServletRequest request, HttpServletResponse response) {

		ArrayList<Member> list = new ArrayList<Member>();

		try {
			conn = getConnection();

			// 후보 조회 화면 쿼리
			String sql = "SELECT ";
			sql += " M.m_no, ";
			sql += " M.m_name, ";
			sql += " P.p_name, ";
			sql += " DECODE(M.p_school,'1','고졸','2','학사','3','석사','박사') p_school, ";
			sql += " substr(M.m_jumin,1,6)|| ";
			sql += " '-'||substr(M.m_jumin,7) m_jumin, ";
			sql += " M.m_city, ";
			sql += " substr(P.p_tel1,1,2)||'-'||P.p_tel2||'-'||";
			sql += " (substr(P.p_tel3,4)||";
			sql += "  substr(P.p_tel3,4)||";
			sql += "  substr(P.p_tel3,4)||";
			sql += "  substr(P.p_tel3,4)) p_tel ";
			sql += " FROM tbl_member_202005 M, tbl_party_202005 P ";
			sql += " WHERE M.p_code = P.p_code";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				Member member = new Member();
				member.setM_no(rs.getString(1));
				member.setM_name(rs.getString(2));
				member.setP_name(rs.getString(3));
				member.setP_school(rs.getString(4));
				member.setM_jumin(rs.getString(5));
				member.setM_city(rs.getString(6));
				member.setP_tel(rs.getString(7));

				list.add(member);
			}
			request.setAttribute("list", list);
			conn.close();
			ps.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "lookUp.jsp";

	}

	public String memberSelect(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<Member> list = new ArrayList<Member>();
		try {
			conn = getConnection();

			String sql = "SELECT ";
			sql += " M.m_no, M.m_name, count(*) AS v_total";
			sql += " FROM tbl_member_202005 M, tbl_vote_202005 V";
			sql += " WHERE M.m_no = V.m_no AND V.v_confirm = 'Y' ";
			sql += " GROUP BY M.m_no, M.m_name";
			sql += " ORDER BY v_total DESC";

			ps = conn.prepareStatement(sql); // 명령어를 보낸다.
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Member member = new Member();

				member.setM_no(rs.getString(1));
				member.setM_name(rs.getString(2));
				member.setV_total(rs.getString(3));

				list.add(member);
			}
			request.setAttribute("list", list);
			conn.close();
			ps.close();
			rs.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "memberList.jsp";
	}

	public String resultSelect(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<Vote> vlist = new ArrayList<Vote>();
		try {
			conn = getConnection();
			String sql = "SELECT V_NAME, ";
			sql += "'19' || substr(V_JUMIN,1,2) || '년' || substr(V_JUMIN, 3, 2) || '월' || substr(V_JUMIN, 5, 2) || '일' AS V_JUMIN, ";
			sql += "'만' ||(to_number(to_char(sysdate,'yyyy'))-to_number('19'||substr(V_JUMIN,1,2)))||'세' AS V_AGE, ";
			sql += "DECODE(substr(V_JUMIN, 7, 1), '1', '남', '2', '여') AS V_GENDER, ";
			sql += "M_NO, ";
			sql += "substr(V_TIME, 1, 2) || ':' || substr(V_TIME, 3, 2) AS V_TIME, ";
			sql += "DECODE(V_CONFIRM, 'Y', '확인', 'N', '미확인') AS V_CONFIRM ";
			sql += "FROM TBL_VOTE_202005 ";
			sql += "WHERE V_AREA='제1투표장'";
			
			ps = conn.prepareStatement(sql); // 명령어를 보낸다.
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Vote vote= new Vote();

				vote.setV_name(rs.getString(1));
				vote.setV_jumin(rs.getString(2));
				vote.setV_age(rs.getString(3));
				vote.setV_gender(rs.getString(4));
				vote.setM_no(rs.getString(5));
				vote.setV_time(rs.getString(6));
				vote.setV_confirm(rs.getString(7));

				vlist.add(vote);
			}
			request.setAttribute("vlist", vlist);
			conn.close();
			ps.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "voteResult.jsp";
	}
}
