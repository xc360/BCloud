const fs = require('fs');
const pathTool = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

/**
 * 工具类
 */
class XcTool {
    /**
     * 获取目录下所有文件，包含子目录文件
     * 根据后缀筛选
     * folderPath:目录地址
     * suffix:后缀
     */
    static getChildrenFile(filePath, suffix) {
        let allFiles = [];
        let files = fs.readdirSync(filePath);
        files.forEach(function(item, index) {
            let fPath = pathTool.join(filePath, item);
            let stat = fs.statSync(fPath);
            if (stat.isDirectory()) {
                allFiles = allFiles.concat(XcTool.getChildrenFile(fPath, suffix));
            }
            if (suffix) {
                if (stat.isFile() && item.substr(item.lastIndexOf('.')).toLowerCase() === suffix.toLowerCase()) {
                    allFiles.push(fPath);
                }
            } else {
                if (stat.isFile()) {
                    allFiles.push(fPath);
                }
            }
        });
        return allFiles;
    }

    /**
     * 获取入口key
     * filePath 文件绝对路径
     * inputPath 输入路径
     */
    static getEntryKey(filePath, inputPath) {
        const path = filePath.replace(pathTool.join(__dirname, inputPath) + '\\', '');
        return path.substring(0, path.lastIndexOf('.'));
    }

    /**
     * 删除文件夹及文件下所有的文件，包含子集
     * folderPath 文件夹路径
     */
    static deleteFolder(folderPath) {
        if (fs.existsSync(folderPath)) {
            const files = fs.readdirSync(folderPath);
            for (let item of files) {
                const stats = fs.statSync(`${folderPath}/${item}`);
                if (stats.isDirectory()) {
                    XcTool.deleteFolder(`${folderPath}/${item}`);
                } else {
                    fs.unlinkSync(`${folderPath}/${item}`);
                }
            }
            fs.rmdirSync(folderPath);
        }
    }

    /**
     * 拼接文件路径，返回文件路径
     * filePath 文件路径
     * fileName 文件名称
     */
    static spliceFilePath(filePath, fileName) {
        if (filePath) {
            let path = filePath + '/' + fileName;
            if (filePath.length > 0 && filePath.substring(filePath.length - 1) === '/') {
                path = filePath + fileName;
            }
            return path;
        }
        return '';
    }
}

/**
 * html处理插件
 */
class XcHtmlHandlePlugin {

    constructor(userOptions) {
        const options = {
            assets: {
                js: [],
                css: []
            },
            exclude: {
                js: [],
                css: [],
                html: []
            },
            publicPath: ''
        };
        Object.assign(options, userOptions);
        this.options = options;
    }

    apply(compiler) {
        // 指定一个挂载到 webpack 自身的事件钩子。
        compiler.hooks.compilation.tap('XcHtmlHandlePlugin', (compilation, callback) => {
            HtmlWebpackPlugin.getHooks(compilation).beforeAssetTagGeneration.tap('XcHtmlHandlePlugin', (htmlPluginData) => {
                for (let i = 0; i < htmlPluginData.assets.js.length; i++) {
                    let jsPath = decodeURIComponent(htmlPluginData.assets.js[i]);
                    jsPath = jsPath.replace(/\\/g, '/');
                    htmlPluginData.assets.js[i] = jsPath;
                }
                for (let i = 0; i < htmlPluginData.assets.css.length; i++) {
                    let cssPath = decodeURIComponent(htmlPluginData.assets.css[i]);
                    cssPath = cssPath.replace(/\\/g, '/');
                    htmlPluginData.assets.css[i] = cssPath;
                }
                const fileName = htmlPluginData.plugin.options.template;
                // 处理css
                let cssBool = true;
                for (let excludeCss of this.options.exclude.css) {
                    if (fileName.indexOf(excludeCss.replace(/\//g, '\\')) !== -1) {
                        cssBool = false;
                        break;
                    }
                }
                if (cssBool) {
                    const cssArray = [];
                    for (let css of this.options.assets.css) {
                        let path = css;
                        if (this.options.publicPath) {
                            path = pathTool.join(this.options.publicPath, css);
                            path = path.replace(/\\/g, '/');
                        }
                        cssArray.push(path);
                    }
                    htmlPluginData.assets.css = cssArray.concat(htmlPluginData.assets.css);
                }

                // 处理js
                let jsBool = true;
                for (let excludeJs of this.options.exclude.js) {
                    if (fileName.indexOf(excludeJs.replace(/\//g, '\\')) !== -1) {
                        jsBool = false;
                        break;
                    }
                }
                if (jsBool) {
                    const jsArray = [];
                    for (let js of this.options.assets.js) {
                        let path = js;
                        if (this.options.publicPath) {
                            path = pathTool.join(this.options.publicPath, js);
                            path = path.replace(/\\/g, '/');
                        }
                        jsArray.push(path);
                    }
                    htmlPluginData.assets.js = jsArray.concat(htmlPluginData.assets.js);
                }

                // 处理icon
                let iconBool = false;
                for (let excludeIcon of this.options.exclude.icon) {
                    if (fileName.indexOf(excludeIcon.replace(/\//g, '\\')) !== -1) {
                        iconBool = true;
                        break;
                    }
                }
                if (iconBool) {
                    htmlPluginData.assets.favicon = undefined;
                }
            });
        });
    }
}

module.exports = {XcTool, XcHtmlHandlePlugin};
