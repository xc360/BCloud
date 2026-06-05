import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AppService} from '../../../../app.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {environment} from '../../../../../environments/environment';
import {HomeService} from '../../../home.service';
import {FileTool, LoginTool, UploadTool} from '@ccxc/tool';
import {FILE_FORMAT} from '../../../../config/enum';
import PAGE_URLS from '../../../../config/app-page.url';
import {BasicManagementService} from '../../basic-management.service';

declare var editormd: any;

@Component({
  selector: 'app-version-edit',
  templateUrl: './version-doc.component.html',
  styleUrls: ['./version-doc.component.scss']
})
export class VersionDocComponent implements OnInit, OnDestroy {

  public markdown: string = '';
  public editor: any;
  public isSubmit = true;
  public fileUrls = [];
  public isSaveContent: string = '';
  public appId;
  public formInfo: any;
  public field;

  constructor(public basicManagementService: BasicManagementService,
              public activatedRoute: ActivatedRoute,
              public appService: AppService,
              public modalService: NzModalService,
              public message: NzMessageService,
              public homeService: HomeService,
              public router: Router) {
    this.fileUrls = [];
    this.markdown = '';
  }

  ngOnInit() {
    // 获取编辑数据
    this.activatedRoute.queryParams.subscribe(queryParams => {
      const versionId = queryParams.id;
      this.appId = queryParams.appId;
      this.field = queryParams.field;
      this.basicManagementService.getAppVersion(this.appId, versionId).subscribe(res => {
        this.formInfo = res;
        if (res[this.field]) {
          this.markdown = res[this.field];
        }
        this.isSaveContent = JSON.stringify(this.markdown);
        let array = FILE_FORMAT.imageFormat.concat(FILE_FORMAT.fileFormat);
        array = array.concat(FILE_FORMAT.videoFormat);
        this.fileUrls = FileTool.getMidValue(this.markdown, 'http', array);
        this.initMd(this.markdown == null ? '' : this.markdown);
      });
    });
  }

  ngOnDestroy() {
    if (this.isSaveContent !== JSON.stringify(this.markdown)) {
      this.modalService.confirm({
        nzTitle: '你的文章还未保存是否保存？',
        nzContent: '<b style="color: red;">未保存的数据会消失哦！</b>',
        nzOkText: '是',
        nzOkType: 'danger',
        nzOnOk: () => {
          this.submit();
        },
        nzCancelText: '否'
      });
    }
  }

  @HostListener('document:keydown', ['$event'])
  onkeydown(event) {
    if (event.keyCode === 83 && (navigator.platform.match('Mac') ? event.metaKey : event.ctrlKey)) {
      event.preventDefault();
      if (this.isSubmit) {
        this.isSubmit = false;
        this.submit();
      }
    }
  }

  /**
   * 上传文件数据处理
   */
  public uploadFolderFile(file, config, fun) {
    const myThis = this;
    this.homeService.getMyAppUploadFileSign().subscribe(res => {
      const xcUploadTool = new UploadTool(null);
      xcUploadTool.setConfig({
        uploadUrl: res.uploadUrl, // 上传的url地址
        resumed: '0',
        sign: res,
        md5Loading: null,
        uploadLoading: null
      });
      xcUploadTool.setFileData({file, fileName: config.fileName, folderPath: config.folderPath});
      xcUploadTool.start().then(httpRes => {
        fun(httpRes.downloadUrl);
      }).catch(() => {
        myThis.appService.setLoading(false);
        myThis.message.error('文件上传失败！');
      });
    });
  }

  /**
   * 初始化md编辑器
   */
  public initMd(markdown) {
    const that = this;
    let array = FILE_FORMAT.imageFormat.concat(FILE_FORMAT.fileFormat);
    array = array.concat(FILE_FORMAT.videoFormat);
    this.editor = editormd('markdown', {
      height: 428,
      path: PAGE_URLS.editorLib,
      imageUpload: true,
      imageFormats: array,
      saveHTMLToTextarea: true,
      value: markdown,
      uploadFun(file, fun, myThis) {
        const tokenInfo = LoginTool.getToken();
        that.uploadFolderFile(file, {
          folderPath: '/app/' + tokenInfo.userId + '/file',
          fileName: FileTool.randomFileName(file.name)
        }, (url) => {
          that.fileUrls.push(url);
          fun(url);
        });
      },
      async onchange() {
        that.markdown = that.editor.getMarkdown();
        await that.fileHandle().then(() => {
        });
      }
    });
  }

  /**
   * 文件处理
   */
  public fileHandle() {
    return new Promise(async (resolve, reject) => {
      // 找出新添加的图片或文件
      const that = this;
      let array = FILE_FORMAT.imageFormat.concat(FILE_FORMAT.fileFormat);
      array = array.concat(FILE_FORMAT.videoFormat);
      const httpUrls = FileTool.getMidValue(that.markdown, 'http', array);
      // 验证文章文件数
      if (httpUrls.length > 500) {
        this.message.warning('本文文件数不能大于500个！');
      }
      const fileUrls = [];
      for (const url of httpUrls) {
        if (url.indexOf(environment.config.articleFeature) === -1) {
          fileUrls.push(url);
        }
      }
      if (fileUrls.length === 0) {
        resolve(null);
      }
      for (let i = 0; i < fileUrls.length; i++) {
        const url = fileUrls[i];
        await that.fileSaveAs(url).then((resUrl) => {
          that.fileUrls.push(resUrl);
          that.markdown = that.markdown.replace(url, resUrl);
          if (i === fileUrls.length - 1) {
            that.editor.setValue(that.markdown);
            resolve(null);
          }
        });
      }
    });
  }

  /**
   * 下载保存文件
   */
  public fileSaveAs(url) {
    return new Promise<string>((resolve, reject) => {
      const that = this;
      const xhr = new XMLHttpRequest();
      xhr.open('get', url, true);
      xhr.responseType = 'blob';
      xhr.onload = function (res) {
        if (this.status === 200) {
          const blob = this.response;
          const suffix = url.substring(url.lastIndexOf('.'), url.length);
          const fileName = FileTool.randomFileName(suffix);
          const type = this.getResponseHeader('Content-Type');
          const file = new File([blob], fileName, {type});
          const tokenInfo = LoginTool.getToken();
          that.uploadFolderFile(file, {
            folderPath: '/app/' + tokenInfo.userId + '/file',
            fileName: FileTool.randomFileName(file.name)
          }, (resUrl) => {
            resolve(resUrl);
          });
        }
      };
      xhr.send();
    });
  }

  /**
   * 提交代码
   */
  public async submit() {
    this.markdown = this.editor.getMarkdown();
    // 验证文章字数
    if (this.markdown.length > 500000) {
      this.message.warning('文章不能超过500000字符！');
      return;
    }
    this.appService.setLoading(true);
    await this.fileHandle().then(() => {
      let array = FILE_FORMAT.imageFormat.concat(FILE_FORMAT.fileFormat);
      array = array.concat(FILE_FORMAT.videoFormat);
      this.fileUrls = FileTool.getMidValue(this.markdown, 'http', array);
      const req = this.formInfo;
      req[this.field] = this.markdown;
      this.isSaveContent = JSON.stringify(this.markdown);
      this.basicManagementService.updateAppVersion(this.appId, this.formInfo.id, req).subscribe(res => {
        this.isSubmit = true;
        this.appService.setLoading(false);
        this.message.success('保存成功！');
      });
    });
  }

  public revert() {
    this.router.navigateByUrl('/home/basicManage/versionManage/versionList?appId=' + this.appId);
  }
}
