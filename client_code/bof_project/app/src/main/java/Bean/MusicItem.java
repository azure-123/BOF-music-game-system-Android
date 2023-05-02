package Bean;

public class MusicItem {
    private String bofName;
    private String musicName;
    private String musicCreator;
    private int musicId;

    public MusicItem(String bofName,String musicName,String musicCreator)
    {
        this.bofName=bofName;
        this.musicName=musicName;
        this.musicCreator=musicCreator;
    }
    public MusicItem(int musicId,String musicName,String musicCreator)
    {
        this.musicId=musicId;
        this.musicName=musicName;
        this.musicCreator=musicCreator;
    }


    public void setBofName(String bofName) {
        this.bofName = bofName;
    }

    public String getBofName() {
        return bofName;
    }

    public void setMusicName(String musicName)
    {
        this.musicName=musicName;
    }
    public void setMusicCreator(String musicCreator)
    {
        this.musicCreator=musicCreator;
    }
    public String getMusicName()
    {
        return musicName;
    }
    public String getMusicCreator() {
        return musicCreator;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public int getMusicId() {
        return musicId;
    }
}
