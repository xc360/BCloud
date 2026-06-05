import {HttpTool, StorageTool} from '@ccxc/tool';
import HTTP_URLS from '../../config/app-http.url';
import CommonFun from '../../common/common.fun.js';
import XcDialog from '../../common/xc-dialog';
import '../../common/http.interceptor';
import 'babel-polyfill';
import '../../../assets/plugins/alIcon/iconfont.js';
import '../../../assets/css/assembly.css';
import '../../../assets/css/common.css';
import './login.less';
import '../../common/footer/footer.js';
//密码登录
const passwordLogin = document.getElementById('passwordLogin');
const passwordLoginForm = document.getElementById('passwordLoginForm');
const account = document.getElementById('account');
const password = document.getElementById('password');
const rememberPassword = document.getElementById('rememberPassword');
const rememberPasswordBlock = document.getElementById('rememberPasswordBlock');

// 手机登录
const phoneLogin = document.getElementById('phoneLogin');
const phoneLoginForm = document.getElementById('phoneLoginForm');
const phone = document.getElementById('phone');
const phoneCaptcha = document.getElementById('phoneCaptcha');
const phoneCaptchaButton = document.getElementById('phoneCaptchaButton');
const phoneAgreementBlock = document.getElementById('phoneAgreementBlock');
const phoneAgreement = document.getElementById('phoneAgreement');

// 邮箱登录
const emailLogin = document.getElementById('emailLogin');
const emailLoginForm = document.getElementById('emailLoginForm');
const email = document.getElementById('email');
const emailCaptcha = document.getElementById('emailCaptcha');
const emailCaptchaButton = document.getElementById('emailCaptchaButton');
const emailAgreementBlock = document.getElementById('emailAgreementBlock');
const emailAgreement = document.getElementById('emailAgreement');

// 验证码
const imgCaptcha = document.getElementById('imgCaptcha');
const imgCaptchaImg = document.getElementById('imgCaptchaImg');
const xcDialog = new XcDialog('eject', 'imgCaptchaConfirm', 'imgCaptchaClose');
// 注册协议跳转
const userAgreementArray = document.getElementsByClassName('userAgreement');
for (let userAgreement of userAgreementArray) {
    if (userAgreement) {
        userAgreement.addEventListener('click', function () {
            CommonFun.jumpPage('userAgreement');
        }, false);
    }
}
// 隐私协议跳转
const privacyAgreementArray = document.getElementsByClassName('privacyAgreement');
for (let privacyAgreement of privacyAgreementArray) {
    if (privacyAgreement) {
        privacyAgreement.addEventListener('click', function () {
            CommonFun.jumpPage('privacyAgreement');
        }, false);
    }
}
imgCaptchaImg.addEventListener('click', function (e) {
    CommonFun.getImgCaptcha(function (url, code) {
        xcDialog['code'] = code;
        imgCaptchaImg.src = url;
        imgCaptcha.value = '';
    });
}, false);
xcDialog.confirmCallback((that) => {
    if (that.type === 'passwordLogin') {
        if (!imgCaptcha.value) {
            alert('图片验证码必填！');
            return;
        }
        // 提交
        const appId = document.getElementById('appId').innerText;
        // 权限处理
        const authorityIds = [];
        const classNameList = document.getElementsByClassName("authorityId");
        for (const dom of classNameList) {
            if (dom.checked) {
                authorityIds.push(dom.value)
            }
        }
        HttpTool.post(HTTP_URLS.createMyAppToken, {
            accountType: CommonFun.accountType,
            account: account.value,
            password: password.value,
            captcha: imgCaptcha.value,
            code: xcDialog['code'],
            appId,
            authorityIds
        }).then(res => {
            loginSuccess(res)
        }).catch(res => {
            if (res.code === 'captchaError') {
                CommonFun.getImgCaptcha(function (url, code) {
                    xcDialog.open(that.type);
                    xcDialog['code'] = code;
                    imgCaptchaImg.src = url;
                    imgCaptcha.value = '';
                });
                return false;
            } else {
                xcDialog.close(that.type);
                return true;
            }
        });
    } else if (that.type === 'phoneCaptcha') {
        if (!imgCaptcha.value) {
            alert('图片验证码必填！');
            return;
        }
        CommonFun.sendCaptcha(phoneCaptchaButton, phone.value, 'phone_login', {
            captcha: imgCaptcha.value,
            code: xcDialog['code']
        }, (res) => {
            if (res['openImgCaptcha']) {
                alert('图片验证码错误！');
                CommonFun.getImgCaptcha(function (url, code) {
                    xcDialog.open(that.type);
                    xcDialog['code'] = code;
                    imgCaptchaImg.src = url;
                    imgCaptcha.value = '';
                });
                return false;
            } else {
                xcDialog.close(that.type);
                return true;
            }
        });
    } else if (that.type === 'emailCaptcha') {
        if (!imgCaptcha.value) {
            alert('图片验证码必填！');
            return;
        }
        CommonFun.sendCaptcha(emailCaptchaButton, email.value, 'email_login', {
            captcha: imgCaptcha.value,
            code: xcDialog['code']
        }, (res) => {
            if (res['openImgCaptcha']) {
                alert('图片验证码错误！');
                CommonFun.getImgCaptcha(function (url, code) {
                    xcDialog.open(that.type);
                    xcDialog['code'] = code;
                    imgCaptchaImg.src = url;
                    imgCaptcha.value = '';
                });
                return false;
            } else {
                xcDialog.close(that.type);
                return true;
            }
        });
    }
});
xcDialog.closeCallback(() => {
    imgCaptcha.value = '';
    return true;
});

// 登录提交
const submitDom = document.getElementById('submit');
// 跳转找回密码
const forgetJump = document.getElementById('forgetJump');
forgetJump.addEventListener('click', function () {
    CommonFun.jumpPage('forget');
}, false);
// 跳转注册
const registerJump = document.getElementById('registerJump');
registerJump.addEventListener('click', function () {
    CommonFun.jumpPage('register');
}, false);
// 验证账号，true：通过，false：不通过
CommonFun.isAccount = false;
if (!CommonFun.isCaptch) {
    CommonFun.isCaptch = {
        password: true,
        email: true,
        phone: true
    };
}

// 验证码code
function init(type) {
    // 邮箱
    if (emailLogin) {
        emailLogin.className = 'email-login';
        emailLoginForm.className = 'email-login-form';
        emailAgreementBlock.className = 'input-checkbox';
    }
    // 手机
    if (phoneLogin) {
        phoneLogin.className = 'phone-login';
        phoneLoginForm.className = 'phone-login-form';
        phoneAgreementBlock.className = 'input-checkbox';
    }
    // 密码
    if (passwordLogin) {
        passwordLogin.className = 'password-login';
        passwordLoginForm.className = 'password-login-form';
    }

    switch (type) {
        case 'password':
            rememberPasswordBlock.style.display = 'block';
            CommonFun.accountType = 'password';
            passwordLogin.className = 'password-login selected';
            passwordLoginForm.className = 'password-login-form open';
            break;
        case 'email':
            rememberPasswordBlock.style.display = 'none';
            CommonFun.accountType = 'email';
            emailLogin.className = 'email-login selected';
            emailLoginForm.className = 'email-login-form open';
            break;
        case 'phone':
            rememberPasswordBlock.style.display = 'none';
            CommonFun.accountType = 'phone';
            phoneLogin.className = 'phone-login selected';
            phoneLoginForm.className = 'phone-login-form open';
            break;

    }
    // 配置
    CommonFun.isAccount = false;
    CommonFun.isCaptch[type] = true;
}

/********************密码登录********************/
if (passwordLogin) {
    if (passwordLogin.className.indexOf('selected') !== -1) {
        CommonFun.accountType = 'password';
    }
    passwordLogin.addEventListener('click', function () {
        init('password');
    }, false);
    // 验证输入框
    account.addEventListener('blur', verifyInput, false);
    password.addEventListener('blur', verifyInput, false);
}

/********************手机登录********************/
if (phoneLogin) {
    if (phoneLogin.className.indexOf('selected') !== -1) {
        CommonFun.accountType = 'phone';
    }
    phoneLogin.addEventListener('click', function () {
        init('phone');
    }, false);
    // 获取手机验证码
    phoneCaptchaButton.addEventListener('click', function () {
        CommonFun.sendCaptcha(phoneCaptchaButton, phone.value, 'phone_login', {}, (res) => {
            if (res['openImgCaptcha']) {
                CommonFun.getImgCaptcha(function (url, code) {
                    xcDialog.open('phoneCaptcha');
                    xcDialog['code'] = code;
                    imgCaptchaImg.src = url;
                    imgCaptcha.value = '';
                });
                return false;
            } else {
                return true;
            }
        });
    }, false);
    // 验证输入框
    phone.addEventListener('blur', verifyInput, false);
    phoneCaptcha.addEventListener('blur', verifyInput, false);
    phoneAgreement.addEventListener('change', verifyInput, false);
}

/********************邮箱登录********************/
if (emailLogin) {
    if (emailLogin.className.indexOf('selected') !== -1) {
        CommonFun.accountType = 'email';
    }
    emailLogin.addEventListener('click', function () {
        init('email');
    }, false);
    // 获取邮箱验证码
    emailCaptchaButton.addEventListener('click', function () {
        CommonFun.sendCaptcha(emailCaptchaButton, email.value, 'email_login', {}, (res) => {
            if (res['openImgCaptcha']) {
                CommonFun.getImgCaptcha(function (url, code) {
                    xcDialog.open('emailCaptcha');
                    xcDialog['code'] = code;
                    imgCaptchaImg.src = url;
                    imgCaptcha.value = '';
                });
                return false;
            } else {
                return true;
            }
        });
    }, false);
    // 验证输入框
    email.addEventListener('blur', verifyInput, false);
    emailCaptcha.addEventListener('blur', verifyInput, false);
    emailAgreement.addEventListener('change', verifyInput, false);
}

/********************登录提交********************/
submitDom.addEventListener('click', function () {
    submit();
}, false);
// 回车事件
let bool = true;
document.addEventListener('keyup', function (e) {
    if (e.keyCode === 13 && bool) {
        submit();
    }
});
imgCaptcha.addEventListener('keyup', function (e) {
    if (e.keyCode === 13 && bool) {
        document.getElementById('imgCaptchaConfirm').click();
    }
});

// 回填记住密码
const appId = document.getElementById('appId').innerText;
const userInfoKey = CommonFun.base64Encode('userInfoKey' + appId);
const userInfoStr = StorageTool.getStorage(userInfoKey);
if (userInfoStr) {
    const userInfo = JSON.parse(CommonFun.base64Decode(userInfoStr));
    account.value = userInfo.account;
    password.value = userInfo.password;
    rememberPassword.setAttribute('checked', 'true');
}

/**
 * 登录提交
 */
function submit() {
    const appId = document.getElementById('appId').innerText;
    // 权限处理
    const authorityIds = [];
    const classNameList = document.getElementsByClassName("authorityId");
    for (const dom of classNameList) {
        if (dom.checked) {
            authorityIds.push(dom.value)
        }
    }
    switch (CommonFun.accountType) {
        case 'password':
            // 验证账号
            const accountDom = CommonFun.getErrorDom(account);
            if (!account.value) {
                accountDom.innerText = '账号不能为空！';
                accountDom.className = 'error open';
                return;
            }
            // 验证密码
            const passwordDom = CommonFun.getErrorDom(password);
            if (!password.value) {
                passwordDom.innerText = '密码不能为空！';
                passwordDom.className = 'error open';
                return;
            }
            if (password.value.length < 6) {
                passwordDom.innerText = '密码不能小于6位！';
                passwordDom.className = 'error open';
                return;
            }
            // 验证账号
            CommonFun.verifyAccount(account, 'password', function (data) {
                if (!data['accountCorrect']) {
                    CommonFun.isAccount = false;
                    accountDom.innerText = '账号不存在！';
                    accountDom.className = 'error open';
                    return;
                }
                if (data['openCaptcha']) {
                    CommonFun.getImgCaptcha(function (url, code) {
                        xcDialog.open('passwordLogin');
                        xcDialog['code'] = code;
                        imgCaptchaImg.src = url;
                        imgCaptcha.value = '';
                    });
                    return;
                }
                // 提交
                HttpTool.post(HTTP_URLS.createMyAppToken, {
                    accountType: CommonFun.accountType,
                    account: account.value,
                    password: password.value,
                    appId,
                    authorityIds
                }).then(res => {
                    loginSuccess(res);
                });
            });
            break;
        case 'phone':
            const phoneDom = CommonFun.getErrorDom(phone);
            if (!phone.value) {
                phoneDom.innerText = '手机号不能为空！';
                phoneDom.className = 'error open';
                return;
            }
            const phoneCaptchaDom = CommonFun.getErrorDom(phoneCaptcha);
            if (!phoneCaptcha.value) {
                phoneCaptchaDom.innerText = '验证码不能为空！';
                phoneCaptchaDom.className = 'error open';
                return;
            }
            if (!CommonFun.isAccount) {
                const phoneAgreementDom = CommonFun.getErrorDom(phoneAgreement);
                if (!phoneAgreement.checked) {
                    phoneAgreementDom.innerText = '协议必须勾选';
                    phoneAgreementDom.className = 'error open';
                    return;
                }
            }
            // 提交
            HttpTool.post(HTTP_URLS.createMyAppToken, {
                accountType: CommonFun.accountType,
                account: phone.value,
                captcha: phoneCaptcha.value,
                appId,
                authorityIds
            }).then(res => {
                loginSuccess(res);
            });
            break;
        case 'email':
            const emailDom = CommonFun.getErrorDom(email);
            if (!email.value) {
                emailDom.innerText = '邮箱不能为空！';
                emailDom.className = 'error open';
                return;
            }
            const emailCaptchaDom = CommonFun.getErrorDom(emailCaptcha);
            if (!emailCaptcha.value) {
                emailCaptchaDom.innerText = '验证码不能为空！';
                emailCaptchaDom.className = 'error open';
                return;
            }
            if (!CommonFun.isAccount) {
                const emailAgreementDom = CommonFun.getErrorDom(emailAgreement);
                if (!emailAgreement.checked) {
                    emailAgreementDom.innerText = '协议必须勾选';
                    emailAgreementDom.className = 'error open';
                    return;
                }
            }
            // 提交
            HttpTool.post(HTTP_URLS.createMyAppToken, {
                accountType: CommonFun.accountType,
                account: email.value,
                captcha: emailCaptcha.value,
                appId,
                authorityIds
            }).then(res => {
                loginSuccess(res);
            });
    }
}

/**
 * 登录成功
 */
function loginSuccess(res) {
    if (rememberPassword.checked) {
        const userInfo = {account: account.value, password: password.value};
        const userInfoStr = CommonFun.base64Encode(JSON.stringify(userInfo));
        StorageTool.setStorage(userInfoKey, userInfoStr);
    } else {
        StorageTool.deleteStorage(userInfoKey);
    }
    const state = document.getElementById('state').innerText;
    // 适配器，访问其他
    if (window.adapter && window.adapter.loginSuccess) {
        const data = {state, code: res['code']};
        window.adapter.loginSuccess(JSON.stringify(data));
        return;
    }
    // 适配器，访问安卓
    if (window.android && window.android.loginSuccess) {
        const data = {state, code: res['code']};
        window.android.loginSuccess(JSON.stringify(data));
        return;
    }
    const redirectUri = document.getElementById('redirectUri').innerText;
    location.href = redirectUri + '?code=' + res['code'] + '&state=' + state;
}

/**
 * 验证输入框
 */
function verifyInput() {
    const dom = CommonFun.getErrorDom(this);
    switch (this.id) {
        case 'account':
            if (this.value) {
                CommonFun.verifyAccount(this, 'password', function (data) {
                    if (!data['accountCorrect']) {
                        CommonFun.isAccount = false;
                        dom.innerText = '账号不存在！';
                        dom.className = 'error open';
                    } else {
                        dom.innerText = '';
                        dom.className = 'error';
                        CommonFun.isAccount = true;
                    }
                });
            } else {
                dom.innerText = '';
                dom.className = 'error';
            }
            break;
        case 'captcha':
            dom.innerText = '';
            dom.className = 'error';
            break;
        case 'password':
            if (!this.value) {
                dom.innerText = '';
                dom.className = 'error';
            } else if (this.value.length < 6) {
                dom.innerText = '密码不能小于6位！';
                dom.className = 'error open';
            } else {
                dom.innerText = '';
                dom.className = 'error';
            }
            break;
        case 'phone':
            if (this.value) {
                if (this.value.length !== 11 || this.value[0] !== '1') {
                    dom.innerText = '手机号格式不正确！';
                    dom.className = 'error open';
                } else {
                    dom.innerText = '';
                    dom.className = 'error';
                    CommonFun.verifyAccount(this, CommonFun.accountType, function (data) {
                        if (!data['accountCorrect']) {
                            CommonFun.isAccount = false;
                            phoneAgreementBlock.className = 'input-checkbox open';
                        } else {
                            phoneAgreementBlock.className = 'input-checkbox';
                            CommonFun.isAccount = true;
                        }
                    });
                }
            } else {
                dom.innerText = '';
                dom.className = 'error';
            }
            break;
        case 'emailCaptcha':
            dom.innerText = '';
            dom.className = 'error';
            break;
        case 'phoneAgreement':
            if (!this.checked) {
                dom.innerText = '协议必须勾选';
                dom.className = 'error open';
                return;
            } else {
                dom.innerText = '';
                dom.className = 'error';
            }
            break;
        case 'email':
            if (this.value) {
                if (!(this.value.indexOf('@') !== -1 && this.value.indexOf('.') !== -1)) {
                    dom.innerText = '邮箱格式不正确！';
                    dom.className = 'error open';
                } else {
                    dom.innerText = '';
                    dom.className = 'error';
                    CommonFun.verifyAccount(this, CommonFun.accountType, function (data) {
                        if (!data['accountCorrect']) {
                            CommonFun.isAccount = false;
                            emailAgreementBlock.className = 'input-checkbox open';
                        } else {
                            emailAgreementBlock.className = 'input-checkbox';
                            CommonFun.isAccount = true;
                        }
                    });
                }
            } else {
                dom.innerText = '';
                dom.className = 'error';
            }
            break;
        case 'phoneCaptcha':
            dom.innerText = '';
            dom.className = 'error';
            break;
        case 'emailAgreement':
            if (!this.checked) {
                dom.innerText = '协议必须勾选';
                dom.className = 'error open';
                return;
            } else {
                dom.innerText = '';
                dom.className = 'error';
            }
            break;
    }
}
