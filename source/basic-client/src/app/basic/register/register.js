import HTTP_URLS from '../../config/app-http.url';
import CommonFun from '../../common/common.fun.js';
import XcDialog from '../../common/xc-dialog';
import '../../common/http.interceptor';
import 'babel-polyfill';
import '../../../assets/plugins/alIcon/iconfont.js';
import '../../../assets/css/assembly.css';
import '../../../assets/css/common.css';
import './register.less';
import '../../common/footer/footer.js';
import {HttpTool} from '@ccxc/tool';

// 手机注册
const phoneRegister = document.getElementById('phoneRegister');
const phoneRegisterForm = document.getElementById('phoneRegisterForm');
const phone = document.getElementById('phone');
const phoneCaptcha = document.getElementById('phoneCaptcha');
const phoneCaptchaButton = document.getElementById('phoneCaptchaButton');
// 邮箱注册
const emailRegister = document.getElementById('emailRegister');
const emailRegisterForm = document.getElementById('emailRegisterForm');
const email = document.getElementById('email');
const emailCaptcha = document.getElementById('emailCaptcha');
const emailCaptchaButton = document.getElementById('emailCaptchaButton');
// 其他输入框
const password = document.getElementById('password');
const repeatPassword = document.getElementById('repeatPassword');
const agreement = document.getElementById('agreement');
// 验证码
const imgCaptcha = document.getElementById('imgCaptcha');
const imgCaptchaImg = document.getElementById('imgCaptchaImg');
// 初始化选择
if (phoneRegister && !emailRegister) {
    phoneRegister.className = 'phone-register selected';
    phoneRegisterForm.className = 'phone-register-form open';
}
if (emailRegister && !phoneRegister) {
    emailRegister.className = 'email-register selected';
    emailRegisterForm.className = 'email-register-form open';
}
if (phoneRegister && emailRegister) {
    phoneRegister.className = 'phone-register selected';
    phoneRegisterForm.className = 'phone-register-form open';
}
// 用户协议
const userAgreement = document.getElementById("userAgreement");
if (userAgreement) {
    userAgreement.addEventListener('click', function () {
        CommonFun.jumpPage('userAgreement');
    }, false);
}
// 隐私协议
const privacyAgreement = document.getElementById("privacyAgreement");
if (privacyAgreement) {
    privacyAgreement.addEventListener('click', function () {
        CommonFun.jumpPage('privacyAgreement');
    }, false);
}

// 返回登录
const revertLogin = document.getElementById("revertLogin");
if (revertLogin) {
    revertLogin.addEventListener('click', function () {
        CommonFun.jumpPage('login');
    }, false);
}
const xcDialog = new XcDialog('eject', 'imgCaptchaConfirm', 'imgCaptchaClose');
imgCaptchaImg.addEventListener('click', function (e) {
    CommonFun.getImgCaptcha(function (url, code) {
        xcDialog['code'] = code;
        imgCaptchaImg.src = url;
        imgCaptcha.value = '';
    });
}, false);
xcDialog.confirmCallback((that) => {
    if (that.type === 'phoneCaptcha') {
        if (!imgCaptcha.value) {
            alert('图片验证码必填！');
            return;
        }
        CommonFun.sendCaptcha(phoneCaptchaButton, phone.value, 'phone_register', {
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
        CommonFun.sendCaptcha(emailCaptchaButton, email.value, 'email_register', {
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

// 验证输入框
if (agreement) {
    agreement.addEventListener('change', verifyInput, false);
}
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
    if (emailRegister) {
        emailRegister.className = 'email-register';
        emailRegisterForm.className = 'email-register-form';
    }
    //手机
    if (phoneRegister) {
        phoneRegister.className = 'phone-register';
        phoneRegisterForm.className = 'phone-register-form';
    }
    switch (type) {
        case 'email':
            CommonFun.accountType = 'email';
            if (emailRegister) {
                emailRegister.className = 'email-register selected';
                emailRegisterForm.className = 'email-register-form open';
            }
            break;
        case 'phone':
            CommonFun.accountType = 'phone';
            if (phoneRegister) {
                phoneRegister.className = 'phone-register selected';
                phoneRegisterForm.className = 'phone-register-form open';
            }
            break;
    }
    CommonFun.isAccount = false;
    CommonFun.isCaptch[type] = true;
    // 密码
    const passwordDom = CommonFun.getErrorDom(password);
    const repeatPasswordDom = CommonFun.getErrorDom(repeatPassword);
    const agreementDom = CommonFun.getErrorDom(agreement);

    repeatPassword.value = '';
    repeatPasswordDom.innerText = '';
    repeatPasswordDom.className = 'error';

    password.value = '';
    passwordDom.innerText = '';
    passwordDom.className = 'error';

    agreement.checked = false;
    agreementDom.innerText = '';
    agreementDom.className = 'error';
}

/********************手机注册********************/
if (phoneRegister) {
    if (phoneRegister.className.indexOf('selected') !== -1) {
        CommonFun.accountType = 'phone';
    }
    phoneRegister.addEventListener('click', function () {
        init('phone');
    }, false);
    // 获取手机验证码
    phoneCaptchaButton.addEventListener('click', function () {
        if (CommonFun.isAccount) {
            alert('手机号已存在！');
            return;
        }
        CommonFun.sendCaptcha(phoneCaptchaButton, phone.value, 'phone_register', {}, (res) => {
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
}
/********************邮箱注册********************/
if (emailRegister) {
    if (emailRegister.className.indexOf('selected') !== -1) {
        CommonFun.accountType = 'email';
    }
    emailRegister.addEventListener('click', function () {
        init('email');
    }, false);
    // 获取邮箱验证码
    emailCaptchaButton.addEventListener('click', function () {
        if (CommonFun.isAccount) {
            alert('邮箱已存在！');
            return;
        }
        CommonFun.sendCaptcha(emailCaptchaButton, email.value, 'email_register', {}, (res) => {
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
}


/********************注册提交********************/
if (submitDom) {
    submitDom.addEventListener('click', function () {
        submit();
    }, false);
}
// 回车事件
let bool = true;
document.addEventListener('keyup', function (e) {
    if (e.keyCode === 13 && bool) {
        submit();
    }
});
if (imgCaptcha) {
    imgCaptcha.addEventListener('keyup', function (e) {
        if (e.keyCode === 13 && bool) {
            document.getElementById('imgCaptchaConfirm').click();
        }
    });
}

/**
 * 注册提交
 */
function submit() {
    const passwordDom = CommonFun.getErrorDom(password);
    const repeatPasswordDom = CommonFun.getErrorDom(repeatPassword);
    const agreementDom = CommonFun.getErrorDom(agreement);
    switch (CommonFun.accountType) {
        case 'email':
            const emailDom = CommonFun.getErrorDom(email);
            if (!email.value) {
                emailDom.innerText = '邮箱不能为空！';
                emailDom.className = 'error open';
                return;
            }
            if (CommonFun.isAccount) {
                emailDom.innerText = '邮箱已存在！';
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
            if (!agreement.checked) {
                agreementDom.innerText = '协议必须勾选';
                agreementDom.className = 'error open';
                return;
            }
            HttpTool.post(HTTP_URLS.createMyAppRegister, {
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
            if (CommonFun.isAccount) {
                phoneDom.innerText = '手机号已存在！';
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
            if (!agreement.checked) {
                agreementDom.innerText = '协议必须勾选';
                agreementDom.className = 'error open';
                return;
            }
            HttpTool.post(HTTP_URLS.createMyAppRegister, {
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
                        if (data['accountCorrect']) {
                            dom.innerText = '邮箱已存在！';
                            dom.className = 'error open';
                            CommonFun.isAccount = true;
                        } else {
                            dom.innerText = '';
                            dom.className = 'error';
                            CommonFun.isAccount = false;
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
                        if (data['accountCorrect']) {
                            dom.innerText = '手机号已存在！';
                            dom.className = 'error open';
                            CommonFun.isAccount = true;
                        } else {
                            dom.innerText = '';
                            dom.className = 'error';
                            CommonFun.isAccount = false;
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
        case 'agreement':
            if (!this.checked) {
                dom.innerText = '协议必须勾选';
                dom.className = 'error open';
                return;
            } else {
                dom.innerText = '';
                dom.className = 'error';
            }
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
