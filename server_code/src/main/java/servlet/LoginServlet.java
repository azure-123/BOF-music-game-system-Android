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

public class LoginServlet extends HttpServlet {

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
		String role=request.getParameter("role");
		response.setContentType("text/html;charset=utf-8");
		user_nickname= new String(user_nickname.getBytes("ISO8859-1"),"UTF-8");
		if(user_password==""||user_nickname=="")
		{
			System.out.println("������ǳ�Ϊ�գ�");
			return;
		}
		response.setContentType("text/html;charset=utf-8");
		//---------------for debug-----------
		System.out.println("Login data:"+
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
		
		//�鿴�ǳ��Ƿ����
		if(role.equals("user"))
		{
			if(!dbUtils.checkNicknameExists(user_nickname))
			{
				data.setCode(101);
				data.setData(userBean);
				data.setMsg("���û��ǳƲ����ڣ�");
			}
			///////////////////////////
			else if(!dbUtils.checkPassword(user_nickname, user_password))//�鿴�û����������Ƿ�ƥ��
			{
				data.setCode(101);
				data.setMsg("�������");
				data.setData(userBean);
				System.out.println("�������");
			}
			else if(dbUtils.checkPassword(user_nickname, user_password))
			{
				int id=dbUtils.getUserId(user_nickname);
				String sex=dbUtils.getUserSex(user_nickname);
				String birthday=dbUtils.getUserBirthday(user_nickname);
	//			if(sex==null)
	//				sex="unknown";
				if(birthday=="")
					birthday="unknown";
				if(id!=-1)
				{
					userBean.setId(id);
					userBean.setUsername(user_nickname);
					userBean.setPassword(user_password);
					userBean.setUserbirth(birthday);
					if(sex==null)
						userBean.setUsersex("");
					else
						userBean.setUsersex(sex);
					if(birthday==null)
						userBean.setUserbirth("");
					else
						userBean.setUserbirth(birthday);
					System.out.println("sex:"+userBean.getUsersex());
					System.out.println("birth:"+userBean.getUserbirth());
					
				}
				data.setCode(1);
				data.setData(userBean);
				data.setMsg("��¼�ɹ���");
			}
			else
			{
				data.setCode(11);
				data.setMsg("���ݿ����");
				data.setData(userBean);
			}
		}
		else
		{
			if(!dbUtils.checkAdminPassword(user_nickname, user_password))//�鿴�û����������Ƿ�ƥ��
			{
				data.setCode(101);
				data.setMsg("�������");
				data.setData(userBean);
				System.out.println("�������");
			}
			else if(dbUtils.checkAdminPassword(user_nickname, user_password))
			{

				userBean.setId(Integer.parseInt(user_nickname));
				userBean.setPassword(user_password);
				data.setCode(1);
				data.setData(userBean);
				data.setMsg("��¼�ɹ���");
			}
			else
			{
				data.setCode(11);
				data.setMsg("���ݿ����");
				data.setData(userBean);
			}
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
