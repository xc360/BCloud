import {Component, Input, OnInit} from '@angular/core';
import {TableDetailModel} from './table-detail.model';
import {Router} from '@angular/router';
import {CommonTool} from '@ccxc/tool';

@Component({
  selector: 'app-table-detail',
  templateUrl: './table-detail.component.html',
  styleUrls: ['./table-detail.component.scss']
})
export class TableDetailComponent implements OnInit {

  public COMMON_DETAIL = TableDetailModel.COMMON_DETAIL;
  @Input() tableDetailModel: TableDetailModel;
  public openUrl;

  constructor(public router: Router) {
  }

  ngOnInit() {

  }

  // 返回
  public revert(url) {
    this.router.navigateByUrl(url);
  }

  public openImage(img) {
    this.openUrl = img.src;
  }

  public loadImage(imageFrame, image) {
    // 图片处理
    CommonTool.imageShowHandle(imageFrame, image);
  }
}
