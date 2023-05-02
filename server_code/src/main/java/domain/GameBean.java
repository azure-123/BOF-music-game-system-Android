package domain;

import java.util.List;
import java.util.Map;

public class GameBean {
	 
	 private String gamename;//
	 private String gamecname;
	 private String gametime;
	 private String gamecompany;
	 private String gameplatform;
	 private String gameintro;
	 
	 public void setGamename(String gamename)
	 {
		 this.gamename=gamename;
	 }
	 public String getGamename() 
	 {
		 return gamename;
	 }
	 public void setGamecname(String gamecname)
	 {
		 this.gamecname=gamecname;
	 }
	 public String getGamecname()
	 {
		 return gamecname;
	 }
	 public void setGametime(String gametime)
	 {
		 this.gametime=gametime;
	 }
	 public String getGametime()
	 {
		 return gametime;
	 }
	 public void setGamecompany(String gamecompany)
	 {
		 this.gamecompany=gamecompany;
	 }
	 public String getGamecompany()
	 {
		 return gamecompany;
	 }
	 public void setGameplatform(String gameplatform)
	 {
		 this.gameplatform=gameplatform;
	 }
	 public String getGameplatform()
	 {
		 return gameplatform;
	 }
	 public void setGameintro(String gameintro)
	 {
		 this.gameintro=gameintro;
	 }
	 public String getGameinfo()
	 {
		 return gameintro;
	 }
}
