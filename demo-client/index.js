import {CommonTool, LoginTool, HttpTool} from "@ccxc/tool";

const appId = 'xc0320330512';
const oauthLogin = 'http://localhost:8811/oauth/login';
const getMyAppToken = "http://localhost:8080/my_app/token"

LoginTool.initLogin({
    appId: appId,
    loginUrl: oauthLogin,
    getToken(req) {
        return new Promise(function (resolve) {
            const url = CommonTool.analysisParam(getMyAppToken, {code: req['code']});
            HttpTool.get(url).then(res => {
                CommonTool.setCookie('token', res.data.accessToken);
                CommonTool.setCookie('refresh-token', res.data.refreshToken);
                resolve(res.data);
            });
        });
    },
    loginFail(error) {
        return new Promise(function (resolve) {
            if (confirm(error.content)) {
                resolve(true);
            } else {
                resolve(false);
            }
        });
    },
    loginSuccess() {
        CommonTool.jump("/home.html");
    }
});