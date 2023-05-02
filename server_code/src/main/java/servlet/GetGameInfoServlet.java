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
import domain.BofBean;
import domain.GameBean;
import domain.UserBean;
import database.DBUtils;
import com.google.gson.Gson;

public class GetGameInfoServlet extends HttpServlet {

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
		String gameName=request.getParameter("game_name");
		response.setContentType("text/html;charset=utf-8");
		//---------------for debug-----------
		System.out.println("game request:\n"+
				gameName
				+"\n");
		//------------------end----------------
		// �������ݿ�
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		// �����ݿ�����
		BaseBean data = new BaseBean(); // ������󣬻ش����ͻ��˵�json����
		GameBean gameBean=new GameBean();
		gameBean=dbUtils.getGameBean(gameName);
//		bofBean=dbUtils.getBofBean(bofName);
//		bofBean=dbUtils.getMusicInfo(bofBean.getFirstId(),bofBean.getSecondId(),bofBean.getThirdId(),bofBean);
		
		data.setData(gameBean);
		data.setCode(1);
		
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
