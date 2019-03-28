package cn.pconline;

public class JavaFieldGetSet {
	  /**
     * 属性源码信息，如：private int id;
     */
    private String fieldInfo;
    /**
     * get方法的源码信息，如：public int getId();
     */
    private String getInfo;
    /**
     * set方法的源码信息，如：public void setId(int id);
     */
    private String setInfo;

    public JavaFieldGetSet() { }

	public JavaFieldGetSet(String fieldInfo, String getInfo, String setInfo) {
		super();
		this.fieldInfo = fieldInfo;
		this.getInfo = getInfo;
		this.setInfo = setInfo;
	}

	public String getFieldInfo() {
		return fieldInfo;
	}

	public void setFieldInfo(String fieldInfo) {
		this.fieldInfo = fieldInfo;
	}

	public String getGetInfo() {
		return getInfo;
	}

	public void setGetInfo(String getInfo) {
		this.getInfo = getInfo;
	}

	public String getSetInfo() {
		return setInfo;
	}

	public void setSetInfo(String setInfo) {
		this.setInfo = setInfo;
	}
	
}
