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

public class AlterSexServlet extends HttpServlet {

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
		String user_sex=request.getParameter("user_sex");
		
		response.setContentType("text/html;charset=utf-8");
		//---------------for debug-----------
		System.out.println("Like data:"+
				"\nid:"+user_id+
				"\nsex:"+user_sex+
				"\n");
		//------------------end----------------
		// �������ݿ�
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		// �����ݿ�����
		BaseBean data = new BaseBean(); // ������󣬻ش����ͻ��˵�json����
		
		
		//�鿴�Ƿ����ǳ��ظ����
		if(!dbUtils.alterSex(user_id, user_sex))
		{
			data.setCode(1);
			data.setMsg("�޸ĳɹ���");
			System.out.println("�޸��û��Ա�ɹ���");
			
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
