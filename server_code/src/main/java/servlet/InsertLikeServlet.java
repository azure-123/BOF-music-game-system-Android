package servlet;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.xml.ws.Response;

import domain.BaseBean;
import domain.UserBean;
import database.DBUtils;
import com.google.gson.Gson;

public class InsertLikeServlet extends HttpServlet {

	@Override
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	 throws ServletException, IOException {
	 doPost(request, response);
	 }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException 
	{
		System.out.println("request--->"+request.getRequestURL()+"===="+request.getParameterMap().toString());
//		String user_password=request.getParameter("user_password");//�ͻ��˴���ע������
//		String user_nickname=request.getParameter("user_nickname");//�ͻ��˴���ע���ǳ�
		String user_id=request.getParameter("user_id");
		String music_id=request.getParameter("music_id");
		String like_flag=request.getParameter("like_flag");
		
		response.setContentType("text/html;charset=utf-8");
		//---------------for debug-----------
		System.out.println("Like data:"+
				"\npassword:"+user_id+
				"\nnickname:"+music_id+
				"\nlike flag:"+like_flag+
				"\n");
		//------------------end----------------
		// �������ݿ�
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		// �����ݿ�����
		BaseBean data = new BaseBean(); // ������󣬻ش����ͻ��˵�json����
		if(!dbUtils.insertLike(user_id,music_id,like_flag))
		{
			data.setCode(1);
			data.setMsg("�ղسɹ���");
			System.out.println("�����ղ����ݳɹ���");
			
			//���û��ǳƺ��û�����������ݿ�
			//dbUtils.insertFrailData(userid, score, strength, health);
		}
		else
		{
			data.setCode(11);
			data.setMsg("���ݿ����");
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(data);
		// ������ת����json�ַ���
		try 
		{
			response.getWriter().println(json);
			// ��json���ݴ����ͻ���
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			response.getWriter().close(); // �ر����������Ȼ�ᷢ�������
		}
		dbUtils.closeConnect(); // �ر����ݿ�����}
	}
}
