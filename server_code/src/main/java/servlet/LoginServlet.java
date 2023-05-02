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
		String user_password=request.getParameter("user_password");//客户端传来注册密码
		String user_nickname=request.getParameter("user_nickname");//客户端传来注册昵称
		String role=request.getParameter("role");
		response.setContentType("text/html;charset=utf-8");
		user_nickname= new String(user_nickname.getBytes("ISO8859-1"),"UTF-8");
		if(user_password==""||user_nickname=="")
		{
			System.out.println("密码或昵称为空！");
			return;
		}
		response.setContentType("text/html;charset=utf-8");
		//---------------for debug-----------
		System.out.println("Login data:"+
				"\npassword:"+user_password+
				"\nnickname:"+user_nickname+
				"\n");
		//------------------end----------------
		// 请求数据库
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		// 打开数据库连接
		BaseBean data = new BaseBean(); // 基类对象，回传给客户端的json对象
		UserBean userBean = new UserBean(); // user的对象
		
		//查看昵称是否存在
		if(role.equals("user"))
		{
			if(!dbUtils.checkNicknameExists(user_nickname))
			{
				data.setCode(101);
				data.setData(userBean);
				data.setMsg("该用户昵称不存在！");
			}
			///////////////////////////
			else if(!dbUtils.checkPassword(user_nickname, user_password))//查看用户名和密码是否匹配
			{
				data.setCode(101);
				data.setMsg("密码错误！");
				data.setData(userBean);
				System.out.println("密码错误！");
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
				data.setMsg("登录成功！");
			}
			else
			{
				data.setCode(11);
				data.setMsg("数据库错误！");
				data.setData(userBean);
			}
		}
		else
		{
			if(!dbUtils.checkAdminPassword(user_nickname, user_password))//查看用户名和密码是否匹配
			{
				data.setCode(101);
				data.setMsg("密码错误！");
				data.setData(userBean);
				System.out.println("密码错误！");
			}
			else if(dbUtils.checkAdminPassword(user_nickname, user_password))
			{

				userBean.setId(Integer.parseInt(user_nickname));
				userBean.setPassword(user_password);
				data.setCode(1);
				data.setData(userBean);
				data.setMsg("登录成功！");
			}
			else
			{
				data.setCode(11);
				data.setMsg("数据库错误！");
				data.setData(userBean);
			}
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(data);
		System.out.println(json);
		// 将对象转化成json字符串
		try 
		{
			response.getWriter().println(json);
			// 将json数据传给客户端
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			response.getWriter().close(); // 关闭这个流，不然会发生错误的
		}
		dbUtils.closeConnect(); // 关闭数据库连接}
	}
}
