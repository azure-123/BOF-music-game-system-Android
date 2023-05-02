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
		String user_id=request.getParameter("user_id");//客户端传来注册密码
		String music_id=request.getParameter("music_id");//客户端传来注册昵称

		response.setContentType("text/html;charset=utf-8");
		//---------------for debug-----------
		System.out.println("Like data:"+
				"\nuser id:"+user_id+
				"\nmusic id:"+music_id+
				"\n");
		//------------------end----------------
		// 请求数据库
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		// 打开数据库连接
		BaseBean data = new BaseBean(); // 基类对象，回传给客户端的json对象
		UserBean userBean = new UserBean(); // user的对象
		
		//查看收藏记录是否存在
		if(!dbUtils.checkLikeExists(user_id,music_id))
		{
			data.setCode(2);
			data.setData(userBean);
			data.setMsg("该曲目未收藏！");
		}
		///////////////////////////
		else if(dbUtils.checkLikeExists(user_id,music_id))
		{
			data.setCode(3);
			data.setMsg("该曲目已收藏！");
			data.setData(userBean);
		}
		else
		{
			data.setCode(11);
			data.setMsg("数据库错误！");
			data.setData(userBean);
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
