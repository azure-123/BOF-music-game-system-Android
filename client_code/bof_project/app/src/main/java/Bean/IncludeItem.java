package Bean;

public class IncludeItem {
    private String musicName;
    private String level1;
    private String level2;
    private String level3;
    private String level4;
    private int flag;

    public IncludeItem(String musicName,String level1,String level2,String level3,String level4,int flag)
    {
        this.musicName=musicName;
        this.level1=level1;
        this.level2=level2;
        this.level3=level3;
        this.level4=level4;
        this.flag=flag;
    }
    public String getMusicName() {
        return musicName;
    }
    public String getLevel1()
    {
        return level1;
    }

    public String getLevel2() {
        return level2;
    }

    public String getLevel3() {
        return level3;
    }

    public String getLevel4() {
        return level4;
    }

    public int getFlag() {
        return flag;
    }

    public void setMusicName(String musicName)
    {
        this.musicName=musicName;
    }
    public void setLevel1(String level1)
    {
        this.level1=level1;
    }
    public void setLevel2(String level2)
    {
        this.level2=level2;
    }
    public void setLevel3(String level3)
    {
        this.level3=level3;
    }
    public void setLevel4(String level4)
    {
        this.level4=level4;
    }
    public void setFlag(int flag)
    {
        this.flag=flag;
    }

}
