import environments from './environments';

let PATH = environments.config.url;
let PAGE = PATH + environments.config.pagePath;
if (!environments.config.pagePath) {
	PAGE = location.origin;
}
const PAGE_URLS = {
	basicRegister: PAGE + '/register',
	basicOauthLogin: PAGE + '/oauth/login',
	basicForget: PAGE + '/forget',
	basicUserAgreement: PAGE + '/agreement/user',
	basicPrivacyAgreement: PAGE + '/agreement/privacy',
	basicCaptcha: PAGE + "/captcha"
};
export default PAGE_URLS;
