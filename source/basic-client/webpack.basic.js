const {XcTool, XcHtmlHandlePlugin} = require('./xc.tool.js');
const pathTool = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const webpack = require('webpack');

class WebpackBasic {
    // 初始化
    static init(config) {
        // 删除静态文件目录
        XcTool.deleteFolder(pathTool.join(__dirname, config.output.templatePath));
        // 删除模板文件目录
        XcTool.deleteFolder(pathTool.join(__dirname, config.output.staticPath));
        // 获取输入路径
        const fileDir = pathTool.join(__dirname, config.input.path);
        // 扫描js
        const jsArray = XcTool.getChildrenFile(fileDir, '.js');
        // 扫描html
        const htmlArray = XcTool.getChildrenFile(fileDir, '.html');
        // 处理js文件
        let entry = {};
        for (let js of jsArray) {
            let bool = false;
            for (const excludeJs of config.exclude.js) {
                if (js.indexOf(excludeJs.replace(/\//g, '\\')) !== -1) {
                    bool = true;
                }
            }
            if (bool) {
                continue;
            }
            const key = XcTool.getEntryKey(js, config.input.path);
            entry[key] = js;
        }
        // 处理html文件
        let plugins = [];
        for (const html of htmlArray) {
            let bool = false;
            for (const excludeHtml of config.exclude.html) {
                if (html.indexOf(excludeHtml.replace(/\//g, '\\')) !== -1) {
                    bool = true;
                }
            }
            if (bool) {
                continue;
            }
            const key = XcTool.getEntryKey(html, config.input.path);
            const filePath = html.replace(pathTool.join(__dirname, config.input.path), '');
            const filename = pathTool.join(__dirname, config.output.templatePath) + filePath;
            plugins.push(
                new HtmlWebpackPlugin({
                    template: html,	//设置打包后的文件,插入到的模板html文件是哪个(以来的模板文件)
                    filename: filename,//输出文件的名称(打包后的html文件名,可以自己设置,最好不要变动)
                    hot: true,					//自动刷新浏览器
                    favicon: config.assets.favicon,
                    chunks: [key],			//代表指定的入口文件是哪个
                    inject: 'body'
                })
            );
        }
        // 复制文件处理
        const patterns = [];
        for (let copyFile of config.copyFiles) {
            patterns.push({
                from: pathTool.join(__dirname, copyFile.from),
                to: pathTool.join(__dirname, copyFile.to),
                globOptions: {
                    ignore: copyFile.excludes
                }
            });
        }
        // webpack配置
        return {
            entry: entry,
            plugins: [
                // 自定义HTML处理插件
                new XcHtmlHandlePlugin({
                    assets: config.assets,
                    exclude: config.exclude,
                    publicPath: config.publicPath
                }),
                // 配置jQuery
                new webpack.ProvidePlugin({
                    $: 'jquery',
                    jQuery: 'jquery',
                    'window.jQuery': 'jquery'
                }),
                // 打包css
                new MiniCssExtractPlugin({
                    filename: '[name].[hash].css'
                }),
                // 拷贝静态资源
                new CopyWebpackPlugin({patterns})
            ].concat(plugins),
            optimization: {
                splitChunks: {
                    cacheGroups: {//缓存组
                        common: {//公共的模块
                            name: 'common',
                            chunks: 'initial',//刚开始就要抽离
                            priority: 1,//添加权重
                            minSize: 0,//大小大于0字节的时候需要抽离出来
                            minChunks: 2//重复2次使用的时候需要抽离出来
                        },
                        vendor: {
                            name: 'common.bundle',
                            priority: 2,//添加权重,设置匹配优先级，数字越小，优先级越低
                            test: /node_modules/,//把这个目录下符合下面几个条件的库抽离出来
                            chunks: 'initial',//刚开始就要抽离
                            minSize: 0,//大小大于0字节的时候需要抽离出来
                            minChunks: 2//重复2次使用的时候需要抽离出来
                        }
                    }
                }
            },
            module: {
                rules: [
                    {
                        test: /\.(less|css)$/,
                        use: [MiniCssExtractPlugin.loader, 'css-loader', 'less-loader']
                    },
                    {
                        test: /\.js$/,
                        exclude: /node_modules/,
                        use: {
                            loader: 'babel-loader',
                            options: {
                                presets: ['es2015', 'stage-2'], //更改
                                plugins: ['transform-runtime']
                            }
                        }
                    }
                ]
            },
            resolve: {
                modules: ['node_modules'],
                alias: {  // 设置别名
                    '@': pathTool.resolve('src') // 这样配置后 @ 可以指向 src 目录
                }
            },
            output: {
                filename: '[name].[hash].js',
                path: pathTool.join(__dirname, config.output.staticPath),
                publicPath: config.publicPath != null ? config.publicPath : undefined,
                assetModuleFilename: XcTool.spliceFilePath(config.cssImgPath, '[hash][ext][query]')
            }
        };
    }
}

module.exports = WebpackBasic;
