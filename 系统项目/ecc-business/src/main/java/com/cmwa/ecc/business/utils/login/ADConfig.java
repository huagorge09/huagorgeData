package com.cmwa.ecc.business.utils.login;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import com.cmwa.ec.base.util.SpringUtil;

/**
 * <p>Title: Legion EBank</p>
 *
 * <p>Description: Legion 银行接口</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: Legion Technology</p>
 *
 * @author xuhw
 * @version 1.0
 */

public class ADConfig{
    
	private final Map<String,String> props = new HashMap<String,String>();
	
    public ADConfig()
            throws Exception {
        try {
        	//String appHome=System.getProperty("appHome");
            // props = new LoadProperties(appHome+"/ADcheckResources");
        	props.clear();
        	props.put("WINAD_URL", SpringUtil.getProperty("WINAD_URL"));
        	props.put("WINAD_DOMAIN", SpringUtil.getProperty("WINAD_DOMAIN"));
        	props.put("WINAD_JNDI", SpringUtil.getProperty("WINAD_JNDI"));
        }catch (MissingResourceException e) {
            throw new Exception("域验证配置文件不存在！详细描述：" + e.getMessage());

        }
    }

    /**
     * 获取URL
     * @return String
     * @throws Exception
     */
    public String getURL() throws Exception {
        try {
            return props.get("WINAD_URL");
        }
        catch (MissingResourceException e) {
            throw new Exception("域验证配置文件中无法获取到URL！详细描述：" + e.getMessage());
        }
        catch (Exception uee) {
            throw new Exception("域验证配置文件中无法获取到URL！详细描述：" + uee.getMessage());
        }
    }

    /**
     * 获取DOMAIN
     * @return String
     * @throws Exception
     */
    public String getDomain()
            throws Exception {
        try {
            return props.get("WINAD_DOMAIN");
        }
        catch (MissingResourceException e) {
            throw new Exception("域验证配置文件中无法获取到DOMAIN！详细描述：" + e.getMessage());
        }
        catch (Exception uee) {
            throw new Exception("域验证配置文件中无法获取到DOMAIN！详细描述：" + uee.getMessage());
        }
    }

    /**
     * 获取JNDI
     * @return String
     * @throws Exception
     */
    public String getJNDI()
            throws Exception {
        try {
            return props.get("WINAD_JNDI");
        }
        catch (MissingResourceException e) {
            throw new Exception("域验证配置文件中无法获取到JNDI！详细描述：" + e.getMessage());
        }
        catch (Exception uee) {
            throw new Exception("域验证配置文件中无法获取到JNDI！详细描述：" + uee.getMessage());
        }
    }
}
