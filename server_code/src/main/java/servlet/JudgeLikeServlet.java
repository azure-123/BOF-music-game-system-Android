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

public class JudgeLikeServlet extends HttpServlet {

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
		String user_id=request.getParameter("user_id");//�ͻ��˴���ע������
		String music_id=request.getParameter("music_id");//�ͻ��˴���ע���ǳ�

		response.setContentType("text/html;charset=utf-8");
		//---------------for debug-----------
		System.out.println("Like data:"+
				"\nuser id:"+user_id+
				"\nmusic id:"+music_id+
				"\n");
		//------------------end----------------
		// �������ݿ�
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		// �����ݿ�����
		BaseBean data = new BaseBean(); // ������󣬻ش����ͻ��˵�json����
		UserBean userBean = new UserBean(); // user�Ķ���
		
		//�鿴�ղؼ�¼�Ƿ����
		if(!dbUtils.checkLikeExists(user_id,music_id))
		{
			data.setCode(2);
			data.setData(userBean);
			data.setMsg("����Ŀδ�ղأ�");
		}
		///////////////////////////
		else if(dbUtils.checkLikeExists(user_id,music_id))
		{
			data.setCode(3);
			data.setMsg("����Ŀ���ղأ�");
			data.setData(userBean);
		}
		else
		{
			data.setCode(11);
			data.setMsg("���ݿ����");
			data.setData(userBean);
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(data);
		System.out.println(json);
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
