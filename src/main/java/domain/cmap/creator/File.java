package domain.cmap.creator;

public class File {
	private String name;
	private String path;
	
	
	public File(String name2, String path2) {
		this.name= name2;
		this.path=path2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
