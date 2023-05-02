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
//		String user_password=request.getParameter("user_password");//客户端传来注册密码
//		String user_nickname=request.getParameter("user_nickname");//客户端传来注册昵称
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
		// 请求数据库
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		// 打开数据库连接
		BaseBean data = new BaseBean(); // 基类对象，回传给客户端的json对象
		if(!dbUtils.insertLike(user_id,music_id,like_flag))
		{
			data.setCode(1);
			data.setMsg("收藏成功！");
			System.out.println("插入收藏数据成功！");
			
			//将用户昵称和用户密码插入数据库
			//dbUtils.insertFrailData(userid, score, strength, health);
		}
		else
		{
			data.setCode(11);
			data.setMsg("数据库错误！");
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(data);
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
