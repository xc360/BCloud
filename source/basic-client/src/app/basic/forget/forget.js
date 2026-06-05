import HTTP_URLS from '../../config/app-http.url';
import CommonFun from '../../common/common.fun.js';
import '../../common/http.interceptor';
import 'babel-polyfill';
import '../../../assets/plugins/alIcon/iconfont.js';
import '../../../assets/css/assembly.css';
import '../../../assets/css/common.css';
import './forget.less';
import '../../common/footer/footer.js';
import {HttpTool} from '@ccxc/tool';

// 手机找回
const phoneForget = document.getElementById('phoneForget');
const phoneForgetForm = document.getElementById('phoneForgetForm');
const phone = document.getElementById('phone');
const phoneCaptcha = document.getElementById('phoneCaptcha');
const phoneCaptchaButton = document.getElementById('phoneCaptchaButton');
// 邮箱找回
const emailForget = document.getElementById('emailForget');
const emailForgetForm = document.getElementById('emailForgetForm');
const email = document.getElementById('email');
const emailCaptcha = document.getElementById('emailCaptcha');
const emailCaptchaButton = document.getElementById('emailCaptchaButton');
// 其他输入框
const password = document.getElementById('password');
const repeatPassword = document.getElementById('repeatPassword');

// 初始化选择
if (phoneForget && !emailForget) {
    phoneForget.className = 'phone-forget selected';
    phoneForgetForm.className = 'phone-forget-form open';
}
if (emailForget && !phoneForget) {
    emailForget.className = 'email-forget selected';
    emailForgetForm.className = 'email-forget-form open';
}
if (phoneForget && emailForget) {
    phoneForget.className = 'phone-forget selected';
    phoneForgetForm.className = 'phone-forget-form open';
}
// 返回登录
const revertLogin = document.getElementById("revertLogin");
if (revertLogin) {
    revertLogin.addEventListener('click', function () {
        CommonFun.jumpPage('login');
    }, false);
}
// 验证输入框
if (password) {
    password.addEventListener('blur', verifyInput, false);
}
if (repeatPassword) {
    repeatPassword.addEventListener('blur', verifyInput, false);
}
// 登录提交
const submitDom = document.getElementById('submit');
// 跳转登录
const loginJump = document.getElementById('loginJump');
if (loginJump) {
    loginJump.addEventListener('click', function () {
        CommonFun.jumpPage('login');
    }, false);
}
// 验证账号，true：通过，false：不通过
CommonFun.isAccount = false;
if (!CommonFun.isCaptch) {
    CommonFun.isCaptch = {
        email: true,
        phone: true
    };
}

function init(type) {
    // 邮箱
    if (emailForget) {
        emailForget.className = 'email-forget';
        emailForgetForm.className = 'email-forget-form';
    }
    // 手机
    if (phoneForget) {
        phoneForget.className = 'phone-forget';
        phoneForgetForm.className = 'phone-forget-form';
    }
    switch (type) {
        case 'email':
            CommonFun.accountType = 'email';
            emailForget.className = 'email-forget selected';
            emailForgetForm.className = 'email-forget-form open';
            break;
        case 'phone':
            CommonFun.accountType = 'phone';
            phoneForget.className = 'phone-forget selected';
            phoneForgetForm.className = 'phone-forget-form open';
            break;
    }
    CommonFun.isAccount = false;
    CommonFun.isCaptch[type] = true;
    // 密码
    const passwordDom = CommonFun.getErrorDom(password);
    const repeatPasswordDom = CommonFun.getErrorDom(repeatPassword);
    passwordDom.innerText = '';
    repeatPasswordDom.innerText = '';
    passwordDom.className = 'error';
    repeatPasswordDom.className = 'error';
    password.value = '';
    repeatPassword.value = '';
}

/********************手机找回********************/
if (phoneForget) {
    if (phoneForget.className.indexOf('selected') !== -1) {
        CommonFun.accountType = 'phone';
    }
    phoneForget.addEventListener('click', function () {
        init('phone');
    }, false);
    // 获取手机验证码
    phoneCaptchaButton.addEventListener('click', function () {
        if (!CommonFun.isAccount) {
            alert('手机号不存在！');
            return;
        }
        CommonFun.sendCaptcha(phoneCaptchaButton, phone.value, 'phone_forget_password');
    }, false);
    // 验证输入框
    phone.addEventListener('blur', verifyInput, false);
    phoneCaptcha.addEventListener('blur', verifyInput, false);
}

/********************邮箱找回********************/
if (emailForget) {
    if (emailForget.className.indexOf('selected') !== -1) {
        CommonFun.accountType = 'email';
    }
    emailForget.addEventListener('click', function () {
        init('email');
    }, false);
    // 获取邮箱验证码
    emailCaptchaButton.addEventListener('click', function () {
        if (!CommonFun.isAccount) {
            alert('邮箱不存在！');
            return;
        }
        CommonFun.sendCaptcha(emailCaptchaButton, email.value, 'email_forget_password');
    }, false);
    // 验证输入框
    email.addEventListener('blur', verifyInput, false);
    emailCaptcha.addEventListener('blur', verifyInput, false);
}
/********************找回提交********************/
if (submitDom) {
    submitDom.addEventListener('click', function () {
        submit();
    }, false);
}
// 回车事件
document.addEventListener('keyup', function (e) {
    if (e.keyCode === 13) {
        submit();
    }
});

/**
 * 找回提交
 */
function submit() {
    const passwordDom = CommonFun.getErrorDom(password);
    const repeatPasswordDom = CommonFun.getErrorDom(repeatPassword);
    switch (CommonFun.accountType) {
        case 'email':
            const emailDom = CommonFun.getErrorDom(email);
            if (!email.value) {
                emailDom.innerText = '邮箱不能为空！';
                emailDom.className = 'error open';
                return;
            }
            if (!CommonFun.isAccount) {
                emailDom.innerText = '邮箱不存在！';
                emailDom.className = 'error open';
                return;
            }
            const emailCaptchaDom = CommonFun.getErrorDom(emailCaptcha);
            if (!emailCaptcha.value) {
                emailCaptchaDom.innerText = '验证码不能为空！';
                emailCaptchaDom.className = 'error open';
                return;
            }
            if (!password.value.trim()) {
                passwordDom.innerText = '密码不能为空！';
                passwordDom.className = 'error open';
                return;
            }
            if (password.value.length < 6) {
                passwordDom.innerText = '密码不能小于6位！';
                passwordDom.className = 'error open';
                return;
            }
            if (!repeatPassword.value.trim()) {
                repeatPasswordDom.innerText = '密码不能为空！';
                repeatPasswordDom.className = 'error open';
                return;
            }
            if (repeatPassword.value.length < 6) {
                repeatPasswordDom.innerText = '密码不能小于6位！';
                repeatPasswordDom.className = 'error open';
                return;
            }
            if (repeatPassword.value !== password.value) {
                repeatPasswordDom.innerText = '两次密码不相同！';
                repeatPasswordDom.className = 'error open';
                return;
            }
            HttpTool.post(HTTP_URLS.createMyAppForgetPassword, {
                accountType: CommonFun.accountType,
                account: email.value,
                password: password.value,
                captcha: emailCaptcha.value
            }).then(() => {
                CommonFun.jumpPage('login');
            });
            break;
        case 'phone':
            const phoneDom = CommonFun.getErrorDom(phone);
            if (!phone.value) {
                phoneDom.innerText = '手机号不能为空！';
                phoneDom.className = 'error open';
                return;
            }
            if (!CommonFun.isAccount) {
                phoneDom.innerText = '手机号不存在！';
                phoneDom.className = 'error open';
                return;
            }
            const phoneCaptchaDom = CommonFun.getErrorDom(phoneCaptcha);
            if (!phoneCaptcha.value) {
                phoneCaptchaDom.innerText = '验证码不能为空！';
                phoneCaptchaDom.className = 'error open';
                return;
            }
            if (!password.value.trim()) {
                passwordDom.innerText = '密码不能为空！';
                passwordDom.className = 'error open';
                return;
            }
            if (password.value.length < 6) {
                passwordDom.innerText = '密码不能小于6位！';
                passwordDom.className = 'error open';
                return;
            }

            if (!repeatPassword.value.trim()) {
                repeatPasswordDom.innerText = '密码不能为空！';
                repeatPasswordDom.className = 'error open';
                return;
            }
            if (repeatPassword.value.length < 6) {
                repeatPasswordDom.innerText = '密码不能小于6位！';
                repeatPasswordDom.className = 'error open';
                return;
            }
            if (repeatPassword.value !== password.value) {
                repeatPasswordDom.innerText = '两次密码不相同！';
                repeatPasswordDom.className = 'error open';
                return;
            }
            HttpTool.post(HTTP_URLS.createMyAppForgetPassword, {
                accountType: CommonFun.accountType,
                account: phone.value,
                password: password.value,
                captcha: phoneCaptcha.value
            }).then(() => {
                CommonFun.jumpPage('login');
            });
            break;
    }
}

/**
 * 验证输入框
 */
function verifyInput() {
    const dom = CommonFun.getErrorDom(this);
    switch (this.id) {
        case 'email':
            if (this.value) {
                if (!(this.value.indexOf('@') !== -1 && this.value.indexOf('.') !== -1)) {
                    dom.innerText = '邮箱格式不正确！';
                    dom.className = 'error open';
                } else {
                    CommonFun.verifyAccount(this, CommonFun.accountType, function (data) {
                        if (!data['accountCorrect']) {
                            dom.innerText = '邮箱不存在！';
                            dom.className = 'error open';
                            CommonFun.isAccount = false;
                        } else {
                            dom.innerText = '';
                            dom.className = 'error';
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
        case 'phone':
            if (this.value) {
                if (this.value.length !== 11 || this.value[0] !== '1') {
                    dom.innerText = '手机号格式不正确！';
                    dom.className = 'error open';
                } else {
                    CommonFun.verifyAccount(this, CommonFun.accountType, function (data) {
                        if (!data['accountCorrect']) {
                            dom.innerText = '手机号不存在！';
                            dom.className = 'error open';
                            CommonFun.isAccount = false;
                        } else {
                            dom.innerText = '';
                            dom.className = 'error';
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
        case 'password':
            if (this.value && this.value.length < 6) {
                dom.innerText = '密码不能小于6位！';
                dom.className = 'error open';
                return;
            }
            const repeatPasswordDom = CommonFun.getErrorDom(repeatPassword);
            if (repeatPassword.value && repeatPassword.value.length < 6) {
                repeatPasswordDom.innerText = '密码不能小于6位！';
                repeatPasswordDom.className = 'error open';
                return;
            }
            if (repeatPassword.value && this.value) {
                if (repeatPassword.value !== this.value) {
                    dom.innerText = '两次密码不相同！';
                    dom.className = 'error open';
                    return;
                } else {
                    repeatPasswordDom.innerText = '';
                    repeatPasswordDom.className = 'error';
                }
            }
            dom.innerText = '';
            dom.className = 'error';
            break;
        case 'repeatPassword':
            if (this.value && this.value.length < 6) {
                dom.innerText = '密码不能小于6位！';
                dom.className = 'error open';
                return;
            }
            const passwordDom = CommonFun.getErrorDom(password);
            if (password.value && password.value.length < 6) {
                passwordDom.innerText = '密码不能小于6位！';
                passwordDom.className = 'error open';
                return;
            }
            if (password.value && this.value) {
                if (password.value !== this.value) {
                    dom.innerText = '两次密码不相同！';
                    dom.className = 'error open';
                    return;
                } else {
                    passwordDom.innerText = '';
                    passwordDom.className = 'error';
                }
            }
            dom.innerText = '';
            dom.className = 'error';
            break;
    }
}
