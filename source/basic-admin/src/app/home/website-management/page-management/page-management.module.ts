import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PageManagementRoutingModule} from './page-management-routing.module';
import {PageListComponent} from './page-list/page-list.component';
import {NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {TableFindModule} from '../../../../../projects/xc-common/src/common/table-find/table-find.module';
import {FormInfoModule} from '../../../../../projects/xc-common/src/common/form-info/form-info.module';
import {FormsModule} from '@angular/forms';


@NgModule({
  declarations: [PageListComponent],
  imports: [
    CommonModule,
    PageManagementRoutingModule,
    FormsModule,
    NzFormModule,
    TableFindModule,
    FormInfoModule,
    NzSelectModule,
    NzModalModule,
    NzInputModule
  ]
})
export class PageManagementModule {
}
