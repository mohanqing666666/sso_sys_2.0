package com.baomidou.kisso.jfinal;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.kisso.AuthToken;
import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.SSOToken;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.common.SSOProperties;
import com.jfinal.core.Controller;

/**
 * 
 * @author moshuai
 *
 */
public class LoginController extends Controller {
	/**
	* <p>Title: LoginController.java</p>  
	* <p>Description: 子系统登录，重定向到sso认证</p>  
	* @author moshuai
	 * @throws Exception 
	* @date 2019年8月11日
	 */
	public void index() {
		SSOProperties prop = SSOConfig.getSSOProperties();
		SSOToken token = SSOHelper.getToken(getRequest());
		if ( token == null) {
			/**
			 * 重定向至代理跨域地址页
			 */
			redirect(prop.get("sso.login.url")+"?ReturnURL="+prop.get("my.login.url"));
			return;
		}else {
			setAttr("userId", token.getUid());//用于登录成功后名称相似
		}
		render("index.html");
	}
	/**
	 * 
	* <p>Title: LoginController.java</p>  
	* <p>Description: 从sso获取的token</p>  
	* @author moshuai
	 * @throws Exception 
	* @date 2019年8月16日
	 */
	public void replyToken() throws Exception {
		Token token = SSOHelper.getToken(this.getRequest());
		setAttr("userId", token.getUid());//用于登录成功后名称相似
		render("index.html");
	}
	

   

	
	
}
