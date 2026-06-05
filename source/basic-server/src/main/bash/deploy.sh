# 解压templates
if [ ! -f "./templates.zip" ];then
  echo "./templates.zip压缩包不存在！"
else
 rm -rf ./templates
 unzip ./templates.zip -d ./
 rm -rf ./templates.zip
 echo "./templates.zip解压成功！"
fi
# 解压admin
if [ ! -f "./public/admin.zip" ];then
  echo "./public/admin.zip压缩包不存在！"
else
  mkdir -p ./temp
  mv -f ./public/admin/assets/config.js ./temp/
  rm -rf ./public/admin
  unzip ./public/admin.zip -d ./public/
  mv -f ./temp/config.js ./public/admin/assets/
  rm -rf ./temp
  rm -rf ./public/admin.zip
  echo "./public/admin.zip解压成功！"
fi
# 解压static
if [ ! -f "./public/static.zip" ];then
  echo "./public/static.zip压缩包不存在！"
else
  mkdir -p ./temp
  mv -f ./public/static/assets/config.js ./temp/
  rm -rf ./public/static
  unzip ./public/static.zip -d ./public/
  mv -f ./temp/config.js ./public/static/assets/
  rm -rf ./temp
  rm -rf ./public/static.zip
  echo "./public/static.zip解压成功！"
fi


