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

public class RegisterServlet extends HttpServlet {

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
		String user_password=request.getParameter("user_password");//�ͻ��˴���ע������
		String user_nickname=request.getParameter("user_nickname");//�ͻ��˴���ע���ǳ�
		response.setContentType("text/html;charset=utf-8");
		user_nickname= new String(user_nickname.getBytes("ISO8859-1"),"UTF-8");
		if(user_password==""||user_nickname=="")
		{
			System.out.println("������ǳ�Ϊ�գ�");
			return;
		}
		response.setContentType("text/html;charset=utf-8");
		//---------------for debug-----------
		System.out.println("Register data:"+
				"\npassword:"+user_password+
				"\nnickname:"+user_nickname+
				"\n");
		//------------------end----------------
		// �������ݿ�
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		// �����ݿ�����
		BaseBean data = new BaseBean(); // ������󣬻ش����ͻ��˵�json����
		UserBean userBean = new UserBean(); // user�Ķ���
		
		//�鿴�Ƿ����ǳ��ظ����
		if(dbUtils.checkNicknameExists(user_nickname))
		{
			data.setCode(101);
			data.setData(userBean);
			data.setMsg("���û��ǳ��Ѵ��ڣ�");
		}
		else if(!dbUtils.insertRegisterUser(user_nickname, user_password))
		{
			data.setCode(1);
			data.setMsg("ע��ɹ���");
			System.out.println("�������û��ɹ���");
			
			//���û��ǳƺ��û�����������ݿ�
			//dbUtils.insertFrailData(userid, score, strength, health);
			int id=dbUtils.getUserId(user_nickname);
			if(id!=-1)
			{
				userBean.setId(id);
				userBean.setUsername(user_nickname);
				userBean.setPassword(user_password);
			}
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
