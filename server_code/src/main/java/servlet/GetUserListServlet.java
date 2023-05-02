package servlet;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class GetUserListServlet extends HttpServlet {

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
		response.setContentType("text/html;charset=utf-8");
		// �������ݿ�
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		// �����ݿ�����
		BaseBean data = new BaseBean(); // ������󣬻ش����ͻ��˵�json����
		//����������ƣ���øñ����������б�
		ResultSet rs=dbUtils.getUserList();
		//��List��װ���ص�����
		List<Map<String,Object>> userList=new ArrayList<Map<String,Object>>();

		if (rs != null) 
		{
			try 
			{
				while (rs.next()) 
				{
					//��HashMapװ��������
					Map<String,Object> map=new HashMap<String,Object>();
				
//					map.put("musicId", rs.getInt("music_id"));
//					map.put("musicName", rs.getString("music_name"));
//					map.put("musicCreator", rs.getString("music_creator"));
//					map.put("musicLink", rs.getString("music_link"));
					map.put("userId",rs.getInt("user_id"));
					map.put("userNickname",rs.getString("user_nickname"));
					System.out.println("Success!");
					userList.add(map);
				}
				data.setData(userList);
				data.setCode(1);
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			data.setCode(101);
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
