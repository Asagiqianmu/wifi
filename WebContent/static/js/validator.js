function verifyMobile(val) {
	Alert(val);
	var regex = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1})|(19[0-9]{1}))+\d{8})$/g;
	if (!regex.test(val)) {
		console.log("10");
		return false;
	}
	return true;
	console.log("11");
}