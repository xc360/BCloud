import {environment} from '../../environments/environment';

const PATH = environment.config.url;
const BASIC = PATH + environment.config.basicPath; // 基础服务
let PAGE = PATH + environment.config.pagePath;
if (!environment.config.pagePath) {
  PAGE = location.origin;
}
const PAGE_URLS = {
  /************************其他页面************************/
  login: PAGE + '/#/login', // 重定向到登录
  editorLib: PAGE + '/assets/editor.md/lib/' // 编辑器lib目录
};
export default PAGE_URLS;
