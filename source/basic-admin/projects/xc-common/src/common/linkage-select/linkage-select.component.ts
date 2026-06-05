import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LinkageChildren} from '../form-info/form-info.model';

@Component({
  selector: 'app-linkage-select',
  templateUrl: './linkage-select.component.html',
  styleUrls: ['./linkage-select.component.scss']
})
export class LinkageSelectComponent implements OnInit {

  @Input() public selectItem: Array<LinkageChildren>;
  @Input() public formInfo;

  constructor(public http: HttpClient) {
  }

  ngOnInit() {
  }
}
