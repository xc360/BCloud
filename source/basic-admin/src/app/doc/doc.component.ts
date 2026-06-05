import {Component, OnInit} from '@angular/core';
import {TableDetailModel} from '../../../projects/xc-common/src/common/table-detail/table-detail.model';
import {FormInfoModel} from '../../../projects/xc-common/src/common/form-info/form-info.model';

@Component({
  selector: 'app-doc',
  templateUrl: './doc.component.html',
  styleUrls: ['./doc.component.scss']
})
export class DocComponent implements OnInit {

  public tableDetailModel: TableDetailModel;
  public formInfoModel: FormInfoModel;

  constructor() {
    this.tableDetailModel = new TableDetailModel();
  }

  ngOnInit(): void {
    const that = this;
    // this.tableDetailModel.setConfig({
    //   modules: [
    //     {name: '基础信息', type: COMMON_DETAIL.header},
    //     {name: '图片', field: 'urls', type: COMMON_DETAIL.lineImages}
    //   ]
    // });
    // this.tableDetailModel.setFormInfo({
    //   urls: []
    // });
    this.formInfoModel = new FormInfoModel();
    this.formInfoModel.initForm({
      components: [
        {
          name: '上传图片',
          field: 'images',
          required: true,
          uploadPicture: {
            loading: false,
            multiple: true,
            maxLength: 3,
            clickFun() {
              that.formInfoModel.formInfo['images'].push({url: 'https://img.alicdn.com/tfs/TB1Z0PywTtYBeNjy1XdXXXXyVXa-186-200.svg'});
              debugger
            }
          }
        }
      ]
    }, {images: [{url: 'https://img.alicdn.com/tfs/TB1g.mWZAL0gK0jSZFtXXXQCXXa-200-200.svg'}]});
  }

}
