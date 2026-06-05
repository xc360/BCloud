import {HttpTool} from '@ccxc/tool';

HttpTool.setConfig({
    requestIntercept(data, headers, config) {
        headers['Content-Type'] = 'application/json;charset=UTF-8';
        return JSON.stringify(data);
    },
    responseIntercept(res, config) {
        return new Promise((resolve, reject) => {
            if (res.status === 200) {
                if (res.http.getResponseHeader('Content-Type') &&
                    res.http.getResponseHeader('Content-Type').indexOf('application/json') !== -1) {
                    resolve(JSON.parse(res.http.responseText));
                } else {
                    resolve(res);
                }
            } else if (res.status === 400) {
                const data = JSON.parse(res.http.responseText);
                alert(data['message']);
                reject(data);
            } else {
                alert("服务器异常！");
                console.log(res)
                reject(res);
            }
        });
    }
});
