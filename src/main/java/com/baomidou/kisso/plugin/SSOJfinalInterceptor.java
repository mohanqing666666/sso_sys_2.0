
package com.baomidou.kisso.plugin;

import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;
import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.SSOToken;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.common.SSOProperties;
import com.baomidou.kisso.common.encrypt.RSA;
import com.baomidou.kisso.common.util.Base64Util;
import com.baomidou.kisso.web.interceptor.KissoAbstractInterceptor;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 登录权限验证
 * <p>
 *  拦截器，Controller 方法调用前处理。
 *  过滤controller中请求，目的校验获取token
 * </p>
 * 
 */
public class SSOJfinalInterceptor extends KissoAbstractInterceptor implements Interceptor {

	private static final Logger logger = Logger.getLogger("SSOJfinalInterceptor");


	@Override
	public void intercept(Invocation inv) {
		Controller con = inv.getController();
		Token token = SSOHelper.getToken(con.getRequest());
		if(null == token) {
			SSOProperties prop = SSOConfig.getSSOProperties();
			String tokenStr =con.getPara("token");
			System.out.println(tokenStr);
			if(null == tokenStr || "".equals(tokenStr)) {
				con.redirect(prop.get("sso.login.url")+"?ReturnURL="+prop.get("my.login.url"));
			} else {
				try {
					byte[] decryptBytes = Base64Util.decode(tokenStr);
					decryptBytes = RSA.decryptByPublicKey(decryptBytes, prop.get("sso.defined.sso_public_key"));
					String result = new String(decryptBytes);
//					System.out.println("公钥解密结果： " + result);
					SSOToken ssoToken = JSON.parseObject(result, SSOToken.class);
					SSOHelper.setSSOCookie(con.getRequest(), con.getResponse(), ssoToken, false);
					String url = con.getRequest().getRequestURI();
					con.redirect(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			inv.invoke();
		}
	}


}
