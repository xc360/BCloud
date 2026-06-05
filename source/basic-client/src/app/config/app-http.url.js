// 请求地址配置
import environments from './environments';

let PATH = environments.config.url;
let BASIC = PATH + environments.config.basicPath; // 基础服务
const HTTP_URLS = {
    createMyAppToken: BASIC + '/my_app/oauth/token', // 创建token
    createMyAppRegister: BASIC + '/my_app/register', // 注册
    createMyAppForgetPassword: BASIC + '/my_app/forget_password', // 找回密码
    getMyAppVerifyAccount: BASIC + '/my_app/verify_account/{accountType}', // 验证账号
    getMyAppImgCaptcha: BASIC + '/my_app/img_captcha', // 获取图片验证码
    createMyAppCaptcha: BASIC + '/my_app/captcha/{messageCode}/{accountType}', // 发送验证码
};
export default HTTP_URLS;


