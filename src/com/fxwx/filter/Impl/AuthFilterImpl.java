package com.fxwx.filter.Impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.View;
import org.nutz.mvc.view.JspView;
import org.nutz.mvc.view.ViewWrapper;

import com.fxwx.entiy.PortalUser;
import com.fxwx.filter.AuthFilter;

public class AuthFilterImpl implements AuthFilter{
	private static Logger logger = Logger.getLogger(AuthFilterImpl.class);

	@Override
	public View match(ActionContext context) {
		String path=context.getPath();
		HttpServletRequest req=context.getRequest();
		Map<String, String[]> params=req.getParameterMap();
		PortalUser user=(PortalUser) req.getSession().getAttribute("user");
		logger.error("-----"+path);
		if("/".equals(path)&&user!=null){
			return new ViewWrapper(new JspView("/error/error"), "null");
		}
		if(!("/PROT/unifiedEntrance".equals(path)||"/PROT/unifyingTurn".equals(path)||"/PROT/toSwitch".equals(path)
				||"/PROT/forgetPass".equals(path)||"/PROT/uniteLogin".equals(path)||"/PROT/toSpecalLogin".equals(path)
				||"/personal/perfectInformation".equals(path)||"/priceConfig/getMeal".equals(path)
				||"/quickPayment/payNotify".equals(path)||"/priceConfig/payResult".equals(path)
				||"/pay/aliPayNotify".equals(path)||"/priceConfig/wechatResult".equals(path)
				||"/pcQuickPayment/pcPayNotify".equals(path)
				||"/wechatpayPc/wechatpayPc".equals(path)||"/wechatpayPc/towechatpayPc".equals(path)
				||"/PROT/wechatNotify".equals(path)
				||"/PROT/common_problem".equals(path)
				||"/PROT/common_problemA".equals(path)
				||"/PROT/instruction".equals(path)
				||"/PROT/instructionA".equals(path)
				||"/PROT/feedback".equals(path)
				||"/PROT/save_feedback".equals(path)
				||"/PROT/homepage".equals(path)
				||"/PROT/userAgreement".equals(path)
				||"/PROT/userAgreementA".equals(path)
				||"/PROT/inquiry".equals(path)
				||"/PROT/goIosDownLoad".equals(path)
				||"/PROT/toAppLogin".equals(path)
				||"/PROT/test".equals(path)
				||"/PROT/addWanLanMac".equals(path)
				||"/PROT/temporaryRelease".equals(path)
				||"/PROT/acceptAppAuth".equals(path)
				||"/PROT/doOffLine".equals(path)
				||"/PROT/wechats".equals(path)
				||"/PROT/choice_tc".equals(path)
				||"/PROT/toWechatLogin".equals(path)
				||"/PROT/wechatJd".equals(path)
				||"/PROT/wechatpay".equals(path)
				||"/PROT/getAD".equals(path)
				||"/PROT/wxAuth".equals(path)       
				||"/PROT/checkWxUser".equals(path)
				||"/PROT/toH3CLogin".equals(path)
				||"/PROT/cashPledgeCheck".equals(path)
				||"/PROT/operation".equals(path)
				||"/PROT/cashPledge".equals(path)
				||"/PROT/unifiedorder".equals(path)
				||"/PROT/wechatpayNotify".equals(path)
				||"/PROT/wechatPayOrderQuery".equals(path)
				||"/whPc/wechartPayMonth".equals(path)
				||"/PROT/wechartPayCashPledge".equals(path)
				||"/PROT/useDateIsExpire".equals(path)
				||"/PROT/cashPledge".equals(path)
				||"/PROT/choiceTc".equals(path)
				||"/PROT/wechatchoiceTc".equals(path)
				||"/PROT/getphonenumber".equals(path)
				||"/PROT/payforMemberPack".equals(path)
				||"/PROT/wechatpayforMemberPack".equals(path)
				||"/PROT/getCommercialTenant".equals(path)
				||"/PROT/wechatGetUserInfo".equals(path)
				||"/PROT/wecahtToken".equals(path)
				||"/PROT/inputphone".equals(path)
				||"/PROT/loading".equals(path)
				||"/PROT/hint".equals(path)
				||"/PROT/slideshow".equals(path)
				||"/PROT/balance_inquiry".equals(path)
				||"/PROT/personalCenter".equals(path)
				||"/PROT/checkCharge".equals(path)
				||"/whPc/towechatpayMonth".equals(path))
				&&user==null){
			if(params==null||params.size()==0){
				return new ViewWrapper(new JspView("/error/error"), "null");
			}
			
		}
		if(req.getSession().getAttribute("legalUrl")==null){
			if("/PROT/goIosDownLoad".equals(path)||"/PROT/toAppLogin".equals(path)){
				return new ViewWrapper(new JspView("/error/error"), "null");
			}
		}
		return null;
	}
}

