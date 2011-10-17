/**
 * 
 */
package org.techno.blackthree.common.event;

import java.io.Serializable;

/**
 * @author Bageshwar
 *
 */
public class GameEvent implements Serializable {

	String code;
	
	Object payLoad;
	/**
	 * @return the payLoad
	 */
	public Object getPayLoad() {
		return payLoad;
	}

	/**
	 * @param payLoad the payLoad to set
	 */
	public void setPayLoad(Object payLoad) {
		this.payLoad = payLoad;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public GameEvent(String code,Object payload){
		this.code = code;
		payLoad = payload;
	}
	public GameEvent(String code) {
		this(code,null);
		
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public String toString() {
	return this.code+"[ "+payLoad+" ]";
	}
}
