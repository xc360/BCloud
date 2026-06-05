import HTTP_URLS from '../config/app-http.url';
import {CommonTool, HttpTool} from '@ccxc/tool';
import environments from '../config/environments';
import PAGE_URLS from '../config/app-page.url';


export default class CommonFun {
    /**
     * 获取显示错误信息的dom
     */
    static getErrorDom(that) {
        for (const childNode of that.parentNode.childNodes) {
            if (childNode.className && childNode.classList && childNode.classList.toString().indexOf('error') !== -1) {
                return childNode;
            }
        }
    }

    /**
     * 获取图片验证码
     */
    static getImgCaptcha(fun) {
        HttpTool.get(HTTP_URLS.getMyAppImgCaptcha).then(res => {
            const url = URL.createObjectURL(CommonTool.dataURItoBlob(res['imgBytes']));
            fun(url, res['code']);
        });
    }

    /**
     * 验证账号
     */
    static verifyAccount(that, type, fun) {
        let url = CommonTool.analysisUrl(HTTP_URLS.getMyAppVerifyAccount, {accountType: type});
        url = CommonTool.analysisParam(url, {account: that.value});
        HttpTool.get(url).then(res => {
            fun(res);
        });
    }

    /**
     * 编码
     */
    static base64Encode(data) {
        return window.btoa(data);
    }

    /**
     * 解码
     */
    static base64Decode(data) {
        return window.atob(data);
    }

    /**
     * 发送验证码
     */
    static sendCaptcha(captchaButton, account, messageCode, data, fun) {
        if (!account) {
            alert('账号不能为空！');
            return;
        }
        if (CommonFun.accountType === 'phone') {
            if (account.length !== 11 || account[0] !== '1') {
                alert('手机号格式不正确！');
                return;
            }
        }
        if (CommonFun.accountType === 'email') {
            if (!(account.indexOf('@') !== -1 && account.indexOf('.') !== -1)) {
                alert('邮箱格式不正确！');
                return;
            }
        }
        if (CommonFun.isCaptch[CommonFun.accountType]) {
            CommonFun.isCaptch[CommonFun.accountType] = false;
            let url = CommonTool.analysisUrl(HTTP_URLS.createMyAppCaptcha, {
                accountType: CommonFun.accountType,
                messageCode: messageCode
            });
            const appId = document.getElementById('appId').innerText;
            url = CommonTool.analysisParam(url, {appId});
            HttpTool.post(url, {
                account,
                captcha: data ? data.captcha : null,
                code: data ? data.code : null
            }).then(res => {
                if ((!fun || fun(res))) {
                    let index = 60;
                    const captchaInterval = setInterval(() => {
                        index--;
                        if (index <= 0) {
                            captchaButton.innerText = '获取验证码';
                            CommonFun.isCaptch[CommonFun.accountType] = true;
                            clearInterval(captchaInterval);
                        } else {
                            captchaButton.innerText = index + '秒后重试';
                        }
                    }, 1000);
                } else {
                    CommonFun.isCaptch[CommonFun.accountType] = true;
                }
            }).catch(() => {
                CommonFun.isCaptch[CommonFun.accountType] = true;
            });
        }
    }

    /**
     * 加载组件
     * @param array
     */
    static load(...array) {
        for (let arr of array) {
            arr.initial();
        }
    }

    /**
     * 跳转页面
     */
    static jumpPage(type) {
        const appId = document.getElementById('appId').innerText;
        let redirectUri = document.getElementById('redirectUri').innerText;
        const state = document.getElementById('state').innerText;
        let url = PAGE_URLS.basicOauthLogin;
        if (type === 'forget') {
            url = PAGE_URLS.basicForget;
        } else if (type === 'register') {
            url = PAGE_URLS.basicRegister;
        } else if (type === 'userAgreement') {
            url = PAGE_URLS.basicUserAgreement;
        } else if (type === 'privacyAgreement') {
            url = PAGE_URLS.basicPrivacyAgreement;
        }
        location.href = CommonTool.analysisParam(url, {appId, redirectUri, state});
    }
}

