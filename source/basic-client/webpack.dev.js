//引入webpack.basic.js文件
const WebpackBasic = require('./webpack.basic');
// 读取配置文件
const config = {
    'input': {
        'path': '/src/app'
    },
    'output': {
        'templatePath': '../xc-basic/basic-server/target/classes/templates',
        'staticPath': '../xc-basic/basic-server/target/classes/public/static'
    },
    'copyFiles': [
        {
            'from': './src/assets',
            'to': '../xc-basic/basic-server/target/classes/public/static/assets',
            'excludes': [
                '**/src/assets/img/**'
            ]
        }
    ],
    'publicPath': '/static',
    'cssImgPath': './img',
    'assets': {
        'js': [
            '/assets/config.js'
        ],
        'css': [],
        'favicon': './favicon.ico'
    },
    'exclude': {
        'css': [
            '/src/app/common'
        ],
        'js': [
            '/src/app/common',
            '/src/app/config'
        ],
        'html': [],
        'icon': [
            '/src/app/common'
        ]
    }
};
const data = WebpackBasic.init(config);
data['mode'] = 'development';
module.exports = data;
