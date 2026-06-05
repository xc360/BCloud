// 生产环境
// const CONFIG = {
//   url: location.origin,
//   basicPath: '',
//   pagePath: '/admin',
//   appId: 'xc1832640471',
//   oauthLogin: "https://ccxc.vip/oauth/login"
// };

// 开发环境
const CONFIG = {
  url: location.origin,
  basicPath: '/api',  // 本机地址
  pagePath: '/admin', // 页面路径
  appId: 'xc1832640471', // 应用ID
  oauthLogin: 'https://basic.ccxc.vip/oauth/login'
};
// 本地环境
// const CONFIG = {
//   url: "http://localhost:8811",
//   basicPath: '',  // 本机地址
//   pagePath: '/admin', // 页面路径
//   appId: 'xc1954335814', // 应用ID
//   oauthLogin: 'http://localhost:8811/oauth/login'
// };
window['basicConfig'] = CONFIG;
