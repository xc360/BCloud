* 安装

```
npm i
```

* 编译

```
npm run build
```

* 登录，发布到远程仓库（编译用`npm run tsc`，需要进入dist目录执行发布命令）

```
npm login
```

* 修改镜像为npm仓库

```
npm config set registry https://registry.npmjs.org
```

* 设置token

```
npm config set //registry.npmjs.org/:_authToken=
```

* 在npm仓库开启

```
Two-Factor Authentication
```

* 发布

```
npm publish --access public
```

